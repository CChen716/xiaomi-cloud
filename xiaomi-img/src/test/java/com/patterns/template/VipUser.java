package com.patterns.template;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/14 21:35
 */
public class VipUser extends User{


    @Override
    public Integer computeVip() {
        return 10; //vip用户额外送10积分
    }

    @Override
    public Integer computeSvip() {
        return 0;
    }
}
