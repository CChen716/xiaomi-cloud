package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 自定义响应体
 * @author cg
 * @Date 2024/11/29 16:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    //响应数据
    private Object date;

    //响应消息
    private String msg;

    //响应数据类型
    private Class<?> dataType;



}
