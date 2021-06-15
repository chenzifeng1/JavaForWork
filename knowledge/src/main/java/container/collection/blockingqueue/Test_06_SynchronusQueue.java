package container.collection.blockingqueue;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author: czf
 * @Description: 用于两个线程之间传递任务
 * 容量为0，并非是来装元素的
 * 这个与Exchanger相似，一个线程put之后会阻塞，直到另一个线程来take
 * 同样一个线程take之后会阻塞，直到另一个线程put
 *
 * 注：这个队列无法使用add,只能调用阻塞式的put
 *
 * 线程池进行任务调度时使用
 * @Date: 2021-03-29 19:32
 * @Version: 1.0
 **/
public class Test_06_SynchronusQueue {


    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> sq = new SynchronousQueue<String>();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + " take： " + sq.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        sq.put(Thread.currentThread().getName());
        System.out.println("size:" + sq.size());
    }
}
