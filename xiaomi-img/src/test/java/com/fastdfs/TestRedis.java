package com.fastdfs;


import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.ImgApp;
import com.yc.bean.Manager;
import com.yc.util.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class TestRedis {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ObjectMapper objectMapper;   //字符串转对象

    @Autowired
    private RedisTemplate redisTemplate;


    //测试Redis的缓存方法
    @Test
    public void testAdd() throws JsonProcessingException {

        Manager manager=new Manager(1,"22","3");
        Manager manager0=new Manager(2,"哈哈","333");
        List<Manager> list=new ArrayList<>();
        list.add(manager);
        list.add(manager0);

        //1.测试单个对象
        cacheService.add("key",manager);
        String manager1=cacheService.get("key");
        Manager manager2=objectMapper.readValue(manager1,Manager.class);
        System.out.println(manager2.getUsername());
        System.out.println(manager.getId());

        //2.测试集合对象       //key    集合       过期时间    时间单位
        cacheService.add("list",list,1, TimeUnit.HOURS);
        List<Manager> list2=null;

        list2= cacheService.getList("list",Manager.class);
        for (Manager manager3 : list2) {
            System.out.println(manager3);
        }



    }


}
