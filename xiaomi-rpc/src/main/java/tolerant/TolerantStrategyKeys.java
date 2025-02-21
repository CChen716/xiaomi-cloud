package tolerant;

/** 容错机制常量
 * @author cg
 * @Date 2025/1/13 16:29
 */
public interface TolerantStrategyKeys {

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}
