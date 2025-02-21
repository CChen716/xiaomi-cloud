package core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import entity.RpcDTO;
import entity.RpcResponse;
import entity.ServerInfo;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import loadbalance.LoadBalancer;
import loadbalance.LoadBalancerFactory;
import loadbalance.LoadBalancerKey;
import protocol.*;
import retry.RetryStrategy;
import retry.RetryStrategyFactory;
import serialize.Serialize;
import serialize.SerializeUtils;
import server.tcp.TcpBufferHandlerWrapper;
import server.tcp.VertxTcpClient;
import tolerant.TolerantStrategy;
import tolerant.TolerantStrategyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** 自定义代理对象执行类
 * @author cg
 * @Date 2024/11/27 14:37
 */
public class ProxyServiceInvocation  implements InvocationHandler {

    /** 当用户调用某个接口的方法时，会改为调用invoke方法，并根据代理对象，方法信息以及参数列表来执行具体的调用
     *  该方法的功能就是执行具体逻辑后 返回给客户端数据
     * @param proxy 代理对象
     * @param method 方法名
     * @param args 参数
     * @return
     * @throws Throwable
     */

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //当调用代理对象的方法时，就会跳转到这里
        //invoke方法的原本含义是执行目标方法，但是在此处是改成发送远程请求，让在客户端的角度来看调用了这个远程方法就像在本地调用一样！

        //proxy就是代理对象  method就是目标方法  args就是参数列表
        //改为用户动态选择序列化配置
       // Serialize serializeUtils=SerializerFactory.getInstance(RpcConfigApplication.rpcConfig.getSerialize());

        //构造器模式创建请求对象
        RpcDTO rpcDTO=RpcDTO.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramList(args)
                .paramTypes(method.getParameterTypes())
                .build();

        //构造好对象后是否需要进行安全校验  也就是检查提供端是否支持该方法进行远程调用  防止调用到一个类下面的非远程方法
        Map<String, List<String>> serverMap= NacosConfigPull.checkMethod(rpcDTO);
        if(serverMap==null){//如果返回null 则代表没有对应的服务信息
            System.out.println("当前接口不支持远程调用,请重新检查接口名或者确认远程调用信息！！！");
            return null;
        }

        String serverName="";
        List<String> serverList=new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : serverMap.entrySet()) {
             serverName = entry.getKey();
             serverList= entry.getValue();
        }

        //获取到服务名后 通过负载均衡器获取到最终发送的服务地址
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(serverName, LoadBalancerKey.ROUND_ROBIN);//TODO:后续更改为配置中心获取
        HashMap<String, Object> requestParams = new HashMap<>();//这里使用一个map是确保如果使用的是一致性hash的话 那么同一个方法请求的hash值肯定是一样的，这样会送到同一个服务器上
        requestParams.put("methodName",method.getName());
        String serverTarget = loadBalancer.select(requestParams, serverList);//负载均衡器返回的是某一个服务地址 然后分割处理
        String[] split = serverTarget.split(":");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHost(split[0]);
        serverInfo.setPort(Integer.valueOf(split[1])); //此处需要替换成rpc的服务端口  checkMethod返回的是web服务的地址

        //发送请求
        //RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcDTO,serverInfo);
        RpcResponse rpcResponse ;
        //请求加入重试机制 发送TCP请求
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(RpcConfigApplication.rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcDTO, serverInfo)
            );
       //     rpcResponse=  VertxTcpClient.doRequest(rpcDTO, serverInfo);

        }catch (Exception e){
            //容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(RpcConfigApplication.rpcConfig.getTolerantStrategy());
            rpcResponse  = tolerantStrategy.doTolerant(null, e);

        }
        return rpcResponse.getDate();

    }
}
