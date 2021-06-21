package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName:
 * @ClassName: MyInputHandler
 * @Author: czf
 * @Description: 处理read事件的handler
 * @Date: 2021/6/21 22:50
 * @Version: 1.0
 **/
@Slf4j
public class MyInputHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        CharSequence charSequence = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
        log.info("{} 收到的数据为 {}", ctx.channel().remoteAddress(), charSequence);
        // 之后在对客户端进行回复
        ctx.writeAndFlush(buf);
    }
}
