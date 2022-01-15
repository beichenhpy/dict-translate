package cn.beichenhpy.dictionary.annotation;


import cn.beichenhpy.dictionary.DictTranslate;
import cn.beichenhpy.dictionary.enums.DictType;

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
 * @see DictTranslate#dictTranslate(Object)
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
    DictType dictType();

    /**
     * 转换后的字典字段名
     * @return 返回名
     */
    String ref();

    /**
     * SIMPLE类型时的拓展
     * @return simple类型拓展
     */
    SimplePlugin simplePlugin() default @SimplePlugin;

    /**
     * CUSTOMIZE模式
     * <p>传入类、方法、参数即可进行翻译 注解信息
     * @return 返回本地翻译需要的注解信息
     */
    CustomizeSignature commonSignature() default @CustomizeSignature;
}
