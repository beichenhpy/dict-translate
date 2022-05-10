package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.MethodPlugin;
import cn.beichenhpy.dictionary.constant.DictType;
import cn.beichenhpy.dictionary.extension.annotation.SpringMethodPlugin;
import cn.beichenhpy.dictionary.extension.constant.SpringDictType;
import cn.beichenhpy.sample.service.FooService;
import cn.beichenhpy.sample.service.FooUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:05
 */
@Data
@Accessors(chain = true)
public class Student {

    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;

    @Dict(dictType = DictType.METHOD, ref = "statusDict")
    @MethodPlugin(type = StatusEnum.class, method = "getValue", arg = Long.class)
    private Long status;

    private String statusDict;

    @Dict(dictType = SpringDictType.SPRING_METHOD, ref = "healthText")
    @SpringMethodPlugin(type = FooUtil.class, method = "getValue", arg = String.class)
    private String health;

    private String healthText;

    private List<Teacher> teachers;

    private Map<String, String> test;

    private Date date;

    private List<Date> dates;
}
