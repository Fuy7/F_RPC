package top.fuy.rpc.provider;

/**
 * 保存和提供 服务实例对象（服务提供者）
 * @author fuy
 */
public interface ServiceProvider {


    <T> void addServiceProvider(T service, String serviceName);

    Object getServiceProvider(String serviceName);

}
