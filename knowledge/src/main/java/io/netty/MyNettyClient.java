package io.netty;

import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Scanner;
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
    public static final String DEFAULT_IP = "127.0.0.1";
    public static final int DEFAULT_PORT = 8080;

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
        try {
            channelFuture.sync();
            log.info("client send data success : {}", string);
            connect.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void run() {
        ChannelPipeline pipeline = channel.pipeline();
        //这里埋入一个handler来处理读事件，这里是 响应式的
        pipeline.addLast(new MyInputHandler());

        log.info("开始写----------");
        AtomicInteger counter = new AtomicInteger();

        EVENT_EXECUTORS.execute(() -> {
            try {
                while (true) {
                    if (counter.get() == 10) {
                        break;
                    }
                    int i = counter.incrementAndGet();
                    write("counter " + i);
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        });


    }

}
