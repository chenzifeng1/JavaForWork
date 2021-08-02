package concurrent;

import utils.TimeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * 拼多多面试题：
 * 模拟一个后端处理程序： 有一个随机的输入，输入之间有先后顺序，要求使用线程池来进行处理。且按照输入的顺序顺序处理
 *
 * @Date: 2021-07-28 9:56
 * @Version: 1.0
 **/
public class PDDInterview {


    /**
     * 模拟串口接收以及处理
     * @param goods
     */
    public static void dealRequest(List<String> goods){

    }


    /**
     * 模拟服务接口
     * @param id
     */
    public static void doDealService(String id){
        try {
            System.out.println("Consumer: " + id);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
