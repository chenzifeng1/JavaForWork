package cas.juc_synchronization_lock.interview;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-04-26 16:42
 * @Version: 1.0
 **/
public class LoopPrint {
    static volatile int num = 1;
    static Object lock = new Object();
    static Object lock1 = new Object();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    if (num == 1) {
                        System.out.println(num);
                        num = 2;
                        lock.notifyAll();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    if (num == 2) {
                        System.out.println(num);
                        num = 3;
                        lock.notifyAll();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        Thread thread3 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    if (num == 3) {
                        System.out.println(num);
                        num = 1;
                        lock.notifyAll();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });


        thread1.start();
        thread2.start();
        thread3.start();
    }
}
