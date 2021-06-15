package cas.juc_synchronization_lock.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-11 8:37
 * @Version: 1.0
 **/
public class AQSLearn {

    public static void main(String[] args) {
        AQSLearn aQSLearn = new AQSLearn();
        aQSLearn.ThreadLocalLearn();

    }


    public void ThreadLocalLearn(){
        ThreadLocal<Node<String>> threadLocal = new ThreadLocal<>();

        threadLocal.set(new Node<>(Thread.currentThread().getName()));
        new Thread(()->{
            threadLocal.set(new Node<>(Thread.currentThread().getName()));
            System.out.println(threadLocal.get());
        }).start();

        System.out.println(threadLocal.get());
    }


    public void testReentrantLock(){
        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            reentrantLock.lock();

        }finally {
            reentrantLock.unlock();
        }
    }

    static class Node<T>{
        T value;
        Node next;

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
