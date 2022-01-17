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
import cn.beichenhpy.dictionary.util.TranslateHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


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
        return handler.dictTranslate(point);
    }

}
