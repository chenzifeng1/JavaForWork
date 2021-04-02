# 线程池

## 基础概念
1. Callable<V> : 与Runnable一样，设计出来是为了一个线程去执行的。
   但是，与Runnable不同的是Callable有返回值,且是异步的，不需要等待线程执行完得到结果。
   而是线程执行完后会通知对应线程来返回结果。
2. Future<V> : 
### 接口
1. Worker类
2. submit方法
3. execute
4. addWorker

Executor->ExecutorService->ThreadPoolExecutor

## Executor
Executor作为一个接口，目的就是将定义与运行分离。
```java
public interface Executor {
    void execute(Runnable var1);
}
```
## ExecutorService
ExecutorService拓展了Executor接口，定义了一个线程池生命周期的一些方法
```java
public interface ExecutorService extends Executor {
    // 关闭线程池
    void shutdown();
    // 马上关闭线程池
    List<Runnable> shutdownNow();
    // 线程池是否结束
    boolean isShutdown();
    // 线程池是否已终止
    boolean isTerminated();
    // 等待线程池终止
    boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException;
    // 提交任务
    <T> Future<T> submit(Callable<T> var1);
    // 提交任务
    <T> Future<T> submit(Runnable var1, T var2);
    // 提交任务，将任务扔给线程池，什么时候执行由线程池自己决定
    Future<?> submit(Runnable var1);
    // 激活所有任务
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> var1) throws InterruptedException;
    
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> var1, long var2, TimeUnit var4) throws InterruptedException;

    <T> T invokeAny(Collection<? extends Callable<T>> var1) throws InterruptedException, ExecutionException;

    <T> T invokeAny(Collection<? extends Callable<T>> var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException;
}

```

## 线程池
1. ThreadPoolExecutor
2. ForkJoinPool 
   - 分解汇总任务
   - 用很少的线程可以执行很多的任务（子任务），TPE做不到先执行子任务
   - CPU密集型
   
Executors可以看作线程池的工厂
   
### ThreadPoolExecutor
阿里手册要求程序员在构建线程池的时候手动构建，原因是`Executors`的构造线程池方法默认的阻塞队列是`LinkedBlockingQueue` 是无界阻塞队列，不断向里面添加任务会造成内存溢出。
  
- 这些线程池在阿里手册中都不建议使用【强制】，但是我们还是必须要了解一下这几个线程池，因为这会为我们提供一个思路：如果利用不同种类的线程池

手动构造线程池的几个参数:
1. int corePoolSize,核心线程数，核心线程一般来说常驻内存
2. int maximumPoolSize, 最大线程数：在核心线程不够用的情况下最多扩到多少个线程
3. long keepAliveTime, 线程存活时间：非核心线程长时间不干活了，需要将其归还给操作系统；可以理解为非核心线程的最大空闲时间
4. TimeUnit unit, 时间单位
5. BlockingQueue<Runnable> workQueue, 任务队列，阻塞队列； 如果指定的阻塞队列是SynchronousQueue,容量为0，也就意味着来一个任务立刻需要一个线程来进行处理，不然其他任务无法加入。 
6. ThreadFactory threadFactory, 线程工厂；如何创建线程，需要实现ThreadFactory这个接口。阿里开发手册：【强制】创建线程或者线程池时
   请指定有意义的线程名称，方便出错回溯
7. RejectedExecutionHandler handler 拒绝策略：
   线程池工作流程：假设核心线程为2，最大线程为4，阻塞队列长度为5  
   --> 第一个任务加入，线程池此时无线程，会创建一个线程去执行这个任务，由于当前线程数没达到核心线程最大值，因此该线程为核心线程，会常驻内存  
   --> 第二个任务同理，如果第一个核心线程在忙，会创建第二个核心线程。  
   --> 第三个任务加入时，如果两个核心线程都在忙碌，则会将其加入任务队列等待执行。   
   ...  
   --> 第八个任务加入时，由于核心线程在忙碌，且任务队列已满。会创建临时线程来处理这个任务  
   --> 第九个任务加入时，创建第二个临时线程进行处理  
   --> 第十个任务加入时，由于达到最大线程数，且任务队列已满，因此线程池需要拒绝该任务，拒绝的策略  
   决策策略可以自定义，JDK默认提供了四种拒绝策略：
   1. Abort:抛出异常
   2. Discard：扔掉，不抛异常
   3. DiscardOldest:扔掉排队最久的任务线程
   4. CallerRuns: 调用者处理任务
   实际情况下会自己定义对应的拒绝策略，实现对应的接口。一般会将过多的任务写到缓存，记录日志。想办法记录没有执行的任务，然后空闲时恢复任务继续执行。
      
### SingleThreadPool
线程池内只有一个线程，原因： 
1. 该线程池可以保证任务是顺序执行的。
2. 线程池内维护一个任务队列，以及线程池的生命周期也会维护。不需要我们来维护  
可以看一下线程池的创建参数：
```java
public class Executors{
   public static ExecutorService newSingleThreadExecutor() {
      return new Executors.FinalizableDelegatedExecutorService(
              new ThreadPoolExecutor(
                      1, //核心线程数  
                      1, //最大线程数
                      0L, //非核心线程空闲时间
                      TimeUnit.MILLISECONDS, //时间单位 
                      new LinkedBlockingQueue())) //任务队列：该队列最大值为int类型的最大值，并非无限
              ;
   }
}   
```
- 核心线程数为1，最大线程数为1保证线程池内只维护一个线程。
- 任务队列使用`LinkedBlockingQueue`,这也是阿里不推荐使用`Executors`创建线程池的原因


### CachedThreadPool
缓存线程池，这个线程池很有意思，我们先看他的初始化参数：
```java
public class Executors{
   public static ExecutorService newCachedThreadPool() {
      return new ThreadPoolExecutor(
              0, //核心线程为0 
              2147483647, //最大线程数 为int类型的最大值
              60L, 
              TimeUnit.SECONDS, 
              new SynchronousQueue());//任务队列使用SynchronousQueue,来一个任务就处理一个，容量为0
   }
}
```
- 核心线程数为0，这样如果没有任务时，销毁所有线程。降低维护线程的资源使用
- 最大线程数为int的最大值，这样尽可能让多的线程来执行任务
- 任务队列使用`SynchronousQueue`,该阻塞队列的容量为0，即来一个任务就需要一个线程来执行，不然就阻塞着。

### FixedThreadPool
固定线程数的线程池，这个线程可以与`CachedThreadExecutor`这个线程池比较一下，两者适用于不同的场景。
```java
public class Executors{

   public static ExecutorService newFixedThreadPool(int nThreads) {
      return new ThreadPoolExecutor(
              nThreads, 
              nThreads, 
              0L, 
              TimeUnit.MILLISECONDS, 
              new LinkedBlockingQueue());
   }
}
```
该线程的核心线程与最大线程相等，意味着初始化之后线程池内的线程均为核心线程，常驻内存。所以初始化的时候指定多少线程就需要考量一下。
1. 线程数过多会引起线程之间竞争CPU和内存资源，且线程切换会耗费CPU资源
2. 线程数过少会引起CPU的利用率较低

确定线程数的方法：
1. CPU数+1
2. 线程个数=CPU个数× 期望CPU利用率 ×(1+线程等待时间/线程计算时间);

### ScheduledThreadPool
定时任务线程池，该线程池可以定时执行提交的任务，比较常用。但是如果定时任务比较复杂，且业务量多的时候建议使用`quartz`。看一下线程池的参数设置：
```java
public class Executors{

   public ScheduledThreadPoolExecutor(int corePoolSize) {
      super(
              corePoolSize,
              2147483647,//最大线程池依旧是int的最大值
              10L,
              TimeUnit.MILLISECONDS,
              new ScheduledThreadPoolExecutor.DelayedWorkQueue());//任务队列使用的是 DelayWorkQueue
   }
}
```
之前学习DelayWorkQueue的时候，介绍到该队列会根据设定的时间优先级来执行任务。


## 拒绝策略
jdk还提供默认的四种拒绝策略：
1. AbortPolicy : 丢弃任务，并抛出异常
2. DiscardPolicy ： 丢弃任务，不抛出异常
3. DiscardOldestPolicy ：丢弃任务队列中最老的任务
4. CallerRunsPolicy ： 交给调用者线程去执行任务。

四种拒绝策略都不不经常使用，一般需要自己定义线程拒绝策略，比如说将无法处理的任务放入消息队列，或者是记录日志、等空闲时复原执行。
自己定义拒绝策略只需要实现`RejectedExecutionHandler`接口即可
```java
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable var1, ThreadPoolExecutor var2);
}
```



