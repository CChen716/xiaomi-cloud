package protocol;

import lombok.Getter;

/** 消息类型枚举
 * @author cg
 * @Date 2024/12/30 16:06
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BAT(2),
    OTHER(3);


    private final int key;

    ProtocolMessageTypeEnum(int key){
        this.key=key;
    }

    public static ProtocolMessageTypeEnum getEnumByType(int key){
        for (ProtocolMessageTypeEnum aEnum : ProtocolMessageTypeEnum.values()) {
            if (aEnum.key==key){
                return aEnum;
            }

        }
        return null;
    }


}
