package container.collection.copyonwrite;

import config.StaticValue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description: 写时复制 CopyOnWriteArrayList / CopyOnWriteArraySet
 * @Date: 2021-03-29 18:07
 * @Version: 1.0
 **/
public class Test_01_CopyOnWrite {

    private static List<String> list = new CopyOnWriteArrayList<>();

    private static Random random = new Random();
    /**
     * 写线程
     */
    private  final static  ExecutorService WRITE_EXECUTOR = new ThreadPoolExecutor(3, 5, 3, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(()->{
                list.add(String.valueOf(random.nextInt(StaticValue.ONE_HUNDRED_THOUSAND)));
            });
        }
    });

    /**
     * 读线程
     */
    private final static ExecutorService READ_EXECUTOR = new ThreadPoolExecutor(50,75,1,TimeUnit.MINUTES,new ArrayBlockingQueue<>(100),new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            return  new Thread(()->{
                for (int i = 0; i < StaticValue.TEN; i++) {
                    System.out.println(Thread.currentThread().getName() + "进行写操作：" +  random.nextInt(list.size()));
                }
            });
        }
    });




}
