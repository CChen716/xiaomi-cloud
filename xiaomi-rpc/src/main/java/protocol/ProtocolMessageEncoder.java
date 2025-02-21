package protocol;

import core.SerializerFactory;
import io.vertx.core.buffer.Buffer;
import serialize.Serialize;

/** TCP服务器消息编码器
 * @author cg
 * @Date 2025/1/2 13:25
 */
public class ProtocolMessageEncoder {

    /**
     * 对消息进行编码处理
     * @param protocolMessage
     * @return
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws Exception {
        if (protocolMessage==null||protocolMessage.getHeader()==null){
            return Buffer.buffer();
        }
        Header header = protocolMessage.getHeader();
        //按照顺序依次向buffer缓冲区中写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerialize());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getId());
        //获取对应的序列化器
        ProtocolMessageSerializeEnum  serializeEnum= ProtocolMessageSerializeEnum.getEnumByKey(header.getSerialize());
        if (serializeEnum==null){
            throw new RuntimeException("序列化协议不存在！！！");
        }
        Serialize serialize = SerializerFactory.getInstance(serializeEnum.getValue());
        byte[] bodyByte = serialize.serialize(protocolMessage.getBody());//将消息对象的body序列化
        //最后写入body的长度和数据
        buffer.appendInt(bodyByte.length);
        buffer.appendBytes(bodyByte);
        return buffer;
    }

}
