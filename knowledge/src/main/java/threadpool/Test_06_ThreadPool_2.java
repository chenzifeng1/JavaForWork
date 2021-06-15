package threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ProjectName:
 * @ClassName: Test_06_ThreadPool_2
 * @Author: czf
 * @Description: 线程池介绍
 * FixedThreadExecutor 与 CachedThreadExecutor对比：任务来的多但是不连续->FixedTheadPool 任务流量固定平稳->CachedThreadPool
 * 线程池内的线程过多，会竞争处理器和内存资源，浪费大量时间在线程切换上。
 * 线程数过少，则可能会导致CPU利用率较低
 *
 * 线程池大小和CPU的利用率之比可以用以下公式计算
 *
 * N【thread】 = N【cpu】 * U【cpu】*(1+W/C)
 * N【thread】:合理的线程数
 * N【cpu】:处理器个数
 * U【cpu】:期望的cpu的利用率，该值的范围是0-1
 * W/C:等待时间与计算时间的比值
 *
 * @Date: 2021/4/1 21:01
 * @Version: 1.0
 **/

public class Test_06_ThreadPool_2 {

    public static final int CPU_CORE_NUM = 8;

    public static void main(String[] args) {
         Test_06_ThreadPool_2 test =  new Test_06_ThreadPool_2();
        try {
            test.fixedThreadPoolExample();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 固定线程池，线程个数是固定的
     */
    private void fixedThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
    }

    /**
     * 定时任务线程池
     * 初始化是使用的任务队列是 DelayWorkQueue
     * 定时器框架：quartz
     */
    private void scheduleThreadPool(){
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(4);
        threadPool.scheduleAtFixedRate(()->{
            System.out.println("miss you");
        },5,2, TimeUnit.SECONDS);

    }

    /**
     * 面试题： 假设一个闹钟每天早上7点响，有10亿人订阅这个闹钟，也就是每天早上7点会有10亿的并发量
     * 优化措施：
     * 1. 分发到边缘服务器（服务集群）
     * 2. 单台服务器并发量也很多-> 构建任务队列，多个线程去消费
     */
    private void alarmClock(){
    }


    /**
     * 固定线程的例子
     */
    private void fixedThreadPoolExample() throws ExecutionException, InterruptedException {
        int submit;

        long start = System.currentTimeMillis();
        getPrime(1,400000);
        long end = System.currentTimeMillis();
        System.out.println("并发执行耗时：" + (end-start) + " ms");


        start = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(CPU_CORE_NUM);
        MyTask myTask1 = new MyTask(1,50000);
        MyTask myTask2 = new MyTask(50001,100000);
        MyTask myTask3 = new MyTask(100001,150000);
        MyTask myTask4 = new MyTask(150001,200000);
        MyTask myTask5 = new MyTask(200001,250000);
        MyTask myTask6 = new MyTask(250001,300000);
        MyTask myTask7 = new MyTask(300001,350000);
        MyTask myTask8 = new MyTask(350001,400000);


        Future<List<Integer>> submit1 = threadPool.submit(myTask1);
        Future<List<Integer>> submit2 = threadPool.submit(myTask2);
        Future<List<Integer>> submit3 = threadPool.submit(myTask3);
        Future<List<Integer>> submit4 = threadPool.submit(myTask4);
        Future<List<Integer>> submit5 = threadPool.submit(myTask5);
        Future<List<Integer>> submit6 = threadPool.submit(myTask6);
        Future<List<Integer>> submit7 = threadPool.submit(myTask7);
        Future<List<Integer>> submit8 = threadPool.submit(myTask8);

        start = System.currentTimeMillis();
        submit1.get();
        submit2.get();
        submit3.get();
        submit4.get();
        submit5.get();
        submit6.get();
        submit7.get();
        submit8.get();
        end = System.currentTimeMillis();

        System.out.println("并行消耗时间："  + (end-start) + " ms");


    }

    /**
     * 是否是质数
     * @param num
     * @return
     */
    private boolean isPrime(int num){
        for (int i = 2; i < num/2; i++) {
            if(num%i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取范围内的质数
     * @param begin
     * @param end
     */
    private List<Integer> getPrime(int begin,int end){
        List<Integer> list = new ArrayList<>();
        for(int i =begin;i<=end;i++){
            if(isPrime(i)){
                list.add(i);
            }
        }
        return list;
    }


    static class MyTask implements Callable<List<Integer>> {
        int begin;
        int end;

        public MyTask(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public List<Integer> call() {
          return  new Test_06_ThreadPool_2().getPrime(begin,end);
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }
}
