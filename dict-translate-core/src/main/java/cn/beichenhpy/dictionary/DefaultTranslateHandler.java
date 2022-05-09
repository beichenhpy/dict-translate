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
import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.dictionary.enums.TranslateStrategy;
import cn.beichenhpy.dictionary.exception.DictionaryTranslateException;
import cn.beichenhpy.dictionary.processor.TranslateProcessor;
import cn.beichenhpy.dictionary.processor.CustomizeProcessor;
import cn.beichenhpy.dictionary.processor.SimpleProcessor;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认策略
 * <p>递归翻译每个字段中带有{@link Dict}的字段
 *
 * @author beichenhpy
 * @version 0.0.1
 * @see SimpleProcessor
 * @see CustomizeProcessor
 * @since 0.0.1
 * <p> 2022/1/14 09:05
 */
@Slf4j
public class DefaultTranslateHandler extends AbstractTranslateHandler {

    //存储Processors
    private final ConcurrentHashMap<String, TranslateProcessor> translateProcessorStorage = new ConcurrentHashMap<>(10);


    public DefaultTranslateHandler(Map<String, TranslateProcessor> translateProcessorMap) {
        translateProcessorStorage.putAll(translateProcessorMap);
    }

    /**
     * 不进行翻译的类，用户输入
     *
     * @see EnableDictTranslate#ignore()
     */
    protected static final ThreadLocal<Class<?>[]> IGNORE_CLASSES_HOLDER = new InheritableThreadLocal<>();

    @Override
    protected void registerHandler() {
        registerHandler(TranslateStrategy.DEFAULT);
    }

    @Override
    protected boolean preCheck(ResultWrapper resultWrapper) {
        if (resultWrapper == null) {
            throw new DictionaryTranslateException("未传入传入参数");
        }
        //fix:增加判空，手动调用时无法初始化注解信息
        if (resultWrapper.getEnableDictTranslate() != null) {
            IGNORE_CLASSES_HOLDER.set(resultWrapper.getEnableDictTranslate().ignore());
        }
        return true;
    }


    @Override
    public Object translate(Object result) {
        Class<?>[] ignoreClasses = IGNORE_CLASSES_HOLDER.get();
        //进入方法先判断是否满足条件?
        if (checkNonBasicOrString(result) && checkNotInBlackList(result, ignoreClasses)) {
            if (result instanceof Collection) {
                for (Object o : ((Collection<?>) result)) {
                    if (checkNonBasicOrString(o) && checkNotInBlackList(o, ignoreClasses)) {
                        translate(o);
                    }
                }
            } else {
                //添加类缓存
                List<Field> fields = getAvailableFields(result, ignoreClasses);
                for (Field field : fields) {
                    //fix 高版本会出现InaccessibleObjectException
                    try {
                        field.setAccessible(true);
                    } catch (Exception e) {
                        log.warn("由于{}的原因,跳过对{}的翻译," +
                                        "可以在EnableDictTranslate注解中的ignore属性添加{}字段以抑制警告",
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
                    if (checkNonBasicOrString(key)) {
                        translate(key);
                    } else {
                        Dict dict = DICT_ANNO_CACHE.get(field);
                        if (dict == null) {
                            dict = field.getAnnotation(Dict.class);
                            DICT_ANNO_CACHE.put(field, dict);
                        }
                        if (dict == null) {
                            continue;
                        }
                        //获取processor翻译每个字段的数据
                        TranslateProcessor translateProcessor = translateProcessorStorage.get(dict.dictType());
                        if (translateProcessor == null) {
                            throw new DictionaryTranslateException("未找到" + dict.dictType() + "对应的translateProcessor");
                        }
                        result = translateProcessor.process(dict, result, key, field);
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected void afterTranslate(ResultWrapper resultWrapper) {
        IGNORE_CLASSES_HOLDER.remove();
    }

}