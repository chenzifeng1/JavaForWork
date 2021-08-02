package container.collection.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName:
 * @ClassName: ThreadPoolTest
 * @Author: czf
 * @Description:
 * @Date: 2021/7/28 17:50
 * @Version: 1.0
 **/

public class ThreadPoolTest {

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue(50),
            runable -> new Thread(runable, "ThreadPoot")
    );
}
