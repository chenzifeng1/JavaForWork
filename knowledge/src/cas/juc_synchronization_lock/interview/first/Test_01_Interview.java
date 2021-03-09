package cas.juc_synchronization_lock.interview.first;

import config.StaticValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-09 14:48
 * @Version: 1.0
 **/
public class Test_01_Interview {

//    List<Object> list = new ArrayList<Object>();
//    volatile List<Object> list = new ArrayList<Object>();
    List<Object> list = Collections.synchronizedList(new ArrayList<>());
    public synchronized void add(Object o) {
        list.add(o);
    }

    public synchronized int size() {
        return list.size();
    }

    public static void main(String[] args) {

        Test_01_Interview test_01_Interview = new Test_01_Interview();


        new Thread(()->{
            for (int i = 0; i < StaticValue.TEN; i++) {
                test_01_Interview.add(new Object());
//                TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
                System.out.println("add:" + i);
            }
        }).start();

        new Thread(()->{
            while (true){
                if(test_01_Interview.size()==3){
                    break;
                }
            }
            System.out.println("the end ");
        }).start();


    }


}
