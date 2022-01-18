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

package cn.beichenhpy.dictionary.annotation.plugin;

import cn.beichenhpy.dictionary.annotation.plugin.base.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解扩展
 * 因为注解不支持extend因此提供了一些可用的基本注解插件，如果觉得用起来不方便，可以重写此类，为保证SIMPLE和
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/18 19:43
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DictPlugin {

    /**
     * db插件
     *
     * @return 默认为DbPlugin
     */
    DbPlugin dbPlugin() default @DbPlugin;

    /**
     * 方法签名插件
     *
     * @return 返回Signature
     */
    Signature signature() default @Signature;

    /**
     * 方法签名插件
     *
     * @return 返回Signature
     */
    Signature[] signatures() default {};

    /**
     * boolean类型插件
     *
     * @return 返回BoolPlugin
     */
    BoolPlugin whether() default @BoolPlugin;

    /**
     * int类型插件
     *
     * @return Int
     */
    IntPlugin number() default @IntPlugin;

    /**
     * int类型插件
     *
     * @return IntPlugin[]
     */
    IntPlugin[] numbers() default {};

    /**
     * String插件
     *
     * @return StringPlugin
     */
    StringPlugin strValue() default @StringPlugin;

    /**
     * String插件
     *
     * @return StringPlugin[]
     */
    StringPlugin[] strValues() default {};

    /**
     * class插件
     *
     * @return ClassPlugin
     */
    ClassPlugin clazz() default @ClassPlugin;


    /**
     * class插件
     *
     * @return ClassPlugin[]
     */
    ClassPlugin[] classes() default {};
}
