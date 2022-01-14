package cn.beichenhpy;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.*;

/**
 * 字典类
 * @author han.pengyu
 * @version 1.0.1
 * @since 1.0.0
 * <p> 2022/1/13 8:43
 */
@Data
@SuperBuilder
public class CodeDictionary {
    private String dictType;
    private String dictKey;
    private String dictValue;
    private int sort;
    private String status;
    private String remarks;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public static List<CodeDictionary> getDictionaryByType(String dictType) {
        List<CodeDictionary> codeDictionaries = new LinkedList<>();
        //todo 数据查询
        codeDictionaries = fake();
        return codeDictionaries;
    }
    //todo delete fake data
    private static List<CodeDictionary> fake(){
        CodeDictionary yes = CodeDictionary.builder().dictKey("10041001").dictValue("是").dictType("1004").build();
        CodeDictionary no = CodeDictionary.builder().dictKey("10041002").dictValue("否").dictType("1004").build();
        return new ArrayList<>(Arrays.asList(yes, no));
    }


    /**
     * 在字典集合中查询某个key的value值，如果不存在则返回
     * <p> example
     * <pre>
     *     10041001:[未定义该字典值]
     * </pre>
     * @param dictType 字典类型
     * @param dictKey key
     * @return 字典值
     */
    public static String getDictValueFromDictionaryList(String dictType, String dictKey) {
        return getDictValueFromDictionaryList(getDictionaryByType(dictType), dictKey);
    }


    /**
     * 在字典集合中查询某个key的value值，如果不存在则返回
     * <p> example
     * <pre>
     *     10041001:[未定义该字典值]
     * </pre>
     * @param codeDictionaries 字典集合
     * @param dictKey key
     * @return 字典值
     */
    public static String getDictValueFromDictionaryList(List<CodeDictionary> codeDictionaries, String dictKey) {
        return codeDictionaries.stream()
                .parallel()
                .filter(codeDictionary -> codeDictionary.getDictKey().equals(dictKey))
                .map(CodeDictionary::getDictValue)
                .findFirst()
                .orElse(dictKey + ":[未定义该字典值]");
    }

}
