package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.enums.DictType;
import cn.beichenhpy.dictionary.processor.CustomizeProcessor;
import cn.beichenhpy.dictionary.processor.SimpleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置文件，只提供默认的两个处理器和切面配置，需要按需引入processor
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:12
 */
@Configuration
public class DictTranslateHandlerConfig {

    @Bean
    public SimpleProcessor simpleProcessor(){
        return new SimpleProcessor(DictType.SIMPLE);
    }

    @Bean
    public CustomizeProcessor customizeProcessor(){
        return new CustomizeProcessor(DictType.CUSTOMIZE);
    }


    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

}
