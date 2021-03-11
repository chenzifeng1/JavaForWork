package cas.juc_synchronization_lock.reference;

/**
 * @ProjectName: spring-security
 * @ClassName: M
 * @Author: czf
 * @Description:
 * @Date: 2021/3/10 20:47
 * @Version: 1.0
 **/

public class M {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
    }
}
