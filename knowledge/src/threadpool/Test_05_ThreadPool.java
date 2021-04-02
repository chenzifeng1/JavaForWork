package threadpool;

import config.StaticValue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-04-01 11:42
 * @Version: 1.0
 **/
public class Test_05_ThreadPool {

    public static void main(String[] args) {

    }


    /**
     * 单线程线程池
     */
    public void singleThreadPool(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < StaticValue.FIVE; i++) {
            final int j = i;
            service.execute(new Thread(()->{
                System.out.println(j+" " + Thread.currentThread().getName());
            },"thread--"+i));
        }
        service.shutdown();
    }

    /**
     * 缓存线程池
     *
     */
    public void cachedThreadPool(){
        ExecutorService service = Executors.newCachedThreadPool();

    }


}
