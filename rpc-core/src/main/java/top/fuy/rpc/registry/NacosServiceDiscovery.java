package top.fuy.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.enumeration.RpcError;
import top.fuy.rpc.exception.RpcException;
import top.fuy.rpc.loadbalancer.LoadBalancer;
import top.fuy.rpc.loadbalancer.RandomLoadBalancer;
import top.fuy.rpc.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos服务 发现
 *
 * @author fuy
 */
public class NacosServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    //负载均衡对象
    private final LoadBalancer loadBalancer;

    /**
    *@Description 创建 Nacos服务 发现 时需要指定负载均衡方式
    *@Author fuy
    */
    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if(loadBalancer == null) this.loadBalancer = new RandomLoadBalancer();
        else this.loadBalancer = loadBalancer;
    }

    /**
    *@Description 查询服务方法 这就用到负载均衡的方式
    *@Author fuy
    */
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if(instances.size() == 0) {
                logger.error("找不到对应的服务: " + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            //使用 loadBalancer.select(instances); 方法获取接口
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}
/**
*@Description 而这个负载均衡策略，也可以在创建客户端时指定，
 * 例如无参构造 NettyClient 时就用默认的策略，也可以有参构造传入策略，具体的实现留给大家。
*/