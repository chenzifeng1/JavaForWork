package jvm;

/**
 * @ProjectName:
 * @ClassName: JVM_05_CacheLine
 * @Author: czf
 * @Description: 缓存行问题
 * @Date: 2021/4/26 22:18
 * @Version: 1.0
 **/

public class JVM_05_CacheLine {
    public static T[] arr = new T[2];

    static{
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            for (long i = 0; i < 1000_0000l; i++) {
                arr[0].x=i;
            }
        }) ;

        Thread t2 = new Thread(()->{
            for (long i = 0; i < 1000_0000l; i++) {
                arr[1].x=i;
            }
        }) ;

        final  long start = System.nanoTime();

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start)/100_0000L);

    }

    private static class T{
        private volatile long x = 0L;
    }
}
