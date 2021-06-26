package io.netty;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
    public static final String DEFAULT_HOST = "192.168.17.27";
    public static final int DEFAULT_PORT = 7777;

    /**
     * 服务端模式
     */
    public void serverMode() {
        NioEventLoopGroup selector = new NioEventLoopGroup(10);

        NioServerSocketChannel server = new NioServerSocketChannel();
        // 将channel注册到selector
        selector.register(server);
        // 获取channel的pipeline
        ChannelPipeline pipeline = server.pipeline();
        // 在这里埋入对accept socket的处理handler
        pipeline.addLast(new MyAcceptHandler(selector, new ChannelInit()));
        ChannelFuture bind = server.bind(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));

        try {
            // isOutputShutdown : 是否open 并且已经active
            if (server.isOpen()) {
                log.info("--------------- Server Listen {} {} ---------------", DEFAULT_HOST, DEFAULT_PORT);
                bind.sync().channel().closeFuture().sync();
                log.info("--------------- Server Close {} {} ---------------", DEFAULT_HOST, DEFAULT_PORT);
            } else {
                log.error("--------------- Server open fail {} {} ---------------", DEFAULT_HOST, DEFAULT_PORT);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void nettyMode() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(3);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(3);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyInHandler());

                    }
                }).bind(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));

        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
