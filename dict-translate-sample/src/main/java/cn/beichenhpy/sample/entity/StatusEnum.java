package cn.beichenhpy.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:06
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    AVAILABLE(10041001L, "可用"),
    UNAVAILABLE(10041002L, "不可用");

    private final Long key;
    private final String value;

    public static String getValue(Long key){
        for (StatusEnum value : StatusEnum.values()) {
            if (key.equals(value.getKey())){
                return value.getValue();
            }
        }
        return key + ":不存在";
    }
}
