package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName:
 * @ClassName: SocketMultiplexingSingleThread_1_1
 * @Author: czf
 * @Description: 增加写事件
 * 在上一个版本的基础上将写事件从读时间中分离：
 * 1. 在读事件完成之后，注册关心写事件
 * 2. 在select中处理写事件：写回读事件中数据，进行短暂等待之后结束连接
 * @Date: 2021/6/5 17:11
 * @Version: 1.0
 **/

public class SocketMultiplexingSingleThread_1_1 {
    private final int port = 8080;
    private ServerSocket serverSocket;
    private ServerSocketChannel serverSocketChannel;
    Selector selector = null;

    /**
     *
     */
    private void initServer(){
        try {
            //打开channel
            serverSocketChannel = ServerSocketChannel.open();
            //设为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //打开selector
            selector = Selector.open();
            //将serverSocketChannel和selector联系起来  将selector注册到channel上去
            // 在channel上selector的事件以selectionKey形式存在?
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("-----------------服务初始化完成-------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void run(){
        initServer();

        try {
            while (true){
//                Set<SelectionKey> keys = selector.keys();
//                System.out.println("selector keys size： " + keys.size());
                while (selector.select()>0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()){
                            // 连接事件
                            acceptHandler(key);
                        }else if(key.isReadable()){
                            // 读事件
                            readEventHandler(key);
                        }else if(key.isWritable()){
                            // 写事件
                            writeHandler(key);
                        }
                    }

                }

            }
        }catch (IOException e) {


        }

    }

    /**
     * 处理连接事件
     * @param key
     */
    private void acceptHandler(SelectionKey key){
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            System.out.println("--------------------新的客户端 开始连接 ------------------------");
            SocketChannel client = channel.accept();
            client.configureBlocking(false);
            // 读缓冲区
            ByteBuffer buffer =  ByteBuffer.allocate(8096);
            // 将读事件注册到selector的关注事件
            client.register(selector,SelectionKey.OP_READ,buffer);

            System.out.println("连接的客户端为： " + client.getRemoteAddress());
            System.out.println("--------------------新的客户端 连入成功------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读事件处理器
     * @param key
     */
    private void readEventHandler(SelectionKey key){
        System.out.println("read handler");
       SocketChannel client  = (SocketChannel) key.channel();
       //Attach附加对象
        //可以将一个对象或者更多的信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。例如，可以附加与通道一起使用的Buffer，或是包含聚集数据的某个对象。
       ByteBuffer buffer = (ByteBuffer) key.attachment();
       buffer.clear();
        int read = 0;
        try {
            while(buffer.hasRemaining()){
                read = client.read(buffer);
                if(read>0){
                    System.out.println(buffer.get());
                    client.register(selector,SelectionKey.OP_WRITE,buffer);
                }else if(read == 0 ){
                    System.out.println("------------------------读取结束-------------------");
                    break;
                }else {
                    client.close();
                    break;
                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void writeHandler(SelectionKey key){
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        try {
            while (buffer.hasRemaining()){
                channel.write(buffer);
                TimeUnit.SECONDS.sleep(2);
                buffer.clear();
                key.cancel();
                channel.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
