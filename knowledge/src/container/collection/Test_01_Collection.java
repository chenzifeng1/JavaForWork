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
        int[] a = new int[10];
        for (int i = 0; i < 5; i++) {
            a[i] = i;
        }

        System.arraycopy(a,2,a,3,3);
//        a[2] = 99;
        for (int i : a) {
            System.out.println(i);
        }

//        Arrays.copyOf()
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
