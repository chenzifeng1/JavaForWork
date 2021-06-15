package jvm;

/**
 * @ProjectName:
 * @ClassName: JVM_06_WriteCombining
 * @Author: czf
 * @Description: 合并写问题
 *
 *  |-寄存器-|------[WCBuffer]     |     |-寄存器-|
 *  |-  L1缓存  -|    |           |     |-  L1缓存  -|
 *  |-    l2缓存    -|           |     |-    l2缓存    -|
 *  |-                    L3缓存                       -|
 * CPU中存在一个WCBuffer: WriteCombiningBuffer 合并写的缓存，该缓存层级在寄存器与L1缓存中间，即比L1的读写速度更快
 * 在CPU遇到对数据进行连续写的时候，由于要写回内存，所以会有个先向L2缓存写的过程
 * 合并写是指写操作不会每执行一次就去往L2缓存中去写，而是先写到WCBuffer（4byte）中,然后一块写回到L2中
 * 那么每次如果写的值超过了4byte，就会存在分割，等待下次写的过程，所以利用好合并写特性可以优化程序执行效率
 * @Date: 2021/4/27 20:33
 * @Version: 1.0
 **/

public final class JVM_06_WriteCombining {

    private static final int ITERATIONS = Integer.MAX_VALUE;
    private static final int ITEMS = 1 << 24;
    private static final int MASK = ITEMS - 1;

    private static final byte[] ARRAY_A = new byte[ITEMS];
    private static final byte[] ARRAY_B = new byte[ITEMS];
    private static final byte[] ARRAY_C = new byte[ITEMS];
    private static final byte[] ARRAY_D = new byte[ITEMS];
    private static final byte[] ARRAY_E = new byte[ITEMS];
    private static final byte[] ARRAY_F = new byte[ITEMS];

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            System.out.println("runCaseOne:" + runCaseOne());
            System.out.println("runCaseTwo:" + runCaseTwo());
            System.out.println("----------------------------");
        }

    }


    public static long runCaseOne() {
        long start = System.nanoTime();
        int i = ITERATIONS;
        while (i-- != 0) {
            int slot = i & MASK;
            byte b = (byte)i;
            ARRAY_A[slot] = b;
            ARRAY_B[slot] = b;
            ARRAY_C[slot] = b;
            ARRAY_D[slot] = b;
            ARRAY_E[slot] = b;
            ARRAY_F[slot] = b;
        }
        return System.nanoTime()-start;
    }

    public static long runCaseTwo() {
        long start = System.nanoTime();
        int i = ITERATIONS;
        while (i-- != 0) {
            int slot = i & MASK;
            byte b = (byte)i;
            ARRAY_A[slot] = b;
            ARRAY_B[slot] = b;
            ARRAY_C[slot] = b;
        }
        i = ITERATIONS;
        while (i-- != 0) {
            int slot = i & MASK;
            byte b = (byte)i;
            ARRAY_D[slot] = b;
            ARRAY_E[slot] = b;
            ARRAY_F[slot] = b;
        }
        return System.nanoTime()-start;
    }

}
