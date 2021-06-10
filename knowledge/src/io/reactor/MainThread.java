package io.reactor;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-08 14:47
 * @Version: 1.0
 **/
public class MainThread {

    public static final int SINGLE_THREAD =1;
    public static final int MULTI_THREAD =3;


    public static void main(String[] args) {
        MultipleSelectorThreadGroup boss = new MultipleSelectorThreadGroup(MULTI_THREAD);
        SelectorThreadGroup worker = new PollingSelectorThreadGroup(MULTI_THREAD);
        boss.setWorker(worker);
        boss.bind(8087);
        boss.bind(8088);
        boss.bind(8089);
        boss.bind(8090);


    }
}
