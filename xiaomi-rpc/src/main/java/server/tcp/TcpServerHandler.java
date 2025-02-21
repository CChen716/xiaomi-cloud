package server.tcp;

import core.RpcMsgDispatch;
import entity.RpcDTO;
import entity.RpcResponse;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import protocol.*;

/**处理请求Handler类
 * @author cg
 * @Date 2024/12/30 19:26
 */
public class TcpServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper tcpBufferHandlerWrapper=new TcpBufferHandlerWrapper(buffer -> {
            //接收到请求，开始解码
            ProtocolMessage<RpcDTO> protocolMessage = null;
            try {
                protocolMessage = (ProtocolMessage<RpcDTO>) ProtocolMessageDecoder.decode(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RpcDTO request = protocolMessage.getBody();
            RpcMsgDispatch rpcMsgDispatch = new RpcMsgDispatch();

            //处理请求
            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            try {
                //获取要调用的服务实现类，通过反射调用
                Object result = rpcMsgDispatch.methodExecute(request);
                //封装返回结果
                rpcResponse.setDate(result);
                rpcResponse.setMsg("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMsg(e.getMessage());
                rpcResponse.setMsg("fail");
            }

            //发送响应
            //对响应对象进行编码
            Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (Exception e) {
                throw new RuntimeException("协议消息编码错误");
            }

        });
        netSocket.handler(tcpBufferHandlerWrapper);
        }
    }

