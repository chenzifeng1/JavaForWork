package container.collection.blockingqueue;

import java.util.concurrent.LinkedTransferQueue;

/**
 * @ProjectName:
 * @ClassName: Test_08_TransferQueue
 * @Author: czf
 * @Description: 线程之间传递，核心场景是一个线程需要另一个线程来返回结果才能继续执行
 *
 * 实现消息队列的消息确认
 * 面对面收钱
 *
 * @Date: 2021/3/29 23:47
 * @Version: 1.0
 **/

public class Test_08_TransferQueue {


    public static void main(String[] args) throws InterruptedException{
        LinkedTransferQueue<String> transferQue = new LinkedTransferQueue<String>();

        new Thread(()->{
            try {
                System.out.println(transferQue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        //这个transfer将元素装入队列之后阻塞等待，直到其他线程消费之后在继续执行
        transferQue.transfer("aaa");
        //put是当队列未满时，将元素塞入队列就继续执行，除非队列已满才会阻塞等待
//        transferQue.put("aaa");



    }
}
