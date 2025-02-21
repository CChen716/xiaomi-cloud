package testRpc;

import com.yc.CartApp;
import entity.RpcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author cg
 * @Date 2024/12/4 21:56
 */
@SpringBootTest(classes = CartApp.class)
public class TestRpc {

    @Autowired
    private RpcConfig config;

    @Test
    public void testYml(){
        System.out.println(config.getPackName());
    }


}
