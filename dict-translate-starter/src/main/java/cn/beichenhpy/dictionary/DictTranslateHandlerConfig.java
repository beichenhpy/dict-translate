package cn.beichenhpy.dictionary;

import cn.beichenhpy.dictionary.factory.DefaultTranslateHandler;
import cn.beichenhpy.dictionary.processor.CustomizeTranslateProcessor;
import cn.beichenhpy.dictionary.processor.DefaultCustomizeProcessor;
import cn.beichenhpy.dictionary.processor.DefaultSimpleProcessor;
import cn.beichenhpy.dictionary.processor.SimpleTranslateProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:12
 */
@Configuration
public class DictTranslateHandlerConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Bean
    public SimpleTranslateProcessor simpleTranslateProcessor(){
        return new DefaultSimpleProcessor();
    }

    @Bean
    public CustomizeTranslateProcessor customizeTranslateProcessor(){
        return new DefaultCustomizeProcessor();
    }

    @Bean
    public TranslateHandler entityTranslate() {
        SimpleTranslateProcessor simpleTranslateProcessor = applicationContext.getBean(SimpleTranslateProcessor.class);
        CustomizeTranslateProcessor customizeTranslateProcessor = applicationContext.getBean(CustomizeTranslateProcessor.class);
        return new DefaultTranslateHandler(simpleTranslateProcessor, customizeTranslateProcessor);
    }


    @Bean
    public DictAspect dictAspect() {
        return new DictAspect();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
