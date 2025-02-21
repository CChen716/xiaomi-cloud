package protocol;

import lombok.Data;

/** 协议消息体头
 * @author cg
 * @Date 2024/12/30 14:45
 */
@Data
public class Header {
    /**
     * 魔数 用于安全校验
     */
    private byte magic;
    /**
     * 版本号 用于确定一致性
     */
    private byte version;
    /**
     * 类型 用于确定请求类型
     */
    private byte type;
    /**
     * 序列化方式
     */
    private byte serialize;
    /**
     * 状态码
     */
    private byte status;
    /**
     * 请求ID
     */
    private long id;
    /**
     * 请求体长度
     */
    private int bodyLength;

}
