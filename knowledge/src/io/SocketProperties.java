package io;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-05-28 14:53
 * @Version: 1.0
 **/
public class SocketProperties {
    /**
     * 数据接收缓存区大小
     */
    public static final int RECEIVE_BUFFER = 10;
    /**
     * 表示等待客户连接的超时时间。一般不设置，会持续等待
     */
    public static final int SO_TIMEOUT = 0;
    /**
     * 表示是否允许重用服务器所绑定的地址。一般不设置
     */
    public static final boolean REUSE_ADDR = false;
    /**
     * 允许几个额外的client连入
     */
    public static final int BACK_LOG = 2;


    //client socket listen property on server endpoint: 服务端需要设置的客户端监听属性

    public static final boolean CLI_KEEPALIVE = false;
    /**
     * 置 OOBINLINE 选项时，在套接字上接收的所有 TCP 紧急数据都将通过套接字输入流接收
     */
    public static final boolean CLI_OOB = false;
    /**
     * 接收缓存区大小
     */
    public static final int CLI_REC_BUF = 20;
    /**
     * 发送缓存区大小
     */
    public static final int CLI_SEND_BUF = 20;
    /**
     * 是否
     */
    public static final boolean CLI_REUSE_ADDR = false;
    /**
     * 是否允许关闭时逗留
     */
    public static final boolean CLI_LINGER = true;
    /**
     * 指定关闭时逗留的超时值
     */
    public static final int CLI_LINGER_N = 0;
    public static final int CLI_TIMEOUT = 0;
    public static final boolean CLI_NO_DELAY = false;



}
