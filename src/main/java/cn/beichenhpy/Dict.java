package cn.beichenhpy;


import cn.beichenhpy.enums.DictType;

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
 *      //TABLE模式
 *      <t>@Dict</t>(dictType = DictType.TABLE,
 *                       ref = "payStatusDict",
 *                       dictTableType = "1001")
 *      private String payStatus;
 *      private String payStatusDict;
 *
 *
 *      //LOCAL模式
 *      //getValue方法必须为静态方法
 *      //localEnumMethodParameterType是getValue的方法参数类型
 *      <t>@Dict</t>(dictType = DictType.LOCAL,
 *                  ref = "payStatusDict",
 *                  localEnumClass = Status.class,
 *                  localEnumMethod = "getValue",
 *                  localEnumMethodParameterType = String.class)
 *      private String status;
 *      private String payStatusDict;
 * </pre>
 * <p>
 * [JSON模式举例,不用写出字典翻译字段,直接返回json数据]<br>
 * <pre>
 *      //SIMPLE模式
 *      <t>@Dict</t>(dictType = DictType.SIMPLE,
 *                      ref = "genderDict")
 *      private Integer gender;
 *
 *      //TABLE模式
 *      <t>@Dict</t>(dictType = DictType.TABLE,
 *                       ref = "payStatusDict",
 *                       dictTableType = "1001")
 *      private String payStatus;
 *
 *
 *      //LOCAL模式
 *      //getValue方法必须为静态方法
 *      //localEnumMethodParameterType是getValue的方法参数类型
 *      <t>@Dict</t>(dictType = DictType.LOCAL,
 *                  ref = "payStatusDict",
 *                  localEnumClass = Status.class,
 *                  localEnumMethod = "getValue",
 *                  localEnumMethodParameterType = String.class)
 *      private String status;
 * </pre>
 * <p>所有需要<t>翻译</t>的实体类，都需要继承<t>NeedTranslate</t>
 * @author han.pengyu
 * @see DictTranslate#dictTranslate(Object)
 * @see NeedRecursionTranslate
 * @since 1.0.0
 * @version 1.0.1
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
     * TABLE类型时 字典对应type值
     * @return type
     */
    DbSignature dbSignature() default @DbSignature;

    /**
     * COMMON模式
     * <p>传入类、方法、参数即可进行翻译 注解信息
     * @return 返回本地翻译需要的注解信息
     */
    CommonSignature commonSignature() default @CommonSignature;
}
