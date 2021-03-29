package container.collection.blockingqueue;

import config.StaticValue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-29 19:29
 * @Version: 1.0
 **/
public class Test_01_ConcurrentQueue {

    public static void main(String[] args) {
        //双端队列 ConcurrentLinkedDeque
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < StaticValue.TEN; i++) {
            //相当于 add 但是offer会有返回值：添加成功返回true，添加失败()返回false
            queue.offer("a" + i);
        }

        System.out.println(queue);
        System.out.println(queue.size());
        // get and remove
        System.out.println(queue.poll());
        System.out.println(queue.size());
        // get and not remove
        System.out.println(queue.peek());
        System.out.println(queue.size());


    }
}
