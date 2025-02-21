package core;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Nacos配置加载
 * @author cg
 * @Date 2024/12/12 15:03
 */
public class NacosConfigLoad {


    //将配置写入Nacos
     public static boolean pushNacosConfig(List<String> configContent) throws Exception {
         //首先要获取到配置对象中的Nacos相关配置
         String nacosAddr = RpcConfigApplication.rpcConfig.getNacosAddr();
         String dataId = RpcConfigApplication.rpcConfig.getDataId();
         String group = RpcConfigApplication.rpcConfig.getGroup();

         ConfigService configService= NacosFactory.createConfigService(nacosAddr);


         //然后需要将配置内容写入  --将一个List字符串集合转为yml格式 传入进来的格式： 全限定类名:方法名   以：做分割 前面为map的key 后面为value
         Map<String,List<String>> mapYml=new HashMap<>();

//         for (String methods : configContent) {
//             String[] split = methods.split(":");
//             mapYml.computeIfAbsent(split[0],k->new ArrayList<>()).add(split[1]);
//             //map的这个方法的意思是 如果key不存在 则new一个新List并将值添加到list中，如果存在 则返回对应的List，然后add添加到返回的这个list中
//         }
//
         //重新优化格式  在所有的url中加入host和port地址前缀

      //   mapYml.put("rpcUrl",configContent);

        List<String> rpcUrl=new ArrayList<>();
         //将服务地址添加上去
         String serverHost = RpcConfigApplication.rpcConfig.getServerHost();
         String serverPort = String.valueOf(RpcConfigApplication.rpcConfig.getServerPort());
         //TODO：此处需要进行分布式优化

         //后续优化内容
         for (String url : configContent) {
             rpcUrl.add(serverHost+":"+serverPort+"/"+url); // 格式：localhost:8888/com....rpc:testRpc
         }
         mapYml.put("rpcUrl",rpcUrl);

         ArrayList<String> serverInfo = new ArrayList<>();
         serverInfo.add(serverHost+":"+serverPort);
         mapYml.put("server",serverInfo);

         //然后需要将格式化好的map转为String 因为Nacos的api只支持String类型
         ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
         String ymlContent = objectMapper.writeValueAsString(mapYml);
         //转换完毕可以上传到nacos中
         boolean res = configService.publishConfig(dataId, group, ymlContent,"application/x-yaml");

         System.out.println("nacos config push result:"+res);
         return res;

     }

}
