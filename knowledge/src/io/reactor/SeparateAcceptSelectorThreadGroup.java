package io.reactor;

import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;

/**
 * @ProjectName:
 * @ClassName: SeparateAcceptSelectorThreadGroup
 * @Author: czf
 * @Description: 分离读写和accept事件
 * @Date: 2021/6/10 22:09
 * @Version: 1.0
 **/

public class SeparateAcceptSelectorThreadGroup extends AbstractSelectorThreadGroup {

    public SeparateAcceptSelectorThreadGroup(int num) {
        super(num);
    }

    @Override
    public void nextSelector(Channel channel) {
        try {
            if(channel instanceof ServerSocketChannel){
                //如果是accept事件 ，那么我们从selectorGroup中取第一个 SelectorThread 来处理
                SelectorThread selectorThread = selectorThreads[0];
                selectorThread.lbq.put(channel);
                selectorThread.getSelector().wakeup();
            }else {
                //如果是读写事件，我们从剩下的SelectorThread选择一个来处理
                SelectorThread next = next();
                next.lbq.put(channel);
                next.getSelector().wakeup();
            }
        }catch (InterruptedException e){
            e.printStackTrace();;
        }

    }

    /**
     * 只在1，2两个元素中处理读写事件
     * @return
     */
    @Override
    public SelectorThread next() {
        int index = xid.incrementAndGet() % (selectorThreads.length - 1);
        return selectorThreads[index+1];
    }
}
