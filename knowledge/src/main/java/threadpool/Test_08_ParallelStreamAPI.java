package threadpool;

import config.StaticValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ProjectName:
 * @ClassName: Test_08_ParallelStreamAPI
 * @Author: czf
 * @Description: 并发处理API
 * @Date: 2021/4/6 21:56
 * @Version: 1.0
 **/

public class Test_08_ParallelStreamAPI {
    static List<Integer> list = new ArrayList<>(StaticValue.ONE_HUNDRED_THOUSAND);
    static Random random = new Random();

    static {
        for (int i = 0; i < StaticValue.ONE_HUNDRED_THOUSAND; i++) {
            list.add(random.nextInt(StaticValue.ONE_HUNDRED_THOUSAND));
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        list.forEach(v->isPrime(v));
        long end = System.currentTimeMillis();
        System.out.println("流式处理 用时：" + (end - start) + "ms");

        start = System.currentTimeMillis();
        list.parallelStream().forEach(Test_08_ParallelStreamAPI::isPrime);
        end = System.currentTimeMillis();
        System.out.println("并发流式处理 用时：" + (end - start) + "ms");

    }

    private static boolean isPrime(int num) {
        for (int i = 1; i < (num / 2); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

}
