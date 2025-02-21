package com.rpc.starter;

import com.rpc.annotation.RpcReference;
import core.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author cg
 * @Date 2025/1/23 13:01
 */

public class RpcConsumerBootstrap implements BeanPostProcessor {


    @DependsOn("rpcConsumerInit")
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        //遍历bean对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) { //遍历那些成员变量 检查是否@RpcReference注解
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                //如果有的话 那么就是为这个对象生成代理对象
                try {
                    System.out.println("检测到rpc访问bean:"+beanName);
                    field.setAccessible(true);//将权限改为可访问
                    Object proxy = ProxyFactory.createProxy(field.getType()); //注意是getType返回的是这个字段的类型 而不是getClass返回的是Field类型 这里我们需要获取这个属性类型也就是远程接口对象
                    field.set(bean, proxy);//将proxy对象替换掉bean对象中的那个字段属性
                    field.setAccessible(false);//保持封装性
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    throw new RuntimeException("字段注入失败",e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
