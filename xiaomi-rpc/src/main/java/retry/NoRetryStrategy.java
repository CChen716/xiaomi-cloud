package retry;

import entity.RpcResponse;

import java.util.concurrent.Callable;

/** 不重试-重试策略
 * @author cg
 * @Date 2025/1/12 14:23
 */
public class NoRetryStrategy implements RetryStrategy{


    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();//不重试就直接调用然后返回结果
    }
}
