package cn.beichenhpy.sample.service;

import cn.beichenhpy.dictionary.ResultWrapper;
import cn.beichenhpy.dictionary.factory.AbstractTranslateHandler;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/16 14:20
 */
@Component
public class JSONTranslateHandler extends AbstractTranslateHandler {
    @Override
    protected void registerHandler() {
        registerHandler("JSON");
    }

    @Override
    protected boolean preCheck(ResultWrapper resultWrapper) {
        return true;
    }

    @Override
    protected Object translate(Object result) {
        return result;
    }
}