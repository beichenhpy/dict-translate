package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.factory.DefaultTranslateHandler;
import cn.beichenhpy.dictionary.processor.impl.DefaultCustomizeProcessor;
import cn.beichenhpy.dictionary.processor.impl.DefaultSimpleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new DefaultTranslateHandler(new DefaultSimpleProcessor(), new DefaultCustomizeProcessor());
    }


    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

}
