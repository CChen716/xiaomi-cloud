package com.demo;

import com.yc.ImgApp;
import com.yc.config.RedisBloomConfig;
import com.yc.dao.ComDao;
import com.yc.util.JedisUtil;
import io.rebloom.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 将数据填入布隆过滤器中demo
 * @author 12547
 * @version 1.0
 * @Date 2024/2/21 22:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)    //要加测试单元的启动类
public class RedisBloomDemo {

    @Autowired
    private ComDao comDao;

    /**
     * 已经成功添加 2.22
     */
//    @Test
//    //往布隆过滤器中添加商品id  Demo
//    public void addRedisBloomPhone(){
//
//        List<String> list = comDao.selectRedisBloomPhoneId();//获取所有id
//        Jedis jedis= JedisUtil.getJedis();
//        Client client=new Client(jedis);
//        for (String id: list) {
//            System.out.println("添加id:"+id+"是否成功"+client.add(RedisBloomConfig.key, id));
//        }
//
//    }
//
//
//    /************************重新初始化布隆过滤器****************************/
//    @Test
//    //创建布隆过滤器
//    public void createRedisBloom(){
//        Jedis jedis= JedisUtil.getJedis();
//        Client client=new Client(jedis);
//        // 十万容量  容错率1%
//        client.createFilter("phoneid",100000,0.01);
//
//    }
//
//
//    @Test
//    //删除布隆过滤器
//    public void deleteRedisBloom(){
//        Jedis jedis= JedisUtil.getJedis();
//        Client client=new Client(jedis);
//        boolean phoneid = client.delete("phoneid");
//        System.out.println(phoneid);
//    }

}
