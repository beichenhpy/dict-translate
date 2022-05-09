package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.enums.DictType;
import cn.beichenhpy.dictionary.factory.DefaultTranslateHandler;
import cn.beichenhpy.dictionary.processor.TranslateProcessor;
import cn.beichenhpy.dictionary.processor.impl.DefaultCustomizeProcessor;
import cn.beichenhpy.dictionary.processor.impl.DefaultSimpleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:12
 */
@Configuration
public class DictTranslateHandlerConfig {
    @Bean
    public TranslateHandler entityTranslate() {
        Map<String, TranslateProcessor> translateProcessorMap = new HashMap<>();
        translateProcessorMap.put(DictType.SIMPLE, new DefaultSimpleProcessor());
        translateProcessorMap.put(DictType.CUSTOMIZE, new DefaultCustomizeProcessor());
        return new DefaultTranslateHandler(translateProcessorMap);
    }


    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

}
