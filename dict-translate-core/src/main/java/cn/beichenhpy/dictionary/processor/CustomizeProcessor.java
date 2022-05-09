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
import cn.beichenhpy.dictionary.annotation.plugin.CustomizePlugin;
import cn.beichenhpy.dictionary.exception.DictionaryTranslateException;
import cn.beichenhpy.dictionary.processor.TranslateProcessor;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static cn.beichenhpy.dictionary.util.DictionaryUtil.checkFieldClassSameAsAnno;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/19 19:02
 */
@Slf4j
public class CustomizeProcessor implements TranslateProcessor {

    @Override
    public Object process(Dict dict, Object result, Object keyValue, Field field) {
        String ref = dict.ref();
        CustomizePlugin customizePlugin = field.getAnnotation(CustomizePlugin.class);
        if (customizePlugin == null){
            log.warn("该field: {}未添加CustomizePlugin注解", field.getName());
            return result;
        }
        //本地字典表
        Class<?> clazz = customizePlugin.type();
        //不为默认Object则进行转换
        if (!clazz.equals(Object.class)) {
            String methodName = customizePlugin.method();
            if (methodName.isEmpty()) {
                throw new DictionaryTranslateException("字典转换失败：未传入[method]");
            }
            Class<?> parameterType = customizePlugin.arg();
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
            boolean isSameClazz = checkFieldClassSameAsAnno(keyValue, parameterType);
            if (!isSameClazz) {
                throw new DictionaryTranslateException("字典转换失败：检查字段与注解arg参数是否一致，" +
                        "字段类型为:" + keyValue.getClass() + "注解参数类型为:" + parameterType);
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
        }
        return result;
    }

}
