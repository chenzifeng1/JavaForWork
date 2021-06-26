package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

/**
 * @ProjectName:
 * @ClassName: MyInputHandler
 * @Author: czf
 * @Description: 处理read事件的handler
 * @Date: 2021/6/21 22:50
 * @Version: 1.0
 **/
@Slf4j
public class MyInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("----------------Client registered --------------");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("----------------Client active --------------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("------------------异常捕获----------------------");
        log.error(cause.getMessage(),cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        CharSequence charSequence = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
        log.info("{} 收到的数据为 {}", ctx.channel().remoteAddress(), charSequence);
        // 这里不做回复了，因为这里写回复的话，就会出现 客户端->服务端 服务端->客户端 无限死循环 因为两者都是复用的同一个handler
    }

    ByteBuf ackBuf(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(3);
        buffer.setCharSequence(0,"ack",CharsetUtil.UTF_8);
        return buffer;
    }
}
