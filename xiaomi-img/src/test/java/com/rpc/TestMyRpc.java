package com.rpc;

import com.yc.ImgApp;
import com.yc.biz.UserBizImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cg
 * @Date 2025/1/27 15:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class TestMyRpc {

    @Autowired
    private UserBizImpl userBiz;

    @Test
    public void testRpc(){
        userBiz.testRPC();
    }
}
