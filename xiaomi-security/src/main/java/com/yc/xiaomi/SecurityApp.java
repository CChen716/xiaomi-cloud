package com.yc.xiaomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.yc.xiaomi.dao")
@EnableDiscoveryClient
@EnableSwagger2
@EnableOpenApi
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400) //过期时间
public class SecurityApp {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApp.class);
    }
}
