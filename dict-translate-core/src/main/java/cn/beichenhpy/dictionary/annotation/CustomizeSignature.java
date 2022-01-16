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

package cn.beichenhpy.dictionary.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CUSTOMIZE情况，方法签名
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface CustomizeSignature {
    /**
     * LOCAL类型时处理
     * 本地枚举类 Object不处理
     * @return 返回枚举类
     */
    Class<?> type() default Object.class;

    /**
     * LOCAL类型时处理
     * 本地枚举类对应获取value值的方法
     * @return 返回方法名
     */
    String method() default "";

    /**
     * LOCAL类型时处理
     * 枚举方法的参数类型 默认String
     * @return 枚举方法的参数类型
     */
    Class<?> arg() default String.class;
}
