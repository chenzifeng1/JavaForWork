package cas.juc_synchronization_lock.interview.first;

import config.StaticValue;
import utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-09 15:13
 * @Version: 1.0
 **/
public class Test_02_Interview {

    List<Object> list = Collections.synchronizedList(new ArrayList<>());

    public void add(Object o) {
        list.add(o);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Object lock = new Object();

        Test_02_Interview test_02_Interview = new Test_02_Interview();

        new Thread(()->{
            synchronized (lock){
                System.out.println("t2 启动");
                if(test_02_Interview.size()!=5){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2 结束");
                lock.notify();
            }
        },"t2").start();

        TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);

        new Thread(()->{
                synchronized (lock){
                    System.out.println("t1 启动");
                    for (int i = 0; i < StaticValue.TEN; i++) {
                        test_02_Interview.add(new Object());
                        System.out.println("add " + i);
                        if(i == 5){
                            // notify 不释放锁，所以这里还是没办法在i==5时结束t2  wait 是释放锁的操作
                            lock.notify();
                            try {
                                lock.wait();
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            TimeUtils.timeUintSleep(1,TimeUnit.SECONDS);
                        }
                    }
                    System.out.println("t1 结束");
                }
        },"t1").start();
    }


}
