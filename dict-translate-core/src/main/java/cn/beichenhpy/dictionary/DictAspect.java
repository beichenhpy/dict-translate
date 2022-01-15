package cn.beichenhpy.dictionary;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.lang.reflect.Method;

import static cn.beichenhpy.dictionary.AbstractDictTranslate.NO_TRANSLATE_CLASS_HOLDER;

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

    /**
     * EnableDictTranslate注解的缓存
     */
    private static final SimpleCache<Method, EnableDictTranslate> METHOD_ENABLE_DICT_TRANSLATE_CACHE = new SimpleCache<>();

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
        String mode = enableDictTranslate.mode();
        DictTranslate handler = dictTranslateFactory.getHandler(mode);
        if (handler == null){
            throw new Exception("NoDictTranslateHandler: 无可选择的字典翻译器");
        }
        if (!handler.unsatisfied(point)){
            NO_TRANSLATE_CLASS_HOLDER.set(enableDictTranslate.noTranslate());
            result = handler.dictTranslate(result);
        }
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
        Method method = ReflectUtil.getMethod(clazz, methodSignature.getName(), methodSignature.getParameterTypes());
        if (method == null){
            return null;
        }
        EnableDictTranslate enableDictTranslate = METHOD_ENABLE_DICT_TRANSLATE_CACHE.get(method);
        if (enableDictTranslate == null){
            enableDictTranslate = method.getAnnotation(EnableDictTranslate.class);
            METHOD_ENABLE_DICT_TRANSLATE_CACHE.put(method, enableDictTranslate);
        }
        return enableDictTranslate;
    }

}
