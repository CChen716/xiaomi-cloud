package loadbalance;

/**
 * @author cg
 * @Date 2025/1/7 16:48
 */
public interface LoadBalancerKey {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性Hash
     */
    String CONSISTENT_HASH = "consistentHash";
}
