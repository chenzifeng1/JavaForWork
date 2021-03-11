package cas.juc_synchronization_lock.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-11 8:37
 * @Version: 1.0
 **/
public class AQSLearn {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            reentrantLock.lock();

        }finally {
            reentrantLock.unlock();
        }
    }
}
