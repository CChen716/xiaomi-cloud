package core;


import entity.SerializeKeys;
import serialize.JsonSerializer;
import serialize.KryoSerializer;
import serialize.Serialize;
import serialize.SerializeUtils;

import java.util.HashMap;
import java.util.Map;

/** 序列化器工厂 提供序列化对象
 * @author cg
 * @Date 2024/12/10 21:46
 */
public class SerializerFactory {

    //初始化序列对象映射
    private static final Map<String, Serialize> SERIALIZE_MAP=new HashMap<String,Serialize>(){{

        put(SerializeKeys.JSON,new JsonSerializer());
        put(SerializeKeys.KRYO,new KryoSerializer());
        put(SerializeKeys.HESSIAN,new SerializeUtils());

    }};


    private static final Serialize DEFAULT_SERIALIZER=new SerializeUtils(); //默认为hessian序列化

    public static Serialize getInstance(String key){
        return SERIALIZE_MAP.getOrDefault(key,DEFAULT_SERIALIZER);

    }
}
