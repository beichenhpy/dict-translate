package cn.beichenhpy.dictionary;


import cn.beichenhpy.dictionary.factory.DictTranslateFactory;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author beichenhpy
 * 字典翻译切面
 * @see DictTranslate
 * @see DictTranslateFactory
 * @since 0.0.1
 * @version 0.0.1
 * <p> 2022/1/12 10:42
 */
@Aspect
@Slf4j
public class DictAspect {

    @Resource
    private DictTranslateFactory dictTranslateFactory;

    //切点,需要根据注解位置来修改
    @Pointcut(value = "@annotation(cn.beichenhpy.dictionary.EnableDictTranslate)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object parse(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        //获取注解EnableDictTranslate的值
        EnableDictTranslate enableDictTranslate = getEnableDictTranslate(point);
        if (enableDictTranslate == null) {
            throw new Exception("NoSuchMethodException: 目标方法不存在");
        }
        String type = enableDictTranslate.type();
        DictTranslate handler = dictTranslateFactory.getHandler(type);
        if (handler == null){
            throw new Exception("NoDictTranslateHandler: 无可选择的字典翻译器");
        }
        handler.dictTranslate(result);
        return result;
    }

    /**
     * 获取注解
     *
     * @param point 切点
     * @return 返回注解
     */
    private EnableDictTranslate getEnableDictTranslate(ProceedingJoinPoint point) {
        //获取目标对象类
        Class<?> clazz = point.getTarget().getClass();
        //获取方法签名->获取方法名和参数
        Signature signature = point.getSignature();
        //转换为MethodSignature
        MethodSignature methodSignature = Convert.convert(MethodSignature.class,signature);
        //通过目标类反射获取方法对象
        try {
            Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            return method.getAnnotation(EnableDictTranslate.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
