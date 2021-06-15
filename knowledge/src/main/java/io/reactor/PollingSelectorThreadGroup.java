package io.reactor;

import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: czf
 * @Description: 使用轮询策略 单线程
 * @Date: 2021-06-08 14:54
 * @Version: 1.0
 **/
public class PollingSelectorThreadGroup extends AbstractSelectorThreadGroup {

    public PollingSelectorThreadGroup(int num) {
        super(num);
    }
    /**
     * 这里即处理SeverSocketChannel的accept问题，也会处理SocketChannel的read/write问题
     * @param channel
     */
    @Override
    public void nextSelector(Channel channel) {
        try {
            SelectorThread next = next();
            next.lbq.put(channel);
            //唤醒 正在阻塞的线程 让其返回
            next.getSelector().wakeup();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    /**
     * 这个方法我们在一个Group对象内选择方法轮询
     * @return
     */
    @Override
    public SelectorThread next() {
        return selectorThreads[xid.getAndIncrement()% selectorThreads.length]; 
    }

}
