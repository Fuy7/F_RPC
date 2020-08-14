package top.fuy.rpc.transport;

import top.fuy.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author fuy
 */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    //向 Nacos 注册服务
    <T> void publishService(T service, String serviceName);

}
