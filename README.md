# dict-translate

作为一个用于字典翻译的工具包

## 使用方式

### 注册翻译处理器

1. 选择你需要的处理器进行注册，使用默认策略
如果不注册对应的处理器，使用对应的`DictType`则会报错
```java
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
```

### SIMPLE模式

1. 简单类型的翻译，主要是对 true/false 0/1 进行对应的翻译 支持：直接赋值和反序 默认 true->是 false->否 0->否 1—>是

```java
class User {

    //默认使用 true->是 false->否
    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;


    //finishDict填充时 会按照 true->否 false->是 填充
    @Dict(dictType = DictType.SIMPLE, ref = "finishDict")
    @SimplePlugin(isRevert = true)
    private Boolean finish;

    private String finishDict;
    
    
    public User(Boolean gender, Boolean finish){
        this.finish = finish;
        this.gender = gender;
    }
}

class Test{

    @EnableDictTranslate(mode = TranslateStrategy.DEFAULT)
    public User getUser(){
        return new User(true, true);
    }
}
```
### Customize模式，用户可以根据传入的方法签名进行翻译。
待完善
