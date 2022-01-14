package cn.beichenhpy.dictionary;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 11:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface SimplePlugin {

    /**
     * 是否反序
     * false 0->否 1->是 false->否 true->是
     * @return false or true
     */
    boolean isRevert() default false;
}
