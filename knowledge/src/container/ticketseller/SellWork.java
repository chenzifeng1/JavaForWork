package container.ticketseller;

/**
 * @ProjectName:
 * @ClassName: SellWork
 * @Author: czf
 * @Description: 售票工作
 * @Date: 2021/3/22 10:23
 * @Version: 1.0
 **/
public interface SellWork {

    /**
     * 售票行为
     * @param start
     */
    public void doSell(int start,Ticket[] tickets);

}
