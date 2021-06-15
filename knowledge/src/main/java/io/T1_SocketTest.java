package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ProjectName:
 * @ClassName: T1_SocketTest
 * @Author: czf
 * @Description: Socket测试精简版
 * @Date: 2021/5/25 23:02
 * @Version: 1.0
 **/

public class T1_SocketTest {

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(8090);
            System.out.println("ServerSocket Build----------------------");
            while (true){
                //获取会话
                Socket client = server.accept();
                System.out.println("client port:" + client.getPort());

                new Thread(new Runnable() {
                    Socket ss;
                    //set方法，设置ss,返回对象
                    public Runnable setSs(Socket s) {
                        this.ss = s;
                        return this;
                    }
                    @Override
                    public void run() {

                    }
                }.setSs(client)

                ).start();



            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
