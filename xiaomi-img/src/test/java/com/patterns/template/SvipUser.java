package com.patterns.template;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/14 21:35
 */
public class SvipUser extends User{



    @Override
    public Integer computeVip() {
        return 10; //svip用户享有vip用户所有权益
    }

    @Override
    public Integer computeSvip() {
        return 100; //svip额外再送100
    }
}
