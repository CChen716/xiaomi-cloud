package tolerant;

import entity.RpcResponse;

import java.util.Map;

/** 静默处理
 * @author cg
 * @Date 2025/1/13 16:23
 */
public class FailSafeTolerantStrategy implements TolerantStrategy{

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        System.out.println("Fail-Safe处理异常"+e);
        return new RpcResponse();//返回一个空对象
    }
}
