package top.fuy.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 放在启动的入口类上（main 方法所在的类）
 * 标识服务的扫描的包的范围，扫描范围
 *
 * 服务扫描的基包
 * @author ziyang
 */
@Target(ElementType.TYPE)   //描述注解的使用范围
@Retention(RetentionPolicy.RUNTIME)     //表示需要在什么级别保存该注释信息，用于描述注解的生命周期,在运行时有效
public @interface ServiceScan {

    /**
    *@Description 值定义为扫描范围的根包
     * 默认值为入口类所在的包，扫描时会扫描该包及其子包下所有的类，找到标记有 Service 的类，并注册
    *@Author fuy
    */
    public String value() default "";

}
