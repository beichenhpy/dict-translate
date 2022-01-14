package cn.beichenhpy.factory;


import cn.beichenhpy.DictTranslate;

import java.util.Map;

import static cn.beichenhpy.factory.AbstractDictTranslate.TRANSLATE_HANDLERS;


/**
 * 字典翻译处理器工厂
 * @author han.pengyu
 * @version 1.0.0
 * <p> 2022/1/13 14:47
 * @since 1.0.0
 */

public class DictTranslateFactory {

    public DictTranslate getHandler(String type) {
        for (Map.Entry<String, DictTranslate> entry : TRANSLATE_HANDLERS.entrySet()) {
            if (entry.getKey().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
