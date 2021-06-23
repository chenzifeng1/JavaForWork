package io.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-23 8:16
 * @Version: 1.0
 **/
public class MyNettyServer {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 8080;

    public void run(){
        NioEventLoopGroup selector = new NioEventLoopGroup();

        NioSocketChannel channel = new NioSocketChannel();
        channel.bind(new InetSocketAddress(DEFAULT_HOST,DEFAULT_PORT));
        ChannelPipeline pipeline = channel.pipeline();
        // 在这里埋入对accept socket的处理handler
        pipeline.addLast(new MyAcceptHandler(selector,new MyInputHandler()));

    }
}
