package cn.beichenhpy.sample.entity;

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
    private String address;
}
