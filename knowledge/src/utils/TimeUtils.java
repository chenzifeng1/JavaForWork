package utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {


    /**
     * 睡眠
     * @param sleepTime 睡眠的时间
     * @param unit 时间单位
     */
    public static void timeUintSleep(int sleepTime, TimeUnit unit){
        try {
            unit.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
