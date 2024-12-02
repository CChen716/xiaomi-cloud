package com.yc.myRpc.core;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/** 本地注册类 将需要被代理的实现类注册到一个map中
 * @author cg
 * @Date 2024/12/1 14:02
 */
public class LocalRegister {
    //key 接口对象名 value: 接口实现类对象
    public static final ConcurrentHashMap<String,Object> proxyMap=new ConcurrentHashMap<>();

    static {
            register("com.yc.biz.CartBiz");
    }


    //根据接口名 找到他的实现类并把对象实例存放到map中
    public static void register(String interfaceName){
        //如果根据名字找 则接口实现类必须严格按照命名规范  接口名+Impl
        try {
            //如果实现类有多个 则需要加一个版本号标识 比如impl2 impl3才可以区分
            Class<?> implClass = Class.forName(interfaceName + "Impl");
            Object proxy = implClass.getDeclaredConstructor().newInstance();
            proxyMap.put(interfaceName,proxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
