package cn.beichenhpy.sample;

import cn.beichenhpy.dictionary.AbstractDictTranslate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/16 02:39
 */
@Component
public class JSONDictTranslateHandler extends AbstractDictTranslate {
    @Override
    protected void registerHandler() {
        TRANSLATE_HANDLERS.put("JSON", this);
    }

    @Override
    public Object dictTranslate(Object result) throws Exception {
        return result;
    }

    @Override
    public boolean unsatisfied(ProceedingJoinPoint joinPoint) {
        return false;
    }
}
