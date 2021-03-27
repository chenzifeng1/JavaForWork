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

    private UUID no;
    private int status;

    public Ticket(UUID no, int status) {
        this.no = no;
        this.status = status;
    }

    public Ticket() {
    }

    public UUID getNo() {
        return no;
    }

    public void setNo(UUID no) {
        this.no = no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
