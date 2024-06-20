package com.patterns.template;

import com.patterns.chain.Order;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/14 21:57
 */
public class Test {


    public static void main(String[] args) {
        Order order=new Order("111",6000);
        BasicUser basicUser = new BasicUser();
        VipUser vipUser = new VipUser();
        SvipUser svipUser = new SvipUser();
        basicUser.computePoints(order);
        vipUser.computePoints(order);
        svipUser.computePoints(order);

    }


}
