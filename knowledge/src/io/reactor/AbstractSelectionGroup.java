package io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-10 9:56
 * @Version: 1.0
 **/
public abstract class AbstractSelectionGroup implements SelectionThreadGroup {

    ServerSocketChannel server;

    SelectorThread[] selectorThreads ;

    private static final int DEFAULT_PORT = 8081;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            3,
            3,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50),
            (r)->new Thread(r,"Selector处理线程池")
    );

    @Override
    public void bind(int port){
        // 初始化
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            // 在这里面完成绑定
            server.bind(new InetSocketAddress(port));
            // 为选择一个selector进行注册
            nextSelector(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 如果是多个selectorThread的话，我们需要选一个
        SelectorThread next = next();
        next.register(server);
    }

    public AbstractSelectionGroup(int num) {
        selectorThreads = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            selectorThreads[i] = new SelectorThread();
            THREAD_POOL_EXECUTOR.execute(selectorThreads[i]);
        }
    }


}
