package jvm;

/**
 * @ProjectName:
 * @ClassName: JVM_07_Disorder
 * @Author: czf
 * @Description: 指令乱序证明
 * @Date: 2021/4/27 21:05
 * @Version: 1.0
 **/

public class JVM_07_Disorder {

    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {

        while (true){
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = 1;
                    x = b;
                }
            });

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    y = a;
                }
            });
            thread1.start();thread2.start();
            thread1.join();thread2.join();
            if(x==0&&y==0){
                System.out.println("disorder get!!!");
                break;
            }
        }

    }

    public static void shortWait(int waitTime) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + waitTime > end);
    }
}
