package retry;

import java.util.HashMap;

/** 重试策略工厂类 返回重试策略对象
 * @author cg
 * @Date 2025/1/12 15:21
 */
public class RetryStrategyFactory {


    private static final HashMap<String,RetryStrategy> RETRY_STRATEGY_HASH_MAP=new HashMap<String, RetryStrategy>(){{
        put(RetryStrategyKeys.NO,new NoRetryStrategy());
        put(RetryStrategyKeys.FIXED_INTERVAL,new FixedIntervalRetryStrategy());
    }};

    /**
     * 不重试
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY=new NoRetryStrategy();

    public static RetryStrategy getInstance(String key){
        return RETRY_STRATEGY_HASH_MAP.get(key);
    }


}
