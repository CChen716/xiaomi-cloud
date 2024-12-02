
import com.yc.biz.CartBiz;
import core.ProxyFactory;

/**
 * @author cg
 * @Date 2024/11/30 21:13
 */

public class RpcApp {

    //模拟调用  消费端
    public static void main(String[] args) throws ClassNotFoundException {

        //消费端的业务逻辑
        //......


        //需要远程调用另一个服务中的方法
        //通过代理工厂获得一个代理对象 然后调用目标方法
        CartBiz cartBiz= ProxyFactory.createProxy(CartBiz.class);
        //消费端调用方法 实际会跳转到invoke方法中进行远程调用  但在消费端是无感知的
        String s = cartBiz.TestRpc("66", "8");
        System.out.println(s);
    }
}
