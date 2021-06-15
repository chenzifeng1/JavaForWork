# Collection

## 关于System.arraycopy()与Arrays.copyOf()


` public static native void arraycopy(Object src,  int  srcPos, Object dest, int destPos, int length);`  
该方法五个参数：  （dest->src）
- Object src : 拷贝的目的数组
- int srcPos : 需要将元素拷贝到目的数组的哪个位置
- Object dest : 被拷贝的数组（元素待拷贝数组）
- int destPos : 从哪个位置开始拷贝元素
- int length : 需要拷贝多少个元素

`public static <T> T[] copyOf(T[] original, int newLength)`  
该方法



## Queue
重要方法：  
1. offer:
2. add:
3. peek:
4. pull:

### BlockingQueue
相对于Queue多出两个实现阻塞的方法：  
1. put:一定要加入队列，如果队列满了就阻塞当前线程，等待队列空出来在添加进去
2. take: 从队列获取元素，如果队列空了就阻塞当前线程

