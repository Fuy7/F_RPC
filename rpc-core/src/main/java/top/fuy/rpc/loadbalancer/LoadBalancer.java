package top.fuy.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 *
 * 负载均衡器接口 规定负载均衡公共方法
 *
 * @author fuy
 */
public interface LoadBalancer {

    /**
    *@Description 从一系列的 Instance 中选择
    *@Author fuy
    */
    Instance select(List<Instance> instances);

}
