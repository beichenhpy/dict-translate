package cn.beichenhpy.dictionary;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COMMON情况，方法签名
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface CommonSignature {
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
