package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** RPC消息传递DTO
 * @author cg
 * @Date 2024/11/27 15:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcDTO implements Serializable {


    //RPC框架在传递过程中的序列化与反序列化的对象
    /**全限定类名*/
    private String className;

    /**方法名*/
    private String methodName;

    /**参数列表*/
    private Object[] paramList;

    //参数类型
    private Class<?>[] paramTypes;


}
