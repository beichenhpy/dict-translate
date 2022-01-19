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


import cn.beichenhpy.dictionary.TranslateHandler;
import cn.beichenhpy.dictionary.ResultWrapper;
import cn.beichenhpy.dictionary.annotation.plugin.DefaultPlugin;
import cn.beichenhpy.dictionary.annotation.plugin.DictPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典翻译注解<p>
 * 使用方式：注解在作为字典转换的字段上<p>
 * <p>
 * [ENTITY模式举例,需要给出字典翻译字段,返回翻译后的实体类]<p>
 * <pre>
 *     <t>@Dict</t>(dictType = DictType.SIMPLE, ref = "genderDict")
 *     private Boolean gender;
 *
 *     private String genderDict;
 *
 *     <t>@Dict</t>(dictType = DictType.CUSTOMIZE, ref = "statusDict",
 *             commonSignature = @CustomizeSignature(type = StatusEnum.class, method = "getValue", arg = Long.class))
 *     private Long status;
 *
 *     private String statusDict;
 * </pre>
 * <p>
 * @author beichenhpy
 * @see TranslateHandler#dictTranslate(ResultWrapper)
 * @since 0.0.1
 * @version 0.0.1
 * <p> 2021/9/2 13:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    /**
     * 字典类型
     * @return 字典类型
     */
    String dictType();

    /**
     * 转换后的字典字段名
     * @return 返回名
     */
    String ref();

    /**
     * 默认插件
     * @return Default
     */
    DefaultPlugin defaultPlugin() default @DefaultPlugin;
    /**
     * 翻译插件
     * @return 返回插件
     */
    DictPlugin plugin() default @DictPlugin;
}
