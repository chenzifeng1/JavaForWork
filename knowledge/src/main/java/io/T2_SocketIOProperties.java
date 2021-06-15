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
 * @Description:
 *  Socket IO 执行时可设置的参数
 *
 * @Date: 2021-05-27 18:42
 * @Version: 1.0
 **/
public class T2_SocketIOProperties {

    static ThreadPoolExecutor socketThreadPool = new ThreadPoolExecutor(10,
            15,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            runnable -> new Thread(runnable, "socket-thread-pool")
    );


    // server socket listen property:以下是Server Socket监听用到的属性1

    /**
     * 数据接收缓存区大小
     */
    private static final int RECEIVE_BUFFER = 10;
    /**
     * 表示等待客户连接的超时时间。一般不设置，会持续等待
     */
    private static final int SO_TIMEOUT = 0;
    /**
     * 表示是否允许重用服务器所绑定的地址。一般不设置
     */
    private static final boolean REUSE_ADDR = false;
    /**
     * 允许几个额外的client连入
     */
    private static final int BACK_LOG = 2;


    //client socket listen property on server endpoint: 服务端需要设置的客户端监听属性

    private static final boolean CLI_KEEPALIVE = false;
    /**
     * 置 OOBINLINE 选项时，在套接字上接收的所有 TCP 紧急数据都将通过套接字输入流接收
     */
    private static final boolean CLI_OOB = false;
    /**
     * 接收缓存区大小
     */
    private static final int CLI_REC_BUF = 20;
    /**
     * 发送缓存区大小
     */
    private static final int CLI_SEND_BUF = 20;
    /**
     * 是否
     */
    private static final boolean CLI_REUSE_ADDR = false;
    /**
     * 是否允许关闭时逗留
     */
    private static final boolean CLI_LINGER = true;
    /**
     * 指定关闭时逗留的超时值
     */
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
                Socket client = serverSocket.accept();

                //客户端是否允许
                client.setReuseAddress(CLI_REUSE_ADDR);
                client.setKeepAlive(CLI_KEEPALIVE);
                //关闭时是否逗留，以及逗留时长
                client.setSoLinger(CLI_LINGER,CLI_LINGER_N);
                client.setSoTimeout(CLI_TIMEOUT);
                //置 OOBINLINE 选项时，在套接字上接收的所有 TCP 紧急数据都将通过套接字输入流接收
                client.setOOBInline(CLI_OOB);
                client.setReceiveBufferSize(CLI_REC_BUF);
                client.setSendBufferSize(CLI_SEND_BUF);
                client.setTcpNoDelay(CLI_NO_DELAY);

                InputStream inputStream = client.getInputStream();
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
