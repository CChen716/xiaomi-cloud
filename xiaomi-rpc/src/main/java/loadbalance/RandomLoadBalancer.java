package loadbalance;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author cg
 * @Date 2025/1/7 16:16
 */
public class RandomLoadBalancer implements LoadBalancer{

    private final Random random=new Random();

    @Override
    public String select(Map<String, Object> requestParams, List<String> serviceAddressList) {
        int size = serviceAddressList.size();
        if (size==0){
            return null;
        }

        //只有1个不用随机
        if (size==1){
            return serviceAddressList.get(0);
        }
        return serviceAddressList.get(random.nextInt(size));
    }
}
