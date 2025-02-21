package serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** 序列化与反序列化工具类
 * @author cg
 * @Date 2024/11/27 15:12
 */
public class SerializeUtils implements Serialize{

    /**
     * 序列化 将JAVA对象转为二进制对象
     * @param msgBean
     * @param <T>
     * @return
     */
    public <T>  byte[] serialize(T msgBean) throws IOException {
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        Hessian2Output ho=new Hessian2Output(oos);
        ho.writeObject(msgBean);
        ho.flush();
//        oos.close();
//        ho.close();
        return  oos.toByteArray();
    }

    /**
     * 反序列化  将二进制数据转换为Java对象
     * @param clazz
     * @param serializeArray
     * @param <T>
     * @return
     */
    public <T>  T deSerialize(Class<T> clazz,byte[] serializeArray) throws IOException {
        ByteArrayInputStream inp = new ByteArrayInputStream(serializeArray);
        Hessian2Input hi= new Hessian2Input(inp);
//        inp.close();
//        hi.close();
        return (T) hi.readObject(clazz); //将二进制数据转为目标对象
    }
}
