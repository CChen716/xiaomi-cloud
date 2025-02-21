package com.rpc.annotation;

import com.rpc.starter.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * rpc框架服务提供端注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE) //作用在提供端启动类上
@Import(RpcProviderBootstrap.class)
public @interface RpcProvider {
}
