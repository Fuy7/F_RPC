package top.fuy.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡器
 *
 * @author fuy
 */
public class RandomLoadBalancer implements LoadBalancer {

    /**
    *@Description 随机进行选择
    *@Author fuy
    */
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }

}
