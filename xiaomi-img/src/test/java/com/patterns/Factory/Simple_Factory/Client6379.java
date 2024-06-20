package com.patterns.Factory.Simple_Factory;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/3 23:16
 */
public class Client6379 extends RedisClient{

    public Client6379(){
        System.out.println("生产端口为6379的Client对象-------");
    }

}
