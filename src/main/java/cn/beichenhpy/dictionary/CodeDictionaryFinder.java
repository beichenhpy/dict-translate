package cn.beichenhpy.dictionary;

import java.util.Map;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 09:18
 */
public interface CodeDictionaryFinder<K,V> {
    Map<K, V> findCodeDictionary();
}
