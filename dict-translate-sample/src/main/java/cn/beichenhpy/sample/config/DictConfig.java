package cn.beichenhpy.sample.config;

import cn.beichenhpy.dictionary.DefaultTranslateStrategyHandler;
import cn.beichenhpy.dictionary.TranslateStrategyHandler;
import cn.beichenhpy.dictionary.processor.AbstractTranslateProcessor;
import cn.beichenhpy.dictionary.processor.MethodPluginProcessor;
import cn.beichenhpy.dictionary.processor.SimplePluginProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 手动配置处理器
 * @author beichenhpy
 * <p> 2022/5/9 21:57
 */
@Configuration
public class DictConfig {

    @Resource
    private SimplePluginProcessor simplePluginProcessor;

    @Resource
    private MethodPluginProcessor methodPluginProcessor;

    @Bean
    public TranslateStrategyHandler translateHandler() {
        List<AbstractTranslateProcessor> processors = new ArrayList<>();
        processors.add(simplePluginProcessor);
        processors.add(methodPluginProcessor);
        return new DefaultTranslateStrategyHandler(processors);
    }
}
