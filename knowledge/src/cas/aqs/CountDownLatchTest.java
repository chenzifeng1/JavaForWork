package cas.aqs;

import config.StaticValue;

import java.util.concurrent.CountDownLatch;

/**
 * @ProjectName: spring-security
 * @ClassName: CountDownLatchTest
 * @Author: czf
 * @Description: CountDownLatch 学习使用
 * Latch 门栓
 * @Date: 2021/3/4 21:59
 * @Version: 1.0
 **/

public class CountDownLatchTest {

    public static void usingCountDown() {
        Thread[] threads = new Thread[StaticValue.HUNDRED];
        CountDownLatch countDownLatch = new CountDownLatch(threads.length);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                int result = 0;
                for (int j = 0; j < 10000; j++) {
                    result += j;
                }
                //倒数一下
                System.out.println(Thread.currentThread().getName() + " end");
                countDownLatch.countDown();
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        try {
            //这里 可以认为门栓拴住了 当countDownLatch的计数为0时，就会打开
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end latch ");
    }


    public static void usingJoin() {
        Thread[] threads = new Thread[StaticValue.HUNDRED];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                int result = 0;
                for (int j = 0; j < 10000; j++) {
                    result += j;
                }
                System.out.println(Thread.currentThread().getName() + " end");
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                System.out.println(Thread.currentThread().getName()+" join");
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end join");
    }


    public static void main(String[] args) {
//        usingCountDown();
        usingJoin();
    }
}
