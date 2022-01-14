package cn.beichenhpy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface EnumSignature {
    /**
     * LOCAL类型时处理
     * 本地枚举类 Object不处理
     * @return 返回枚举类
     */
    Class<?> localEnumClass() default Object.class;

    /**
     * LOCAL类型时处理
     * 本地枚举类对应获取value值的方法
     * @return 返回方法名
     */
    String localEnumMethod() default "";

    /**
     * LOCAL类型时处理
     * 枚举方法的参数类型 默认String
     * @return 枚举方法的参数类型
     */
    Class<?> localEnumMethodParameterType() default String.class;
}
