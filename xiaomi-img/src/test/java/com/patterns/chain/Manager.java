package com.patterns.chain;

/** 店长
 * @author cg
 * @version 1.0
 * @Date 2024/6/13 16:19
 */
public class Manager  extends Leader{

    public Manager(String name) {
        super(name);
    }

    @Override
    public void handleRequest(Order order) {
        if (order.getMoney()>5000){
            System.out.println("订单交由店长处理金额:"+order.getMoney());
        }else {
            System.out.println("不由店长处理");
        }
    }
}
