package cas.juc_synchronization_lock.aqs;

import utils.TimeUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: czf
 * @Description: 读写锁
 * 读锁： 允许读线程读，不允许写线程操作
 * 写锁： 不允许其他线程进行读写
 * @Date: 2021-03-08 14:44
 * @Version: 1.0
 **/
public class ReadWriteLockTest {
    static ReentrantLock lock = new ReentrantLock();
    static int value = 0;

    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    static Lock readLock = readWriteLock.readLock();
    static Lock writeLock = readWriteLock.writeLock();


    /**
     * 读操作
     * @param lock
     */
    public static void read(Lock lock){
        try {
            lock.lock();
            TimeUtils.timeUintSleep(2, TimeUnit.SECONDS);
            System.out.println("value now is " + value);
        }finally {
            System.out.println(Thread.currentThread().getName() + "read over");
            lock.unlock();
        }
    }

    /**
     * 写操作
     * @param v 要写入的值
     * @param lock
     */
    public static void write(int v,Lock lock){
        try {
            lock.lock();
            value = v;
            System.out.println("write ok , value is " + value);
            TimeUtils.timeUintSleep(2,TimeUnit.SECONDS);
        }finally {
            System.out.println("write over");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable readWork = () -> read(readLock);
        Runnable writeWork = () -> write(new Random().nextInt(10),writeLock);

        for (int i = 0; i < 2; i++) {
            new Thread(writeWork).start();
        }
        for (int i = 0; i < 18; i++) {
            new Thread(readWork).start();
        }




    }
}
