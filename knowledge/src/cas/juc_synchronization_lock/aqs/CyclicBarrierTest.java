package cas.juc_synchronization_lock.aqs;

import config.StaticValue;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ProjectName: knowledge
 * @Package: cas.juc_synchronization_lock.reentrantlock
 * @ClassName: CyclicBarrierTest
 * @Author: czf
 * @Description: 循环栅栏 可以做到线程控制
 * 构造方法 public CyclicBarrier(int parties, Runnable barrierAction)
 * parties: 等待线程个数
 * barrierAction: 线程等待完成之后 执行的操作
 * 与CountDownLatch 并不是一个倒数操作一个正数操作
 * CountDownLatch 是可以在一个线程内多次CountDown 直到计数器为0。
 * CyclicBarrier 是多个线程阻塞住，知道等够指定的线程才会继续执行
 *
 * 应用场景：
 * 多个并发操作需要都完成时才能进行下面的操作，可以使用CyclicBarrier进行阻塞
 *
 * @Date: 2021/3/5 16:40
 * @Version: 1.0
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(20,()-> System.out.printf("人满，发车"));

        for (int i = 0; i < StaticValue.HUNDRED; i++) {

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+ "上车");
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }).start();

        }
    }
}
