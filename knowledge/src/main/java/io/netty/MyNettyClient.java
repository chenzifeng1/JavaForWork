package io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName:
 * @ClassName: MyNettyClient
 * @Author: czf
 * @Description: NettyClient
 * 客户端
 * 连接别人
 * 1. 主动发数据
 * 2. 什么时候给我发数据  基于event selector
 * @Date: 2021/6/21 19:57
 * @Version: 1.0
 **/
@Slf4j
public class MyNettyClient {
    public static final String DEFAULT_IP = "192.168.17.27";
    public static final int DEFAULT_PORT = 7777;

    NioSocketChannel channel;
    /**
     * 这个可以看作SelectorManager
     */
    public final static NioEventLoopGroup EVENT_EXECUTORS = new NioEventLoopGroup(10);

    ChannelFuture connect;


    String ip;
    int port;

    public MyNettyClient() {
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    public MyNettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        // 这里是channel
        channel = new NioSocketChannel();
        //连接之前需要将 channel注册到selector上去 有点类似 epoll_ctl(fd,ADD)
        EVENT_EXECUTORS.register(channel);
        // 发起连接
        this.connect = channel.connect(new InetSocketAddress(this.ip, this.port));
        try {
            //等待连接建立
            connect.sync();


            log.info("client connected ----");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 写操作 ： Netty的写操作是异步的，所以，我们需要等待客户端简历 等待写操作完成
     *
     * @param string
     */
    public void write(String string) {
        ChannelFuture channelFuture = channel.writeAndFlush(Unpooled.copiedBuffer(string.getBytes()));
        // 在这里埋入一个handler，来处理读事件
//        try {
//            channelFuture.sync();
//            log.info("client send data success : {}", string);
//        } catch (InterruptedException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    /**
     * 客户端模式
     */
    public void clientMode() {
        ChannelPipeline pipeline = channel.pipeline();
        //这里埋入一个handler来处理读事件，这里是 响应式的
        pipeline.addLast(new MyInHandler());

        log.info("开始写----------");
        final AtomicInteger counter = new AtomicInteger();

        Future<?> submit = EVENT_EXECUTORS.submit(() -> {
            try {
                while (true) {
                    if (counter.get() == 10) {
                        break;
                    }

                    write(Thread.currentThread().getName() + " counter " + counter.incrementAndGet());
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        try {
            submit.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void nettyMode() {
        NioEventLoopGroup threadGroup = new NioEventLoopGroup(3);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(threadGroup)
                .channel(NioSocketChannel.class)
                // 这个ChannelInitializer 就是 Shareable 做成单例 只负责添加后续的Handler使用的
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                }).connect(new InetSocketAddress(DEFAULT_IP, DEFAULT_PORT));
        try {
            // 获取channel
            Channel channel = connect.sync().channel();
            // Netty收发数据都是基于ByteBuf的，因此这里我们把要发的数据组织到ByteBuf中
            ByteBuf buf = Unpooled.copiedBuffer("hello world".getBytes());
            ChannelFuture channelFuture = channel.writeAndFlush(buf);
            channelFuture.sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
