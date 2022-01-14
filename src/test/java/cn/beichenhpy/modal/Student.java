package cn.beichenhpy.modal;

import cn.beichenhpy.dict.Dict;
import cn.beichenhpy.dict.NeedRecursionTranslate;
import cn.beichenhpy.dict.enums.DictType;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:41
 */
public class Student implements NeedRecursionTranslate {

    @Dict(dictType = DictType.COMMON, ref = "genderDict")
    private Boolean gender;

    private String genderDict;
}
