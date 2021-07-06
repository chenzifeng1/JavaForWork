package jvm;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ProjectName:
 * @ClassName: Hello
 * @Author: czf
 * @Description: 加载类的测试类
 * @Date: 2021/4/25 20:35
 * @Version: 1.0
 **/

public class Hello {

    HashMap<String,String> hashMap = new HashMap<String, String>();
    ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap<String, String>();
    ConcurrentSkipListSet<String> css = new ConcurrentSkipListSet<String>();
    ConcurrentSkipListMap<String,String> csm = new ConcurrentSkipListMap<String, String>();
    Queue<String> queue = new LinkedList<String>();
    Deque<String> deque = new LinkedList<>();
    CopyOnWriteArraySet<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    ConcurrentSkipListSet<String> concurrentSkipListSet = new ConcurrentSkipListSet<>();



    public void sayHi(){
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {

        System.out.println((byte)255&0xff);
        System.out.println((byte)255);
        System.out.println((byte)127&0xff);
        System.out.println((byte)128);
        System.out.println((byte)128&0xff);

        Hello h = new Hello();
    }


}
