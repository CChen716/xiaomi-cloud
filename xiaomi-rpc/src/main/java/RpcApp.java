
import bootstrap.ConsumerBootstrap;
import com.alibaba.nacos.api.exception.NacosException;
import com.yc.biz.CartBiz;
import core.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import serialize.SerializeUtils;

import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * @author cg
 * @Date 2024/11/30 21:13
 */
public class RpcApp {

    //模拟调用  消费端
    public static void main(String[] args) throws ClassNotFoundException, NacosException {

        //消费端的业务逻辑
        //......
        // 初始化
//        RpcConfigApplication.init();
//        //消费端拉取配置
//        NacosConfigPull.pull();
//
//        NacosProviderWatch nacosProviderWatch = new NacosProviderWatch();
//        nacosProviderWatch.start(); //启动监听类
//        此处需要暂时创建一个端口为8888的模拟服务
         ConsumerBootstrap.init();


         HashMap<String, Boolean> testMap = new HashMap<>();
         testMap.put("localhost:8888",true);
//        testMap.put("localhost:9999",true);
        NacosProviderWatch.HealthInstanceStatus.put("xiaomi-cart",testMap);

        //需要远程调用另一个服务中的方法
        //通过代理工厂获得一个代理对象 然后调用目标方法
        CartBiz cartBiz= ProxyFactory.createProxy(CartBiz.class);
        //消费端调用方法 实际会跳转到invoke方法中进行远程调用  但在消费端是无感知的

        //TODO：后续应该在消费端也添加配置项 mock配置
//        CartBiz cartBiz2 = MockProxyFactory.creatMockProxy(CartBiz.class);
//        cartBiz2.TestMock();

        String s = cartBiz.TestRpc("66", "8");
        System.out.println(cartBiz.TestRpc("77", "9"));
        System.out.println(s);
        //框架初始化

    }
}
