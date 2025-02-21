package com.rpc.starter;

import core.NacosConfigPull;
import core.NacosProviderWatch;
import core.RpcConfigApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author cg
 * @Date 2025/1/27 15:43
 */

public class RpcConsumerInit implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private RpcConfig rpcConfig;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Class<?> mainApplicationClass = event.getSpringApplication().getMainApplicationClass();
        System.out.println("检测到启动类："+mainApplicationClass.getName());

        RpcConfigApplication.init();
        NacosConfigPull.pull();
        NacosProviderWatch nacosProviderWatch = new NacosProviderWatch();
        nacosProviderWatch.start(rpcConfig.getNacosAddr());
    }
}
