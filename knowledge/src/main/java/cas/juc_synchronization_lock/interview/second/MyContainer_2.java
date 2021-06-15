package cas.juc_synchronization_lock.interview.second;

import utils.TimeUtils;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: czf
 * @Description: 设置线程间通信的条件
 * 生产者 -唤醒-> 消费者
 * 消费者 -唤醒-> 生产者
 * condition的本质： 等待队列
 * condition.awati()  当前线程进入该等待队列进行等待
 * condition.signalAll()  唤醒该等待队列的所有线程
 * @Date: 2021-03-10 11:33
 * @Version: 1.0
 **/
public class MyContainer_2<T> {

    private LinkedList<T> list = new LinkedList<>();

    public static final int MAX = 10;

    private int count = 0;

    private final ReentrantLock lock = new ReentrantLock();
    /**
     * 消费者条件
     */
    private final Condition consumer = lock.newCondition();
    /**
     * 生产者条件
     */
    private final Condition producer = lock.newCondition();


    /**
     * 生产者线程
     *
     * @param t
     */
    public void put(T t) {
        try {
            lock.lock();
            while (count == MAX) {
                // 只有所有的producer的线程会被阻塞
                producer.await();
            }
            list.add(t);
            count++;

            // 通知所有的消费者线程
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T get() {
        T t = null;
        try {
            lock.lock();
            while (count == 0) {
                consumer.await();
            }
           t = list.removeFirst();
            count--;
            producer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return t;

    }


    public static void main(String[] args) {
//        test();

      ReentrantLock lock = new ReentrantLock();
      lock.lock();
      lock.unlock();

    }


    /**
     * 测试方法
     */
    public static void test(){
        MyContainer_2<String> container = new MyContainer_2<>();

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5; j++) {
                    String p = container.get();
                    System.out.println("消费者：" + Thread.currentThread().getName() + "消费 " + p);
                }
            },"consumer"+i).start();
        }

        TimeUtils.timeUintSleep(2, TimeUnit.SECONDS);

        for (int i = 0; i < 2; i++) {

            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    String name = Thread.currentThread().getName()+"_"+j;
                    container.put(name);
                    System.out.println("生产者：" + Thread.currentThread().getName() + " 生产 " + name);
                }
            },"product"+i).start();
        }
    }
}
