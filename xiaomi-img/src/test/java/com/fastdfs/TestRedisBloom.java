package com.fastdfs;

import com.yc.util.JedisUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.rebloom.client.Client;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * redisssion操作布隆过滤器
 * @author 12547
 * @version 1.0
 * @Date 2024/2/18 19:52
 */

@SpringBootTest
public class TestRedisBloom {

    /********************redission操作的并不是redis中安装的布隆过滤器，而是redission客户端自带的********************/    //测试连接 并在redis容器中使用命令创建好一个过滤器后看这边能不能查到
//    @Test
//    public void testRedission(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.85.128:6379");
//        RedissonClient redissonClient = Redisson.create(config);
//        //测试redission操作的是不是redis中配置的redisbloom插件
//        RBloomFilter<String> bloomFilter=redissonClient.getBloomFilter("phoneid");  //返回true代表成功
//        System.out.println(bloomFilter.getSize());
//        boolean exists = bloomFilter.isExists();
//        System.out.println(exists);
//        redissonClient.shutdown();
//    }
//
//    @Test
//    public void testRedissionApi(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.85.128:6379");
//        RedissonClient redissonClient = Redisson.create(config);
//        //测试redission操作的是不是redis中配置的redisbloom插件
//        RBloomFilter<String> bloomFilter=redissonClient.getBloomFilter("test1");
//        bloomFilter.tryInit(1000,0.01);//容量   误判率
//        redissonClient.shutdown();
//    }
    /**lettuce也没有提供相应api*************傻逼GPT**********************/
//
//    public void testLettuce(){
//        //建立连接
//        RedisURI redisURI=RedisURI.builder()
//                .withHost("192.168.85.128")
//                .withPort(6379)
//                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
//                .build();
//        //创建客户端
//        RedisClient redisClient=RedisClient.create(redisURI);
//        StatefulRedisConnection<String, String> connection = redisClient.connect();     // <3> 创建线程安全的连接
//        RedisCommands<String, String> redisCommands = connection.sync();                // <4> 创建同步命令

    /**JreBloom提供的jedis可以操控redis中的布隆过滤器********************************/
//        @Test
//      public void testJedis(){
//        //经过测试此是操作redis中的布隆过滤器
//          //改为jedisPool连接池
//         Jedis jedis= JedisUtil.getJedis();
//        Client client=new Client(jedis);
//          //测试是否和redis容器同步
//          System.out.println(client.exists("phoneid", "9999"));
//          System.out.println(client.exists("phoneid", "6666"));
//
//          //单个添加操作
//          client.add("phoneid","0716");
//          client.exists("phoneid","0716");
//
//          //批量添加
//          client.addMulti("phoneid","name1","name2","name3");
//          boolean[] booleans = client.existsMulti("phoneid", "name1", "name2", "name3", "name4");
//            for (int i = 0; i < booleans.length; i++) {
//                System.out.println("第"+i+booleans[i]+"个");
//            }
//
//          //创建一个过滤器            key           容量             容错率
//
//          client.createFilter("userid",100000,0.01);  //如若重复创建会报错
//          //需要进行异常处理
//
//
//          client.bfInsert("userid","999");
//          //两者都是往布隆过滤器中插值
//          System.out.println(client.add("userid", "999"));//add方法如果元素存在则返回false   应该返回false
//          boolean[] userids = client.bfInsert("userid", "999");//如果元素存在则覆盖值
//            for (boolean userid : userids) {
//                System.out.println(userid);
//           }
//
//         // client.delete(key)  删除过滤器
//        }
//





}
