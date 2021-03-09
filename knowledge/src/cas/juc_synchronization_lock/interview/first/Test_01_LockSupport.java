package cas.juc_synchronization_lock.interview.first;

import config.StaticValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-09 15:53
 * @Version: 1.0
 **/
public class Test_01_LockSupport {
    List<Object> list = Collections.synchronizedList(new ArrayList<>());

    static Thread t1 ,t2= null;

    public void add(Object o) {
        list.add(o);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Test_01_LockSupport test_01_lockSupport = new Test_01_LockSupport();

        test_01_lockSupport.test();
    }

    public void test(){


         t2 = new Thread(() -> {
            System.out.println("t2 start");
            if (size() != 5) {
                LockSupport.park();
                LockSupport.unpark(t1);
            }
            System.out.println("t2 end");
        }, "t2");


       t1 = new Thread(() -> {
            System.out.println("t1 start");
            for (int i = 0; i < StaticValue.TEN; i++) {
                add(new Object());
                System.out.println("add" + i);
                if (i == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
            System.out.println("t1 end");
        }, "t1");
        t2.start();
        t1.start();

    }


}
