package threadpool;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-31 19:45
 * @Version: 1.0
 **/
public class Test_04_ThreadPoolExecutor {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                4,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(4), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        System.out.println("创建线程：");
                        Thread thread = new Thread(runnable,"任务执行线程");
                        return thread;
                    }},
                new ThreadPoolExecutor.DiscardOldestPolicy());



        for (int i = 0; i < 8; i++) {
            threadPool.execute(new MyTask(String.valueOf(i)));
        }
        System.out.println(threadPool.getQueue());
        threadPool.execute(new MyTask("100"));
        System.out.println(threadPool.getQueue());
        //关闭线程池
        threadPool.shutdown();
    }


    static class MyTask extends Thread{

        public MyTask(String name) {
            super(name);
        }

        public MyTask() {
        }

        @Override
        public void run() {
            try {
                //阻塞住，来模拟耗时任务
                System.out.println(Thread.currentThread().getName() + " 正在执行任务");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "czf thread -" + getName();
        }
    }
}
