package com.patterns.strategy;

/** 环境类 也叫上下文 负责具体的策略类交互
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 17:05
 */
public class Context {

    private PayStrategy payStrategy; //策略接口

    public Context(PayStrategy payStrategy){
        this.payStrategy=payStrategy;
    }

    public void setPayStrategy(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    //计算价格
    public Double operate(double money){
        //通过接口变量调用对应的策略
        return  this.payStrategy.pay(money);
    }
}
