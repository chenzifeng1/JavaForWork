package container.collection.blockingqueue;

import java.util.PriorityQueue;

/**
 * @ProjectName:
 * @ClassName: Test_05_PriorityQueue
 * @Author: czf
 * @Description: 优先级队列
 * 优先级队列底层实现是一个二叉树 （小顶堆）
 * @Date: 2021/3/29 22:15
 * @Version: 1.0
 **/

public class Test_05_PriorityQueue<T>  {


    public static void main(String[] args) {
        PriorityQueue<MyStringTask> pq = new PriorityQueue<MyStringTask>();
        MyStringTask task1 = new MyStringTask("a_1","a");
        MyStringTask task2 = new MyStringTask("a_2","d");
        MyStringTask task3 = new MyStringTask("a_3","c");
        MyStringTask task4 = new MyStringTask("a_4","b");
        MyStringTask task5 = new MyStringTask("a_5","z");

        pq.add(task1);
        pq.add(task2);
        pq.add(task3);
        pq.add(task4);
        pq.add(task5);

        for (int i = 0; i < 5; i++) {
            System.out.println(pq.poll().toString());
        }
    }


    static class MyStringTask implements Comparable<String>{
        String name;
        String priority;

        public MyStringTask(String name, String priority) {
            this.name = name;
            this.priority = priority;
        }

        public MyStringTask() {
        }



        @Override
        public int compareTo(String o) {
            return this.priority.compareTo(o);
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "MyStringTask{" +
                    "name='" + name + '\'' +
                    ", priority='" + priority + '\'' +
                    '}';
        }
    }

}
