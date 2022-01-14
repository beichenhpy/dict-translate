package cn.beichenhpy.enums;

/**
 * 字典转换类型
 * 如果有其他类型，可以继承此接口
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 08:55
 */
public interface TranslateType {
    /**
     * JSON类型转换
     * @see cn.beichenhpy.factory.EntityDictTranslateHandler
     */
    String JSON = "JSON";

    /**
     * ENTITY
     * @see cn.beichenhpy.factory.JSONDictTranslateHandler
     */
    String ENTITY = "ENTITY";
}
