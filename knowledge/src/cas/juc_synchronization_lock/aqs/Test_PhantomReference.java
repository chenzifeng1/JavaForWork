package cas.juc_synchronization_lock.aqs;

import utils.TimeUtils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * 虚引用：基本不会用到 写JVM、Netty可以用到
 * 另外： 与弱引用不同，WeakReference.get() 在GC之前是可以拿到引用的对象的
 *     但是虚引用，PhantomReference.get()，是拿不到引用对象的
 *
 *
 * @Date: 2021-03-11 10:25
 * @Version: 1.0
 **/
public class Test_PhantomReference {
    private static final List<Object> list =new LinkedList<>();
    private static final ReferenceQueue<M> referenceQueue = new ReferenceQueue<>();


    public static void main(String[] args) {
        // 虚引用 两个参数，一个是引用对象，另一个是引用队列  内存中 phantomReference对象以虚引用指向M和引用队列
        PhantomReference<M> phantomReference = new PhantomReference<>(new M(),referenceQueue);

        // 这里不断向内存放入对象，当内存到达阈值触发GC,这时候去看有没有虚引用被回收
        new Thread(()->{
            while (true){
                list.add(new byte[1024*1024]);
                TimeUtils.timeUintSleep(1,TimeUnit.SECONDS);
                System.out.println(phantomReference.get());
            }
        }).start();

        new Thread(()->{
            while (true){
                Reference<? extends M> poll =referenceQueue.poll();
                if(poll!=null){
                    System.out.println("JVM 已经回收了：" + poll.get());
                }

            }
        }).start();
    }


    static class M{
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize");
        }
    }


}
