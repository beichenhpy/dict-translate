package cn.beichenhpy.sample.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * <p> 2022/5/10 13:22
 */
@Component
@AllArgsConstructor
public class FooUtil {

    private final FooService fooService;


    public String getValue(String key){
        return fooService.getValue(key);
    }
}
