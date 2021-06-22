package io.netty;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;



/**
 * @ProjectName:
 * @ClassName: MyOutputHandler
 * @Author: czf
 * @Description: 处理服务端事件的handler
 * 相比于客户端，服务端的handler获取到的是accept socket
 * @Date: 2021/6/21 22:50
 * @Version: 1.0
 **/
@Slf4j
public class MyAcceptHandler extends ChannelInboundHandlerAdapter {

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 8080;

    private final ChannelHandler handler;
    private final EventLoopGroup selector;


    public MyAcceptHandler(ChannelHandler handler, EventLoopGroup selector) {
        this.handler = handler;
        this.selector = selector;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("----------------服务端已启动---------------");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 作为服务器  接收到的msg就不是简单的socket了，是 accept socket
        SocketChannel client = (SocketChannel) msg;
        // 将处理读事件的handler加入到处理链
        client.pipeline().addLast(handler);
        // 向EventLoopGroup去注册该socket
        selector.register(client);
    }
}
