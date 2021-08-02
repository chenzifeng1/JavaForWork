package cas.reentrantlock;

import config.StaticValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ProjectName: spring-security
 * @ClassName: ReentrantLockT03
 * @Author: czf
 * @Description: ReentrantLock比synchronized强大的地方
 *
 * 封装了一些方法 tryLock()
 * lockInterruptibly() : 可以被打断的加锁，我们可以通过其他线程来打断线程的加锁。 这个具体用法还需要自己查资料！
 * 更轻量级 使用CAS
 * @Date: 2021/3/3 20:44
 * @Version: 1.0
 **/

public class ReentrantLockT03 {

    public static ReentrantLock staticLock = new ReentrantLock();

    private void m1(){

        try {
            staticLock.lockInterruptibly();
            for (int i = 0; i < StaticValue.TEN; i++) {
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            staticLock.unlock();
        }
    }

    private void m2(){
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(()->{
           try {
               lock.lockInterruptibly();
               System.out.println("t1 start");
               TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
               System.out.println("t1 end");
           } catch (InterruptedException e) {
               System.out.println("t1 interruped!!!");
           }finally {
               lock.unlock();
           }
        });



        Thread t2 = new Thread(()->{
           try {
               lock.lock();
               System.out.println("t2 start");
               TimeUnit.SECONDS.sleep(5);
               System.out.println("t2 end");
           }catch (Exception e){
               System.out.println("t2 interrupted!");
           }finally {
               lock.unlock();
           }
        });
        t1.start();
        t1.interrupt();
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
