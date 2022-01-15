package cn.beichenhpy.dictionary.factory;

import cn.beichenhpy.dictionary.Dict;
import cn.beichenhpy.dictionary.DictTranslate;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

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
 * <p>如需自定义处理方法，则继承该抽象类，实现 {@link #registerHandler()} 和 {@link #dictTranslate(Object, Class[])} 即可
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
            //*****************日期类*********************
            Date.class, Calendar.class, Year.class
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
     * 翻译处理器，存放处理器类型和处理器 runtime时只会进行get操作，线程安全
     */
    protected static final Map<String, DictTranslate> TRANSLATE_HANDLERS = new HashMap<>();

    /**
     * 将处理器存放到TRANSLATE_HANDLERS中
     */
    protected abstract void registerHandler();

    /**
     * 处理SIMPLE类型的翻译
     *
     * @param current    当前对象值
     * @param field      字段
     * @param fieldValue 当前字段值
     * @param ref        赋值字段
     * @param dict       注解
     * @throws Exception 异常
     */
    protected abstract Object doSimpleTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception;

    /**
     * 处理CUSTOMIZE类型的翻译
     *
     * @param current    当前对象值
     * @param field      字段
     * @param fieldValue 当前字段值
     * @param ref        赋值字段
     * @param dict       注解
     * @throws Exception 异常
     */
    protected abstract Object doCustomizeTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception;


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
        Class<? extends Field> clazz = field.getClass();
        if (type.isArray() || clazz.isArray()) {
            return false;
        }
        if (type.isAnnotation() || clazz.isAnnotation()) {
            return false;
        }
        if (type.isEnum() || clazz.isEnum()) {
            return false;
        }
        return !ArrayUtil.contains(DEFAULT_TRANSLATE_BLACKLIST, type) && !ArrayUtil.contains(noTranslateClasses, type);
    }


    /**
     * 获取所有满足条件的字段
     *
     * @param record 实体
     * @return 返回字段数组
     */
    protected List<Field> getAvailableFields(Object record, Class<?>[] noTranslateClasses) {
        Class<?> clazz = record.getClass();
        Field[] allFields = ReflectUtil.getFields(clazz);
        return Arrays.stream(allFields)
                .parallel()
                .filter(field -> checkFieldIsAvailable(field, noTranslateClasses))
                .collect(Collectors.toList());
    }
}
