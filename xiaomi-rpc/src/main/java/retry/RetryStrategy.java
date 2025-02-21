package retry;

import entity.RpcResponse;

import java.util.concurrent.Callable;

/** 重试策略接口
 * @author cg
 * @Date 2025/1/12 14:22
 */
public interface RetryStrategy {
    /**
     * 重试接口方法
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
