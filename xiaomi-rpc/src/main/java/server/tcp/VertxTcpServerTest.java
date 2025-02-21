package server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/** VertX TCP服务端
 * @author cg
 * @Date 2024/12/30 18:47
 */
public class VertxTcpServerTest {

    public void  doStart(){
        //创建Vertx实例
        Vertx vertx =Vertx.vertx();
        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求  已修改为自定义TCPServerHandler
        server.connectHandler(new TcpServerHandler());

        //启动TCP服务并监听指定端口
        server.listen(8888,result->{
            if(result.succeeded()){
                System.out.println("Server is now listening on port " + "8888");
            }else{
                System.err.println("Failed to start server: " + result.cause());
            }
        });

    }

    public static void main(String[] args) {
       new VertxTcpServerTest().doStart();
      //  new VertxTcpServer().Start(8888);
    }


    //演示半包粘包问题
//    public void Start(int port){
//        //创建 Vert.x 实例
//        Vertx vertx = Vertx.vertx();
//
//        //创建 TCP 服务器
//        NetServer server = vertx.createNetServer();
//
//        //处理请求
//        server.connectHandler(socket -> {
//            String testMessage = "Hello,server!Hello,server!Hello,server!";
//            int messageLength = testMessage.getBytes().length;
//
//            //构造parser
//            RecordParser parser = RecordParser.newFixed(messageLength);//每次读取固定值长度的内容
//            parser.setOutput(new Handler<Buffer>() {
//
//                @Override
//                public void handle(Buffer buffer) {
//                    String str = new String(buffer.getBytes());
//                    System.out.println(str);
//                    if (testMessage.equals(str)) {
//                        System.out.println("good");
//                    }
//                }
//            });
//
//            socket.handler(parser);
//        });
//
//        //启动 TCP 服务器并监听指定端口
//        server.listen(8888,result->{
//            if(result.succeeded()){
//                System.out.println("Server is now listening on port " + "8888");
//            }else{
//                System.err.println("Failed to start server: " + result.cause());
//            }
//        });
//    }



}
