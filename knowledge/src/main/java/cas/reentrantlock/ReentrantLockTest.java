package cas.reentrantlock;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: spring-security
 * @ClassName: ReentrantLock
 * @Author: czf
 * @Description: ReentrantLock
 * @Date: 2021/3/1 22:56
 * @Version: 1.0
 **/

public class ReentrantLockTest {


    public static void main(String[] args) {

        Task task = new Task();


    }

    static class Task implements Callable<String>{

        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(2);
            return "success";
        }
    }
}
