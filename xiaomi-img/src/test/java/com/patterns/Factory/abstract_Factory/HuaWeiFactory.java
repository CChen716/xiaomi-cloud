package com.patterns.Factory.abstract_Factory;

/** 具体工厂 华为工厂 可以生产华为手机 和华为平板
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 15:24
 */
public class HuaWeiFactory implements AbstractFactory{


    @Override
    public Phone creatPhone() {
        return new HuaWeiPhone();
    }

    @Override
    public Pad creatPad() {
        return new HuaWeiPad();
    }
}
