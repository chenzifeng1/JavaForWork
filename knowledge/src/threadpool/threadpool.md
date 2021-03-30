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