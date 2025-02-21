package loadbalance;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author cg
 * @Date 2025/1/7 16:36
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{


    /**
     * 一致性Hash环，存放虚拟节点
     * @param requestParams 请求参数
     * @param serviceAddressList 可用服务列表
     * @return
     */
    private final TreeMap<Integer,String> virtualNodes=new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_MODE_NUM=100;


    @Override
    public String select(Map<String, Object> requestParams, List<String> serviceAddressList) {
        if (serviceAddressList.isEmpty()){
            return null;
        }

        //构建虚拟节点环
        for (String serverAddr : serviceAddressList) {
            for (int i = 0; i < VIRTUAL_MODE_NUM; i++) {
                int hash=getHash(serverAddr+"#"+i);
                virtualNodes.put(hash,serverAddr);
            }
        }

        //获取调用请求的hash值
        int hash = getHash(requestParams);

        //选择最近且大于等于调用请求hash值节点
        Map.Entry<Integer, String> entry = virtualNodes.ceilingEntry(hash);
        if (entry==null){
            //如果没有大于等于调用请求hash值的虚拟节点，则返回首部节点
            entry=virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 后续可以优化 实现自定义hash
     * @param requestParams
     * @return
     */
    private int getHash(Object requestParams){
        return requestParams.hashCode();

    }

}
