package container.collection.blockingqueue;

import config.StaticValue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description: 无界队列 使用链表实现的阻塞队列  BlockingQueue天生的消费者生产者模型
 * @Date: 2021-03-29 19:29
 * @Version: 1.0
 **/
public class Test_01_LinkedBlocklingQueue {
    private final static LinkedBlockingQueue<String> STR_QUEUE = new LinkedBlockingQueue<>();
    private static Random random = new Random();

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < StaticValue.HUNDRED; i++) {
                try {
                    STR_QUEUE.put("a"+ i);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                try {
                    while (true){
                        System.out.println(Thread.currentThread().getName() + " take: " + STR_QUEUE.take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
