package top.fuy.test;

import top.fuy.rpc.annotation.ServiceScan;
import top.fuy.rpc.serializer.CommonSerializer;
import top.fuy.rpc.transport.RpcServer;
import top.fuy.rpc.transport.netty.server.NettyServer;

/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author fuy
 */
@ServiceScan    //使用注解自动扫描路径
public class NettyTestServer {

    public static void main(String[] args) {
        //指定地址和端口还有序列化方式
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        //启动服务器端
        server.start();
    }

}
