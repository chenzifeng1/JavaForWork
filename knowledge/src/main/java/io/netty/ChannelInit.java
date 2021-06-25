package io.netty;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.channels.ServerSocketChannel;

/**
 * @Author: czf
 * @Description:
 * 之所以有这样一个类，是因为 服务端需要复用读时间的Handler,但是这个handler对象需要设计成单例，然后处理多个客户端连入的读请求
 * 这样就需要这个handler不能有自己的属性，因为单例中，如果有属性的话，可能会被多个线程同时修改引发错误
 *
 * 这个类不需要处理read，只需要在注册的时候，将真正处理读事件的handler加进去就行了
 * @Date: 2021-06-25 18:38
 * @Version: 1.0
 **/
@ChannelHandler.Sharable
public class ChannelInit extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MyInputHandler());
        // 将自身移除，因为在整个处理链中，这个handler的作用这是在socket刚注册的时候，将真正的事件处理器加进来，之后的过程 触发read的时候不需要经过该handler，
        // 如果不移除，那么在触发channelRead方法的时候也会先调用这个类的channelRead方法，即使这个类的该方法没有重写
        pipeline.remove(this);

    }
}
