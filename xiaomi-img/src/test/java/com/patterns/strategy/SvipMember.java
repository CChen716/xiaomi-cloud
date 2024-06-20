package com.patterns.strategy;

/** Svip用户 策略实现类
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 17:04
 */
public class SvipMember implements PayStrategy{
    @Override
    public double pay(Double money) {
        return money*0.9; //svip用户  9折购买
    }
}
