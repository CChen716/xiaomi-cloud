package com.patterns.Factory.Simple_Factory;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/3 23:21
 */
public class Test {
    public static void main(String[] args) {
        SimpleFactory simpleFactory=new SimpleFactory();
        RedisClient client= simpleFactory.creatClient("6379");
        RedisClient client2= simpleFactory.creatClient("6400");
    }
}
