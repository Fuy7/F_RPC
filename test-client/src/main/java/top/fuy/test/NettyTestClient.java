package top.fuy.test;

import top.fuy.rpc.api.ByeService;
import top.fuy.rpc.api.HelloObject;
import top.fuy.rpc.api.HelloService;
import top.fuy.rpc.serializer.CommonSerializer;
import top.fuy.rpc.transport.RpcClient;
import top.fuy.rpc.transport.RpcClientProxy;
import top.fuy.rpc.transport.netty.client.NettyClient;

import static java.lang.System.out;

/**
 * 测试用Netty消费者
 *
 * @author fuy
 */
public class NettyTestClient {

    public static void main(String[] args) {
        //创建客户端 并指定序列化方式
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        //创建客户端对应的代理对象
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        //使用代理对象调用远端服务，需要传入接口，并返回处理好的结果
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        //测试方法
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        out.println(res);
        //测试其他远程方法
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        out.println(byeService.bye("Netty"));
    }

}
