package com.Caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yc.ImgApp;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**测试Caffeine本地缓存
 * @author 12547
 * @version 1.0
 * @Date 2024/3/21 20:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class TestCaffeine {

    @Autowired
    private Cache<String,Object> caffeine;

    @Test
    public void testCaffe(){
         String test1="hallo,caffeine";
         caffeine.put("test",test1);
         System.out.println(caffeine.getIfPresent("test"));
    }
}
