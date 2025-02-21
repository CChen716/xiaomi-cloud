package serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.RpcDTO;
import entity.RpcResponse;

import java.io.IOException;
import java.lang.reflect.Type;

/** JSON序列化
 * @author cg
 * @Date 2024/12/10 15:14
 */
public class JsonSerializer implements Serialize{

    private static final ObjectMapper objectMapper=new ObjectMapper();


    public <T> byte[] serialize(T obj) throws Exception {
       return objectMapper.writeValueAsBytes(obj);

    }

    public <T> T deSerialize(Class<T> classType,byte[] bytes) throws Exception {
        //这里为什么要用TypeReference 而不是直接 classType 是由于目标对象中的原始对象Object类型会被擦除，导致运行时并不知道反序列化为具体的泛型对象，
        // 导致返回的是一个LinkedHashMap,无法转换成具体的对象  此处RpcDTO 和Response对象中的属性都有原始对象类型 所以要做特殊处理

        T obj = objectMapper.readValue(bytes, classType);
        if(obj instanceof RpcDTO){
            return handleRequest((RpcDTO) obj,classType);
        }
        if (obj instanceof RpcResponse){
            return handleResponse((RpcResponse) obj,classType);
        }


        return obj;
    }


    public <T> T handleRequest(RpcDTO rpcDTO,Class<T> classType) throws Exception {
        Class<?>[] paramTypes = rpcDTO.getParamTypes(); //获取参数类型
        Object[] paramList = rpcDTO.getParamList();

        //循环处理每个参数的类型
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            //那么就需要去处理每一个参数的类型是否正确 如果不正确则处理为正确的类型
            if(!paramType.isAssignableFrom(paramList[i].getClass())){  //这个方法是表示检查是否是对应类的实现类或者子类
                //如果类型不一致
                byte[] bytes = objectMapper.writeValueAsBytes(paramList[i]); //将目标序列化成字节数组
                paramList[i]=objectMapper.readValue(bytes,paramType);//然后反序列化为对应的类型
            }
        }
        return classType.cast(rpcDTO);//强制转换的方法
    }

    public <T> T handleResponse(RpcResponse response,Class<T> classType) throws Exception {
        byte[] bytes = objectMapper.writeValueAsBytes(response.getDate());
        response.setDate(objectMapper.readValue(bytes,response.getDataType()));
        return classType.cast(response);
    }


}
