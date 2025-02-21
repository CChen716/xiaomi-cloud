package core;

import annotation.EnableRpc;
import entity.RpcConfig;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 注解扫描类  扫描类上面的注解
 * @author cg
 * @Date 2024/12/4 13:27
 */
public class InspectAnnotation {

    /**
     * 扫描指定包下的rpc注解
     */
    public static void scanAnnotation() throws Exception {
        //扫描业务类 也就是要扫描某个包下的所有类 然后再去检查这些类的哪些方法上有rpc注解 就知道该注册哪些类和方法
        RpcConfig rpcConfig = RpcConfigApplication.getRpcConfig();

        String packageName=rpcConfig.getPackName(); //指定包名  改为从配置文件对象中获取
        List<Class> classes=new ArrayList<>(); //创建class对象集合 用来存放符合的类对象

        //将.换成/ 是因为会按照文件路径去找 而java里面的路径分割是。 而文件路径分割是/ 所以要替换
        String path = packageName.replace(".", "/");

        File file=new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(path).getFile()));
        //返回如果是空报异常                                                     根据路径获得URL对象表示该资源的位置，然后getFile从url对象中找到文件路径
        if(file.exists()){
            File[] files = file.listFiles();//获取包下所有文件
            if(files!=null){
                for (File cls : files) {
                    if(cls.getName().endsWith(".class")){//找到左右class文件
                        String className=packageName+"."+cls.getName().substring(0,cls.getName().length()-6); //需要去掉.class 构成class对象的全限定类名
                        classes.add(Class.forName(className));//将class对象添加进去
                    }
                }
            }
        }
        //到这里获取到了指定包下的所有类的类对象

        //下一步就是通过反射检查这些类对象里面的方法上面有没有注解
        List<String> allMethod=new ArrayList<>();
        for (Class targetClass : classes) {
            Method[] methods = targetClass.getDeclaredMethods();//返回所有方法
            for (Method method : methods) {
                if(method.isAnnotationPresent(EnableRpc.class)){//找到所有方法上面带对应注解的对象
                    //由于本注解只是标识作用 注解中没有值 所以不需要获取注解对象
                    allMethod.add(targetClass.getName()+":"+method.getName()); //包名:方法名
                }
            }
        }
        //至此包下的所有远程方法的名字已经全部获取到  格式  包名：方法名
        System.out.println("rpc all method:"+allMethod);


        //后续搭配远程注册中心开发
        NacosConfigLoad.pushNacosConfig(allMethod);
    }


}
