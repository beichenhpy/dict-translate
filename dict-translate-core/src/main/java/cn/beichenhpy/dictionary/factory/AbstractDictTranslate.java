package cn.beichenhpy.dictionary.factory;

import cn.beichenhpy.dictionary.Dict;
import cn.beichenhpy.dictionary.DictTranslate;
import cn.beichenhpy.dictionary.NeedRecursionTranslate;
import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 翻译抽象接口，主要提供一些方法
 * <p>如需自定义处理方法，则继承该抽象类，实现 {@link #add()} 和 {@link #dictTranslate(Object)} 即可
 *
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 13:33
 * @see EntityDictTranslateHandler
 * @see JSONDictTranslateHandler
 * @since 0.0.1
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
     * 处理SIMPLE类型的翻译
     * @param current 当前对象值
     * @param field 字段
     * @param fieldValue 当前字段值
     * @param ref 赋值字段
     * @param dict 注解
     * @throws Exception 异常
     */
    protected abstract void doSimpleTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception;

    /**
     * 处理COMMON类型的翻译
     * @param current 当前对象值
     * @param field 字段
     * @param fieldValue 当前字段值
     * @param ref 赋值字段
     * @param dict 注解
     * @throws Exception 异常
     */
    protected abstract void doCommonTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception;

    /**
     * 处理DB类型的翻译
     * @param current 当前对象值
     * @param field 字段
     * @param fieldValue 当前字段值
     * @param ref 赋值字段
     * @param dict 注解
     * @throws Exception 异常
     */
    protected abstract void doDbTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception;


    @Override
    public Object dictTranslate(Object result) throws Exception {
        handleTranslate(result);
        return result;
    }

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
     *
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
                    doSimpleTranslate(record, field, key, ref, annotation);
                    break;
                case COMMON:
                    doCommonTranslate(record, field, key, ref, annotation);
                    break;
                case DB:
                    doDbTranslate(record, field, key, ref, annotation);
                    break;
                default:
                    break;

            }
        }
    }
}
