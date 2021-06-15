package container.ticketseller;

import java.util.UUID;

/**
 * @ProjectName: spring-security
 * @ClassName: Ticket
 * @Author: czf
 * @Description: 车票
 * @Date: 2021/3/18 22:28
 * @Version: 1.0
 **/

public class Ticket {

    private UUID ticketID;
    private int no;

    public Ticket(UUID ticketID, int no) {
        this.ticketID = ticketID;
        this.no = no;
    }

    public Ticket() {
    }

    public UUID getTicketID() {
        return ticketID;
    }

    public void setTicketID(UUID ticketID) {
        this.ticketID = ticketID;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
