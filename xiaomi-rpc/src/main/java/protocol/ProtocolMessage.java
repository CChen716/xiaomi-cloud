package protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 协议消息结构体
 * @author cg
 * @Date 2024/12/30 14:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 请求头
     */
    private Header header;
    /**
     * 请求体（请求或者响应对象）
     */
    private T body;


}
