package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.CommonSignature;
import cn.beichenhpy.dictionary.Dict;
import cn.beichenhpy.dictionary.NeedRecursionTranslate;
import cn.beichenhpy.dictionary.enums.DictType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:03
 */
@Data
@SuperBuilder
public class Teacher implements NeedRecursionTranslate {

    @Dict(dictType = DictType.SIMPLE, ref = "genderDict")
    private Boolean gender;

    private String genderDict;

    @Dict(dictType = DictType.COMMON, ref = "statusDict",
            commonSignature = @CommonSignature(type = StatusEnum.class, method = "getValue", arg = Long.class))
    private Long status;

    private String statusDict;

    private List<LocalDateTime> localDateTimes;

    private List<List<Home>> homes;
}
