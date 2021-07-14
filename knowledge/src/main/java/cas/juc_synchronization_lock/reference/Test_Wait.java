package cas.juc_synchronization_lock.reference;

import config.StaticValue;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-07-12 11:15
 * @Version: 1.0
 **/
public class Test_Wait {

    static Object lock = new Object();

    public static void main(String[] args) {




    }


    public static synchronized void test1() throws InterruptedException {
        lock.notify();
        lock.wait();
    };

    public static void test2(){
        lock.notify();


    }

    public void test3() throws InterruptedException {
        lock.wait();
    }

}
