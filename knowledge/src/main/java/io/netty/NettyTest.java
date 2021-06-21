package io.netty;

import org.junit.Test;

/**
 * @ProjectName:
 * @ClassName: NettyTest
 * @Author: czf
 * @Description: Netty测试类
 * @Date: 2021/6/21 20:35
 * @Version: 1.0
 **/

public class NettyTest {


    @Test
    public void clientTest(){
        MyNettyClient client = new MyNettyClient();
        client.run();

    }
}
