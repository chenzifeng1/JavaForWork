package cas.juc_synchronization_lock.aqs;



import config.StaticValue;
import utils.TimeUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: czf
 * @Description:  相比于wait/notify 更灵活
 * @Date: 2021-03-09 13:25
 * @Version: 1.0
 **/
public class LockSupportTest {


    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < StaticValue.TEN; i++) {
                System.out.println(i);
                TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
                if (i == 2) {
                    // park方法的作用是使线程停止
                    LockSupport.park();
                }
            }
        });
        t.start();
        TimeUtils.timeUintSleep(5,TimeUnit.SECONDS);
        System.out.println("main thread sleeping is over");
        LockSupport.unpark(t);
    }


}
