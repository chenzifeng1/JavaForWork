package threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-04-02 16:57
 * @Version: 1.0
 **/
public class MyRejectedPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        System.out.println(runnable.toString() + "进入消息队列");
    }
}
