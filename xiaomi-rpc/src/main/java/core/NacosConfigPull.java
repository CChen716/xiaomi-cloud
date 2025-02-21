package core;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import entity.NacosConfigRpc;
import entity.RpcDTO;
import entity.ServerInfo;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Nacos配置获取类---消费端
 * @author cg
 * @Date 2024/12/16 20:38
 */
public class NacosConfigPull {


    public static final HashMap<String,List<String>> rpcUrlMap=new HashMap<>();


    /**
     * 返回一个服务地址  :此处后续可以进行扩展优化  考虑多节点 负载均衡等等
     * @return
     */
    public static String getServer(){
        //返回一个支持的服务地址
        //TODO:此处可以有许多策略 如果是分布式多节点 那么可以轮巡或者随机 或者权重
        List<String> list = rpcUrlMap.get(NacosConfigRpc.server);
        return list.get(0);
        //TODO:判空处理
    }


    /**
     * 从Nacos上拉取rpc配置文件  那么该方法需要在启动时执行
     */
    public  static void pull()  {

        String dataId = RpcConfigApplication.rpcConfig.getDataId();
        String group = RpcConfigApplication.rpcConfig.getGroup();

        String config = null; //根据id和group获取指定的配置文件  此处后续可以优化
        try {
            ConfigService configService= NacosFactory.createConfigService(RpcConfigApplication.rpcConfig.getNacosAddr()); //读取配置文件
            config = configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        //获取到配置
        System.out.println("Nacos rpc config pull:"+config);
        //将字符串转为yml对象 然后就方便获取对应的值了
        Yaml yaml = new Yaml();
        Map<String,Object> configData = yaml.load(config);
        List<String> urlList= (List<String>) configData.get(NacosConfigRpc.rpcUrl);
        List<String> serverList= (List<String>) configData.get(NacosConfigRpc.server);
        //存放到一个map当中
        rpcUrlMap.put(NacosConfigRpc.server,serverList);
        rpcUrlMap.put(NacosConfigRpc.rpcUrl,urlList);
    }

    /**
     * 安全校验  是否是符合远程调用的接口和方法名
     * @param rpcDTO
     * @return
     */
    public static Map<String,List<String>> checkMethod(RpcDTO rpcDTO){
        //检查类名和方法名是否一致
        String className = rpcDTO.getClassName()+"Impl";  //由于消费端扫描的是接口实现类 而消费端是只能通过接口对象来发送 所以暂时要在次数+上Impl才能保持一致
        String methodName = rpcDTO.getMethodName();

        List<String> urlList = rpcUrlMap.get(NacosConfigRpc.rpcUrl);

        Map<String,List<String>> resultMap=new HashMap<>();
        //检查是否含有匹配的接口名和方法名
        for (String url : urlList) { //此处后续更改  格式为 host:port/classname:methodName
            if (url.contains(className+":"+methodName)){ //如果有对应的接口和方法名
                String addr = url.split("/")[0];
                String[] serverAddress = addr.split(":");
                String ip = serverAddress[0];//IP
                String port = serverAddress[1];//端口

                //然后通过端口和IP 在NacosProviderWatch监听类中找到对应的服务名 然后通过服务名找到对应的健康服务列表
                for (Map.Entry<String, Map<String, Boolean>> entry : NacosProviderWatch.HealthInstanceStatus.entrySet()) {
                    Map<String, Boolean> ipMap = entry.getValue();
                    // 使用 Stream 判断是否存在 IP 匹配的键
                    boolean ipExists = ipMap.keySet().stream()
                            .anyMatch(key -> key.split(":")[0].equals(ip));
                    if (ipExists){  //此处改为不需要判断端口 只需要判断服务ip是否存在
                         //返回服务名 和对应的服务实例列表
                        String name = entry.getKey(); //服务名
                        ArrayList<String> serverList = new ArrayList<>(ipMap.keySet());//服务列表
                        //此处将port替换成rpc的端口 而不是web服务端口
                        List<String> updateServerList = serverList.stream().map(entryList -> {
                            String[] split = entryList.split(":");
                            return split[0] +":"+ port; //将端口替换成rpc的端口
                        }).collect(Collectors.toList());
                        resultMap.put(name,updateServerList);
                        return resultMap;
                    }
                }
                return null;
            }
        }
        //如果没有匹配的 则代表非法方法
        return null;
    }











    public static void main(String[] args) throws NacosException {
//        ConfigService configService= NacosFactory.createConfigService("localhost:8848");
//
//        //获取配置 需要 id group
//        String dataId="RPC_INFO";
//        String group= "DEFAULT_GROUP";
//        String config = configService.getConfig(dataId, group, 5000);//超时时间
//        //获取到的配置
//        System.out.println(config);
//
//        //将字符串转为yml对象 然后就方便获取对应的值了
//        Yaml yaml = new Yaml();
//        Map<String,Object> configData = yaml.load(config);
//        List<String> urlList= (List<String>) configData.get("rpcUrl");
//        List<String> serverList= (List<String>) configData.get("server");
//        for (String s : serverList) {
//            System.out.println(s);
//        }
//
//        for (String s : urlList) {
//            System.out.println(s);
//        }
        //框架初始化
        RpcConfigApplication.init();
        pull();
    }




}
