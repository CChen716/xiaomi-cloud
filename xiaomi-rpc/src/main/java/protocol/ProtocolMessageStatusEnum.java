package protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {

    ok("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text=text;
        this.value=value;
    }

    /**
     * 根据value获取枚举值
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for (ProtocolMessageStatusEnum aEnum : ProtocolMessageStatusEnum.values()) {
            if (aEnum.value==value){
                return aEnum;
            }

        }
        return null;
    }



}
