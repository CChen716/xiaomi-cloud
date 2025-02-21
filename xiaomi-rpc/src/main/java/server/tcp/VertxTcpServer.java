package server.tcp;

import core.InspectAnnotation;
import core.RpcConfigApplication;
import entity.RpcConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;


/**  Http监听服务器--采用高性能Vertx框架
 * @author cg
 * @Date 2024/11/28 15:00
 */
public class VertxTcpServer {


//    public static void main(String[] args) throws Exception {
//        //1.框架初始化
//        core.RpcConfigApplication.init();
//        //2.扫描注解 并上传配置
//        InspectAnnotation.scanAnnotation();
//
//        new VertxTcpServerTest().doStart();
//
//        //Http服务器
//      //  VertxHttpServer vertxHttpServer=new VertxHttpServer();
//      //  Vertx vertx=Vertx.vertx();
//        //部署verticle,会自动调用start方法
//      //  vertx.deployVerticle(vertxHttpServer);
//    }


    /**继承AbstractVerticle 然后重写start方法和stop方法 在start方法中处理业务逻辑*/

    public  void start(Integer port) {
        //全局配置对象
      //  RpcConfig rpcConfig = RpcConfigApplication.getRpcConfig();

        //创建Vertx实例
        Vertx vertx =Vertx.vertx();
        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求  已修改为自定义TCPServerHandler
        server.connectHandler(new TcpServerHandler());

        //启动TCP服务并监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("  __  ____     __     _____  _____   _____ \n" +
                        " |  \\/  \\ \\   / /    |  __ \\|  __ \\ / ____|\n" +
                        " | \\  / |\\ \\_/ /_____| |__) | |__) | |     \n" +
                        " | |\\/| | \\   /______|  _  /|  ___/| |     \n" +
                        " | |  | |  | |       | | \\ \\| |    | |____ \n" +
                        " |_|  |_|  |_|       |_|  \\_\\_|     \\_____|\n" +
                        "                                          ");
                System.out.println("Server is now listening on port " + port);

            }else{
                System.err.println("Failed to start server: " + result.cause());
            }
        });

    }


}
