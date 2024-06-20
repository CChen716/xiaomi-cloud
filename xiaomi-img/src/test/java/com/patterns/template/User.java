package com.patterns.template;

import com.patterns.chain.Order;

/**  模板方法  抽象类
 * 抽象类定义整个流程骨架
 * @author cg
 * @version 1.0
 * @Date 2024/6/14 20:33
 */

public abstract class User {
        //模板方法，使用final修改，防止子类改变算法的实现步骤
        public final void computePoints(Order order){  //计算积分
                Integer ponits;
                Integer allUser = computeAllUser(order);
                Integer vipUser = computeVip();
                Integer svip = computeSvip();
                ponits=allUser+vipUser+svip;

                System.out.println(ponits);
        }

        public Integer computeAllUser(Order order){
                //所有用户计算
                Integer point = order.getMoney();  //所有用户会基础积分=订单金额
                return point;
        }

        public abstract Integer computeVip();  //vip用户计算

        public abstract  Integer computeSvip(); //svip用户计算


}
