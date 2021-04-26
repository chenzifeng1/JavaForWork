package jvm;

/**
 * @ProjectName:
 * @ClassName: JVM_05_CacheLine1
 * @Author: czf
 * @Description: 缓存行对比
 * @Date: 2021/4/26 22:31
 * @Version: 1.0
 **/

public class JVM_05_CacheLine1 {

    public static C[] arr = new C[2];

    static{
        arr[0] = new C();
        arr[1] = new C();
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

    private static class PaddingCacheLine{
         long p1,p2,p3,p4,p5,p6,p7;
    }


    private static class C extends PaddingCacheLine{
        private volatile long x = 0L;
    }
}
