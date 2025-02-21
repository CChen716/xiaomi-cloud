package utils;

import cn.hutool.setting.dialect.Props;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**构造配置文件对象工具类
 * @author cg
 * @Date 2024/12/5 15:24
 */
public class ConfigUtils {


    /**
     * 加载配置文件 --properties文件
     * @param configClass 配置类对象
     * @param prefix 配置前缀
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> configClass,String prefix){
        //TODO：此方法暂时未测试
        StringBuilder configBuilder = new StringBuilder("application");
        configBuilder.append(".properties"); //此处应该是否应该考虑 可能为yml或者properties
        Props props = new Props(configBuilder.toString()); //hutool包工具类
        return props.toBean(configClass,prefix);
    }


    public static <T> T loadConfig(Class<T> configClass){
        //解析YML文件对象
        Yaml yaml = new Yaml();
        //将yml文件中的对应的配置 转为java对象
        //错误用法： 找了半天错误
       // yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream("application.yml"), RpcConfig.class);

        //注意框架会将yml文件整个进行转换！！！ 所以要使用Map结构来接收 否则会报错 方法返回的是LinkedHashMap结构
        Map linkedHahMap = yaml.loadAs(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.yml"), Map.class);
        //注意返回的value还是一个LinkedHashMap
        Object map = linkedHahMap.get("rpc");
        //所以直接将map转对象
        ObjectMapper objectMapper = new ObjectMapper();
        T rpcConfig = objectMapper.convertValue(map, configClass);
        return rpcConfig;
    }








}
