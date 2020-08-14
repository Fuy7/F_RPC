package top.fuy.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于注解进行服务的自动注册
 * 放在一个类上，标识这个类提供一个服务 标识一个类可以提供服务
 * 表示一个服务提供类，用于远程接口的实现类
 * @author ziyang
 */
// --元注解--
@Target(ElementType.TYPE)   //指定使用范围为：类、接口、枚举、Annotation类型
@Retention(RetentionPolicy.RUNTIME) //运行时有效 可以被反射机制读取
public @interface Service {

    /**
    *@Description 值定义为该服务的名称
     * 默认值是该类的完整类名
    *@Author fuy
    */
    public String name() default "";

}
