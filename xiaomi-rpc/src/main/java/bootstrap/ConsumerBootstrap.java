package bootstrap;

import core.NacosConfigPull;
import core.NacosProviderWatch;
import core.RpcConfigApplication;

/** rpc框架消费端启动初始化
 * @author cg
 * @Date 2025/1/15 21:00
 */
public class ConsumerBootstrap{

    /**
     * 框架初始化
     */
    public static void init(){
        RpcConfigApplication.init(); //1.初始化配置对象
        NacosConfigPull.pull();//2.从注册中心拉取配置
        NacosProviderWatch nacosProviderWatch = new NacosProviderWatch();
        nacosProviderWatch.start(RpcConfigApplication.rpcConfig.getNacosAddr());//3.启动Nacos监听类
    }
}
