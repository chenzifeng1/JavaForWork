package jvm;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName:
 * @ClassName: Test
 * @Author: czf
 * @Description:
 * @Date: 2021/7/7 21:10
 * @Version: 1.0
 **/

public class Test {
    static  Boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            System.out.println("start");
            while (!flag) {

            }
            System.out.println("end");
        }).start();
        TimeUnit.SECONDS.sleep(1);
        flag = true;
    }
}
