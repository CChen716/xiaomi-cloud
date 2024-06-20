package com.patterns.strategy;

/** 传统实现模式
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 16:50
 */
public class tradition {

     //传统实现模式  普通用户原价购买，vip用户享有95折，svip用户享有9折。
    public Double pay(String type,Double money){

        if (type.equals("vip用户")){
            return money*0.95;
        }
        if (type.equals("svip用户")){
            return money*0.9;
        }
        return money;  //普通会员计费
    }

}
