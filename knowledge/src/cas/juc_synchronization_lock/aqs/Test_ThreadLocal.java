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

    static Person person = new Person("张三");

    public static void main(String[] args) {
        ThreadLocal<Person> personThreadLocal = new ThreadLocal<>();

        personThreadLocal.set(person);

        new Thread(()->{
            TimeUtils.timeUintSleep(3, TimeUnit.SECONDS);
            // 线程独有 因此在主线程设置的Person对象 在子线程拿不到对应的值
            System.out.println(personThreadLocal.get().getName());
        }).start();


        new Thread(()->{
            TimeUtils.timeUintSleep(1, TimeUnit.SECONDS);
            personThreadLocal.get().setName("李四");
        }).start();
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
