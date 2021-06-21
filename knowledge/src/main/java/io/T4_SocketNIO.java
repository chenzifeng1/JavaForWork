package io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-05-28 13:37
 * 在NIO模型中，IO请求的连接需要设置成非阻塞的，数据接收的Socket也要设置成非阻塞的
 * OS NON BLACKING 系统调用过程
 * fd1 = socket
 * bind(fd1,port)
 * listen(fd1)
 * socket fd1 non blacking //设置为非阻塞
 * -1 | fd2 <-accept(fd1)
 * fd2 non blacking //设置为非阻塞
 * -1 | data<-recv(fd2)
 * @Version: 1.0
 **/
@Slf4j
public class T4_SocketNIO {

    private static final int PORT = 81;

    public static void main(String[] args) {
        // 这里是client的集合，到时候需要遍历该集合来 获取数据
        List<SocketChannel> clients = new ArrayList<>();
        try {
            //接受客户端的连接
            Thread.sleep(1000);

            // 服务端开启监听，接受客户端
            ServerSocketChannel ss = ServerSocketChannel.open();
            // 绑定端口
            ss.bind(new InetSocketAddress("127.0.0.1",8080));
            // 设置为不阻塞 这里时第一次设置 这里是 OS NONBLCAKING 在接受客户端连接时不进行阻塞
            ss.configureBlocking(false);
            log.info("sever begin to listen 8080 -----");
            while(true){
                TimeUnit.SECONDS.sleep(1);
                SocketChannel client = ss.accept();
                if (client==null){
                    System.out.println("no client connect ...");
                }else {
                    // 这里有客户端连进来了
                    System.out.println(client.getRemoteAddress()+ "连入------------> 服务端");
                    // 第二次设置 这里设置的非阻塞是指接受数据时 非阻塞  不会阻塞？  fd=-1  返回对象：NULL
                    client.configureBlocking(false);
                    // 我们要把连入的客户端加入待遍历集合
                    clients.add(client);
                }
                // allocate(int) :分配到堆内内存  ; allocateDirect(int) :分配到堆外内存，这种效率高一点，因为不需要从堆内通过系统调用转到堆内内存了？
                ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

                clients.forEach(x->{
                    //在这里遍历 连进来的client是否有数据需要读
                    try {
                        // 这里不会阻塞，要么返回-1 要么返回数据数据的长度
                        int num = x.read(buffer);
                        if(num>0){
                            buffer.flip();
                            byte[] a =new byte[buffer.limit()];
                            buffer.get(a);
                            String str = new String(a);
                            System.out.println(x.socket().getInetAddress() +" 获取的数据为：" +str);
                            buffer.clear();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                });



            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}
