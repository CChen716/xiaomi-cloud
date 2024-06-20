package com.patterns.strategy;

/** 普通用户 策略实现类
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 17:01
 */
public class PrimaryMember implements PayStrategy{
    @Override
    public double pay(Double money) {
        return money;  //普通会员没有优惠  原价购买
    }
}
