package container.collection;

import cas.juc_synchronization_lock.reference.M;

import java.util.*;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-16 9:31
 * @Version: 1.0
 **/

public class Test_01_Collection {


    List<Object> list;
    Set<Object> set;
    Queue<Object> queue;


    public static void main(String[] args) {

    }


    public void linkedListTest(){
        LinkedList<Object> linkedList = new LinkedList<>();

        linkedList.addFirst(new M<String>("czf"));
    }
    public void arrayListTest(){
        ArrayList<Object> arrayList = new ArrayList<>();

        arrayList.add(new M<String >("czf"));
    }




}
