package io;

import utils.TimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
 * @Date: 2021-05-28 13:43
 * @Version: 1.0
 **/
public class T3_SocketBIO {

    static final String URL = "http://127.0.0.1";
    static final int PORT = 80;
    static final int BACK_LOG = 2;


    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            runnable -> new Thread(runnable, "BIO_Socket_Thread_Pool"),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT, BACK_LOG);
            while (true) {
                Socket client = server.accept();
                threadPoolExecutor.submit(new SocketTask(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class SocketTask implements Runnable {
        private final Socket client;


        @Override
        public void run() {

            InetAddress inetAddress = client.getInetAddress();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

                while (true) {
                    String data = br.readLine();
                    if (data != null) {
                        System.out.println(inetAddress + " 接收到数据：" + data);
                    } else {
                        client.close();
                        break;
                    }
                }
                System.out.println("客户端断开连接");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public SocketTask(Socket client) {
            this.client = client;
        }


    }
}
