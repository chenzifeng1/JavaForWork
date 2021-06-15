package container.ticketseller;

import java.util.List;

/**
 * @ProjectName: spring-security
 * @ClassName: SellerThread
 * @Author: czf
 * @Description: 售票窗口
 * @Date: 2021/3/18 22:28
 * @Version: 1.0
 **/

public class SellerThread extends Thread{
    int start;
    List<Ticket> tickets;

    public SellerThread(int start, List<Ticket>  tickets) {
        this.start = start;
        this.tickets = tickets;
    }


    /**
     * 这里不能分段买票，买票应该是一张张买
     */
    @Override
    public void run() {
        System.out.println("售出票："+tickets.remove(start).getNo());
    }
}
