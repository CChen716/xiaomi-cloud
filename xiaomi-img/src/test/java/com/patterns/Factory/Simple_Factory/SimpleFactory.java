package com.patterns.Factory.Simple_Factory;

/** 简单工厂方法
 * @author cg
 * @version 1.0
 * @Date 2024/6/3 23:18
 */

public class SimpleFactory {
    public RedisClient creatClient(String type){


        switch (type){
            case "6379": return new Client6379();
            case "6400": return new Client6400();
            default:break;
        }
        return null;
    }

}
