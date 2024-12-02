package core;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import entity.RpcDTO;
import entity.RpcResponse;
import utils.SerializeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        SerializeUtils serializeUtils = new SerializeUtils();

        //构造器模式创建请求对象
        RpcDTO rpcDTO=RpcDTO.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramList(args)
                .paramTypes(method.getParameterTypes())
                .build();




        try {
            //序列化请求
            byte[] serialize = serializeUtils.serialize(rpcDTO);
            //下面可以发送请求了
            try(HttpResponse response= HttpRequest.post("localhost:8888")
                    .body(serialize)
                    .execute()){
                //接收到的响应
                byte[] res = response.bodyBytes();
                //将响应反序列化 拿到结果
                RpcResponse rpcResponse = serializeUtils.deSerialize(RpcResponse.class, res);
                return rpcResponse.getDate();
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        return null;
    }
}
