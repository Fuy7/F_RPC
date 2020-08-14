package top.fuy.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.enumeration.RpcError;
import top.fuy.rpc.exception.RpcException;
import top.fuy.rpc.util.NacosUtil;

import java.net.InetSocketAddress;

/**
 * Nacos服务注册中心
 *
 * @author fuy
 */
public class NacosServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    /**
    *@Description 注册功能
     *  传入 服务名 和 地址
    *@Author fuy
    */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            //调动Nacos的注册服务方法 进行注册
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
