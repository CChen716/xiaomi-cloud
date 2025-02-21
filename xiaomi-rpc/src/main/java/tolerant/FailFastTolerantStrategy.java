package tolerant;

import entity.RpcResponse;

import java.util.Map;

/** 快速失败 容错策略
 * @author cg
 * @Date 2025/1/13 14:55
 */
public class FailFastTolerantStrategy implements TolerantStrategy{


    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        System.out.println("Fail-fast处理异常"+e);//可替换成自选的日志打印
        throw new RuntimeException("服务报错",e);
    }
}
