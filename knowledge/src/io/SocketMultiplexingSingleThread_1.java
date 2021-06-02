package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: czf
 * @Description: 单线程情境下实现多路复用器
 * 1. 建立连接：  1. socket->bind->listen  2. 设为非阻塞  3.
 * 2. select.open
 * 3. getSelectKey  获得有状态的fd集合
 * 4. 遍历：  fd可能是accept，2.也可能时读状态
 * 1. 是否是 accpet->当作连接来处理
 * 2. 是否是读状态的fd，获取数据
 * @Date: 2021-06-02 16:40
 * @Version: 1.0
 **/
public class SocketMultiplexingSingleThread_1 {

    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;
    int port = 8080;

    public void initServer() {
        try {
            // 这个open相当于在服务端建立一个socket对象，返回文件描述符fd1
            serverSocketChannel = ServerSocketChannel.open();
            //设为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定  这里serverSocketChannel相当于 listen状态下的fd
            serverSocketChannel.bind(new InetSocketAddress(port));
            //这个open操作： 在epoll中调起 epoll_create()->fd4  fd4描述在内存中开辟的来维护fd红黑树的一块内存空间
            selector = Selector.open();
            //这里register相当于
            //1. select/poll 开辟一个内存数组-> fd1 + buffer 来存放和fd1相关的fd
            //2. epoll中 相当于epoll_ctl(fd4,ADD,fd1，ENPOLLIN) 将fd1注册到fd4所在的内核空间中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器 Start---------------------- 启动端口：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        initServer();
        try {
            // 这里获得的selectionKeys相当于带状态的fd
            //死循环，等待IO事件，accept或者读事件
            while (true) {
                // 获取文件描述符
                Set<SelectionKey> keys = selector.keys();
                System.out.println("selectionKeys数量："+keys.size());
                //懒加载：当selector调用select的时候才会触发 epoll_ctl
                while (selector.select()>0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            //如果是accept的事件
                            acceptHandler(selectionKey);
                        }
                        if(selectionKey.isReadable()){
                            //如果是读事件 这里可能是阻塞方法，在一个线程中去处理accept和receive请求，假设这里阻塞住了，那我们也无法处理连进来的accept请求了
                            receiveHandler(selectionKey);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接请求处理器
     * @param selectionKey accept类型IO事件
     */
    public void acceptHandler(SelectionKey selectionKey) {
        try {
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            // 这里是通过调用accept来接受客户端请求
            SocketChannel client = channel.accept();
            client.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(8096);
            //这里将新的fd注册到selector开辟的空间中去 调起了epoll_ctl
            //这里是非阻塞的
            client.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("-----------------------------------------------------");
            System.out.println("新的客户端注册进来："+client.getRemoteAddress());
            System.out.println("-----------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读事件请求处理器
     * @param selectionKey
     */
    public void receiveHandler(SelectionKey selectionKey) {
        try {
            // 这里就是调用receive方法了，开始读数据了
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            buffer.clear();
            int read =0;
            while(true){
                read = channel.read(buffer);
                if(read>0){
                    buffer.flip();
                    while(buffer.hasRemaining()){
                        channel.write(buffer);
                    }
                    buffer.clear();
                }else if(read==0){
                    break;
                }else {
                    //可能读到-1，即通讯方将连接关闭
                    channel.close();

                    System.out.println("读取结束");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
