package cn.beichenhpy.sample.controller;

import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.sample.entity.Home;
import cn.beichenhpy.sample.entity.Student;
import cn.beichenhpy.sample.service.FooService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:16
 */
@Slf4j
@RestController
public class FooController {
    @Resource
    private FooService fooService;

    @GetMapping("/test")
    @EnableDictTranslate
    public ResponseEntity<IPage<Student>> test() {
        IPage<Student> test = fooService.test();
        return ResponseEntity.ok(test);
    }


    @GetMapping("/test1")
    @EnableDictTranslate(strategy = "JSON", ignore = {HttpStatus.class})
    public ResponseEntity<List<Student>> test1() {
        List<Student> test = fooService.test1();
        return ResponseEntity.ok(test);
    }

    @GetMapping("/test3")
    public ResponseEntity<Set<Student>> test3() {
        Set<Student> test = fooService.test3();
        return ResponseEntity.ok(test);
    }

    @GetMapping("/test2")
    public ResponseEntity<Student> test2() {
        Student test = fooService.test2();
        return ResponseEntity.ok(test);
    }


    @GetMapping("/test4")
    public ResponseEntity<List<String>> test4() {
        List<String> test = fooService.test4();
        return ResponseEntity.ok(test);
    }

    @GetMapping("/test5")
    public ResponseEntity<Map<String, String>> test5() {
        Map<String, String> map = fooService.test5();
        return ResponseEntity.ok(map);
    }

    @GetMapping("/test6")
    public ResponseEntity<Integer> test6() {
        Integer integer = fooService.test6();
        return ResponseEntity.ok(integer);
    }

    @GetMapping("/test7")
    public ResponseEntity<String> test7() {
        String s = fooService.test7();
        return ResponseEntity.ok(s);
    }

    @GetMapping("/test8")
    public ResponseEntity<Date> test8() {
        Date s = fooService.test8();
        return ResponseEntity.ok(s);
    }

    @GetMapping("/test9")
    public ResponseEntity<LocalDateTime> test9() {
        LocalDateTime s = fooService.test9();
        return ResponseEntity.ok(s);
    }


    @GetMapping("/test10")
    public ResponseEntity<JSONObject> test10() {
        JSONObject s = fooService.test10();
        return ResponseEntity.ok(s);
    }

    //10_000_000 数据翻译 20s以上 需要优化
    @GetMapping("/test11")
    public void test11() {
        for (int i = 0; i < 10; i++) {
            List<Student> students = fooService.bigData();
        }
        log.info("处理完成");
    }

    @GetMapping("/test12")
    @EnableDictTranslate
    public ResponseEntity<Home> test12(){
        Home home = new Home().setOk("1");
        return ResponseEntity.ok(home);
    }

}
