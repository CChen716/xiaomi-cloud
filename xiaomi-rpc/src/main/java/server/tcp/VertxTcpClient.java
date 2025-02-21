package server.tcp;

import cn.hutool.core.util.IdUtil;
import core.RpcConfigApplication;
import entity.RpcDTO;
import entity.RpcResponse;
import entity.ServerInfo;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import protocol.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/** TCP客户端
 * @author cg
 * @Date 2024/12/30 18:47
 */
public class VertxTcpClient {


    /**
     * TCP客户端发送请求
     * @return
     */
    public static RpcResponse doRequest(RpcDTO rpcDTO, ServerInfo serverInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx(); //创建实例  发送TCP请求
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();//异步编程类 这里用于将vertx的处理异步改为同步
        netClient.connect(serverInfo.getPort(),serverInfo.getHost(),
                result->{
                    if (result.succeeded()){
                        System.out.println("Connect to TCP server");
                        NetSocket socket = result.result();
                        //发送请求
                        //构造消息对象
                        ProtocolMessage<RpcDTO> protocolMessage = new ProtocolMessage<>();
                        Header header = new Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerialize((byte) ProtocolMessageSerializeEnum.getEnumByValue(RpcConfigApplication.rpcConfig.getSerialize()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        //生成全局请求id
                        header.setId(IdUtil.getSnowflakeNextId()); //雪花ID耗时较长 后续考虑可以优化
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcDTO);  //body就是rpc请求对象

                        //编码请求
                        try {
                            Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encodeBuffer);
                        } catch (Exception e) {
                            throw new RuntimeException("协议消息编码错误");
                        }

                        //接受响应
                        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                            try {
                                ProtocolMessage<RpcResponse> responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseFuture.complete(responseProtocolMessage.getBody());//该方法作用将CompletableFuture中的任务改为已完成  那么下面的get方法就会停止阻塞
                            } catch (Exception e) {
                                throw new RuntimeException("协议消息解码错误");
                            }
                        }
                        );
                        socket.handler(tcpBufferHandlerWrapper);

                    }else {
                        System.out.println("Fail to Connect to TCP server!!!!");
                        responseFuture.completeExceptionally(new RuntimeException("连接失败")); // 使用 completeExceptionally
                        //  return;
                    }
                });
        //这里主要是用于阻塞 和complete方法搭配使用
        RpcResponse rpcResponse = responseFuture.get();
        //关闭连接
        netClient.close();
        return rpcResponse;
    }



    //测试连接
//    public void  start(){
//            //创建实例
//        Vertx vertx = Vertx.vertx();
//        vertx.createNetClient().connect(8888,"localhost",result->{
//            if(result.succeeded()){
//                System.out.println("connect to tcp server");
//                NetSocket socket = result.result();
//                //发送数据
//                socket.write("hello server");
//                //接受响应
//                socket.handler(buffer -> {
//                    System.out.println("接受到响应数据：" + buffer.toString());
//                });
//            }else {
//                System.out.println("Failed to connect to tcp server");
//            }
//
//        });
//    }


    //演示半包粘包问题
//    public void doStart(){
//        // 创建Vert.x实例
//        Vertx vertx = Vertx.vertx();
//
//        vertx.createNetClient().connect(8888, "localhost", result -> {
//            if (result.succeeded()) {
//                System.err.println("connect to TCP server");
//                io.vertx.core.net.NetSocket socket = result.result();
//
//                // 发送数据(模拟发送1000次请求)
//                for(int i=0;i<1000;i++){
//                    socket.write("Hello,server!Hello,server!Hello,server!");
//                }
//
//                // 接收响应
//                socket.handler(buffer -> {
//                    System.out.println("received response from server: " + buffer.toString());
//                });
//            } else {
//                System.err.println("fail to connect to TCP server");
//            }
//        });
//
//    }


}
