package protocol;

/** 协调包相关常量
 * @author cg
 * @Date 2024/12/30 15:00
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
     int MESSAGE_HEADER_LENGTH=17;
    /**
     * 协议魔数
     */
     byte PROTOCOL_MAGIC=0x1;
    /**
     * 版本号
     */
     byte PROTOCOL_VERSION=0x1;
}
