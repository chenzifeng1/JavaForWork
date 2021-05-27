package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description: 多线程解决阻塞IO的问题：
 * 1. BIO的问题所在：阻塞，在接收连接的时候 `accept()`是阻塞的，在接收数据的时候也是阻塞的。
 * 2. 如果我们一个服务器同时要和多个客户端建立来连接，并进行数据交换。那么BIO模型中就不能在主线程中接收数据时发生阻塞。只能另启一个线程来接收数据，这样其他的客户端才能继续建立连接
 * <p>
 * 伪代码：
 * ServerSocket  ss = new SeverSocket()
 * while(true) {
 * Socket client = ss.accept //建立连接，此处是阻塞的，可以接收，因为每当有连接进来才会放开阻塞，能保证连接不会被拒绝
 * <p>
 * InputStream in = client.getInputStream();
 * BufferReader br = new BufferReader(new InputStreamReader(in));
 * br.read()  //这里不能阻塞，因为如果这里阻塞了，在第一个请求进来却没有传输数据的时候，主线程阻塞在这里，后续的请求无法被accept。 所以这里每个请求在接收数据的时候都需要new一个线程来接收。
 * 这个就是基本的BIO模型
 * <p>
 * }
 * @Date: 2021-05-27 18:42
 * @Version: 1.0
 **/
public class T2_SocketIOPropertites {

    static ThreadPoolExecutor socketThreadPool = new ThreadPoolExecutor(10,
            15,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            runnable -> new Thread(runnable, "socket-thread-pool")
    );


    // server socket listen property:以下是Server Socket监听用到的属性1
    private static final int RECEIVE_BUFFER = 10;
    private static final int SO_TIMEOUT = 0;
    private static final boolean REUSE_ADDR = false;
    private static final int BACK_LOG = 2;
    //client socket listen property on server endpoint: 服务端需要设置的客户端监听属性
    private static final boolean CLI_KEEPALIVE = false;
    private static final boolean CLI_OOB = false;
    private static final int CLI_REC_BUF = 20;
    private static final boolean CLI_REUSE_ADDR = false;
    private static final int CLI_SEND_BUF = 20;
    private static final boolean CLI_LINGER = true;
    private static final int CLI_LINGER_N = 0;
    private static final int CLI_TIMEOUT = 0;
    private static final boolean CLI_NO_DELAY = false;


    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {
            // 设置socket的监听端口，以及额外请求数，最大请求数等于1+BACK_LOG,指的是连进来的请求数小于这个值时，连接会被建立(ESTABLISHED),服务器不会回应超出这个数的请求
            serverSocket = new ServerSocket(12121, BACK_LOG);
            // 设置socket调用InputStream读数据的超时时间
            serverSocket.setSoTimeout(SO_TIMEOUT);
            // 设置socket是否进行地址复用
            serverSocket.setReuseAddress(REUSE_ADDR);
            // 设置接收缓冲区的大小
            serverSocket.setReceiveBufferSize(RECEIVE_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("server up use 9090!");

        socketThreadPool.execute(new SocketHandler(serverSocket));

    }


    static class SocketHandler implements Runnable {

        final ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                Socket accept = serverSocket.accept();
                InputStream inputStream = accept.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)) ;
                char[] data = new char[1024];
                while (true) {
                    //别无限循环了，这样连接就无法释放了
                    int readNum = br.read(data);
                    if(readNum>0){
                        System.out.println(data);
                    }else if(readNum==0){
                        System.out.println("client readed nothing!");
                        continue;
                    }else {
                        System.out.println("client readed -1...");
                        System.in.read();
                        serverSocket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public SocketHandler(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }
    }

}
