package io.reactor;

import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;

/**
 * @ProjectName:
 * @ClassName: MultipleSelectorThreadGroup
 * @Author: czf
 * @Description: 将读写和accept分别放在多个事件组里面处理
 * @Date: 2021/6/10 22:21
 * @Version: 1.0
 **/

public class MultipleSelectorThreadGroup extends AbstractSelectorThreadGroup{

    SelectorThreadGroup worker ;

    public MultipleSelectorThreadGroup(int num) {
        super(num);
       worker = this;
    }

    @Override
    public void nextSelector(Channel channel) {
        try {
            if(channel instanceof ServerSocketChannel){
                //accept的事件有 boss 完成，也就是有本身对象完成
                SelectorThread nextBossThread = getNextBossThread();
                // 设置worker对象，这样处理
                nextBossThread.setWorker(worker);
                nextBossThread.lbq.put(channel);
                nextBossThread.getSelector().wakeup();
            }else {
                //如果是读写事件交给 worker来读
                worker.nextSelector(channel);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Override
    public SelectorThread next() {
        return worker.next();
    }

    public void setWorker(SelectorThreadGroup worker) {
        this.worker = worker;
    }


    private SelectorThread getNextBossThread(){
        return selectorThreads[ xid.incrementAndGet()% selectorThreads.length];
    }
}
