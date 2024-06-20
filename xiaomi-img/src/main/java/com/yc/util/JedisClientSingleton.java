package com.yc.util;

import io.rebloom.client.Client;
import redis.clients.jedis.Jedis;

/**
 * @author 12547
 * @version 1.0
 * client双重检查锁
 * @Date 2024/2/26 14:45
 */
public class JedisClientSingleton {

    private static volatile Client client;

    private JedisClientSingleton(){}  //私有构造，防止外部实例化

    public static Client getInstance(){
        if (client==null){   //如果对象没有被创建     假设线程A和B同时争抢
            synchronized (JedisClientSingleton.class){//其中一个线程抢到锁
                if (client==null){
                     //       Jedis jedis=JedisUtil.getJedis();Client中自带了jedisPool 不需要在手动配置
                    client=new Client("192.168.85.128",6379); //
                    //创建好配置好的client对象
                }
            }
        }
        return client;
    }

}
