package com.patterns.Factory.abstract_Factory;

/**
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 15:25
 */
public class Test {
    public static void main(String[] args) {
        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        huaWeiFactory.creatPad();  //创建平板对象
        huaWeiFactory.creatPhone(); //创建手机对象


        IphoneFactory iphoneFactory = new IphoneFactory();
        iphoneFactory.creatPad();
        iphoneFactory.creatPhone();
    }
}
