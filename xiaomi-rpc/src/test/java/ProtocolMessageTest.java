import cn.hutool.core.util.IdUtil;
import entity.RpcDTO;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;
import protocol.*;

import java.io.IOException;

/**
 * @author cg
 * @Date 2025/1/2 14:23
 */
public class ProtocolMessageTest {
    @Test
    public void testEncodeAndDecode() throws Exception {
        //构造消息

        ProtocolMessage<Object> objectProtocolMessage = new ProtocolMessage<>();
        Header header = new Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerialize((byte) ProtocolMessageSerializeEnum.HESSIAN.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.ok.getValue());
        header.setId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcDTO rpcDTO = new RpcDTO();
        rpcDTO.setClassName("serviceMy");
        rpcDTO.setMethodName("add");
        rpcDTO.setParamList(new Object[]{"aaa","bbb"});
        rpcDTO.setParamTypes(new Class[]{System.class});

        objectProtocolMessage.setHeader(header);
        objectProtocolMessage.setBody(rpcDTO);
        Buffer encode = ProtocolMessageEncoder.encode(objectProtocolMessage);
        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);
        Assert.assertNotNull(decode);

    }

    


}
