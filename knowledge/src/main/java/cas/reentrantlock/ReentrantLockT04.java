package cas.reentrantlock;

import config.StaticValue;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ProjectName: spring-security
 * @ClassName: ReentrantLockT04
 * @Author: czf
 * @Description: 公平锁
 * 公平锁介绍： 假设现在多个线程在竞争同一把锁，如果现在一个新的线程来竞争这把锁，那么公平锁与非公平锁的差异在
 * 1. 公平锁会看一下锁的等待队列中是否其他线程在竞争这把锁，如果有则加入队列，按照先后顺序获取这把锁
 * 2. 非公平锁不看之前还有其他线程在竞争这把锁，在锁释放时直接去抢占该锁。可能能直接得到该锁，也可能得不到
 * @Date: 2021/3/4 20:43
 * @Version: 1.0
 **/

public class ReentrantLockT04 implements Runnable{

    /**
     * 构造方法传true 表明该锁使用的是公平锁 与非公平的差异可以看测试结果
     */
    static ReentrantLock lock = new ReentrantLock(true);

    @Override
    public  void run(){
        for (int i = 0; i < StaticValue.HUNDRED; i++) {
            lock.lock();
            try {

                System.out.println(Thread.currentThread().getName()+" 获得锁");
            }finally {
                lock.unlock();
            }
        }

    }

    public static void main(String[] args) {
        ReentrantLockT04 rockT04 = new ReentrantLockT04();
        Thread t1 = new Thread(rockT04);
        Thread t2 = new Thread(rockT04);
        t1.start();
        t2.start();

    }
}
