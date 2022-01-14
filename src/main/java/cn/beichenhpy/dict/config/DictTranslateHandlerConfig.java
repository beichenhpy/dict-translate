package cn.beichenhpy.dict.config;

import cn.beichenhpy.dict.DictAspect;
import cn.beichenhpy.dict.DictTranslate;
import cn.beichenhpy.dict.factory.DictTranslateFactory;
import cn.beichenhpy.dict.factory.EntityDictTranslateHandler;
import cn.beichenhpy.dict.factory.JSONDictTranslateHandler;
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
    public DictTranslate jsonTranslate() {
        return new JSONDictTranslateHandler();
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
