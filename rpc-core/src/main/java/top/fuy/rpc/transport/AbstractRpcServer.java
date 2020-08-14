package top.fuy.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.registry.ServiceRegistry;
import top.fuy.rpc.annotation.Service;
import top.fuy.rpc.annotation.ServiceScan;
import top.fuy.rpc.enumeration.RpcError;
import top.fuy.rpc.exception.RpcException;
import top.fuy.rpc.provider.ServiceProvider;
import top.fuy.rpc.util.ReflectUtil;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author fuy
 */
//由于扫描服务这一步是一个比较公共的方法，无论是 Socket 还是 Netty 的服务端都需要这个方法，使用了一个抽象类 AbstractRpcServer
// 实现了 RpcServer 接口，而 NettyServer 和 SocketServer 继承自 AbstractRpcServer，
// 将 scanServices 方法放在抽象类中，而 start 方法则由具体实现类来实现。
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    /**
    *@Description 扫描服务  主要是先获取 @ServiceScan注解，然后根据注解指定的路径获取 @Service 指定的类
     *                      使用反射创建类并进行服务注册
    *@Author fuy
    */
    public void scanServices() {
        //使用反射工具类获取栈空间(主要是为了获取 main 方法所在的类)
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            //根据全限定类名获取启动类
            startClass = Class.forName(mainClassName);
            //根据反射获取指定注解 @ServiceScan
            if(!startClass.isAnnotationPresent(ServiceScan.class)) {
                logger.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        //获取到注解指定的扫描路径
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        //使用默认路径
        if("".equals(basePackage)) {
            //默认值为入口类所在的包，扫描时会扫描该包及其子包下所有的类
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        //根据路径扫描到所有带有 @Service 的类，并装入set中
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);  //用来存储注册方法的
        for(Class<?> clazz : classSet) {
            //使用反射 获取 @Service标记的类
            if(clazz.isAnnotationPresent(Service.class)) {
                //获取 标记类的 服务的名称
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    //反射创建需要注册的接口
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                //为默认服务名称
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        //进行注册
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

    /**
    *@Description 向 Nacos 中注册服务
    *@Author fuy
    */
    @Override
    public <T> void publishService(T service, String serviceName) {
        //服务提供者 向本地注册
        serviceProvider.addServiceProvider(service, serviceName);
        //服务提供者 向Nacos注册
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }

}
