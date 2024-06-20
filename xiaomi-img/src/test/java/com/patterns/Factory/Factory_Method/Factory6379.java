package com.patterns.Factory.Factory_Method;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 14:47
 */

public class Factory6379 implements FactoryClient{

    @Override
    public RedisClint creatClient() {
        return new Client6379();
    }
}
