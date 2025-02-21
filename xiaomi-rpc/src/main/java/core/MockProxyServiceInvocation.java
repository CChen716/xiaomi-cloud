package core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** mock模拟接口调用实现类
 * @author cg
 * @Date 2024/12/6 15:05
 */
public class MockProxyServiceInvocation implements InvocationHandler {



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();//根据返回值类型生成特定的值
        System.out.println("mock invoke info: "+method.getName());
        return getDefaultValue(returnType);
    }



    private Object getDefaultValue(Class returnType){
        if(returnType.isPrimitive()){//次方法是检查是否是原始数据类型
            if (returnType == boolean.class) {
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            }
        }
        return null;

    }
}
