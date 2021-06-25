package io.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-23 8:16
 * @Version: 1.0
 **/
@Slf4j
public class MyNettyServer {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 8080;

    public void run(){
        NioEventLoopGroup selector = new NioEventLoopGroup();

        NioSocketChannel server = new NioSocketChannel();
        // 将channel注册到selector
        selector.register(server);
        // 获取channel的pipeline 
        ChannelPipeline pipeline = server.pipeline();
        // 在这里埋入对accept socket的处理handler
        pipeline.addLast(new MyAcceptHandler(selector,new ChannelInit()));
        ChannelFuture bind = server.bind(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));
        try {
            log.info("--------------- Server Listen {} {} ---------------",DEFAULT_HOST,DEFAULT_PORT);
            bind.sync().channel().closeFuture().sync() ;
            log.info("--------------- Server Close ---------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
