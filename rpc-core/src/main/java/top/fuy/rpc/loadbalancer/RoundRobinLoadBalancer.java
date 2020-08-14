package top.fuy.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 循环负载均衡器
 *
 * @author fuy
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    //位置变量
    private int index = 0;

    @Override
    public Instance select(List<Instance> instances) {

        if(index >= instances.size()) {
            //使用对接口数取余操作进行循环
            index %= instances.size();
        }
        return instances.get(index++);
    }

}
