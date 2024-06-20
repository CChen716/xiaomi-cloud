package com.yc.util;

import org.springframework.web.bind.annotation.Mapping;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface InterceptorReq {
    //value = "execution(* com.yc.controller.SeckillController.doSeckill(..))
    String clazz();//类的全限定名

    long overdue() default 5000;//失效时间，默认5秒
}

