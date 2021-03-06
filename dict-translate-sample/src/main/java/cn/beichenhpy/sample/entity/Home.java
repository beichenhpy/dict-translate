package cn.beichenhpy.sample.entity;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.SimplePlugin;
import cn.beichenhpy.dictionary.constant.DictType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 20:37
 */
@Data
@Accessors(chain = true)
public class Home {

    private String province;

    private String provinceText;

    @Dict(dictType = DictType.SIMPLE, ref = "okText")
    @SimplePlugin(isRevert = true)
    private String ok;

    private String okText;
}
