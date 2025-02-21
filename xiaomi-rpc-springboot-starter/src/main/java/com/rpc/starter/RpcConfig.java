package com.rpc.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** rpcConfig配置类properties
 * @author cg
 * @Date 2025/1/21 10:47
 */
@ConfigurationProperties(prefix = "rpc")  //要和EnableConfigurationProperties搭配使用
@Data
public class RpcConfig {

    private String packName; //支持rpc的包路径名
    private Integer serverPort; //rpc服务端口号
    private boolean mock;//模拟调用
    private String serialize;//序列化器
    private String serverHost; //服务地址
    private String nacosAddr; //Nacos服务地址
    //这两个参数应该是可选 不选择则使用默认值 可选则选择用户自定义配置
    private String dataId="RPC_INFO";//rpc配置ID
    private String group="DEFAULT_GROUP";//配置分组
    //TODO 负载均衡器
    private String retryStrategy="fixedInterval";//重试策略
    private String tolerantStrategy= "failFast";//容错策略
}
