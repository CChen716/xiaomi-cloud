package com.yc.myRpc.core;

import com.yc.myRpc.entity.RpcDTO;
import entity.RpcResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;


/**  Http监听服务器--采用高性能Vertx框架
 * @author cg
 * @Date 2024/11/28 15:00
 */
public class VertxHttpServer extends AbstractVerticle {


    public static void main(String[] args) {
        VertxHttpServer vertxHttpServer=new VertxHttpServer();
        Vertx vertx=Vertx.vertx();

        //部署verticle,会自动调用start方法
        vertx.deployVerticle(vertxHttpServer);
    }


    /**继承AbstractVerticle 然后重写start方法和stop方法 在start方法中处理业务逻辑*/
    @Override
    public void start() {

        //这里可以通过this获取当前的vertx
        Vertx vertx=this.vertx;

        //创建一个HttpServer
        HttpServer httpServer=vertx.createHttpServer();
        //改为自定义Handler类处理业务逻辑
        httpServer.requestHandler(new HttpServerHandler());

        httpServer.listen(8888,result->{
            if(result.succeeded()){
                System.out.println("Server is now listening on port " + "8888");
            }else{
                System.err.println("Failed to start server: " + result.cause());
            }
        });
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("VertxHttpServer  stop !!!!");
    }

}
