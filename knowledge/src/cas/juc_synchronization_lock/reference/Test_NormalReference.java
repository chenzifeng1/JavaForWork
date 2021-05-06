package cas.juc_synchronization_lock.reference;

import java.io.IOException;

/**
 * @ProjectName: spring-security
 * @ClassName: Test_NormalReference
 * @Author: czf
 * @Description:
 * @Date: 2021/3/10 20:47
 * @Version: 1.0
 **/

public class Test_NormalReference {


    public static void main(String[] args) {
        M m = new M();
        m = null;
        // 手动调用GC
        System.gc();
        try {
            //阻塞住线程
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
