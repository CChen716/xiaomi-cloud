package com.patterns.strategy;

/** 用户类
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 17:12
 */
public class TestUser {

    public static void main(String[] args) {

        //具体的行为策略
        PayStrategy payStrategy=new PrimaryMember(); //接口回调
        PayStrategy payStrategyVip=new VipMember();
        PayStrategy payStrategySvip=new SvipMember();

        //用户可以选择不同的策略
        Context context=new Context(payStrategy);
        Context contextVip=new Context(payStrategyVip);
        Context contextSvip=new Context(payStrategySvip);

        context.operate(300); //普通会员  300元
        contextVip.operate(300); //vip 会员 285元
        contextSvip.operate(300);//svip会员 270元
    }

}
