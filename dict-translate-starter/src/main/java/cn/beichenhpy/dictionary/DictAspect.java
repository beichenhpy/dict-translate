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


import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.dictionary.factory.AbstractDictTranslate;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


/**
 * @author beichenhpy
 * 字典翻译切面
 * @version 0.0.1
 * <p> 2022/1/12 10:42
 * @see DictTranslate
 * @since 0.0.1
 */
@Aspect
@Slf4j
public class DictAspect {

    //切点,需要根据注解位置来修改
    @Pointcut(value = "@annotation(enableDictTranslate)")
    public void pointCut(EnableDictTranslate enableDictTranslate) {

    }

    @Around("pointCut(enableDictTranslate)")
    public Object parse(ProceedingJoinPoint point, EnableDictTranslate enableDictTranslate) throws Throwable {
        //获取handler
        DictTranslate handler = AbstractDictTranslate.getHandler(enableDictTranslate.mode());
        if (handler == null) {
            throw new Exception("NoDictTranslateHandler: 无可选择的字典翻译器");
        }
        return handler.dictTranslate(wrapper(point, enableDictTranslate));
    }

    /**
     * 包装原有方法
     * @param point 切点
     * @param enableDictTranslate 注解
     * @return 返回包装类
     * @throws Throwable 异常
     */
    private ResultWrapper wrapper(ProceedingJoinPoint point ,EnableDictTranslate enableDictTranslate) throws Throwable {
        ResultWrapper resultWrapper = new ResultWrapper();
        //获取目标对象类
        Class<?> clazz = point.getTarget().getClass();
        //获取方法签名->获取方法名和参数
        Signature signature = point.getSignature();
        //转换为MethodSignature
        MethodSignature methodSignature = Convert.convert(MethodSignature.class,signature);
        //通过目标类反射获取方法对象
        Method method = ReflectUtil.getMethod(clazz, methodSignature.getName(), methodSignature.getParameterTypes());
        resultWrapper.setResult(point.proceed());
        resultWrapper.setEnableDictTranslate(enableDictTranslate);
        resultWrapper.setTargetClass(clazz);
        resultWrapper.setTargetMethod(method);
        return resultWrapper;
    }

}
