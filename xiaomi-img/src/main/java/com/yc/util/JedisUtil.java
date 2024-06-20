package com.yc.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * Jedis线程池  貌似不需要
 * rebloom提供的Client默认集成了jedispool 貌似不需要
 * @author 12547
 * @version 1.0
 * @Date 2024/2/19 14:35
 */
@Component
public class JedisUtil {

    private final static String host="192.168.85.128";
    private final static  int port=6379;
    private final static int maxTotal=20;  //最大空闲连接数
    private final static int maxWaitMillis=30000;  //等待时间

    //创建一个连接对象
    private static JedisPool pool;

    static {
        //创建连接池的配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数和最长等待时间
      //  ResourceBundle bundle = ResourceBundle.getBundle("jedis");
        //得到配置文件中的属性值
    //    String host = bundle.getString("host");
       // int port = Integer.parseInt(bundle.getString("port"));
//        int maxTotal = Integer.parseInt(bundle.getString("maxTotal"));
//        int maxWaitMillis = Integer.parseInt(bundle.getString("maxWaitMillis"));
        //设置配置对象的参数
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        //创建连接池对象
        pool = new JedisPool(config, host, port);
    }

    /**
     * 得到redis连接对象
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }
    /**
     * 释放Jedis资源
     */
    public static void close(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
