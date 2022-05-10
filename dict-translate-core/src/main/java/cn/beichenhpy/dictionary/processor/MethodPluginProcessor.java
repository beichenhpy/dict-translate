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

package cn.beichenhpy.dictionary.processor;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.MethodPlugin;
import cn.beichenhpy.dictionary.exception.DictionaryTranslateException;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * 方法插件处理器，只支持
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/19 19:02
 */
@Slf4j
public class MethodPluginProcessor extends AbstractTranslateProcessor {

    public MethodPluginProcessor(String dictType) {
        super(dictType);
    }

    @Override
    public Object process(Dict dict, Object result, Object keyValue, Field field) {
        String ref = dict.ref();
        MethodPlugin methodPlugin = field.getAnnotation(MethodPlugin.class);
        if (methodPlugin == null){
            log.warn("该field: {},未添加CustomizePlugin注解", field.getName());
            return result;
        }
        Class<?> clazz = methodPlugin.type();
        String methodName = methodPlugin.method();
        Class<?> parameterType = methodPlugin.arg();
        Method translateMethod = ReflectUtil.getMethod(clazz, methodName, parameterType);
        if (translateMethod == null) {
            throw new DictionaryTranslateException("字典转换失败：检查传入的[method]是否存在");
        }
        //判断是否为静态方法
        int modifiers = translateMethod.getModifiers();
        boolean isStatic = Modifier.isStatic(modifiers);
        if (clazz.isEnum() && !isStatic) {
            throw new DictionaryTranslateException("字典转换失败: 传入type为枚举类时，必须传入静态方法method!");
        }
        try {
            Object instance = null;
            if (!isStatic) {
                instance = ReflectUtil.newInstance(clazz);
            }
            Object translateValue = ReflectUtil.invoke(instance, translateMethod, keyValue);
            ReflectUtil.setFieldValue(result, ref, translateValue);
        } catch (Exception e) {
            throw new DictionaryTranslateException("字典转换失败：请注意传入的[arg]类型是否正确", e);
        }
        return result;
    }

}
