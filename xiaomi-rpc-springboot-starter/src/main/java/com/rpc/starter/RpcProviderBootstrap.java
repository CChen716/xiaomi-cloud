package com.rpc.starter;

import cn.hutool.core.annotation.AnnotationUtil;
import com.rpc.annotation.RpcProvider;
import core.InspectAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import server.tcp.VertxTcpServer;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author cg
 * @Date 2025/1/21 19:12
 */
@Slf4j
public class RpcProviderBootstrap  implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private RpcConfig rpcConfig;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Class<?> mainApplicationClass = event.getSpringApplication().getMainApplicationClass();
        System.out.println("检测到启动类："+mainApplicationClass.getName());
        try {
            InspectAnnotation.scanAnnotation();//执行扫描注解将接口信息上传到Nacos上
            VertxTcpServer vertxTcpServer = new VertxTcpServer(); //启动TCP服务器
            vertxTcpServer.start(rpcConfig.getServerPort());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("TCP服务器未启动成功");
        }

    }
}
