package cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ProjectName: spring-security
 * @ClassName: AtomicClass
 * @Author: czf
 * @Description: 学习Atomic类的使用
 * @Date: 2021/3/1 23:06
 * @Version: 1.0
 **/

public class AtomicClass {

    static AtomicInteger count1 = new AtomicInteger(0);
    static LongAdder count2 = new LongAdder();
    static long count3 = 0;
    public static int THREAD_NUM = 1000;
    public static int TIME_NUM = 10000;

    static void fun(Operator op) {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i] = new Thread(op::add);
        }
        long start = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        long end  = System.currentTimeMillis();
        System.out.println(op.kind()+" 执行使用的时间是："+(end-start)/1000 + "s");
    }

    public static void main(String[] args) {
        fun(new Operator() {
            @Override
            public void add() {
                for (int i = 0; i < TIME_NUM; i++) {
                    count1.incrementAndGet();
                }
            }

            @Override
            public String kind() {
                return "count1 Atomic " + count1.get();
            }
        });

        fun(new Operator() {

            @Override
            public void add() {
                for (int i = 0; i < TIME_NUM; i++) {
                    count2.increment();
                }
            }

            @Override
            public String kind() {
                return "count2 LongAdder " + count2.longValue();
            }
        });

        fun(new Operator() {

            @Override
            public void add() {
                for (int i = 0; i < TIME_NUM; i++) {
                   synchronized (this){
                       count3++;
                   }
                }
            }

            @Override
            public String kind() {
                return "count3 synchronized " + count3;
            }
        });

    }


    private interface Operator{

        /**
         * 执行+1操作
         */
        void add();

        /**
         * 表明使用的是哪一种方法
         * @return
         */
        String kind();
    }
}
