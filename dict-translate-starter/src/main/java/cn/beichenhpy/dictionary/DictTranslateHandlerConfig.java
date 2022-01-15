package cn.beichenhpy.dictionary;

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
    public DictTranslate entityTranslate() {
        return new EntityDictTranslateHandler();
    }


    @Bean
    public DictTranslateFactory dictTranslateFactory() {
        return new DictTranslateFactory();
    }

    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }



}
