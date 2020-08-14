package top.fuy.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fuy.rpc.codec.CommonDecoder;
import top.fuy.rpc.codec.CommonEncoder;
import top.fuy.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 通道提供者
 * 用于获取 Channel 对象(建立连接)
 * @author fuy
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();

    //用于存储 通道 的map集合
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    /**
    *@Description 获取 Channel 方法，根据地址+序列化方式
    *@Author fuy
    */
    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) throws InterruptedException {

        String key = inetSocketAddress.toString() + serializer.getCode();  //将地址进行序列化
        //如果map集合存在该地址对应的 channel ，就进行复用，避免多次连接服务器
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if(channels != null && channel.isActive()) {
                return channel;
            } else {
                //通道异常就删除
                channels.remove(key);
            }
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                /*自定义序列化编解码器*/
                // RpcResponse -> ByteBuf 将请求对象放入缓存中
                //责任链模式
                ch.pipeline().addLast(new CommonEncoder(serializer))    //根据参数指定编码器
                        .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                        .addLast(new CommonDecoder())   //指定解码器
                        .addLast(new NettyClientHandler()); //指定后置处理器
            }
        });
        Channel channel = null;
        try {
            //建立连接
            channel = connect(bootstrap, inetSocketAddress);
        } catch (ExecutionException e) {
            logger.error("连接客户端时有错误发生", e);
            return null;
        }
        //向通道中发送 数据
        channels.put(key, channel);
        return channel;
    }

    /**
    *@Description 建立连接
    *@Author fuy
    */
    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        //用来保存处理结果
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        //进行连接
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        //返回结果
        return completableFuture.get();
    }

    /**
    *@Description 初始化Bootstrap
     * 进行一些初始化工作
    *@Author fuy
    */
    private static Bootstrap initializeBootstrap() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

}
