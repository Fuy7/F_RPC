package top.fuy.rpc.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.factory.ThreadPoolFactory;
import top.fuy.rpc.util.NacosUtil;

/**
 *      ---- 实现自动注销服务 ----
 * 回调函数： nacos关闭时，调用回调函数自动删除注册的服务
 * @author fuy
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();
    //单例模式
    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    /**
    *@Description 清空服务的回调方法
    *@Author fuy
    */
    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        //Runtime 代表运行时 添加回调函数, 在 JVM 关闭之前被调用
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //调用 Nacos工具类的清空服务方法
            NacosUtil.clearRegistry();
            //关闭线程
            ThreadPoolFactory.shutDownAll();
        }));
    }

}
