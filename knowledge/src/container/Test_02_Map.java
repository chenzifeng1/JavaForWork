package container;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-16 13:20
 * @Version: 1.0
 **/
public class Test_02_Map {


    public static void main(String[] args) {

    }

    /**
     * Hashtable的方法大部分都加了Synchronized锁，但是大部分情况下，只有单线程来使用集合。
     * 因此对于所有方法都加上锁显然是不合理的
     */
    public void hashTableTest(){
        Hashtable<UUID,UUID> hashtable = new Hashtable<UUID, UUID>();
        UUID key = UUID.randomUUID();
        System.out.println("UUID key:" + key);
        hashtable.put(key,UUID.randomUUID());
        System.out.println("UUID value:" + hashtable.get(key));
    }

    public void hashMapTest() {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("czf","vip");

    }
}
