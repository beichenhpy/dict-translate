package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.extension.constant.SpringDictType;
import cn.beichenhpy.dictionary.extension.processor.SpringMethodPluginProcessor;
import cn.beichenhpy.dictionary.processor.MethodPluginProcessor;
import cn.beichenhpy.dictionary.processor.SimplePluginProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置文件，只提供默认的三个处理器和切面配置，需要按需引入processor
 *
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:12
 */
@Configuration
public class DictTranslateHandlerConfig {

    @Bean
    public SimplePluginProcessor simplePluginProcessor() {
        return new SimplePluginProcessor(SpringDictType.SIMPLE);
    }

    @Bean
    public MethodPluginProcessor methodPluginProcessor() {
        return new MethodPluginProcessor(SpringDictType.METHOD);
    }

    @Bean
    public SpringMethodPluginProcessor springMethodPluginProcessor() {
        return new SpringMethodPluginProcessor(SpringDictType.SPRING_METHOD);
    }

    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

}
