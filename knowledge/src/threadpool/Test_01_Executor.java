package threadpool;

import utils.TimeUtils;

import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-30 15:10
 * @Version: 1.0
 **/
public class Test_01_Executor {

    ExecutorService e;
    Future<String> f;

    public static void main(String[] args) {
        Callable<String> c = new Callable() {

            @Override
            public String call() throws Exception {
                TimeUtils.timeUintSleep(5,TimeUnit.SECONDS);
                return "callable return";
            }
        };

        //这里不建议用Executors.newFixedThreadPool，原因是该方法创建线程池时提供的阻塞队列是一个无界的阻塞队列，如果程序有问题很可能会造成内存溢出。
        ExecutorService service = Executors.newFixedThreadPool(5);
        //这里是异步的，主线程继续执行
        Future<String> submit = service.submit(c);
        System.out.println("主线程继续执行");
        TimeUtils.timeUintSleep(2,TimeUnit.SECONDS);
        System.out.println("主线程执行完毕");
        try {
            // Future.get 会阻塞
            System.out.println(submit.get());
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
        //不加这一句，程序会处于等待任务状态。
        service.shutdown();
    }
}
