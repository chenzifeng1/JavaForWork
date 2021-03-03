# CAS （无锁优化 自旋）
1. Compare And Set  
  - 原理: 可以把CAS当作一个方法来解读   
    CAS(Value,Expected,NewValue)  
    1. Value:要改的值
    2. Expected:期望当前线程的值
    3. NewValue:要设定的新值   
  可以看一下伪代码:
       ```
        if Value == Expected
            Value = NewValue
        otherwise try angin or fail
        ```
  意思是 如果我当前的值Value跟我期待的值Expected一致，那么说明当前没有人改过Value,我可以把它设为新的值NewValue
  不一致的话说明有其他线程改动了这个值  

  问： 如果我判断完Value == Expected,在赋值之前Value的值被改了怎么办。  
  答： CAS操作是CPU指令级别的原语操作，即不可被打断。  

2. Atomic类  
凡是以Atomic开头的类，都是基于CAS实现的，而非Synchronized这种重量级锁。
  - 原理：以`AtomicInteger`为例，里面封装了对整型变量的许多线程安全的操作。
```java
 AtomicInteger count = new AtomicInteger(0);
```

比如  `count.incrementAndGet()` 相当于`++count`
我们可以看一下实现代码：
```java

public class AtomicInteger extends Number implements java.io.Serializable {

  private static final jdk.internal.misc.Unsafe U = jdk.internal.misc.Unsafe.getUnsafe();
  
  public final int incrementAndGet() {
    return U.getAndAddInt(this, VALUE, 1) + 1;
  }   
}
```
incrementAndGet()方法调用`UnSafe`对象的`getAndAddInt()`方法  
继续深入下去

```java
public final class Unsafe {
    
  @HotSpotIntrinsicCandidate
  public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
      v = getIntVolatile(o, offset);
    } while (!weakCompareAndSetInt(o, offset, v, v + delta));
    return v;
  }
}
```

## ABA问题
如果A线程在CAS过程中，Value值，假设现在为1，有一个线程B将Value先改为2，在改回1。那么A线程在做判断的时候 Value == Expected。  
解决手段：加版本号，每次做更改操作会更新版本号，在检查的时候同时比较版本号。
注： 基础类型ABA问题影响不大，但是引用类型可能会导致一些问题的发生。

## Unsafe类
Unsafe类，构造方法私有，单例模式。 类似C/C++的指针

    - 直接操作内存 操作JVM的内存
        allocateMemory/freeMemory
    - 直接生成类实例
        allocateInstance

## AtomicXXX 原子类
以Atomic打头的类都是基于CAS实现的，也就是说Atomic是基于无锁情况下来使用CAS操作维持线程安全的。
在多线程竞争的情况下，效率会比synchronized更高效。

## increment 
实现多线程环境下，线程安全的递增：
1. synchronized 有一个锁升级的过程
2. AtomicXXX.incrementAndGet() -> CAS
3. LongAdder -> 使用分段锁，将不同的线程在不同数据段中加锁，全部计算完成之后在统一计算。
