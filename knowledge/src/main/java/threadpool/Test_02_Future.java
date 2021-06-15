package threadpool;

import utils.TimeUtils;

import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description:
 * Future中使用比较多的是FutureTask这个类
 *
 * FutureTask 实现了 RunnableFuture接口，该接口继承了Runnable和Future两个接口
 * FutureTask既可以当作一个任务，也可以保存任务返回的结果。
 * 与Callable+Future不同，Callable+Future是使用Future的来承接Callable.call()方法的返回值
 *
 * @Date: 2021-03-30 16:17
 * @Version: 1.0
 **/
public class Test_02_Future {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<Integer>(()->{
            TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
            return 100;
        });

        new Thread(task).start();
        System.out.println(task.get());

        Callable<Integer> newT = new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                TimeUtils.timeUintSleep(1,TimeUnit.SECONDS);
                return 200;
            }
        };
        ExecutorService executor  = Executors.newFixedThreadPool(2);

        Future<Integer> f = executor.submit(newT);

        System.out.println(f.get());
        executor.shutdown();
    }

}
