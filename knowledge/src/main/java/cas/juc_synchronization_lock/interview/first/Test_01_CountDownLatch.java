package cas.juc_synchronization_lock.interview.first;

import config.StaticValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: czf
 * @Description:
 * 实现一个容器，这个容器有两个方法，add/size
 * 两个线程 线程1添加对象，当添加到一定个数的对象的时候，线程2监控容器的个数，如果到达某个值则提示
 * 只用一个CountDownLatch会有问题 t1线程countDown到门闩的阈值之后还会继续countDown,可能当加到更后面的值时，t2才打印出来
 * 解决方法： 使用两个门闩
 *
 * @Date: 2021-03-09 15:32
 * @Version: 1.0
 **/
public class Test_01_CountDownLatch {


    List<Object> list = Collections.synchronizedList(new ArrayList<>());

    public void add(Object o) {
        list.add(o);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Test_01_CountDownLatch latch = new Test_01_CountDownLatch();
        CountDownLatch cdl_1 = new CountDownLatch(5);
        CountDownLatch cdl_2 = new CountDownLatch(1);



    }


    public void test(CountDownLatch cdl_1, CountDownLatch cdl_2){
        new Thread(()->{
            System.out.println("t2 start");
            if(size()!=5){
                try {
                    cdl_1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t2 end");
            cdl_2.countDown();
        },"t2").start();

        new Thread(()->{
            System.out.println("t1 start");
            for (int i = 0; i < StaticValue.TEN; i++) {
                System.out.println("add "+ i);
                add(new Object());
                cdl_1.countDown();
                if(i==5){
                    try {
                        cdl_2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("t1 end");
        },"t1").start();
    }

}
