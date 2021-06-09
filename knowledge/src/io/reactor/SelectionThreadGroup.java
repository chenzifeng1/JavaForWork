package io.reactor;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-08 14:54
 * @Version: 1.0
 **/
public class SelectionThreadGroup {

    private SelectorThread[] selectorThreads;
    private AtomicInteger xid = new AtomicInteger(0);
    private ServerSocketChannel server;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            3,
            3,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50),
            (r)->new Thread(r,"Selector处理线程池")
    );

    public SelectionThreadGroup(int num) {
        selectorThreads = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            // 这里不仅要初始化该对象，还要将对象启动
            selectorThreads[i] = new SelectorThread();
            THREAD_POOL_EXECUTOR.execute(selectorThreads[i]);
        }

    }


    /**
     * 绑定 selector
     * @param port
     */
    public void bind(int port) {
        //初始化
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            //在这里面完成绑定
            nextSelector(server);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果是多个selectorThread的话，我们需要选一个
        SelectorThread next = next();
        next.bind(server);
    }

    /**
     * 这里即处理SeverSocketChannel的accept问题，也会处理SocketChannel的read/write问题
     * @param server
     */
    private void nextSelector(Channel server) {
        SelectorThread next = next();
        next.bind(server);
    }


    /**
     * 轮询 获取Selector
     * @return
     */
    public SelectorThread next(){
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
