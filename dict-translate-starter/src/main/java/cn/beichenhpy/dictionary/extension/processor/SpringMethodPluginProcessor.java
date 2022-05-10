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

package cn.beichenhpy.dictionary.extension.processor;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.MethodPlugin;
import cn.beichenhpy.dictionary.exception.DictionaryTranslateException;
import cn.beichenhpy.dictionary.extension.annotation.SpringMethodPlugin;
import cn.beichenhpy.dictionary.processor.AbstractTranslateProcessor;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 支持spring bean的方法插件处理器。支持
 *
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/19 19:02
 */
@Slf4j
public class SpringMethodPluginProcessor extends AbstractTranslateProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public SpringMethodPluginProcessor(String dictType) {
        super(dictType);
    }

    @Override
    public Object process(Dict dict, Object result, Object keyValue, Field field) {
        String ref = dict.ref();
        SpringMethodPlugin springMethodPlugin = field.getAnnotation(SpringMethodPlugin.class);
        if (springMethodPlugin == null) {
            log.warn("该field: {},未添加SpringMethodPlugin注解", field.getName());
            return result;
        }
        Class<?> clazz = springMethodPlugin.type();
        String methodName = springMethodPlugin.method();
        Class<?> parameterType = springMethodPlugin.arg();
        Method translateMethod = ReflectUtil.getMethod(clazz, methodName, parameterType);
        if (translateMethod == null) {
            throw new DictionaryTranslateException("字典转换失败：检查传入的[method]是否存在");
        }
        try {
            String beanName = springMethodPlugin.beanName();
            Object instance;
            //通过spring 获取 bean
            try {
                if (StrUtil.isNotBlank(beanName)) {
                    instance = applicationContext.getBean(clazz, beanName);
                } else {
                    instance = applicationContext.getBean(clazz);
                }
            } catch (BeansException e) {
                if (e instanceof NoUniqueBeanDefinitionException){
                    throw new DictionaryTranslateException(clazz.getName() + "该类对应的bean有多个,请设置SpringMethodPlugin的beanName属性");
                } else if (e instanceof NoSuchBeanDefinitionException){
                    throw new DictionaryTranslateException("找不到" + clazz.getName() + "该类对应的bean,检查传入的type是否注册为bean");
                }else {
                    throw new DictionaryTranslateException("bean创建失败");
                }
            }
            Object translateValue = ReflectUtil.invoke(instance, translateMethod, keyValue);
            ReflectUtil.setFieldValue(result, ref, translateValue);
        } catch (Exception e) {
            throw new DictionaryTranslateException("字典转换失败：请注意传入的[arg]类型是否正确", e);
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
