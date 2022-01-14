package cn.beichenhpy.dictionary.enums;

import lombok.Getter;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 08:54
 */
@Getter
public enum DictType {
    /**
     * 转换如 Boolean/boolean/Integer/int类型的
     * true -> 是 false -> 否
     * 1 -> 是 0 -> 否
     */
    SIMPLE,
    /**
     * 转换数据表
     */
    DB,
    /**
     * 转换本地方法提供数据
     */
    COMMON
}
