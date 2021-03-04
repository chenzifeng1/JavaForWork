package cas.reentrantlock;

import config.StaticValue;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ProjectName: spring-security
 * @ClassName: ReentrantLockT02
 * @Author: czf
 * @Description: 讲解ReentrantLock的使用
 * 相比于synchronized,ReentrantLock是要手动加锁和解锁
 * 这也就意味着，如果在加锁代码段中出现异常导致无法解锁，会出现死锁的情况
 * 所以一般来说，lock()方法一般在try代码块的第一行，而unlock方法要在finally的第一行
 * 假设lock不在try的第一行，那么比如说lock前面的代码抛出异常，被捕获到，然后执行finally，unlock会将一个没有加锁的ReentrantLock对象解锁，这里会有问题
 * 同样，如果unlock不在finally的第一行，如果在 finally代码段中，在unlock之前抛出了异常，那么就会陷入死锁状态
 *
 * tryLock()
 * tryLock(times,TimeUnit.SECONDS)
 * tryLock方法，会尝试获取锁，如果获取到了锁（获取到锁对象，即为加锁了）则返回true，否则返回false
 * 我们也可以指定一个时间段，如果tryLock方法在指定的时间段内可以获得锁，则(获取锁，加锁)返回true，否则返回false
 * @Date: 2021/3/3 20:30
 * @Version: 1.0
 **/

public class ReentrantLockT02 {


    private final   ReentrantLock lock = new ReentrantLock();

    public void m1(){
        try {
            lock.lock();
            for (int i = 0; i < StaticValue.TEN; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    public void m2(){
        try {
            lock.lock();
            System.out.println("m2 start");
        }finally {
            lock.unlock();
        }
    }

    public void m3(){
        boolean locked = false;

        try {
            locked = lock.tryLock(3,TimeUnit.SECONDS);
            System.out.println("m3 start");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(locked) {
                lock.unlock();
                System.out.println("m3 获得过锁");
            }else {
                System.out.println("没有获取到锁");
            }
        }

    }

    public static void main(String[] args) {
        test2();
    }


      static void test1(){
        ReentrantLockT02 rockT02 = new ReentrantLockT02();
        rockT02.m1();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rockT02.m2();
    }

     static void  test2(){
        ReentrantLockT02 rockT02 = new ReentrantLockT02();
        new Thread(rockT02::m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       new Thread(rockT02::m3).start();
    }
}
