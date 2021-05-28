       /*
        * Decompiled with CFR.
        */
       package jvm;
       
       import java.math.BigDecimal;
       import java.util.ArrayList;
       import java.util.Date;
       import java.util.List;
       import java.util.concurrent.ScheduledThreadPoolExecutor;
       import java.util.concurrent.ThreadPoolExecutor;
       import java.util.concurrent.TimeUnit;
       
       public class T15_FullGC_Problem01 {
           private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50, new ThreadPoolExecutor.DiscardOldestPolicy());
       
           public static void main(String[] args) throws Exception {
/*37*/         executor.setMaximumPoolSize(50);
               while (true) {
/*40*/             T15_FullGC_Problem01.modelFit();
/*41*/             Thread.sleep(100L);
/*42*/             System.out.println("123456");
/*43*/             System.out.println("sleep over");
               }
           }
       
           private static void modelFit() {
/*48*/         List<CardInfo> taskList = T15_FullGC_Problem01.getAllCardInfo();
               taskList.forEach(info -> executor.scheduleWithFixedDelay(() -> info.m(), 2L, 3L, TimeUnit.SECONDS));
           }
       
           private static List<CardInfo> getAllCardInfo() {
               ArrayList<CardInfo> taskList = new ArrayList<CardInfo>();
/*62*/         for (int i = 0; i < 100; ++i) {
                   CardInfo ci = new CardInfo();
/*64*/             taskList.add(ci);
               }
/*67*/         return taskList;
           }
       
           private static class CardInfo {
               BigDecimal price = new BigDecimal(0.0);
               String name = "张三";
               int age = 5;
               Date birthdate = new Date();
       
               private CardInfo() {
               }
       
               public void m() {
               }
           }
       }

