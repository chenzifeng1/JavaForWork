package cas.juc_synchronization_lock.aqs;

import utils.TimeUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description: 信号量  线程同步
 *  可以做限流
 * @Date: 2021-03-08 16:07
 * @Version: 1.0
 **/
public class SemaphoreTest {

    public static void main(String[] args) {
        // 构造方法的参数是最大允许的线程数
//        Semaphore sem = new Semaphore(2);
        Semaphore sem = new Semaphore(2,true);


        new Thread(()->{
            try {
                // 获取信号量,阻塞
                sem.acquire();
                System.out.println(" T 1 is running ...");
                TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
                System.out.println(" T 1 is running ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                // 释放信号量
                sem.release();
            }
        }).start();


        new Thread(()->{
            try {
                // 获取信号量
                sem.acquire();
                System.out.println(" T 2 is running ...");
                TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
                System.out.println(" T 2 is running ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                // 释放信号量
                sem.release();
            }
        }).start();

        System.out.println("两个线程执行完");
    }

}
