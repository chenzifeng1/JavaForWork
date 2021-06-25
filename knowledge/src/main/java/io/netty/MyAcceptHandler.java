package io.netty;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
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

    /**
     * 这个处理读请求的handler
     */
    private final ChannelHandler handler;
    /**
     * 这个是selector
     */
    private final EventLoopGroup selector;


    public MyAcceptHandler(EventLoopGroup selector,ChannelHandler handler) {
        this.handler = handler;
        this.selector = selector;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("----------------客户端 {} 注册---------------",channel.remoteAddress());
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
