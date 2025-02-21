package core;


import entity.RpcConfig;
import utils.ConfigUtils;

/**维护Rpc全局配置对象应用类
 * @author cg
 * @Date 2024/12/5 14:29
 */
public class RpcConfigApplication {

    //全局配置类对象应该只有一个 采用单例双重检查锁模式
    public static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化方法
     */
    public static void init()  {
        //从yml文件中加载出对应的配置对象
        //TODO：后续是否考虑支持两种配置文件  yml或者properties
        rpcConfig= ConfigUtils.loadConfig(RpcConfig.class); //目前暂时以yml为例
        System.out.println("rpc config init:"+rpcConfig.toString());

        //2.扫描注解并上传Naocs  TODO:消费端误用同一个初始化 导致覆盖消费端上传的配置文件
//        try {
//            InspectAnnotation.scanAnnotation();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取配置对象
     * @return
     */
    public static RpcConfig getRpcConfig(){
        if (rpcConfig==null){
            synchronized (RpcConfigApplication.class){
                if (rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
