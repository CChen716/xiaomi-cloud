package core;


import entity.RpcDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/** 监听服务器接收到请求后 对数据的处理派发类
 * @author cg
 * @Date 2024/11/27 20:11
 */
@Component
public class RpcMsgDispatch {

    //这里也就是拿到请求数据反序列化后的dto对象 然后进行反射调用
    public  Object methodExecute(RpcDTO dto)  {

        Object result;

        //成功执行响应结果 未成功执行 异常处理
        try{
            //根据全限定类名找到目标类的Class对象  ————》这一步可以进一步优化 比如将所有支持远程调用的方法和类注册到map中 这样会提高更多效率

            //使用了本地注册 改为从map中拿取对应的对象实例
            //是否需要分析先拿实例和先拿class对象两者顺序差别的性能优劣？
            //1.拿到实例反推Class对象
            Object instance = LocalRegister.proxyMap.get(dto.getClassName());
            Class<?> implClass = instance.getClass();
            Method method = implClass.getMethod(dto.getMethodName(), dto.getParamTypes());
            //注意invoke方法传递的是一个实例对象 也就是得需要有一个接口实现类的实例对象  而不是class对象
            result = method.invoke(instance, dto.getParamList());//执行具体的方法
            //注意invoke的返回值是和目标方法一致  如果目标方法返回对象则需要注意对象处理，如果void 则返回null

            //2先拿到class对象 再在map中拿实例  --第一种和第二种关键就是 map中找与 Class.forName方法找 性能比较  明显map中性能更优优点更多
//            Class<?> targetClass = Class.forName(dto.getClassName()+"Impl");
//            Method method = targetClass.getMethod(dto.getMethodName(), dto.getParamTypes());
//            result = method.invoke(targetClass.getConstructor().newInstance(), dto.getParamList());

        }catch (Exception e){
            System.out.println("方法:"+dto.getMethodName()+"调用失败"+e.getMessage());
            result="error";
        }
        return result;
    }

}
