package cn.beichenhpy.dictionary;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 字典翻译接口
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:01
 */
public interface DictTranslate {
    /**
     * 字典翻译,需要实现DictType类型的翻译
     * @see cn.beichenhpy.dictionary.enums.DictType
     * @param joinPoint 切点
     */
    Object dictTranslate(ProceedingJoinPoint joinPoint) throws Throwable;
}
