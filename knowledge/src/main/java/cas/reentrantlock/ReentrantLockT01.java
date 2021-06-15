package cas.reentrantlock;

import config.StaticValue;

import java.util.concurrent.TimeUnit;


/**
 * @ProjectName: spring-security
 * @ClassName: T01_ReentrantLock
 * @Author: czf
 * @Description: 可重入锁 - 1
 * 什么叫可重入锁
 * 如下面的例子， 两个线程在执行加锁的两个方法，由于都是一个对象的两个synchronized方法，因此m2无法获取对象的锁，只能等待m1执行结束之后
 *  假设synchronized是不可重入锁，那么父类的synchronized方法就无法被子类调用了
 *
 * 但是如果是在同一个线程中，即在m1中调用m2，则是可以的，说明synchronized锁是可重入的
 *
 * synchronized 方法 锁 加在哪里？
 *
 *
 * @Date: 2021/3/3 19:56
 * @Version: 1.0
 **/

public class ReentrantLockT01 {


    synchronized void m1(){
        for (int i = 0; i < StaticValue.TEN; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            if(i == 2){
                m2();
            }
        }
    }

    synchronized void m2(){
        System.out.println("------------ m2 start ----------");
    }

    /**
     * 要求 两个线程 m1执行1s时，启动m2
     */
    public static void main(String[] args) {
        ReentrantLockT01 rockT01 = new ReentrantLockT01();
        new Thread(rockT01::m1).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        new Thread(rockT01::m2).start();
    }
}
