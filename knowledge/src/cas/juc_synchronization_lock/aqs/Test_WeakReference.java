package cas.juc_synchronization_lock.aqs;

import java.lang.ref.WeakReference;

/**
 * @Author: czf
 * @Description:
 * 弱引用碰到GC就会被回收
 * 弱引用一般用在容器里面：与一个强引用一起指向某个对象，这样当强引用消失时，弱引用指向的对象就会在GC的时候被回收掉。
 * 如果没有弱引用指向该对象，GC的时候该对象可能不会被清理
 *
 * WeakHashMap
 * @Date: 2021-03-11 9:24
 * @Version: 1.0
 **/
public class Test_WeakReference {

    public static void main(String[] args) {
        WeakReference<M> weakReference = new WeakReference<M>(new M());
        System.out.println(weakReference.get());
        System.gc();
        System.out.println(weakReference.get());

        //弱引用的典型应用
        ThreadLocal<M> threadLocal = new ThreadLocal<M>();
        threadLocal.set(new M());
        threadLocal.remove();


    }


    static class M{
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize");
        }
    }
}
