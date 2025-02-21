package server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import protocol.ProtocolConstant;

/** 装饰者模式，  使用recordParser对原有buffer 处理能力进行增强
 * @author cg
 * @Date 2025/1/2 22:46
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser=initRecordParser(bufferHandler);
    }


    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler){
        //构造parser
        RecordParser recordParser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH); //头信息为17个字节 所以先读取前17个字节

        recordParser.setOutput(new Handler<Buffer>() {
            //初始化
            int size=-1;

            //一次完整的读取  头+体
            Buffer resultBuffer=Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (-1==size){
                    //读取消息体长度
                    size=buffer.getInt(13);  //从指定位置开始读取  int 表示读取4个字节
                    recordParser.fixedSizeMode(size);//此时读取到的就是请求头中的 请求体长度值 然后将parser读取大小修改为请求体的长度 那么下次就会读取指定大小的数据
                    //写入头信息到结果
                    resultBuffer.appendBuffer(buffer);
                }else {
                    //写入体信息到结果
                    resultBuffer.appendBuffer(buffer);
                    //已经拼接为完整的Buffer 执行处理
                    bufferHandler.handle(resultBuffer);

                    //重置一轮
                    recordParser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size=-1;
                    resultBuffer=Buffer.buffer();
                }

            }
        });
        return recordParser;
    }
}
