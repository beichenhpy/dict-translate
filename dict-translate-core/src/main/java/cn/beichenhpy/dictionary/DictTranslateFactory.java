package cn.beichenhpy.dictionary;


import java.util.Map;

import static cn.beichenhpy.dictionary.AbstractDictTranslate.TRANSLATE_HANDLERS;


/**
 * 字典翻译处理器工厂
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 14:47
 * @since 0.0.1
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
