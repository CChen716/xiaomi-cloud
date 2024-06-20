package com.yc.util;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;


    @Around(value = "@annotation(InterceptorReq)")
    public Object doBefore(ProceedingJoinPoint joinPoint) {

        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取到请求对象 //主要是为了获取请求的实体
            HttpServletRequest request = attributes.getRequest();

            //优先判断是application/x-www-form-urlencoded，如果取出来的map中没有数据，则当成application/json方式
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
            if (entries == null || entries.isEmpty()) {

                //认为是application/json方式

                //获取注解中传入的值，该值是需要校验数据的类的全名
                InterceptorReq interceptorReq = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterceptorReq.class);
                String value = interceptorReq.clazz();
                long overdue = interceptorReq.overdue();

                String json = "";

                //获取该切面环绕的方法的形参
                Object[] args = joinPoint.getArgs();

                if (args == null) {
                    //请求的方法，没有形参，则该方法根本没有传参，直接拦截直接处理
                    try {
                        Object proceed = joinPoint.proceed();
                        return  proceed;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        log.error("相同请求拦截器异常:{}", throwable.getMessage());
                        return ReturnUtil.returnErr(throwable.getMessage());
                    }
                }

                //方法可能有几个形参，判断哪个形参中的数据需要校验
                for (Object arg : args) {
                    //形参的类名
                    String name = arg.getClass().getName();
                    //如果形参中的类型，和注解中传入的值，一样，则该对象的数据需要校验
                    if (name.equals(value)) {
                        json = JSON.toJSONString(arg);
                    }
                }

                //请求参数为空，不需要校验，直接调用方法执行
                if (StringUtils.isBlank(json)) {
                    try {
                        Object proceed = joinPoint.proceed();
                        return  proceed;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }


                //请求体中数据不为空，则处理数据
                String requestURI = request.getRequestURI();
                //url和请求的数据，拼接形成一个key,转MD5
                String Longkey = requestURI+json;
                String key = DigestUtils.md5DigestAsHex(Longkey.getBytes());
                //分布式的锁
                boolean lock = false;
                try {
                    //判断锁存在不存在，必须设置睡眠时间,默认设置5000
                    lock = redisTemplate.opsForValue().setIfAbsent(key, "1");
                    redisTemplate.expire(key, overdue, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    redisTemplate.expire(key, overdue, TimeUnit.MILLISECONDS);
                }
                if (lock != true) {
                    return ReturnUtil.returnErr("请不要重复提交");
                }


            }else {


                //获取注解中传入的值，该值是需要校验数据的类的全名
                InterceptorReq interceptorReq = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterceptorReq.class);
                long overdue = interceptorReq.overdue();


                String json = JSON.toJSONString(parameterMap);
                String requestURI = request.getRequestURI();
                String Longkey = requestURI+json;
                String key = DigestUtils.md5DigestAsHex(Longkey.getBytes());
                boolean lock = false;
                try {
                    lock = redisTemplate.opsForValue().setIfAbsent(key, "1");
                    redisTemplate.expire(key, overdue, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    redisTemplate.expire(key, overdue, TimeUnit.MILLISECONDS);
                }
                if (lock != true) {
                    return ReturnUtil.returnErr("请不要重复提交");
                }

            }
        }catch (Exception e) {
            log.error("相同请求拦截器异常:{}", e.getMessage());

            return ReturnUtil.returnErr(e.getMessage());
        }

        try {
            Object proceed = joinPoint.proceed();
            return  proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("相同请求拦截器异常:{}", throwable.getMessage());
            return ReturnUtil.returnErr(throwable.getMessage());
        }
    }
}
