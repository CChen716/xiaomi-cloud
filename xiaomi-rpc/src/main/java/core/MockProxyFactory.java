package core;


import java.lang.reflect.Proxy;

/** mock代理对象生成接口
 * @author cg
 * @Date 2024/12/6 15:06
 */
public class MockProxyFactory {

    public static <T> T creatMockProxy(Class<T> mockClass){
     return (T) Proxy.newProxyInstance(mockClass.getClassLoader(),
                new Class[]{mockClass},
                new MockProxyServiceInvocation());
    }

}
