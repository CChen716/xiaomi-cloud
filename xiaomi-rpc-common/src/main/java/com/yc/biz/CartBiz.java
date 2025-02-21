package com.yc.biz;

/**  公共接口类 也就是需要被远程调用的接口  此处只要声明接口即可不用具体实现
 * @author cg
 * @Date 2024/11/28 21:49
 */
public interface CartBiz {

    //测试方法
    public String TestRpc(String userID,String id);

    //
    default String TestMock(){
        return "1";
    }
}
