package cn.beichenhpy.dictionary;


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
 *      //SIMPLE模式
 *      <t>@Dict</t>(dictType = DictType.SIMPLE,
 *                      ref = "genderDict")
 *      private Integer gender;
 *      private String genderDict;
 *
 *
 *
 *      //COMMON模式
 *      //localEnumMethodParameterType是getValue的方法参数类型
 *      <t>@Dict</t>(dictType = DictType.COMMON,
 *                  ref = "payStatusDict",
 *                  localEnumClass = Status.class,
 *                  localEnumMethod = "getValue",
 *                  localEnumMethodParameterType = String.class)
 *      private String status;
 *      private String payStatusDict;
 * </pre>
 * <p>
 * <p>所有需要<t>翻译</t>的实体类，都需要继承<t>NeedTranslate</t>
 * @author beichenhpy
 * @see DictTranslate#dictTranslate(Object)
 * @see NeedRecursionTranslate
 * @since o.0.1
 * @version 0.9.1
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
     * COMMON模式
     * <p>传入类、方法、参数即可进行翻译 注解信息
     * @return 返回本地翻译需要的注解信息
     */
    CommonSignature commonSignature() default @CommonSignature;
}
