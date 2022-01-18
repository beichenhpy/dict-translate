package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.annotation.plugin.CustomizePlugin;
import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.DefaultPlugin;
import cn.beichenhpy.dictionary.enums.DictType;
import cn.beichenhpy.sample.service.FooService;
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

    @Dict(dictType = DictType.CUSTOMIZE, ref = "statusDict",
            defaultPlugin = @DefaultPlugin(
                    customizePlugin = @CustomizePlugin(type = StatusEnum.class, method = "getValue", arg = Long.class)
            ))
    private Long status;

    private String statusDict;

    @Dict(dictType = DictType.CUSTOMIZE, ref = "healthText",
            defaultPlugin = @DefaultPlugin(
                    customizePlugin = @CustomizePlugin(type = FooService.class, method = "getValue", arg = String.class)
            ))
    private String health;

    private String healthText;

    private List<Teacher> teachers;

    private Map<String, String> test;

    private Date date;

    private List<Date> dates;
}
