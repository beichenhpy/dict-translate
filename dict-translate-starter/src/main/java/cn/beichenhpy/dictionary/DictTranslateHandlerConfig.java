package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.enums.DictType;
import cn.beichenhpy.dictionary.processor.AbstractTranslateProcessor;
import cn.beichenhpy.dictionary.processor.CustomizeProcessor;
import cn.beichenhpy.dictionary.processor.SimpleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
        SimpleProcessor simpleProcessor = new SimpleProcessor(DictType.SIMPLE);
        CustomizeProcessor customizeProcessor = new CustomizeProcessor(DictType.CUSTOMIZE);
        List<AbstractTranslateProcessor> processors = new ArrayList<>();
        processors.add(simpleProcessor);
        processors.add(customizeProcessor);
        return new DefaultTranslateHandler(processors);
    }


    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

}
