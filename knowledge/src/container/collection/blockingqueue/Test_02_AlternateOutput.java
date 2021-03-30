package container.collection.blockingqueue;

import javax.swing.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: czf
 * @Description: 使用实现交替打印
 * @Date: 2021-03-30 8:41
 * @Version: 1.0
 **/
public class Test_02_AlternateOutput {
    static Thread t1 = null;
    static Thread t2 = null;
    private static Object o =new Object();
    /**
     * 信号量
     */
    enum ReadyToRun {T1,T2};
    /**
     * 使用volatile使线程间可见
     */
    static volatile ReadyToRun ready =  ReadyToRun.T1;
    static AtomicReference<Character> c = null;

    static final String OK = "ok";

    public static void main(String[] args) {
        AtomicInteger count1 = new AtomicInteger();
        AtomicInteger count2 = new AtomicInteger();
        transferQueue();
    }

    /**
     * 使用 lockSupport进行解决
     */
    private static void lockSupport(){
        Condition c1 = new ReentrantLock().newCondition();
        Condition c2 = new ReentrantLock().newCondition();


        c = new AtomicReference<>('A');
         t1 = new Thread(()->{
           for (int i = 0; i < 26;i++) {
               System.out.println(i);
               LockSupport.unpark(t2);
               LockSupport.park();
           }
        });

         t2 = new Thread(()->{
             while (true){
                 //先把自己阻塞，等待叫醒
                 LockSupport.park();

                 char tempC =  c.getAndSet((char) (c.get()+1));
                 System.out.println(tempC);
                 LockSupport.unpark(t1);
                 if(tempC=='Z'){
                     //判断需要放在LockSupport.unpark(t1)的下面，不然第一个线程就会一直被阻塞在park的地方
                     break;
                 }
             }
         });

         t1.start();
         t2.start();
    }

    /**
     * 使用wait+notify进行解决
     * 关键点：
     * 1. 另外t2线程先start,然后t1线程再start,不然很可能会出现死锁:t1 notify之后进入wait，之后t2运行wait,而且还是获取了o的同步锁之后
     * 2. 调用wait,notify的时候必须使用synchronized进行线程锁定，不然无法调用
     */
    private static void notifyAndWait(){
        c = new AtomicReference<>('A');

        Thread t1 = new Thread(()->{
            synchronized (o){
                for (int i = 0; i < 26; i++) {
                    System.out.println(i);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //这里是必须的，不然无法结束另一个线程
                o.notify();
            }
        });


        Thread t2 = new Thread(() -> {
            synchronized (o) {
                try {
                    while (true){
                        char a = c.getAndSet((char) (c.get()+1));
                        System.out.println(a);
                        o.notify();
                        o.wait();
                        if(a=='Z'){
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    o.notify();
                }
            }
        });
        t1.start();
        t2.start();
    }


    /**
     * 使用ReentrantLock 实现交替打印
     * 注意区分condition的wait和await
     * wait：基类Object的方法，使当前线程进入阻塞状态
     * await: Condition的方法，是当前线程进入condition的阻塞队列进行等待，condition.signal可以唤醒对应阻塞队列的线程，但是无法唤醒wait阻塞的线程
     */
    private static void userReentrantLock(){
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        c = new AtomicReference<>('A');

        new Thread(()->{
            lock.lock();
            try {

                for (int i = 0; i < 26; i++) {
                    System.out.println(i);
                    condition.signal();
                    condition.await();
                }
                condition.signal();
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }).start();


        new Thread(()->{
            lock.lock();
            try {

                for (int i = 0; i < 26; i++) {
                    System.out.println(c.getAndSet((char) (c.get()+1)));
                    condition.signal();
                    condition.await();
                }
                condition.signal();
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }).start();
    }

    /**
     * 使用ReentrantLock + 2个Condition来实现交替打印，Condition的实质相当于等待队列
     */
    private static void userReentrantLockPlus(){
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        c = new AtomicReference<>('A');


        new Thread(()->{
            lock.lock();
            try {
                for (int i = 0; i < 26; i++) {
                    System.out.println(i);
                    condition2.signal();
                    condition1.await();
                }
                condition2.signal();
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }).start();


        new Thread(()->{
            lock.lock();
            try {
                for (int i = 0; i < 26; i++) {
                    System.out.println(c.getAndSet((char) (c.get()+1)));
                    condition1.signal();
                    condition2.await();
                }
                condition1.signal();
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }).start();

    }

    /**
     * 使用CAS操作来完成交替打印，实际上是信号量 + 自旋
     * 开始信号量为T1,在线程A中，跳过循环->输出->改变信号量为T2，此时线程A的第二次循环就会为true，一直循环。
     * 知道线程B抢占了线程，跳过循环->输出->改变信号量为T1，之后线程B就会进入死循环，线程A循环结束
     *
     * 信号量必须是volatile的，另外自选是占用CPU的，wait是挂起线程，不占用CPU
     */
    private static void useByCas(){
        c = new AtomicReference<>('A');

        new Thread(()->{
            for (int i = 0; i < 26; i++) {
                while (ready != ReadyToRun.T1){}
                System.out.println(i);
                ready = ReadyToRun.T2;
            }
        }).start();


        new Thread(()->{
            for (int i = 0; i < 26; i++) {
                while (ready != ReadyToRun.T2){}
                System.out.println(c.getAndSet((char) (c.get()+1)));
                ready = ReadyToRun.T1;
            }
        }).start();
    }

    /**
     * 使用transferQueue进行解决
     *
     */
    @Deprecated
    private static void transferQueue(){
        TransferQueue<String> transferQueue1 = new LinkedTransferQueue<>();
        TransferQueue<String> transferQueue2 = new LinkedTransferQueue<>();
        c = new AtomicReference<>('A');
        //FIXME 这个暂时有问题
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 26; i++) {
                try {
                    System.out.println(i);
                    transferQueue1.transfer(OK);
                    transferQueue2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true){
                try {
                     char take = c.getAndSet((char) (c.get()+1));
                    System.out.println(take);
                    transferQueue1.take();
                    transferQueue2.transfer(OK);
                    if('Z'==(take)){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        t2.start();
        t1.start();
    }
}
