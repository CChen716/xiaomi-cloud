package tolerant;

import java.util.HashMap;
import java.util.Map;

/** 容错机制策略工厂
 * @author cg
 * @Date 2025/1/13 16:26
 */
public class TolerantStrategyFactory {

    private static final Map<String,TolerantStrategy> TOLERANT_STRATEGY_MAP=new HashMap<String,TolerantStrategy>(){{
        put(TolerantStrategyKeys.FAIL_FAST,new FailFastTolerantStrategy());
        put(TolerantStrategyKeys.FAIL_SAFE,new FailSafeTolerantStrategy());
    }};


    private static final TolerantStrategy DEFAULT_TOLERANT=new FailFastTolerantStrategy();

    public static TolerantStrategy getInstance(String key){
        return TOLERANT_STRATEGY_MAP.get(key);
    }
}
