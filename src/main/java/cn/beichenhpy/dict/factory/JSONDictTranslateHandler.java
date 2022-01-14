package cn.beichenhpy.dict.factory;

import cn.beichenhpy.dict.Dict;
import cn.beichenhpy.dict.enums.TranslateType;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:06
 */
public class JSONDictTranslateHandler extends AbstractDictTranslate{

    @PostConstruct
    @Override
    protected void add() {
        TRANSLATE_HANDLERS.put(TranslateType.JSON, this);
    }

    @Override
    protected void doSimpleTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception {
        //todo
    }

    @Override
    protected void doCommonTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception {
        //todo
    }

    @Override
    protected void doDbTranslate(Object current, Field field, Object fieldValue, String ref, Dict dict) throws Exception {
        //todo
    }
}
