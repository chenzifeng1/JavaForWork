package container.ticketseller;

import config.StaticValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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

    static {
        for (int i = 0; i < StaticValue.TEN; i++) {

        }

    }


    public static void main(String[] args) {
        SellerThread[] sellerWindows = new SellerThread[10];
        AtomicInteger index = new AtomicInteger(0);

    }

}
