package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.annotation.CustomizeSignature;
import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.SimplePlugin;
import cn.beichenhpy.dictionary.enums.TranslateConstant;
import cn.beichenhpy.dictionary.enums.TranslateType;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;


/**
 * 实体类翻译<p>
 * 需要定义好对应的字段<p>
 *
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:05
 */
@Slf4j
public class EntityDictTranslateHandler extends AbstractDictTranslate {

    @Override
    protected void registerHandler() {
        TRANSLATE_HANDLERS.put(TranslateType.ENTITY, this);
    }


    @Override
    public boolean unsatisfied(ProceedingJoinPoint joinPoint) {
        return false;
    }


    @Override
    public Object dictTranslate(Object result) throws Exception {
        Class<?>[] noTranslateClasses = NO_TRANSLATE_CLASS_HOLDER.get();
        //进入方法先判断是否满足条件?
        if (!checkBasic(result) && checkNotInBlackList(result, noTranslateClasses)) {
            if (result instanceof Collection) {
                for (Object o : ((Collection<?>) result)) {
                    if (!checkBasic(o) && checkNotInBlackList(o, noTranslateClasses)) {
                        dictTranslate(o);
                    }
                }
            } else {
                //添加类缓存
                List<Field> fields = getAvailableFields(result);
                for (Field field : fields) {
                    //fix 高版本会出现InaccessibleObjectException
                    try {
                        field.setAccessible(true);
                    } catch (Exception e) {
                        log.warn("由于{}的原因,跳过对{}的翻译," +
                                        "可以在EnableDictTranslate注解中的noTranslate属性添加{}字段以抑制警告",
                                e.getMessage(), result.getClass().getName(), result.getClass());
                        continue;
                    }
                    //对象key
                    Object key = ReflectUtil.getFieldValue(result, field);
                    //key的值不存在，则跳过循环
                    if (key == null) {
                        continue;
                    }
                    //是否为基础类型
                    if (!checkBasic(key)) {
                        dictTranslate(key);
                    } else {
                        Dict dict = DICT_ANNO_CACHE.get(field);
                        if (dict == null) {
                            dict = field.getAnnotation(Dict.class);
                            DICT_ANNO_CACHE.put(field, dict);
                        }
                        if (dict == null) {
                            continue;
                        }
                        String ref = dict.ref();
                        switch (dict.dictType()) {
                            case SIMPLE:
                                doSimpleTranslate(result, key, ref, dict);
                                break;
                            case CUSTOMIZE:
                                doCustomizeTranslate(result, key, ref, dict);
                                break;
                            default:
                                break;

                        }
                    }
                }
            }
        }
        return result;
    }

    protected void doSimpleTranslate(Object current, Object fieldValue, String ref, Dict dict) {
        //判断字段类型 boolean 在 getFieldValue时已经装箱为Boolean了
        SimplePlugin simplePlugin = dict.simplePlugin();
        boolean revert = simplePlugin.isRevert();
        if (fieldValue instanceof Boolean) {
            if (!ObjectUtil.isEmpty(fieldValue)) {
                if (fieldValue.toString().equals(TranslateConstant.LOWER_TRUE)) {
                    if (!revert) {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.YES);
                    } else {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.NO);
                    }
                }
                if (fieldValue.toString().equals(TranslateConstant.LOWER_FALSE)) {
                    if (!revert) {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.NO);
                    } else {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.YES);
                    }
                }
            }
        }
        if (fieldValue instanceof Integer) {
            if (!ObjectUtil.isEmpty(fieldValue)) {
                if (fieldValue.toString().equals(TranslateConstant.ONE)) {
                    if (!revert) {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.YES);
                    } else {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.NO);
                    }
                }
                if (fieldValue.toString().equals(TranslateConstant.ZERO)) {
                    if (!revert) {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.NO);
                    } else {
                        ReflectUtil.setFieldValue(current, ref, TranslateConstant.YES);
                    }
                }
            }
        }
    }

    /**
     * 翻译本地字典表
     * <pre>
     *  //本地字典表例子
     *  public enum GenderEnum {
     *     MAN(true, "男"),
     *     WOMAN(false, "女");
     *
     *     private final Boolean key;
     *     private final String value;
     *
     *     public static String getValue(Boolean key){
     *         for (GenderEnum value : GenderEnum.values()) {
     *             if (value.getKey().equals(key)){
     *                 return value.getValue();
     *             }
     *         }
     *         return null;
     *     }
     *
     *     GenderEnum(Boolean key, String value) {
     *         this.key = key;
     *         this.value = value;
     *     }
     *
     *     public Boolean getKey() {
     *         return key;
     *     }
     *
     *     public String getValue() {
     *         return value;
     *     }
     * }
     * </pre>
     *
     * @param dict       注解
     * @param ref        需要赋值的翻译字段
     * @param current    当前实体类
     * @param fieldValue 当前字段值
     */
    protected void doCustomizeTranslate(Object current, Object fieldValue, String ref, Dict dict) throws Exception {
        CustomizeSignature customizeSignature = dict.commonSignature();
        //本地字典表
        Class<?> clazz = customizeSignature.type();
        //不为默认Object则进行转换
        if (!clazz.equals(Object.class)) {
            String methodName = customizeSignature.method();
            if (methodName.isEmpty()) {
                throw new IllegalArgumentException("字典转换失败：未传入[method]");
            }
            Class<?> parameterType = customizeSignature.arg();
            Method translateMethod = ReflectUtil.getMethod(clazz, methodName, parameterType);
            if (translateMethod == null) {
                throw new IllegalArgumentException("字典转换失败：检查传入的[method]是否存在");
            }
            //判断是否为静态方法
            int modifiers = translateMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            if (clazz.isEnum() && !isStatic) {
                throw new IllegalArgumentException("字典转换失败: 传入type为枚举类时，必须传入静态方法method!");
            }
            boolean isSameClazz = checkFieldClassSameAsAnno(fieldValue, parameterType);
            if (!isSameClazz) {
                throw new IllegalArgumentException("字典转换失败：检查字段与注解arg参数是否一致，" +
                        "字段类型为:" + fieldValue.getClass() + "注解参数类型为:" + parameterType);
            }
            try {
                Object instance = null;
                if (!isStatic) {
                    instance = ReflectUtil.newInstance(clazz);
                }
                Object translateValue = ReflectUtil.invoke(instance, translateMethod, fieldValue);
                ReflectUtil.setFieldValue(current, ref, translateValue);
            } catch (Exception e) {
                throw new IllegalArgumentException("字典转换失败：请注意传入的[arg]类型是否正确", e);
            }
        }
    }


}