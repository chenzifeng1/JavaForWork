package threadpool;

import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-31 19:45
 * @Version: 1.0
 **/
public class Test_04_ThreadPoolExecutor {

    public static void main(String[] args) {

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        return null;
                    }},
                new ThreadPoolExecutor.AbortPolicy());
    }
}
