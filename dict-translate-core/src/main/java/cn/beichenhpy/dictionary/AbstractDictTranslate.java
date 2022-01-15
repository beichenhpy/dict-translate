package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.dictionary.factory.EntityDictTranslateHandler;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 翻译抽象接口，主要提供一些方法
 * <p>如需自定义处理方法，则继承该抽象类，实现 {@link #registerHandler()} 和 {@link #dictTranslate(Object)} 即可
 *
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 13:33
 * @see EntityDictTranslateHandler
 * @since 0.0.1
 */
@Slf4j
public abstract class AbstractDictTranslate implements DictTranslate {

    /**
     * 构造函数默认调用子类实现的add方法，将自身注册到TRANSLATE_HANDLERS中
     */
    public AbstractDictTranslate() {
        registerHandler();
    }

    /**
     * 不进行翻译的字段
     */
    protected static final Class<?>[] DEFAULT_TRANSLATE_BLACKLIST = {
            //******************base********************
            Enum.class, Annotation.class
            //*****************日期类*********************
            , Date.class, Calendar.class, Year.class
            , Month.class, LocalDate.class, LocalDateTime.class
            //*****************Map*********************
            , Map.class, HashMap.class, TreeMap.class
            , Hashtable.class, SortedMap.class, WeakHashMap.class
            //*****************Thread*********************
            , ThreadLocal.class
    };

    /**
     * Dict注解信息
     */
    protected static final SimpleCache<Field, Dict> DICT_ANNO_CACHE = new SimpleCache<>();

    /**
     * 不进行翻译的类，用户输入
     *
     * @see EnableDictTranslate#noTranslate()
     */
    protected static final ThreadLocal<Class<?>[]> NO_TRANSLATE_CLASS_HOLDER = new InheritableThreadLocal<>();

    /**
     * 字段值对应的类的可用字段集合缓存
     */
    protected static final SimpleCache<Class<?>, List<Field>> AVAILABLE_FIELD_CACHE = new SimpleCache<>();

    /**
     * 将处理器存放到TRANSLATE_HANDLERS中
     */
    protected abstract void registerHandler();

    /**
     * 翻译
     *
     * @param result 当前对象
     * @throws Exception 异常
     */
    protected abstract Object doTranslate(Object result) throws Exception;

    /**
     * 检查是否为basic/String
     *
     * @param result 实体类
     * @return 是返回true 否则返回false
     */
    protected boolean checkBasic(Object result) {
        return ClassUtil.isBasicType(result.getClass()) || String.class.equals(result.getClass());
    }

    /**
     * 检查注解上的类是否和字段值得一致
     *
     * @param fieldValue 字段值
     * @param arg        注解参数类型
     * @return 是 true 否 false
     */
    protected boolean checkFieldClassSameAsAnno(Object fieldValue, Class<?> arg) {
        return arg.equals(fieldValue.getClass());
    }

    /**
     * 检查是否不在翻译黑名单中
     *
     * @param record             当前类对象
     * @param noTranslateClasses 黑名单
     * @return 不在返回是，否则返回否
     */
    protected boolean checkNotInBlackList(Object record, Class<?>[] noTranslateClasses) {
        return !ArrayUtil.contains(noTranslateClasses, record.getClass()) &&
                !ArrayUtil.contains(DEFAULT_TRANSLATE_BLACKLIST, record.getClass());
    }


    /**
     * 检查字段是否可用
     *
     * @param field              字段
     * @param noTranslateClasses 黑名单
     * @return 可用返回true 否则返回false
     */
    protected boolean checkFieldIsAvailable(Field field, Class<?>[] noTranslateClasses) {
        if (field == null) {
            return false;
        }
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return false;
        }
        Class<?> type = field.getType();
        Class<?> declaringClass = field.getDeclaringClass();
        //如果当前字段所在类属于java.lang,那么直接返回false
        String dp = declaringClass.getPackage().getName();
        log.debug("current package:{}, field name:{}", dp, field.getName());
        if (dp.startsWith("java.lang")) {
            return false;
        }
        if (type.isArray() || declaringClass.isArray()) {
            return false;
        }
        if (type.isAnnotation() || declaringClass.isAnnotation()) {
            return false;
        }
        if (type.isEnum() || declaringClass.isEnum()) {
            return false;
        }
        return !ArrayUtil.contains(DEFAULT_TRANSLATE_BLACKLIST, type)
                && !ArrayUtil.contains(DEFAULT_TRANSLATE_BLACKLIST, declaringClass)
                && !ArrayUtil.contains(noTranslateClasses, type)
                && !ArrayUtil.contains(noTranslateClasses, declaringClass);
    }


    /**
     * 获取所有满足条件的字段
     *
     * @param record 实体
     * @return 返回字段数组
     */
    protected List<Field> getAvailableFields(Object record) {
        Class<?> clazz = record.getClass();
        List<Field> fields = AVAILABLE_FIELD_CACHE.get(clazz);
        if (fields != null) {
            return fields;
        }
        Class<?>[] noTranslateClasses = NO_TRANSLATE_CLASS_HOLDER.get();
        Field[] allFields = ReflectUtil.getFields(clazz);
        fields = Arrays.stream(allFields)
                .filter(field -> checkFieldIsAvailable(field, noTranslateClasses))
                .collect(Collectors.toList());
        AVAILABLE_FIELD_CACHE.put(clazz, fields);
        return fields;
    }
}
