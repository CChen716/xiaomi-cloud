package com.yc.myRpc.core;


import com.yc.myRpc.utils.SerializeUtils;
import entity.RpcDTO;
import entity.RpcResponse;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;

/** http请求处理器
 * @author cg
 * @Date 2024/11/29 17:12
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    //http服务器接收到请求后 在此处理请求
    @Override
    public void handle(HttpServerRequest request) {
        System.out.println("接受到请求："+request.method()+""+request.uri());
        RpcMsgDispatch rpcMsgDispatch = new RpcMsgDispatch();
        RpcResponse rpcResponse = new RpcResponse();//自定义响应体

        //异步处理请求
        request.bodyHandler(body->{

            byte [] bytes=body.getBytes();
            SerializeUtils serializeUtils = new SerializeUtils();
            //到这里开始处理请求中的数据
            try {
                //处理请求 进行反序列化处理
                RpcDTO resultDto = serializeUtils.deSerialize(RpcDTO.class, bytes);

                //执行目标方法
                Object result = rpcMsgDispatch.methodExecute(resultDto);
                //构造响应数据
                rpcResponse.setDate(result);
                rpcResponse.setMsg("200"); //调用成功
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setDate(e.getMessage());
                rpcResponse.setMsg("500");//调用失败

            }
            //响应
            doResponse(rpcResponse,request,serializeUtils);
        });
    }


    //响应方法
    public void doResponse(RpcResponse rpcResponse,HttpServerRequest request,SerializeUtils serializeUtils){
        HttpServerResponse httpServerResponse=request.response()
                .putHeader("content-type", "application/json");
        //响应序列化处理
        try {
            byte[] serialize = serializeUtils.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialize));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }

    }

}
