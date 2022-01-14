package cn.beichenhpy.factory;

import cn.beichenhpy.enums.TranslateType;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:06
 */
public class JSONDictTranslateHandler extends AbstractDictTranslate{
    @SneakyThrows
    @Override
    public Object dictTranslate(Object result) {
        //todo 实现直接修改结果集，添加翻译后的字段
        return result;
    }

    @PostConstruct
    @Override
    protected void add() {
        TRANSLATE_HANDLERS.put(TranslateType.JSON, this);
    }
}
