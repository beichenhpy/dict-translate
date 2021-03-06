/*
 * MIT License
 *
 * Copyright (c) 2022-2032 Pengyu Han
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 翻译抽象接口，主要提供一些方法
 * <p>如需自定义处理方法，则继承该抽象类，实现 {@link #registerHandler()} 和 {@link #translate(Object)}} 即可
 *
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 13:33
 * @see DefaultTranslateStrategyHandler
 * @since 0.0.1
 */
@Slf4j
public abstract class AbstractTranslateStrategyHandler implements TranslateStrategyHandler {

    /**
     * 构造函数默认调用子类实现的add方法，将自身注册到TRANSLATE_HANDLERS中
     */
    public AbstractTranslateStrategyHandler() {
        registerHandler();
        log.trace("{} register success, current handlers {}", this.getClass(), TRANSLATE_STRATEGY_HANDLERS);
    }

    /**
     * 不进行翻译的字段
     */
    protected static final Class<?>[] DEFAULT_TRANSLATE_BLACKLIST = {
            //*****************日期类*********************
            Date.class, Calendar.class
            //*****************Map*********************
            , Map.class, HashMap.class, TreeMap.class
            , Hashtable.class, SortedMap.class, WeakHashMap.class
    };

    /**
     * 翻译处理器，存放处理器类型和处理器 runtime时只会进行get操作，线程安全
     */
    protected static final Map<String, TranslateStrategyHandler> TRANSLATE_STRATEGY_HANDLERS = new HashMap<>();
    /**
     * Dict注解信息
     */
    protected static final SimpleCache<Field, Dict> DICT_ANNO_CACHE = new SimpleCache<>();

    /**
     * 字段值对应的类的可用字段集合缓存
     */
    protected static final SimpleCache<Class<?>, List<Field>> AVAILABLE_FIELD_CACHE = new SimpleCache<>();

    /**
     * 将处理器存放到TRANSLATE_HANDLERS中
     */
    protected abstract void registerHandler();

    /**
     * 将处理器存放到TRANSLATE_HANDLERS中
     *
     * @param type 类型
     */
    protected void registerHandler(String type) {
        TRANSLATE_STRATEGY_HANDLERS.put(type, this);
    }


    /**
     * 预检查
     *
     * @param resultWrapper 包装结果类
     * @return 返回是否满足
     * @throws Throwable 异常
     */
    protected abstract boolean preCheck(ResultWrapper resultWrapper) throws Throwable;

    /**
     * 真正的翻译
     *
     * @param result 返回值
     * @return 翻译后的返回值
     * @throws Throwable 异常
     */
    protected abstract Object translate(Object result) throws Throwable;


    /**
     * 翻译之后执行的方法
     *
     * @throws Throwable 异常
     */
    protected abstract void afterTranslate(ResultWrapper resultWrapper) throws Throwable;


    /**
     * 翻译方法<p>
     * 实现接口的方法，然后自定义
     *
     * @param resultWrapper 包装结果类
     * @return 返回翻译后的值
     * @throws Throwable 异常
     */
    @Override
    public Object dictTranslate(ResultWrapper resultWrapper) throws Throwable {
        Object result = resultWrapper.getResult();
        //预检查
        if (preCheck(resultWrapper)) {
            result = translate(result);
        }
        //后置
        afterTranslate(resultWrapper);
        return result;
    }

    /**
     * 检查是否不是basic/String
     *
     * @param result 实体类
     * @return 是返回false 否则返回true
     */
    protected boolean checkNonBasicOrString(Object result) {
        return !ClassUtil.isBasicType(result.getClass()) && !String.class.equals(result.getClass());
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
     * @param field         字段
     * @param ignoreClasses 黑名单
     * @return 可用返回true 否则返回false
     */
    protected boolean checkFieldIsAvailable(Field field, Class<?>[] ignoreClasses) {
        if (field == null) {
            return false;
        }
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return false;
        }
        Class<?> type = field.getType();
        Class<?> declaringClass = field.getDeclaringClass();
        //如果当前字段所在类属于java.lang/java.time,那么直接返回false
        String dp = declaringClass.getPackage().getName();
        log.debug("current package:{}, field name:{}", dp, field.getName());
        if (dp.startsWith("java.lang")) {
            return false;
        }
        if (dp.startsWith("java.time")) {
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
                && !ArrayUtil.contains(ignoreClasses, type)
                && !ArrayUtil.contains(ignoreClasses, declaringClass);
    }


    /**
     * 获取所有满足条件的字段
     *
     * @param record 实体
     * @return 返回字段数组
     */
    protected List<Field> getAvailableFields(Object record, Class<?>[] ignoreClasses) {
        Class<?> clazz = record.getClass();
        List<Field> fields = AVAILABLE_FIELD_CACHE.get(clazz);
        if (fields != null) {
            return fields;
        }
        Field[] allFields = ReflectUtil.getFields(clazz);
        fields = Arrays.stream(allFields)
                .filter(field -> checkFieldIsAvailable(field, ignoreClasses))
                .collect(Collectors.toList());
        AVAILABLE_FIELD_CACHE.put(clazz, fields);
        return fields;
    }

    /**
     * 获取Handler
     *
     * @param strategy 类型
     * @return 返回处理器
     */
    public static TranslateStrategyHandler getStrategyHandler(String strategy) {
        for (Map.Entry<String, TranslateStrategyHandler> entry : TRANSLATE_STRATEGY_HANDLERS.entrySet()) {
            if (entry.getKey().equals(strategy)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
