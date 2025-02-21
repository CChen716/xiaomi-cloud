package com.rpc.annotation;

import com.rpc.starter.RpcConsumerBootstrap;
import com.rpc.starter.RpcConsumerInit;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cg
 * @Date 2025/1/21 12:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)//作用在消费端启动类上
@Import({RpcConsumerInit.class,RpcConsumerBootstrap.class})
public @interface RpcSubscribe {
}
