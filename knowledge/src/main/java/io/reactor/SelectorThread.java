package io.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-08 14:48
 * @Version: 1.0
 **/
public class SelectorThread extends ThreadLocal<LinkedBlockingQueue<Channel>> implements Runnable {

    private Selector selector;
    /**
     * lbq  在接口或者类中是固定使用方式逻辑写死了。你需要是lbq每个线程持有自己的独立对象
     */
    LinkedBlockingQueue<Channel> lbq = get();

    private SelectorThreadGroup selectorThreadGroup;

    public SelectorThread() {
        try {

            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected LinkedBlockingQueue<Channel> initialValue() {
        return new LinkedBlockingQueue<Channel>();
    }

    @Override
    public void run() {

        try {
            while (true) {
                // 这里会进行阻塞
                while (selector.select() > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }

                // run task deal
                if (!lbq.isEmpty()) {
                    Channel c = lbq.take();
                    if (c instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) c;
                        server.register(selector, SelectionKey.OP_ACCEPT);
                        System.out.println(Thread.currentThread().getName() + " register listen");
                    } else {
                        SocketChannel client = (SocketChannel) c;
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                        System.out.println(Thread.currentThread().getName() + " register read");
                    }

                }


            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 向对象的selector中注册 channel事件，那么该selector就会关注对应的事件
     *
     * @param channel
     */
    public void register(Channel channel) {
        try {
            if (channel instanceof ServerSocketChannel) {
                ServerSocketChannel sever = (ServerSocketChannel) channel;
                sever.register(selector, SelectionKey.OP_ACCEPT);
            } else if (channel instanceof SocketChannel) {
                SocketChannel client = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                client.register(selector, SelectionKey.OP_READ, buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理连接事件
     *
     * @param key
     */
    public void acceptHandler(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            //这个相当于客户端连接的事件
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            // 扔给selectionThreadGroup去选择处理线程
            selectorThreadGroup.nextSelector(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " read......");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.clear();
        try {
            client.configureBlocking(false);
            int read = 0;
            while (true) {
                read = client.read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        client.write(byteBuffer);
                    }
                    byteBuffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    System.out.println("服务器断开连接：" + client.getRemoteAddress());
                    key.cancel();
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置worker
     *
     * @param worker
     */
    public void setWorker(SelectorThreadGroup worker) {
        this.selectorThreadGroup = worker;
    }

    public Selector getSelector() {
        return selector;
    }


}
