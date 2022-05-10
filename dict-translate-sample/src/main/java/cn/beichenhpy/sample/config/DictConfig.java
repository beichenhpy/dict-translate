package cn.beichenhpy.sample.config;

import cn.beichenhpy.dictionary.DefaultTranslateStrategyHandler;
import cn.beichenhpy.dictionary.TranslateStrategyHandler;
import cn.beichenhpy.dictionary.extension.processor.SpringMethodPluginProcessor;
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

    @Resource
    private SpringMethodPluginProcessor springMethodPluginProcessor;

    @Bean
    public TranslateStrategyHandler translateHandler() {
        List<AbstractTranslateProcessor> processors = new ArrayList<>();
        //简单的转换
        processors.add(simplePluginProcessor);
        //无构造函数类的普通方法或静态方法处理
        processors.add(methodPluginProcessor);
        //spring bean的方法处理
        processors.add(springMethodPluginProcessor);
        return new DefaultTranslateStrategyHandler(processors);
    }
}
