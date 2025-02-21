package protocol;

import core.SerializerFactory;
import entity.RpcDTO;
import entity.RpcResponse;
import io.vertx.core.buffer.Buffer;
import serialize.Serialize;

/** TCP服务器消息解码器
 * @author cg
 * @Date 2025/1/2 13:49
 */
public class ProtocolMessageDecoder {


    public static ProtocolMessage<?> decode(Buffer buffer) throws Exception {
        //分别从指定位置读出buffer
        Header header = new Header();
        byte magic = buffer.getByte(0);
        //校验魔术
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息 magic 非法！");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerialize(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        //解决粘包问题，只读取指定长度
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        //解析消息体
        ProtocolMessageSerializeEnum serializeEnum = ProtocolMessageSerializeEnum.getEnumByKey(header.getSerialize());
        if (serializeEnum == null) {
            throw new RuntimeException("序列化消息协议不存在");
        }
        Serialize serialize = SerializerFactory.getInstance(serializeEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByType(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("序列化消息的类型不存在！");
        }

        switch (messageTypeEnum) {
            case REQUEST:
                RpcDTO request = serialize.deSerialize(RpcDTO.class, bodyBytes);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse rpcResponse = serialize.deSerialize(RpcResponse.class, bodyBytes);
                return new ProtocolMessage<>(header, rpcResponse);
            case HEART_BAT:
            case OTHER:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }

    }
}