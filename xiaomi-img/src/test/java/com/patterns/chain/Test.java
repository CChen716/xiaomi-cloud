package com.patterns.chain;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/13 16:39
 */
public class Test {

    public static void main(String[] args) {
        Leader server=new servicer("哈哈客服");
        Leader manager=new Manager("店长");
        server.setSuccessor(manager); //客服后继者 是店长


        Order order=new Order("1111",6000);
        Order order2=new Order("2222",3000);

        //6000元订单
        server.handleRequest(order);

        //3000
        server.handleRequest(order2);

    }
}
