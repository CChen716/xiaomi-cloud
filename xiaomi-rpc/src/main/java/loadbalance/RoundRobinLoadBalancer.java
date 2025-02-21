package loadbalance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**轮询负载均衡器
 * @author cg
 * @Date 2025/1/7 16:00
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 当前轮询的下标 采用原子计数器
     */
    private final AtomicInteger currentIndex=new AtomicInteger(0);


    @Override
    public String select(Map<String, Object> requestParams, List<String> serviceAddressList) {
        if (serviceAddressList.isEmpty()){
            return null;
        }
        int size = serviceAddressList.size();
        //只有1个的时候 无需轮询
        if (size==1){
            return serviceAddressList.get(0);
        }

        //取模算法轮询
        int index=currentIndex.getAndIncrement()%size;
        return serviceAddressList.get(index);
    }
}
