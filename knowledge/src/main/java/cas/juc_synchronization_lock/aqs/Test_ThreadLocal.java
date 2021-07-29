package cas.juc_synchronization_lock.aqs;

import utils.TimeUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-10 16:17
 * @Version: 1.0
 **/
public class Test_ThreadLocal {

    static Person zhangsan = new Person("张三");

    public static void main(String[] args) {
        ThreadLocal<Person> personThreadLocal = new ThreadLocal<>();

        personThreadLocal.set(zhangsan);

        Thread thread1 = new Thread(() -> {
            TimeUtils.timeUintSleep(3, TimeUnit.SECONDS);
            // 线程独有 因此在主线程设置的Person对象 在子线程拿不到对应的值
            personThreadLocal.set(new Person(Thread.currentThread().getName()));
            personThreadLocal.set(new Person("czf"));
            Person person = personThreadLocal.get();
            System.out.println(Thread.currentThread().getName() + " : " + person.getName());
        },"thread1");


        Thread thread2 = new Thread(() -> {
            TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
            personThreadLocal.set(new Person(Thread.currentThread().getName()));
            personThreadLocal.get().setName("李四");
            Person person = personThreadLocal.get();
            System.out.println(Thread.currentThread().getName() + " : " + person.getName());
        },"thread2");

        thread1.start();
        thread2.start();
        Person person = personThreadLocal.get();
        System.out.println(Thread.currentThread().getName() + " : " + person.getName());


    }







    static class Person{
        String name;

        public Person(String name) {
            this.name = name;
        }

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
