package io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-08 14:48
 * @Version: 1.0
 **/
public class SelectorThread implements Runnable{

    private static final int DEFAULT_PORT = 8081;

    private Selector selector;
    private int port = 80;

    public SelectorThread() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        try {
            while (true) {
                // 这里会进行阻塞
                while(selector.select()>0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()){

                        }else if(key.isReadable()){

                        }else if(key.isWritable()){

                        }
                    }
                }

                // run task deal
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bind(Channel channel) {
        try {
            if(channel instanceof ServerSocketChannel){
                ServerSocketChannel sever = (ServerSocketChannel) channel;
                sever.register(selector,SelectionKey.OP_ACCEPT);
            }else if(channel instanceof SocketChannel){
                SocketChannel client = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                client.register(selector,SelectionKey.OP_READ,buffer);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
