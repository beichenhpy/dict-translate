package cn.beichenhpy.factory;

import cn.beichenhpy.CodeDictionary;
import cn.beichenhpy.Dict;
import cn.beichenhpy.DictTranslate;
import cn.beichenhpy.NeedRecursionTranslate;
import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 翻译抽象接口，主要提供一些方法
 * <p>如需自定义处理方法，则继承该抽象类，实现 {@link #add()} 和 {@link #dictTranslate(Object)} 即可
 *
 * @author han.pengyu
 * @version 1.0.0
 * <p> 2022/1/13 13:33
 * @see EntityDictTranslateHandler
 * @see JSONDictTranslateHandler
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractDictTranslate implements DictTranslate {

    //缓存非static字段
    protected static final Map<Class<?>, List<Field>> CLASS_NON_STATIC_FILED_CACHE = new HashMap<>();
    //翻译处理器，存放处理器类型和处理器
    protected static final Map<String, DictTranslate> TRANSLATE_HANDLERS = new HashMap<>();

    /**
     * 将处理器存放到TRANSLATE_HANDLERS中
     */
    protected abstract void add();

    /**
     * 检查是否需要翻译
     *
     * @param key 值
     * @return 是-true 否-false
     */
    protected boolean checkNeedTranslate(Object key) {
        return key instanceof NeedRecursionTranslate;
    }

    /**
     * 获取所有非static变量
     * @param record 实体
     * @return 返回字段数组
     */
    protected List<Field> getNonStaticFiled(Object record) {
        Class<?> clazz = record.getClass();
        List<Field> fields = CLASS_NON_STATIC_FILED_CACHE.get(clazz);
        if (fields != null) {
            return fields;
        }
        Field[] allFields = ReflectUtil.getFields(clazz);
        List<Field> noStaticFields = Arrays.stream(allFields)
                .parallel()
                .filter(Objects::nonNull)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
        CLASS_NON_STATIC_FILED_CACHE.put(clazz, noStaticFields);
        return noStaticFields;
    }

    /**
     * ######################################################ENTITY##############################################################
     * 翻译入口
     *
     * @param record 翻译实体
     */
    @SneakyThrows
    protected void handleTranslate(Object record) {
        if (record instanceof Collection) {
            for (Object o : ((Collection<?>) record)) {
                if (checkNeedTranslate(o)) {
                    handleTranslate(o);
                }
            }
        }
        mainTranslate(record);
    }

    /**
     * 实际翻译方法
     *
     * @param record 翻译对象
     */
    @SneakyThrows(Exception.class)
    protected void mainTranslate(Object record) {
        //添加类缓存
        List<Field> fields = getNonStaticFiled(record);
        for (Field field : fields) {
            //是否为基本类
            field.setAccessible(true);
            //对象key
            Object key = ReflectUtil.getFieldValue(record, field.getName());
            //key的值不存在，则跳过循环
            if (key == null) {
                continue;
            }
            //是否为Collection
            if (key instanceof Collection) {
                for (Object o : ((Collection<?>) key)) {
                    if (checkNeedTranslate(o)) {
                        handleTranslate(o);
                    }
                }
            }
            Dict annotation = field.getAnnotation(Dict.class);
            if (annotation == null) {
                continue;
            }
            String ref = annotation.ref();
            switch (annotation.dictType()) {
                case SIMPLE:
                    doSimpleTrans(key, record, ref);
                    break;
                case LOCAL:
                    doLocalEnumTrans(annotation, ref, key, record);
                    break;
                case TABLE:
                    doTableTrans(record, ref, annotation, key);
                    break;
                default:
                    break;

            }
        }
    }


    /**
     * 翻译简单字段
     *
     * @param key    key值
     * @param record 需要翻译的实体
     * @param ref    需要赋值的字段
     */
    protected void doSimpleTrans(Object key, Object record, String ref) {
        //判断字段类型 boolean 在 getFieldValue时已经装箱为Boolean了
        if (key instanceof Boolean) {
            if (!StringUtils.isEmpty(key)) {
                if (Boolean.TRUE.toString().equals(key.toString())) {
                    ReflectUtil.setFieldValue(record, ref, "是");
                }
                if (Boolean.FALSE.toString().equals(key.toString())) {
                    ReflectUtil.setFieldValue(record, ref, "否");
                }
            }
        }
        if (key instanceof Integer) {
            if (!StringUtils.isEmpty(key)) {
                if ("1".equals(key.toString())) {
                    ReflectUtil.setFieldValue(record, ref, "是");
                }
                if ("0".equals(key.toString())) {
                    ReflectUtil.setFieldValue(record, ref, "是");
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
     * @param annotation 注解
     * @param ref        需要赋值的翻译字段
     * @param key        字典值
     * @param record     翻译实体
     */
    @SneakyThrows
    protected void doLocalEnumTrans(Dict annotation, String ref, Object key, Object record) {
        //本地字典表
        Class<?> enumClass = annotation.localEnumClass();
        //不为默认Object则进行转换
        if (!enumClass.equals(Object.class)) {
            String methodName = annotation.localEnumMethod();
            if (methodName.isEmpty()) {
                throw new IllegalArgumentException("字典转换失败：未传入[localEnumMethod]");
            }
            Class<?> parameterType = annotation.localEnumMethodParameterType();
            Method translateMethod = ReflectUtil.getMethod(enumClass, methodName, parameterType);
            if (translateMethod == null) {
                throw new IllegalArgumentException("字典转换失败：检查传入的[localEnumMethod]是否存在");
            }
            //判断是否为静态方法
            int modifiers = translateMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            if (!isStatic) {
                throw new IllegalArgumentException("字典转换失败：请注意传入的[localEnumMethod]方法，必须为静态方法");
            }
            try {
                Object translateValue = translateMethod.invoke(null, key);
                ReflectUtil.setFieldValue(record, ref, translateValue);
            } catch (Exception e) {
                throw new IllegalArgumentException("字典转换失败：请注意传入的[localEnumMethodParameterType]类型是否正确");
            }
        }
    }

    /**
     * 基于数据库/redis的翻译
     *
     * @param record     需要翻译的实体类
     * @param ref        需要赋值的翻译字段
     * @param annotation 字段上的注解
     * @param key        字段值
     */
    @SneakyThrows
    protected void doTableTrans(Object record, String ref, Dict annotation, Object key) {
        //从数据库取数据
        String dictType = annotation.dictTableType();
        if (dictType.isEmpty()) {
            throw new IllegalArgumentException("字典转换失败：未传入[dictTableType]");
        }
        //根据key查询到对应的dict value
        ReflectUtil.setFieldValue(record, ref, CodeDictionary.getDictValueFromDictionaryList(dictType, key.toString()));
    }
}
