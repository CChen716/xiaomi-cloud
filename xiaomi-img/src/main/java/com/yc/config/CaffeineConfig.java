package com.yc.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**Caffeine本地缓存配置类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/21 20:29
 */
@Configuration
public class CaffeineConfig {


    @Bean
    public Cache<String,Object> caffeineCache(){
        return Caffeine.newBuilder()
                //设置最后一次写后的过期时间   //写后60秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                //设置初始容量
                .initialCapacity(100)
                //缓存最大条数  超出时开始更具淘汰算法淘汰
                .maximumSize(10000)
                .build();
    }

}
