package serialize;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * @author cg
 * @Date 2024/12/10 21:49
 */
public interface Serialize {

    public <T> byte[] serialize(T obj) throws Exception;
    public <T> T deSerialize(Class<T> classType,byte[] bytes) throws Exception;
}
