package com.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cg
 * @Date 2025/1/22 13:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)//字段或者属性  作用于对象上
public @interface RpcReference {
}
