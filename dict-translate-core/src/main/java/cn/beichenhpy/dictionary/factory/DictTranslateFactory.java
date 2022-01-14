package cn.beichenhpy.dictionary.factory;


import cn.beichenhpy.dictionary.DictTranslate;

import java.util.Map;


/**
 * 字典翻译处理器工厂
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 14:47
 * @since 0.0.1
 */

public class DictTranslateFactory {

    public DictTranslate getHandler(String type) {
        for (Map.Entry<String, DictTranslate> entry : AbstractDictTranslate.TRANSLATE_HANDLERS.entrySet()) {
            if (entry.getKey().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
