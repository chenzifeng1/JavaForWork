package cas.juc_synchronization_lock.interview.second;

import utils.TimeUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * 面试题 : 写一个固定容量的同步容器 ，拥有put和get方法，以及getCount方法
 * 能够支持 两个生产者线程及10个消费者线程的阻塞调用
 *
 * 使用 wait和notify以及notifyAll 来实现
 * 
 * @Date: 2021-03-09 16:20
 * @Version: 1.0
 **/
public class MyContainer_1<T> {
    private LinkedList<T> list = new LinkedList<>();

    public static final int MAX = 10;

    private int count = 1;

    private Object lockObj = new Object();


    /**
     * 生产者方法 向集合里面放入元素 如果到达了最大值则无法继续放入元素
     * 之所以加 synchronized 是因为防止count++为完成的时候，其他线程读取到了错误的数据
     * @param t
     */
    public synchronized void put(T t){
        while (list.size()==MAX){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(t);
        count++;
        //通知消费者来消费元素
        this.notifyAll();
    }

    /**
     * 消费者方法
     */
    public synchronized T get(){
        while (list.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        T t = list.removeFirst();
        count--;
        this.notifyAll();
        return t;
    }


    public static void main(String[] args) {
        MyContainer_1<String> container = new MyContainer_1<>();

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName() + " 消费 " + container.get() );
                }
            },"consumer"+i).start();
        }

        TimeUtils.timeUintSleep(2, TimeUnit.SECONDS);

        for (int i = 0; i < 2; i++) {

            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    String name = Thread.currentThread().getName()+"_"+j;
                    container.put(name);
                    System.out.println("生产者放入 "+name);
                }
            },"product"+i).start();
        }
    }
}
