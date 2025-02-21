package retry;

import com.github.rholder.retry.*;
import entity.RpcResponse;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** 固定时间间隔-重试策略
 * @author cg
 * @Date 2025/1/12 15:12
 */
public class FixedIntervalRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer= RetryerBuilder.<RpcResponse>newBuilder() //创建重试器对象
                .retryIfExceptionOfType(IOException.class) //重试条件：指捕获到指定类型的异常后进行重试
                .retryIfExceptionOfType(TimeoutException.class) //仅仅重试网络和超时异常
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))//设置时间 间隔3秒
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))//次数上限
                .withRetryListener(new RetryListener() { //监听器功能
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            System.out.println("重试次数（异常触发）：" + attempt.getAttemptNumber());
                        }

                    }
                })
                .build();


        return retryer.call(callable);
    }
}
