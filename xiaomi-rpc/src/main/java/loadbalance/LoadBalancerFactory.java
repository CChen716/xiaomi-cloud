package loadbalance;

import java.util.HashMap;
import java.util.Map;

/** 负载均衡器工厂类 获取负载均衡对象
 * @author cg
 * @Date 2025/1/7 16:49
 */
public class LoadBalancerFactory {

    private static final Map<String,Map<String,LoadBalancer>> LOADBALANCER_MAP= new HashMap<>();

    /**
     * 默认为轮询
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER=new RoundRobinLoadBalancer();



    //如果用户没有指定 则默认使用轮询
    public static LoadBalancer getInstance(){
        return DEFAULT_LOAD_BALANCER;

    }

    /**
     * 获取指定
     * 参数为服务名和对应的负载均衡器key
     */
    public static LoadBalancer getInstance(String serverName, String key){
        if (!LOADBALANCER_MAP.containsKey(serverName)){//如果没有服务名对应的负载均衡器则创建
            HashMap<String, LoadBalancer> serverLoadMap = new HashMap<>();
            serverLoadMap.put(LoadBalancerKey.CONSISTENT_HASH,new ConsistentHashLoadBalancer());
            serverLoadMap.put(LoadBalancerKey.ROUND_ROBIN,new RoundRobinLoadBalancer());
            serverLoadMap.put(LoadBalancerKey.RANDOM,new RandomLoadBalancer());
            //初始化好服务的负载均衡器对象
            LOADBALANCER_MAP.put(serverName,serverLoadMap);
            return serverLoadMap.get(key);
        }
        //该服务已经使用负载均衡器
        Map<String, LoadBalancer> loadBalancerMap = LOADBALANCER_MAP.get(serverName);
        return loadBalancerMap.get(key);
    }

}
