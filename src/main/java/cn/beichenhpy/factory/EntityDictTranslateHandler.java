package cn.beichenhpy.factory;

import cn.beichenhpy.enums.TranslateType;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;

/**
 * 实体类翻译<p>
 * 需要定义好对应的字段<p>
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:05
 */
public class EntityDictTranslateHandler extends AbstractDictTranslate {


    @SneakyThrows
    @Override
    public Object dictTranslate(Object result) {
        handleTranslate(result);
        return result;
    }

    @PostConstruct
    @Override
    protected void add() {
        TRANSLATE_HANDLERS.put(TranslateType.ENTITY, this);
    }
}