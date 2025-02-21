package bootstrap;

import core.InspectAnnotation;
import core.RpcConfigApplication;
import entity.RpcConfig;
import server.tcp.VertxTcpServer;

/**rpc框架服务提供端启动类
 * @author cg
 * @Date 2025/1/15 20:59
 */
public class ProviderBootstrap {
    /**
     * 服务提供端框架初始化
     * @throws Exception
     */
    public static void init() throws Exception {
        RpcConfigApplication.init();//1.初始化配置对象
        final RpcConfig rpcConfig = RpcConfigApplication.getRpcConfig();
        InspectAnnotation.scanAnnotation();//2.扫描注解并将接口信息上传到Nacos生成配置文件
        VertxTcpServer vertxTcpServer = new VertxTcpServer();//3.启动TCP服务端
        vertxTcpServer.start(rpcConfig.getServerPort());
    }


}
