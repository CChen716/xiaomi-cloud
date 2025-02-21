package com.rpc.starter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**RPc配置相自动加载类
 * @author cg
 * @Date 2025/1/21 10:54
 */
@Configuration
@EnableConfigurationProperties(RpcConfig.class)
public class RpcAutoConfiguration {
}
