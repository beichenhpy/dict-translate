# dict-translate

作为一个用于字典翻译的工具包

## 使用方式

### SIMPLE模式

1. 简单类型的翻译，主要是对 true/false 0/1 进行对应的翻译 支持：直接赋值和反序 默认 true->是 false->否 0->否 1—>是

```java
class User {

    //默认使用 true->是 false->否
    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;


    //finishDict填充时 会按照 true->否 false->是 填充
    @Dict(dictType = DictType.SIMPLE, ref = "finishDict",
            plugin = @DictPlugin(
                    simplePlugin = @SimplePlugin(isRevert = true)
            ))
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
