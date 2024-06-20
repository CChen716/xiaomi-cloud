package com.patterns.Factory.Factory_Method;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 14:49
 */
public class Test {
    public static void main(String[] args) {
        Factory6400 factory6400 = new Factory6400();
        RedisClint redisClint = factory6400.creatClient();


        Factory6379 factory6379=new Factory6379();
        RedisClint redisClint1 = factory6379.creatClient();
    }
}
