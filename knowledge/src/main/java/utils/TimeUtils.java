package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    /**
     * 获取时间
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
