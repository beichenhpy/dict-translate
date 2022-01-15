package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.CustomizeSignature;
import cn.beichenhpy.dictionary.Dict;
import cn.beichenhpy.dictionary.SimplePlugin;
import cn.beichenhpy.dictionary.enums.DictType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:03
 */
@Data
@SuperBuilder
public class Teacher {

    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;

    @Dict(dictType = DictType.CUSTOMIZE, ref = "statusDict",
            commonSignature = @CustomizeSignature(type = StatusEnum.class, method = "getValue", arg = Long.class))
    private Long status;

    private String statusDict;

    private List<LocalDateTime> localDateTimes;

    private List<List<Home>> homes;

    private StatusEnum statusEnum;

    private String[] as;

    private SimplePlugin simplePlugin;
}
