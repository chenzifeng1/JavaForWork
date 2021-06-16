package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @ProjectName:
 * @ClassName: MyNetty
 * @Author: czf
 * @Description: netty的初步了解
 * 基于NIO的相关概念
 * channel bytebuffer selector
 * bytebuffer -> bytebuf (增加了池化的概念 pool)
 *
 * 回顾一下NIO,多路IO选择器的版本。
 * channel可以认为是IO事件走的通道，bytebuffer作为缓冲区，是数据读写的地方，selector则是获取socket事件状态的，然后将带状态的fd告知给application
 *
 * @Date: 2021/6/16 23:13
 * @Version: 1.0
 **/

@Slf4j
public class MyNetty {

    @Test
    public void bufTest(){
        ByteBuf buf = getBuf(4, 20);

    }


    /**
     * 获取Netty的ByteBuf的方式
     * @param initCapacity
     * @param maxCapacity
     * @return
     */
    public ByteBuf getBuf(int initCapacity, int maxCapacity){
        // 这里传参需要两个值 ：一个是初始值 一个是最大值
      return    ByteBufAllocator.DEFAULT.buffer(initCapacity,maxCapacity);

    }
}
