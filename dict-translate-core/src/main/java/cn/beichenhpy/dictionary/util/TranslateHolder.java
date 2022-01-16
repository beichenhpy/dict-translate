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

package cn.beichenhpy.dictionary.util;

import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/16 13:24
 */
public class TranslateHolder {

    protected static final SimpleCache<Method, EnableDictTranslate> METHOD_ENABLE_DICT_TRANSLATE_CACHE = new SimpleCache<>();

    /**
     * 获取EnableDictTranslate注解信息
     * @param point 切点
     * @return 返回注解信息
     */
    public static EnableDictTranslate getEnableDictTranslate(ProceedingJoinPoint point) throws Exception {
        //获取目标对象类
        Class<?> clazz = point.getTarget().getClass();
        //获取方法签名->获取方法名和参数
        Signature signature = point.getSignature();
        //转换为MethodSignature
        MethodSignature methodSignature = Convert.convert(MethodSignature.class,signature);
        //通过目标类反射获取方法对象
        Method method = ReflectUtil.getMethod(clazz, methodSignature.getName(), methodSignature.getParameterTypes());
        if (method == null){
            throw new Exception("no such method");//should not do this
        }
        EnableDictTranslate enableDictTranslate;
        //尝试缓存获取
        enableDictTranslate = METHOD_ENABLE_DICT_TRANSLATE_CACHE.get(method);
        if (enableDictTranslate == null){
            //尝试反射获取
            enableDictTranslate = method.getAnnotation(EnableDictTranslate.class);
            if (enableDictTranslate == null){
                throw new Exception("no such annotation");//should not do this
            }else {
                METHOD_ENABLE_DICT_TRANSLATE_CACHE.put(method, enableDictTranslate);
            }
        }
        return enableDictTranslate;
    }
}
