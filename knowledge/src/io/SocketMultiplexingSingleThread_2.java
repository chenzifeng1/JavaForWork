package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName:
 * @ClassName: SocketMultiplexingSingleThread_2
 * @Author: czf
 * @Description: 单线程版增强，读写操作放在线程池中
 * @Date: 2021/6/7 20:08
 * @Version: 1.0
 **/

public class SocketMultiplexingSingleThread_2 {

    /**
     * 读事件线程池
     */
    private static final ThreadPoolExecutor READ_HANDLER_POOL_SIZE = new ThreadPoolExecutor(
            3,
            3,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50),
            (r)->new Thread(r,"读事件处理线程")
    );
    private static final ThreadPoolExecutor WRITE_HANDLER_POOL_SIZE = new ThreadPoolExecutor(
            3,
            3,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50),
            (r)->new Thread(r,"写事件处理线程")
    );

    private int port = 8084;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private void initServer(){

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("--------------服务启动了-----------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        initServer();

        try {
            while (true) {
                Set<SelectionKey> keys = selector.keys();
                System.out.println("selector 中包含的key数量" + keys.size());
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
                            writeHandler(key);
                        }
                    }


                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void acceptHandler(SelectionKey key){
         ServerSocketChannel channel = (ServerSocketChannel) key.channel();
         ByteBuffer buffer = ByteBuffer.allocate(8096);
        try {
            SocketChannel client =  channel.accept();
            client.configureBlocking(false);
            channel.register(selector,SelectionKey.OP_READ,buffer);

            System.out.println("----------------------start--------------------------");
            System.out.println("新的客户端连入： " + client.getRemoteAddress());
            System.out.println("----------------------end--------------------------");

        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 交给线程池去完成这些操作
     * @param key
     */
    public void readHandler(SelectionKey key){

      READ_HANDLER_POOL_SIZE.execute(()->{
          SocketChannel channel= (SocketChannel) key.channel();
          int read = 0 ;
          ByteBuffer buffer = (ByteBuffer) key.attachment();
          try {
              while (true){
                  read = channel.read(buffer);
                  if(read>0){
                      while(buffer.hasRemaining()){
                          serverSocketChannel.register(selector,SelectionKey.OP_WRITE,buffer.remaining());
                      }
                  }else if(read==0) {
                      break;
                  }else {
                      channel.close();
                      break;
                  }
              }
          }catch (IOException e){
              e.printStackTrace();
          }
      });

    }

    public void writeHandler(SelectionKey key){
        WRITE_HANDLER_POOL_SIZE.execute(()->{
            SocketChannel  channel = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.flip();
            try {
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                    TimeUnit.SECONDS.sleep(3);
                    buffer.clear();
                    channel.close();
                }
            }catch (IOException | InterruptedException  e){
                e.printStackTrace();
            }

        });

    }

}
