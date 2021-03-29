package container.ticketseller;

import config.StaticValue;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName: spring-security
 * @ClassName: TicketSeller
 * @Author: czf
 * @Description: N张火车票，每张火车票都有一个唯一的编号，10个窗口售票，写一个模拟程序。
 * @Date: 2021/3/18 22:21
 * @Version: 1.0
 **/

public class TicketSeller {
    /**
     * 车票池  这里使用普通数组，用其他方法保证数据在多线程情况下的正确性
     */
    static List<Ticket> ticketList = new ArrayList<>(StaticValue.ONE_HUNDRED_THOUSAND);

    static BlockingQueue<Ticket> queue = new ArrayBlockingQueue<Ticket>(StaticValue.ONE_HUNDRED_THOUSAND);

    static Vector<Ticket> ticketVector = new Vector<>(StaticValue.ONE_HUNDRED_THOUSAND);

    static Queue<Ticket> ticketQueue = new ConcurrentLinkedQueue<>();

    static BlockingQueue<Ticket> linkedQue = new LinkedBlockingQueue<>();

    Thread[] threads = new Thread[StaticValue.TEN];

    static {
        for (int i = 0; i < StaticValue.ONE_HUNDRED_THOUSAND; i++) {
            //初始化车票库
            UUID uuID = UUID.randomUUID();
            Ticket ticket = new Ticket(uuID, i);
            ticketList.add(ticket);
            ticketVector.add(ticket);
            linkedQue.add(ticket);
        }

    }


    public static void main(String[] args) {
        SellerThread[] sellerWindows = new SellerThread[10];
        AtomicInteger index = new AtomicInteger(0);
        TicketSeller ticketSeller = new TicketSeller();
        ticketSeller.test_4();
    }

    /**
     * 使用List作为车票容器，在多线程环境下会发生超卖的现象
     * 另外也可能会出现多个窗口售卖同一张票的情况
     */
    private void test_1() {
        for (int i = 0; i < StaticValue.TEN; i++) {
            new Thread(() -> {
                while (ticketList.size() > 0) {
                    Ticket t = ticketList.remove(0);
                    System.out.println(Thread.currentThread().getName() + "售出车票：" + t.getTicketID() + " 第 " + t.getNo() + " 张票");

                }
            }).start();
        }
    }


    /**
     * 这个地方也有问题，虽然使用了线程安全的容器vector，但是由于只是vector的方法加锁，整个 判断->销售 的过程没有加锁，这里会导致多个线程在size=1的时候看到size>0
     * 因此也会发送超卖现象
     */
    private void test_2() {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < StaticValue.TEN; i++) {
            new Thread(() -> {
                while (ticketVector.size() > 0) {
                    counter.incrementAndGet();
                    Ticket t = ticketVector.remove(0);
                    System.out.println(Thread.currentThread().getName() + "售出车票：" + t.getTicketID() + " 第 " + t.getNo() + " 张票");
                }
            }).start();
        }
    }


    /**
     * 一种可行的方法，使用synchronized关键字将整个售票逻辑包裹
     */
    private void test_3() {

        long start = System.currentTimeMillis();

        for (int i = 0; i < StaticValue.TEN; i++) {
            threads[i] = new Thread(() -> {
                synchronized (ticketList) {
                    while (ticketList.size() > 0) {
                        Ticket t = ticketList.remove(0);
                        System.out.println(Thread.currentThread().getName() + "售出车票：" + t.getTicketID() + " 第 " + t.getNo() + " 张票");
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < StaticValue.TEN; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("售票用时：" + (end - start) + "ms");
    }

    /**
     * 一种 比较高效的方法，使用阻塞队列：
     * 1. ArrayBlockingQueue
     * 2. LinkedBlockingQueue
     * <p>
     * 由于售票操作多是顺序遍历，很少涉及到随机访问，多是增加删除元素，因此使用LinkedBlockingQueue比较合适
     */
    private void test_4() {
        long start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < StaticValue.TEN; i++) {
            threads[i] = new Thread(() -> {
                while (true) {
                    Ticket t = linkedQue.poll();
                    if(t==null){
                        //如果queue.poll为空了，说明票已经卖完了
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + "售出车票：" + t.getTicketID() + " 第 " + t.getNo() + " 张票");
                    count.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < StaticValue.TEN; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("售票用时：" + (end - start) + "ms");
        System.out.println("共售出：" + count.get() + "张票");
    }

}
