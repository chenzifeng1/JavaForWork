package container.map;

import config.StaticValue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: czf
 * @Description: 测试hashTable 和Collection.synchronizedCollection的效率高低
 * @Date: 2021-03-16 13:20
 * @Version: 1.0
 **/
public class Test_02_Map {
    /**
     * 1000000的数据量
     */
    static Hashtable<UUID, UUID> hashTable= new Hashtable<UUID, UUID>();
    static HashMap<UUID,UUID> hashMap = new HashMap<UUID, UUID>();
    static Map<UUID, UUID> synchronizedMap = Collections.synchronizedMap(new HashMap<UUID,UUID>());
    static ConcurrentHashMap<UUID,UUID> concurrentHashMap = new ConcurrentHashMap<UUID, UUID>();
    static UUID[] keys = new UUID[StaticValue.BIG_COUNT];
    static UUID[] values = new UUID[StaticValue.BIG_COUNT];
    /**
     * 100个线程
     */
    static Thread[] threads = new Thread[StaticValue.HUNDRED];

    /**
     * 准备相同的测试用例
     */
    static {
        concurrentHashMap.mappingCount();
        for (int i = 0; i < StaticValue.BIG_COUNT; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }


    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < StaticValue.HUNDRED; i++) {
            threads[i] = new WorkThread(i*(StaticValue.BIG_COUNT/StaticValue.HUNDRED));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("take into " + concurrentHashMap.size());
        System.out.println("it takes "+  (endTime-startTime) + "ms");
    }

    /**
     * Hashtable的方法大部分都加了Synchronized锁，但是大部分情况下，只有单线程来使用集合。
     * 因此对于所有方法都加上锁显然是不合理的
     */
    public void hashTableTest() {
        Hashtable<UUID, UUID> hashtable = new Hashtable<UUID, UUID>();
        UUID key = UUID.randomUUID();
        System.out.println("UUID key:" + key);
        hashtable.put(key, UUID.randomUUID());
        System.out.println("UUID value:" + hashtable.get(key));
    }

    public void hashMapTest() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("czf", "vip");

    }


    static class WorkThread extends Thread {
        /**
         * start 是每次线程要读取的第一个数的位置 由于是采取多线程的方式，所以这里分段来读取数据
         * 来保证数据的不冲突
         */
        int start = 0;

        int scope = StaticValue.BIG_COUNT/StaticValue.HUNDRED;

        public WorkThread(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "start----->" + start);
            for (int i = start; i < scope + start; i++) {
                concurrentHashMap.put(keys[i],values[i]);
            }
        }
    }
}
