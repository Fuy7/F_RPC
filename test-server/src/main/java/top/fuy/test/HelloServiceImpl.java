package top.fuy.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.annotation.Service;
import top.fuy.rpc.api.HelloObject;
import top.fuy.rpc.api.HelloService;

/**
 * @author fuy
 */
@Service    //使用自定义注解
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}", object.getMessage());
        return "这是Impl1方法";
    }

}
