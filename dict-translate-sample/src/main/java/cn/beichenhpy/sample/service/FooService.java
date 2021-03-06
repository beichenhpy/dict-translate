package cn.beichenhpy.sample.service;

import cn.beichenhpy.dictionary.annotation.EnableDictTranslate;
import cn.beichenhpy.dictionary.constant.TranslateStrategy;
import cn.beichenhpy.sample.entity.Home;
import cn.beichenhpy.sample.entity.StatusEnum;
import cn.beichenhpy.sample.entity.Student;
import cn.beichenhpy.sample.entity.Teacher;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/14 19:12
 */
@Slf4j
@Service
public class FooService {
    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public IPage<Student> test() {
        IPage<Student> page = new Page<>();
        page.setRecords(prepare());
        return page;
    }


    //    @EnableDictTranslate(mode = TranslateType.DEFAULT)
    public List<Student> test1() {
        return prepare();
    }


    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public Set<Student> test3() {
        return new HashSet<>(prepare());
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public Student test2() {
        Teacher teacher1 =
                new Teacher()
                        .setGender(true)
                        .setStatus(10041001L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));

        Teacher teacher2 =
                new Teacher()
                        .setGender(false)
                        .setStatus(10041002L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));
        return new Student()
                .setDate(new Date())
                .setDates(Collections.singletonList(new Date()))
                .setGender(true)
                .setStatus(10041001L)
                .setHealth("10011002")
                .setTeachers(new ArrayList<>(Arrays.asList(teacher1, teacher2)));
    }


    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public List<String> test4() {
        return new ArrayList<>(Collections.singletonList("1"));
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public Map<String, String> test5() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        return map;
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public Integer test6() {
        return 1;
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public String test7() {
        return "1";
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public Date test8() {
        return new Date();
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public LocalDateTime test9() {
        return LocalDateTime.now();
    }

    @EnableDictTranslate(strategy = TranslateStrategy.DEFAULT)
    public JSONObject test10() {
        String a = "{\"a\":\"a\"}";
        return JSON.parseObject(a);
    }

    @EnableDictTranslate
    public List<Student> bigData() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        List<Student> students = new LinkedList<>();
        Teacher teacher1 =
                new Teacher()
                        .setGender(true)
                        .setStatus(10041001L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));

        Teacher teacher2 =
                new Teacher()
                        .setGender(false)
                        .setStatus(10041002L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));
        Student student =
                new Student()
                        .setDate(new Date())
                        .setDates(Collections.singletonList(new Date()))
                        .setGender(true)
                        .setStatus(10041001L)
                        .setHealth("10011002")
                        .setTest(map)
                        .setTeachers(new ArrayList<>(Arrays.asList(teacher1, teacher2)));
        for (int i = 0; i < 10_000_00; i++) {
            students.add(student);
        }
        log.info("big data????????????");
        return students;
    }

    private List<Student> prepare() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        Teacher teacher1 =
                new Teacher()
                        .setGender(true)
                        .setStatus(10041001L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));

        Teacher teacher2 =
                new Teacher()
                        .setGender(false)
                        .setStatus(10041002L)
                        .setStatusEnum(StatusEnum.AVAILABLE)
                        .setAs(new String[]{"1", "2"})
                        .setHomes(Collections.singletonList(Collections.singletonList(new Home().setProvince("aa"))));
        Student student =
                new Student()
                        .setDate(new Date())
                        .setDates(Collections.singletonList(new Date()))
                        .setGender(true)
                        .setStatus(10041001L)
                        .setHealth("10011002")
                        .setTeachers(new ArrayList<>(Arrays.asList(teacher1, teacher2)));
        Student student2 =
                new Student()
                        .setDate(new Date())
                        .setDates(Collections.singletonList(new Date()))
                        .setGender(true)
                        .setStatus(10041001L)
                        .setHealth("10011002")
                        .setTest(map)
                        .setTeachers(new ArrayList<>(Arrays.asList(teacher1, teacher2)));
        return new ArrayList<>(Arrays.asList(student, student2));
    }

    public String getValue(String key) {
        switch (key) {
            case "10011001":
                return "??????";
            case "10011002":
                return "??????";
            case "10011003":
                return "??????";
            default:
                return key + ":???????????????";
        }
    }
}
