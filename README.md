# dict-translate
## 作为一个用于字典翻译的工具包
### 使用方式
1. SIMPLE模式，按例子加上注解后，即可对Boolean、Integer等简单的进行翻译,通过`simplePlugin = @SimplePlugin(isRevert = true)`可以实现反转  
```java
//实体类
@SuperBuilder
public class Student {

    @Dict(dictType = DictType.SIMPLE, ref = "genderDict", simplePlugin = @SimplePlugin(isRevert = true))
    private Boolean gender;

    private String genderDict;
}
```
方法
```java
@EnableDictTranslate(type = TranslateType.ENTITY)
 public List<Student> prepare() {
        Student student = Student.builder().gender(false).build();
        Student student1 = Student.builder().gender(true).build();
        return new ArrayList<>(Arrays.asList(student, student1));
    }
```
待完善。。。
