package top.fuy.test;

import top.fuy.rpc.annotation.ServiceScan;
import top.fuy.rpc.serializer.CommonSerializer;
import top.fuy.rpc.transport.RpcServer;
import top.fuy.rpc.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 *
 * @author fuy
 */
@ServiceScan
public class SocketTestServer {

    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        server.start();
    }

}
