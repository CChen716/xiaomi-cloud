package serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/** Kryo序列化
 * @author cg
 * @Date 2024/12/10 13:53
 */
public class KryoSerializer implements Serialize{


    //因为kryo线程不安全 所以需要使用ThreadLocal 来使得每个线程都拥有一个kryo对象，避免出现线程安全问题
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL=ThreadLocal.withInitial(()->{

        Kryo kryo = new Kryo();
        //设置动态序列化与反序列化 不提前注册所有类 因为考虑到可能有安全问题
        kryo.setRegistrationRequired(false);
        return kryo;
    });


    /**
     * 序列化
     * @param obj
     * @param <T>
     * @return
     */
    public <T> byte[] serialize(T obj){
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        Output output = new Output(oos);
        KRYO_THREAD_LOCAL.get().writeObject(output,obj);
        output.close();
        return oos.toByteArray();
    }

    /**
     * 反序列化
     * @param classType
     * @param targetArray
     * @param <T>
     * @return
     */
    public <T> T deSerialize(Class<T> classType,byte[] targetArray){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(targetArray);
        Input input = new Input(byteArrayInputStream);
        T res = KRYO_THREAD_LOCAL.get().readObject(input, classType);
        input.close();
        return res;
    }
}
