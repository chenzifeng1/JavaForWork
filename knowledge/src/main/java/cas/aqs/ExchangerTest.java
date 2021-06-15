package cas.aqs;

import java.util.concurrent.Exchanger;

/**
 * @Author: czf
 * @Description:  线程之间交换数据
 * @Date: 2021-03-08 16:35
 * @Version: 1.0
 **/
public class ExchangerTest {

    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {

        new Thread(()->{
            String str = "T1";
            try {
                str =  exchanger.exchange(str);
                System.out.println(Thread.currentThread().getName() + " " + str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t1").start();

        new Thread(()->{
            String str = "T2";
            try {
                str =  exchanger.exchange(str);
                System.out.println(Thread.currentThread().getName() + " " + str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();

    }
}
