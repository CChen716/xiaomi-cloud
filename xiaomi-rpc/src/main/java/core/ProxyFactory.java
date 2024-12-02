package core;

import java.lang.reflect.Proxy;

/** 动态代理对象工厂 --生成目标类的代理对象
 * @author cg
 * @Date 2024/11/27 14:12
 */
public class ProxyFactory {


    /**
     *  根据类名方法名 获取具体的实现类的代理类
     * @return  代理对象
     */
    public static   <T>  T createProxy(Class impClass) throws ClassNotFoundException {

        //    Class<?>  impClass = Class.forName(packAgeName); //得到目标类Class对象
            return (T) Proxy.newProxyInstance(impClass.getClassLoader(), //目标类的类加载器
                                                new Class[]{impClass},   /*目标类实现的接口*/
                                                new ProxyServiceInvocation()); //实现了InvocationHandler接口的类 也就是当代理对象被调用的时候会 自动调用invoke方法)
    }

}
