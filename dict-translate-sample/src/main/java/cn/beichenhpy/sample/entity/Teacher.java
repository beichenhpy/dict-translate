package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.MethodPlugin;
import cn.beichenhpy.dictionary.annotation.plugin.SimplePlugin;
import cn.beichenhpy.dictionary.constant.DictType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:03
 */
@Data
@Accessors(chain = true)
public class Teacher {

    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;

    @Dict(dictType = DictType.METHOD, ref = "statusDict")
    @MethodPlugin(type = StatusEnum.class, method = "getValue", arg = Long.class)
    private Long status;

    private String statusDict;

    private List<LocalDateTime> localDateTimes;

    private List<List<Home>> homes;

    private StatusEnum statusEnum;

    private String[] as;

    private SimplePlugin simplePlugin;
}
