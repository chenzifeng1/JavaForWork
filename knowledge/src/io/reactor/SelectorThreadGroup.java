package io.reactor;

import java.nio.channels.Channel;
import java.nio.channels.Selector;

/**
 * @author czf
 */
public interface SelectorThreadGroup {

    /**
     * 为ServerSocketChannel绑定端口
     * @param port 端口信息
     */
    void bind(int port);

    /**
     * 处理事件
     * @param channel 需要处理通道 可以是SeverSocketChannel 也可以是SocketChannel
     */
    void nextSelector(Channel channel);

    /**
     * selector选择策略
     * @return 返回一个SelectorThread的处理线程
     */
    SelectorThread next();


}
