package io.reactor;

import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: czf
 * @Description: 使用轮询策略
 * @Date: 2021-06-08 14:54
 * @Version: 1.0
 **/
public class PollingSelectionThreadGroup extends AbstractSelectionGroup {

    private SelectorThread[] selectorThreads;
    private AtomicInteger xid = new AtomicInteger(0);
    private ServerSocketChannel server;
    public PollingSelectionThreadGroup(int num) {
        super(num);
    }
    /**
     * 这里即处理SeverSocketChannel的accept问题，也会处理SocketChannel的read/write问题
     * @param server
     */
    @Override
    public void nextSelector(Channel server) {
        SelectorThread next = next();
        next.register(server);
    }

    @Override
    public SelectorThread next() {
        return selectorThreads[xid.getAndIncrement()% selectorThreads.length]; 
    }


    /**
     * 轮询 获取Selector
     * @return
     */
    public SelectorThread nextV1(){
        return selectorThreads[xid.getAndIncrement()% selectorThreads.length];
    }

    /**
     * 第一个来做accept 其他的来处理read.write
     * @return
     */
    public SelectorThread nextV2(){
        int index  = xid.incrementAndGet()% (selectorThreads.length-1);
        return selectorThreads[index+1];
    }

}
