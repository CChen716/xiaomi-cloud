package protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**消息序列化器枚举
 * @author cg
 * @Date 2024/12/30 16:22
 */
@Getter
public enum ProtocolMessageSerializeEnum {

    HESSIAN(1,"hessian"),
    KRYO(2,"kryo"),
    JSON(3,"json");

    private final int key;
    private final String value;

    ProtocolMessageSerializeEnum(int key, String value) {
        this.key=key;
        this.value=value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 key 获取枚举
     *
     * @param key
     * @return
     */
    public static ProtocolMessageSerializeEnum getEnumByKey(int key){
        for (ProtocolMessageSerializeEnum anEnum : ProtocolMessageSerializeEnum.values()){
            if (anEnum.key == key){
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ProtocolMessageSerializeEnum getEnumByValue(String value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (ProtocolMessageSerializeEnum anEnum : ProtocolMessageSerializeEnum.values()){
            if (anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }

}
