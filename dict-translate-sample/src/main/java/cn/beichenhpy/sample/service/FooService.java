package cn.beichenhpy.sample.service;

import cn.beichenhpy.dictionary.EnableDictTranslate;
import cn.beichenhpy.dictionary.enums.TranslateType;
import cn.beichenhpy.sample.entity.Home;
import cn.beichenhpy.sample.entity.Student;
import cn.beichenhpy.sample.entity.Teacher;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:12
 */
@Service
public class FooService {
    @EnableDictTranslate(type = TranslateType.ENTITY)
    public IPage<Student> test() {
        IPage<Student> page = new Page<>();
        page.setRecords(prepare());
        return page;
    }


    @EnableDictTranslate(type = TranslateType.ENTITY)
    public List<Student> test1() {
        return prepare();
    }


    @EnableDictTranslate(type = TranslateType.ENTITY)
    public Set<Student> test3() {
        return new HashSet<>(prepare());
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public Student test2() {
        Teacher teacher1 = Teacher.builder().gender(true).status(10041001L).build();
        Teacher teacher2 = Teacher.builder().gender(false).status(10041002L).build();
        return Student.builder().gender(false).status(10041001L).teachers(new ArrayList<>(Arrays.asList(teacher1, teacher2))).build();
    }


    @EnableDictTranslate(type = TranslateType.ENTITY)
    public List<String> test4() {
        return new ArrayList<>(Collections.singletonList("1"));
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public Map<String, String> test5() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        return map;
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public Integer test6() {
        return 1;
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public String test7() {
        return "1";
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public Date test8() {
        return new Date();
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public LocalDateTime test9() {
        return LocalDateTime.now();
    }

    @EnableDictTranslate(type = TranslateType.ENTITY)
    public JSONObject test10(){
        String a = "{\"a\":\"a\"}";
        return JSON.parseObject(a);
    }

    private List<Student> prepare() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        Teacher teacher1 = Teacher.builder().gender(true).status(10041001L).homes(Collections.singletonList(Collections.singletonList(Home.builder().address("测试").build()))).build();
        Teacher teacher2 = Teacher.builder().gender(false).status(10041002L).localDateTimes(Collections.singletonList(LocalDateTime.now())).build();
        Student student = Student.builder().gender(false).status(10041001L).date(new Date()).dates(Collections.singletonList(new Date())).test(map).teachers(new ArrayList<>(Arrays.asList(teacher1, teacher2))).build();
        Student student1 = Student.builder().gender(true).status(10041002L).date(new Date()).teachers(new ArrayList<>(Arrays.asList(teacher1, teacher2))).build();
        return new ArrayList<>(Arrays.asList(student, student1));
    }
}