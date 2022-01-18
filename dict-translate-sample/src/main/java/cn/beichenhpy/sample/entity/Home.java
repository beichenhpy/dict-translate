package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.DbPlugin;
import cn.beichenhpy.dictionary.annotation.plugin.DictPlugin;
import cn.beichenhpy.dictionary.annotation.plugin.base.StringPlugin;
import cn.beichenhpy.dictionary.enums.DictType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 20:37
 */
@Data
@Accessors(chain = true)
public class Home {

    @Dict(dictType = DictType.DB, ref = "provinceText",
    plugin = @DictPlugin(
            dbPlugin = @DbPlugin(table = "province", code = "code")
    ))
    private String province;

    private String provinceText;

    @Dict(dictType = "xxx", ref = "statusText",
    plugin = @DictPlugin(
            strValue = @StringPlugin(values = {"status", "_"})
    ))
    private String status;

    private String statusText;
}
