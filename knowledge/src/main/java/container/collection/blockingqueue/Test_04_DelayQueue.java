package container.collection.blockingqueue;

import java.sql.Time;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description: 可以实现在时间上的排序
 * 1. 可以实现按照时间进行任务调度
 * 2. 使用queue.foreach();进行便利的结果好像并非按照时间排序的，只用使用take取出元素才是按照时间进行排序
 * 3. 本质上是一个PriorityQueue
 * @Date: 2021-03-29 19:30
 * @Version: 1.0
 **/
public class Test_04_DelayQueue {


    public static void main(String[] args) {
        BlockingQueue<MyTask> queue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        MyTask task1 = new MyTask("task_1", now + 3000);
        MyTask task2 = new MyTask("task_2", now + 1500);
        MyTask task3 = new MyTask("task_3", now + 500);
        MyTask task4 = new MyTask("task_4", now - 2000);
        MyTask task5 = new MyTask("task_5", now + 1000);


        queue.add(task1);
        queue.add(task2);
        queue.add(task3);
        queue.add(task4);
        queue.add(task5);

        for (int i = 0; i < 5; i++) {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class MyTask implements Delayed {
        String name;
        long runningTime;


        public MyTask(String name, long runningTime) {
            this.name = name;
            this.runningTime = runningTime;
        }

        public MyTask() {
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getRunningTime() {
            return runningTime;
        }

        public void setRunningTime(long runningTime) {
            this.runningTime = runningTime;
        }

        @Override
        public String toString() {
            return "MyTask{" +
                    "name='" + name + '\'' +
                    ", runningTime=" + runningTime +
                    '}';
        }
    }
}
