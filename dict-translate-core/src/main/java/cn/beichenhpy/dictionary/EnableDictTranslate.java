package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.enums.TranslateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在哪个方法启用翻译
 * <pre>
 *     <t>@EnableDictTranslate</t>(type = TranslateType.ENTITY)
 *     public IPage&lt;Student&gt; test() {
 *         IPage&lt;Student&gt; page = new Page<>();
 *         page.setRecords(prepare());
 *         return page;
 *     }
 * </pre>
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 08:57
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableDictTranslate {

    /**
     * JSON 直接操作返回值，添加字段
     * ENTITY 对已经有的字段赋值
     *
     * @return 翻译模式
     */
    String mode() default TranslateType.ENTITY;

}
