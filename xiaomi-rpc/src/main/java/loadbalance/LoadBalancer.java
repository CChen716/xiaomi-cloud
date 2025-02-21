package loadbalance;

import java.util.List;
import java.util.Map;

/** 负载均衡通用接口（消费端）
 * @author cg
 * @Date 2025/1/6 21:18
 */
public interface LoadBalancer {

    //返回最后选择的服务地址

    /**
     *
     * @param requestParams 请求参数
     * @param serviceAddressList 可用服务列表
     * @return
     */
    String select(Map<String, Object> requestParams, List<String> serviceAddressList);
}
