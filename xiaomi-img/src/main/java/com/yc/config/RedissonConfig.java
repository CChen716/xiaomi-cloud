package com.yc.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**Redisson配置类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/9 16:12
 */
@Configuration
public class RedissonConfig {


    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.85.128:6379");
        // 创建RedissonClient对象
        return Redisson.create(config);
    }

}
