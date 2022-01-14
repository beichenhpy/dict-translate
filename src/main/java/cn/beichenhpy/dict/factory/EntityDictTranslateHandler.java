package cn.beichenhpy.dict.factory;

import cn.beichenhpy.dict.CommonSignature;
import cn.beichenhpy.dict.Dict;
import cn.beichenhpy.dict.SimplePlugin;
import cn.beichenhpy.dict.enums.TranslateType;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 实体类翻译<p>
 * 需要定义好对应的字段<p>
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:05
 */
public class EntityDictTranslateHandler extends AbstractDictTranslate {

    @PostConstruct
    @Override
    protected void add() {
        TRANSLATE_HANDLERS.put(TranslateType.ENTITY, this);
    }

    @Override
    protected void doSimpleTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) {
        //判断字段类型 boolean 在 getFieldValue时已经装箱为Boolean了
        SimplePlugin simplePlugin = dict.simplePlugin();
        boolean revert = simplePlugin.isRevert();
        if (fieldValue instanceof Boolean) {
            if (!ObjectUtil.isEmpty(fieldValue)) {
                if (Boolean.TRUE.toString().equals(fieldValue.toString())) {
                    if (!revert){
                        ReflectUtil.setFieldValue(current, ref, "是");
                    }else {
                        ReflectUtil.setFieldValue(current, ref, "否");
                    }
                }
                if (Boolean.FALSE.toString().equals(fieldValue.toString())) {
                    if (!revert){
                        ReflectUtil.setFieldValue(current, ref, "否");
                    }else {
                        ReflectUtil.setFieldValue(current, ref, "是");
                    }
                }
            }
        }
        if (fieldValue instanceof Integer) {
            if (!ObjectUtil.isEmpty(fieldValue)) {
                if ("1".equals(fieldValue.toString())) {
                    if (!revert){
                        ReflectUtil.setFieldValue(current, ref, "是");
                    }else {
                        ReflectUtil.setFieldValue(current, ref, "否");
                    }
                }
                if ("0".equals(fieldValue.toString())) {
                    if (!revert){
                        ReflectUtil.setFieldValue(current, ref, "否");
                    }else {
                        ReflectUtil.setFieldValue(current, ref, "是");
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
     * @param dict 注解
     * @param ref        需要赋值的翻译字段
     * @param current 当前实体类
     * @param field 字段
     * @param fieldValue 当前字段值
     */
    @Override
    protected void doCommonTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception{
        CommonSignature commonSignature = dict.commonSignature();
        //本地字典表
        Class<?> clazz = commonSignature.type();
        //不为默认Object则进行转换
        if (!clazz.equals(Object.class)) {
            String methodName = commonSignature.method();
            if (methodName.isEmpty()) {
                throw new IllegalArgumentException("字典转换失败：未传入[method]");
            }
            Class<?>[] parameterType = commonSignature.args();
            Method translateMethod = ReflectUtil.getMethod(clazz, methodName, parameterType);
            if (translateMethod == null) {
                throw new IllegalArgumentException("字典转换失败：检查传入的[method]是否存在");
            }
            //判断是否为静态方法
            int modifiers = translateMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            if (!isStatic) {
                throw new IllegalArgumentException("字典转换失败：请注意传入的[method]方法，必须为静态方法");
            }
            try {
                Object translateValue = translateMethod.invoke(null, fieldValue);
                ReflectUtil.setFieldValue(current, ref, translateValue);
            } catch (Exception e) {
                throw new IllegalArgumentException("字典转换失败：请注意传入的[arg]类型是否正确");
            }
        }
    }

    @Override
    protected void doDbTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) {

    }


}