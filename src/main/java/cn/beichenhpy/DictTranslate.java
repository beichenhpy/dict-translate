package cn.beichenhpy;

/**
 * 字典翻译接口
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:01
 */
public interface DictTranslate {
    /**
     * 字典翻译
     * @param result 原返回值
     */
    Object dictTranslate(Object result) throws Exception;
}
