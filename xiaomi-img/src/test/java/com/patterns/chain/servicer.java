package com.patterns.chain;

/** 客服类
 * @author cg
 * @version 1.0
 * @Date 2024/6/13 15:37
 */
public class servicer extends Leader{


    public servicer(String name) {
        super(name);
    }

    @Override
    public void handleRequest(Order order) { //进行处理
        if (order.getMoney()<=5000){
            System.out.println("客服进行处理金额："+order.getMoney());
        }else {
            if (this.successor!=null){
                this.successor.handleRequest(order);
            }
        }

    }
}
