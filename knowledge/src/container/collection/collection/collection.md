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

