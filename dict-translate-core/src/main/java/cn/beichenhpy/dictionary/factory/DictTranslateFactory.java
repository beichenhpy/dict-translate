package cn.beichenhpy.dictionary.factory;


import cn.beichenhpy.dictionary.DictTranslate;

import java.util.HashMap;
import java.util.Map;


/**
 * 字典翻译处理器工厂
 * @author beichenhpy
 * @version 0.0.1
 * <p> 2022/1/13 14:47
 * @since 0.0.1
 */

public class DictTranslateFactory {

    /**
     * 翻译处理器，存放处理器类型和处理器 runtime时只会进行get操作，线程安全
     */
    protected static final Map<String, DictTranslate> TRANSLATE_HANDLERS = new HashMap<>();

    public DictTranslate getHandler(String type) {
        for (Map.Entry<String, DictTranslate> entry : TRANSLATE_HANDLERS.entrySet()) {
            if (entry.getKey().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
