package threadpool;

import config.StaticValue;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: czf
 * @Description: 普通线程池是一个任务队列，多个Worker线程共享。
 * WorkStealingPool：每个线程独占一个任务队列，如果自己的队列中没有任务了，回去其他线程的队列中 “拿“ 一个
 * WorkStealingPool是ForkJoinPool的一种对象
 * <p>
 * Fork  +  Join
 * Fork 将大的任务分解
 * Join 将小的任务汇总
 *
 * ForkJoinPool 最适合的是计算密集型的任务，如果存在 I/O，线程间同步，sleep() 等会造成线程长时间阻塞的情况时，最好配合使用 ManagedBlocker。
 *
 * ForkJoinPool 与任务量的规模好像有关系，由于会不断分解任务，小的任务又交给其他线程来执行，可能会造成 Stack Overflow 栈溢出
 * 两个主要方法
 * fork()方法类似于线程的Thread.start()方法，但是它不是真的启动一个线程，而是将任务放入到工作队列中。
 *
 * join()方法类似于线程的Thread.join()方法，但是它不是简单地阻塞线程，而是利用工作线程运行其它任务。当一个工作线程中调用了join()方法，它将处理其它任务，直到注意到目标子任务已经完成了。
 * @Date: 2021-04-06 19:43
 * @Version: 1.0
 **/
public class Test_07_ForkJoinPool {
    static int NUM_LENGTH = StaticValue.ONE_HUNDRED_THOUSAND;
    static int[] nums = new int[NUM_LENGTH];
    static final int MAX_NUM = 200000;
    static Random random = new Random();

    static {
        for (int i = 0; i < NUM_LENGTH; i++) {
            nums[i] = random.nextInt(StaticValue.HUNDRED);
        }
        //单线程计算
        System.out.println(Arrays.stream(nums).sum());
    }

    public static void main(String[] args) throws IOException {
        new Test_07_ForkJoinPool().test2();

    }

    public void test2(){
        ForkJoinPool pool = new ForkJoinPool();
        Instant startTime = Instant.now();
        AddTaskResult task = new AddTaskResult(0,nums.length-1);
        ForkJoinTask<Long> submit = pool.submit(task);
        Instant endTime = Instant.now();
        try {
            System.out.println(submit.get() + "  耗时 ：" + Duration.between(startTime,endTime).toMillis()+"ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pool.shutdown();
    }

    public void test1() throws IOException {
        ForkJoinPool fold = new ForkJoinPool();
        AddTask addTask = new AddTask(0, nums.length - 1);
        fold.execute(addTask);
        System.in.read();
    }

    /**
     * 无返回值的 计算
     */
    static class AddTask extends RecursiveAction {
        int start;
        int end;

        public AddTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            if (end - start <= MAX_NUM) {
                //计算的数量小于MAX_NUM 就开始计算
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
                System.out.println("from " + start + " to " + end + ",the sum result is " + sum);
            } else {
                int middle = start + (end - start) >> 1;
                AddTask addTask1 = new AddTask(start, middle);
                AddTask addTask2 = new AddTask(middle + 1, end);
                addTask1.fork();
                addTask2.fork();
            }
        }


    }


    /**
     * 带返回值的任务计算
     */
    static class AddTaskResult extends RecursiveTask<Long> {
        int start;
        int end;

        public AddTaskResult(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long num = 0L;
            if (end - start <= MAX_NUM) {
                for (int i = start; i <= end; i++) {
                    num += nums[i];
                }
                return num;
            }
            int middle = start + (end - start)>>1;
            AddTaskResult add1 = new AddTaskResult(start, middle);
            AddTaskResult add2 = new AddTaskResult(middle + 1, end);
            add1.fork();
            add2.fork();

            return add1.join() + add2.join();
        }
    }
}
