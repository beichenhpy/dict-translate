package cn.beichenhpy.dictionary;


import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.dictionary.factory.AbstractDictTranslate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import static cn.beichenhpy.dictionary.util.TranslateHolder.getEnableDictTranslate;


/**
 * @author beichenhpy
 * 字典翻译切面
 * @see DictTranslate
 * @since 0.0.1
 * @version 0.0.1
 * <p> 2022/1/12 10:42
 */
@Aspect
@Slf4j
public class DictAspect {

    //切点,需要根据注解位置来修改
    @Pointcut(value = "@annotation(cn.beichenhpy.dictionary.annotation.EnableDictTranslate)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object parse(ProceedingJoinPoint point) throws Throwable {
        //获取注解EnableDictTranslate的值
        EnableDictTranslate enableDictTranslate = getEnableDictTranslate(point);
        String mode = enableDictTranslate.mode();
        DictTranslate handler = AbstractDictTranslate.getHandler(mode);
        if (handler == null){
            throw new Exception("NoDictTranslateHandler: 无可选择的字典翻译器");
        }
        return handler.dictTranslate(point);
    }

}
