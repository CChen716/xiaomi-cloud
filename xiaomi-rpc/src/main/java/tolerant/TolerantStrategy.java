package tolerant;

import entity.RpcResponse;

import java.util.Map;

/** 容错策略通用接口
 * @author cg
 * @Date 2025/1/13 14:29
 */
public interface TolerantStrategy {
    /**
     * 容错
     * @param context 上下文数据
     * @param e 异常
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
