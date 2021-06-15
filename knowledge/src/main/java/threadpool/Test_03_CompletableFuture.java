package threadpool;

import utils.TimeUtils;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description: 假设你需要提供一个服务：
 * 这个服务查询汇总各大电商网站的同一类产品进行展示
 * 使用不同线程可以并发获取产品，而非一个个获取后然后展示
 * @Date: 2021-03-31 11:48
 * @Version: 1.0
 **/
public class Test_03_CompletableFuture {

    Random random = new Random();

    static final Test_03_CompletableFuture tc = new Test_03_CompletableFuture();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        CompletableFuture<Double> tb = CompletableFuture.supplyAsync(tc::selectFromTaoBao);
        CompletableFuture<Double> jd = CompletableFuture.supplyAsync(tc::selectFromJingDong);
        CompletableFuture<Double> pdd = CompletableFuture.supplyAsync(tc::selectFromPDD);
        //等待三个都运行完成之后
        CompletableFuture.allOf(tb, jd, pdd).join();

        long end = System.currentTimeMillis();

        System.out.println("总共耗时：" + (end - start) + "s");

        //其他应用  可以针对返回的结果进行一系列操作
        CompletableFuture.supplyAsync(()->tc.selectFromJingDong())
                .thenApply(String::valueOf)
                .thenApply(str->"price" + str)
                .thenAccept(System.out::println);

        TimeUtils.timeUintSleep(5,TimeUnit.SECONDS);

    }

    private double selectFromTaoBao() {
        delay("淘宝");
        return random.nextDouble();
    }

    private double selectFromJingDong() {
        delay("京东");
        return random.nextDouble();
    }

    private double selectFromPDD() {
        delay("pdd");
        return random.nextDouble();
    }


    private void delay(String arm) {
        System.out.println("正在爬取 " + arm);
        TimeUtils.timeUintSleep(random.nextInt(5), TimeUnit.SECONDS);
        System.out.println("爬取" + arm + "完成");
    }
}
