package cas.juc_synchronization_lock.aqs;

import utils.TimeUtils;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: knowledge
 * @Package: cas.juc_synchronization_lock.reentrantlock
 * @ClassName: PhaserTest
 * @Author: czf
 * @Description: 遗传算法 可以使用
 * @Date: 2021/3/5 16:57
 * @Version: 1.0
 */
public class PhaserTest {

    /**
     * phaser 对象
     */
    private static final MarryPhaser phaser = new MarryPhaser();


    public static void main(String[] args) {
        phaser.bulkRegister(7);
        for (int i = 0; i < 5; i++) {
            new Thread(new Person("guest_"+i)).start();
        }

        new Thread(new Person("新娘")).start();
        new Thread(new Person("新郎")).start();
    }

    /**
     * 任务线程
     */
    static class Person implements Runnable {
        /**
         * 名字
         */
        private String name;

        public static Random random = new Random();

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            TimeUtils.timeUintSleep(random.nextInt(3), TimeUnit.SECONDS);
            System.out.println(name + " 到达了");
            phaser.arriveAndAwaitAdvance();
        }


        public void eat() {
            TimeUtils.timeUintSleep(random.nextInt(3), TimeUnit.SECONDS);
            System.out.println(name + " 吃饭了");
            phaser.arriveAndAwaitAdvance();
        }

        public void leave() {
            TimeUtils.timeUintSleep(random.nextInt(3), TimeUnit.SECONDS);
            System.out.println(name + " 离开了");
            phaser.arriveAndAwaitAdvance();

        }

        public void marry(){
            TimeUtils.timeUintSleep(random.nextInt(3),TimeUnit.SECONDS);
            if(name.equals("新郎")||name.equals("新娘")){
                System.out.println(name+" 洞房了");
                phaser.arriveAndAwaitAdvance();
            }else {
                phaser.arriveAndDeregister();
            }

        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            marry();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class MarryPhaser extends Phaser {

        /**
         * 前进 逻辑实现类
         * @param phase 阶段数
         * @param registeredParties 参与该阶段的线程数
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {

            switch (phase){
                case 0:
                    System.out.println("所有人都到齐了，"+registeredParties);
                    System.out.println();
                    break;
                case 1:
                    System.out.println("所有人都开始吃饭了，"+registeredParties);
                    System.out.println();
                    break;
                case 2:
                    System.out.println("所有人都离开了，"+registeredParties);
                    System.out.println();
                    break;
                case 3:
                    System.out.println("新郎新娘入洞房了，"+registeredParties);
                    System.out.println();
                    break;
            }

            return super.onAdvance(phase, registeredParties);
        }
    }
}
