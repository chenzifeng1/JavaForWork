package io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bind(ServerSocketChannel channel) {
        try {
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
