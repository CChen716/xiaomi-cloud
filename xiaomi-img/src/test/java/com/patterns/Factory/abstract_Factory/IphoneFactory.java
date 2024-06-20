package com.patterns.Factory.abstract_Factory;

/**具体工厂 苹果工厂 可以生产苹果手机 和苹果平板
 * @author cg
 * @version 1.0
 * @Date 2024/6/4 15:24
 */
public class IphoneFactory implements AbstractFactory{

    @Override
    public Phone creatPhone() {
        return new Iphone();
    }

    @Override
    public Pad creatPad() {
        return new IphonePad();
    }
}
