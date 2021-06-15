# 线程池

## 基础概念

1. Callable<V> : 与Runnable一样，设计出来是为了一个线程去执行的。 但是，与Runnable不同的是Callable有返回值,且是异步的，不需要等待线程执行完得到结果。 而是线程执行完后会通知对应线程来返回结果。
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

阿里手册要求程序员在构建线程池的时候手动构建，原因是：

1. `Executors`的构造线程池方法默认的阻塞队列是`LinkedBlockingQueue` 是无界阻塞队列，不断向里面添加任务会造成内存溢出。
2. 实际使用中需要根据自己机器的性能、业务场景来手动配置线程池的参数比如核心线程数、使用的任务队列、饱和策略等等。
3. 我们应该显示地给我们的线程池命名，这样有助于我们定位问题。


- 这些线程池在阿里手册中都不建议使用【强制】，但是我们还是必须要了解一下这几个线程池，因为这会为我们提供一个思路：如果利用不同种类的线程池

手动构造线程池的几个参数:

1. int corePoolSize,核心线程数，核心线程一般来说常驻内存
2. int maximumPoolSize, 最大线程数：在核心线程不够用的情况下最多扩到多少个线程
3. long keepAliveTime, 线程存活时间：非核心线程长时间不干活了，需要将其归还给操作系统；可以理解为非核心线程的最大空闲时间
4. TimeUnit unit, 时间单位
5. BlockingQueue<Runnable> workQueue, 任务队列，阻塞队列； 如果指定的阻塞队列是SynchronousQueue,容量为0，也就意味着来一个任务立刻需要一个线程来进行处理，不然其他任务无法加入。
6. ThreadFactory threadFactory, 线程工厂；如何创建线程，需要实现ThreadFactory这个接口。阿里开发手册：【强制】创建线程或者线程池时 请指定有意义的线程名称，方便出错回溯
7. RejectedExecutionHandler handler 拒绝策略： 线程池工作流程：假设核心线程为2，最大线程为4，阻塞队列长度为5  
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
    4. CallerRuns: 调用者处理任务 实际情况下会自己定义对应的拒绝策略，实现对应的接口。一般会将过多的任务写到缓存，记录日志。想办法记录没有执行的任务，然后空闲时恢复任务继续执行。

### SingleThreadPool

线程池内只有一个线程，原因：

1. 该线程池可以保证任务是顺序执行的。
2. 线程池内维护一个任务队列，以及线程池的生命周期也会维护。不需要我们来维护  
   可以看一下线程池的创建参数：

```java
public class Executors {
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
public class Executors {
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
public class Executors {

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

1. CPU密集型：CPU数+1 获取CPU的个数可以使用`Runtime.getRuntime().availableProcessors()`
2. I/O密集型:
2. 线程个数=CPU个数× 期望CPU利用率 ×(1+线程等待时间/线程计算时间);

### ScheduledThreadPool

定时任务线程池，该线程池可以定时执行提交的任务，比较常用。但是如果定时任务比较复杂，且业务量多的时候建议使用`quartz`。看一下线程池的参数设置：

```java
public class Executors {

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

四种拒绝策略都不不经常使用，一般需要自己定义线程拒绝策略，比如说将无法处理的任务放入消息队列，或者是记录日志、等空闲时复原执行。 自己定义拒绝策略只需要实现`RejectedExecutionHandler`接口即可

```java
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable var1, ThreadPoolExecutor var2);
}
```

## 源码 + 注解

- execute：
    1. 核心线程未满->起一个核心线程执行
    2. 核心线程已满,任务队列未满->放入任务队列
    3. 任务队列已满->起一个非核心线程执行任务
    4. 非核心线程达到最大值->执行拒绝策略

- addWorker:
    1. 判断线程池状态(自旋 外循环)
    2. 判断Worker线程状态(自旋 内循环)
    3. ctl worker数量 + 1
    4. 将任务加入容器，start

```java

public class ThreadPoolExecutor extends AbstractExecutorService {
    // 1. ctl 可以看作一个int型变量，高3位表示线程池状态，低29位表示线程池内worker的数量
    private final AtomicInteger ctl;
    // Integer.size - 3 
    private static final int COUNT_BITS = 29;
    private static final int COUNT_MASK = 536870911;
    // 线程状态 RUNNING << SHUTDOWN << STOP << TIDYING << TERMINATED
    // -1<<COUNT_BITS 
    private static final int RUNNING = -536870912;
    // 0<<COUNT_BITS 
    private static final int SHUTDOWN = 0;
    // 1<<COUNT_BITS 
    private static final int STOP = 536870912;
    // 2<<COUNT_BITS 
    private static final int TIDYING = 1073741824;
    // 3<<COUNT_BITS 
    private static final int TERMINATED = 1610612736;
    private final BlockingQueue<Runnable> workQueue;
    private final ReentrantLock mainLock;
    private final HashSet<ThreadPoolExecutor.Worker> workers;
    private final Condition termination;
    private int largestPoolSize;
    private long completedTaskCount;
    private volatile ThreadFactory threadFactory;
    private volatile RejectedExecutionHandler handler;
    private volatile long keepAliveTime;
    private volatile boolean allowCoreThreadTimeOut;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();
    private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
    private static final boolean ONLY_ONE = true;

    // 任务线程执行方法
    public void execute(Runnable command) {
        if (command == null) {
            //判空
            throw new NullPointerException();
        } else {
            //拿到ctl
            int c = this.ctl.get();
            //看一下线程池内工作线程的数量是否小于核心线程
            if (workerCountOf(c) < this.corePoolSize) {
                //如果小于核心线程，则添加该核心线程，然后返回
                if (this.addWorker(command, true)) {
                    return;
                }
                //由于是多线程，在每次操作之前都要读一下ctl的值确保没有被更改
                c = this.ctl.get();
            }
            // worker的数量大于等于核心线程数，workQueue.offer 该任务进入阻塞队列。
            if (isRunning(c) && this.workQueue.offer(command)) {
                // 同样是检查是ctl是否被其他线程改变。
                int recheck = this.ctl.get();
                //如果线程池的当前状态不是RUNNING状态，说明执行过shutdown命令，需要拒绝新加进来的任务
                if (!isRunning(recheck) && this.remove(command)) {
                    // 拒绝
                    this.reject(command);
                    //  如果任务队列 的值还没有到线程的最大值，则开始创建临时线程来处理任务
                } else if (workerCountOf(recheck) == 0) {
                    this.addWorker((Runnable) null, false);
                }
                //如果添加临时线程失败： 拒绝该任务。
            } else if (!this.addWorker(command, false)) {
                this.reject(command);
            }

        }
    }

    /**
     * 添加任务线程
     * 1. 往一个容器内增加线程，涉及到多线程情况，需要考虑线程安全
     * 2. 线程池追求效率，因此要避免使用Synchronized，要么使用自旋，要么使用CAS
     *
     * @param firstTask
     * @param core  是否是核心线程
     * @return
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
        int c = this.ctl.get();

        label247:
        // 外层循环 判断状态：线程池是否是shutdown、stop状态或者是头节点为null、任务队列已满 
        while (!runStateAtLeast(c, 0) || !runStateAtLeast(c, 536870912) && firstTask == null && !this.workQueue.isEmpty()) {
            // 内层循环 判断：
            //  worker数量超过容量: 是否是核心线程 ? 核心线程数 : 最大线程数
            while (workerCountOf(c) < ((core ? this.corePoolSize : this.maximumPoolSize) & 536870911)) {
                // 使用CAS操作修改ctl，如果修改成功了 即 worker数量+1
                if (this.compareAndIncrementWorkerCount(c)) {
                    boolean workerStarted = false;
                    boolean workerAdded = false;
                    ThreadPoolExecutor.Worker w = null;
                    // 下面才是将work线程放入放入线程池执行的逻辑
                    try {
                        //  创建一个worker的引用，将要执行的任务 <? extends Runnable> 
                        w = new ThreadPoolExecutor.Worker(firstTask);
                        // GUESS: 这里可能是看有没有工作线程来执行该任务线程？
                        Thread t = w.thread;
                        if (t != null) {
                            ReentrantLock mainLock = this.mainLock;
                            // 加锁
                            mainLock.lock();

                            try {
                                // 获取ctl
                                int c = this.ctl.get();
                                // 检查ctl的状态 
                                if (isRunning(c) || runStateLessThan(c, 536870912) && firstTask == null) {
                                    if (t.getState() != State.NEW) {
                                        throw new IllegalThreadStateException();
                                    }
                                    // 将该任务线程加入容器
                                    this.workers.add(w);
                                    workerAdded = true;
                                    int s = this.workers.size();
                                    // 修改线程池数量
                                    if (s > this.largestPoolSize) {
                                        this.largestPoolSize = s;
                                    }
                                }
                            } finally {
                                mainLock.unlock();
                            }

                            if (workerAdded) {
                                // 如果任务线程添加成功，则start开始执行
                                t.start();
                                workerStarted = true;
                            }
                        }
                    } finally {
                        if (!workerStarted) {
                            this.addWorkerFailed(w);
                        }
                    }
                    return workerStarted;
                }
                // 如果跳出内外层循环，则重新获取ctl的状态，跳回开头
                c = this.ctl.get();
                if (runStateAtLeast(c, 0)) {
                    continue label247;
                }
            }

            return false;
        }

        return false;
    }

    /**
     * 任务执行方法
     * @param w
     */
    final void runWorker(ThreadPoolExecutor.Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        // 先解锁
        w.unlock();
        boolean completedAbruptly = true;

        try {
            while (task != null || (task = this.getTask()) != null) {
                w.lock();
                if ((runStateAtLeast(this.ctl.get(), 536870912) || Thread.interrupted() && runStateAtLeast(this.ctl.get(), 536870912)) && !wt.isInterrupted()) {
                    wt.interrupt();
                }

                try {
                    this.beforeExecute(wt, task);

                    try {
                        task.run();
                        this.afterExecute(task, (Throwable) null);
                    } catch (Throwable var14) {
                        this.afterExecute(task, var14);
                        throw var14;
                    }
                } finally {
                    task = null;
                    ++w.completedTasks;
                    w.unlock();
                }
            }

            completedAbruptly = false;
        } finally {
            this.processWorkerExit(w, completedAbruptly);
        }

    }

}
```

看一下Worker这个内部类： 在线程池内部用于执行任务的线程类

```java
/**
 * 继承了AbstractQueueSynchronizer 说明使用Worker本身可以加锁，
 * 实现了Runnable接口 说明Worker可以被线程执行 用Runnable重新包装一下，这样可以用来记录一些数值
 */
private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
    private static final long serialVersionUID = 6138294804551838833L;
    // 这个成员变量记录是哪个线程正在执行该Worker对象
    final Thread thread;
    // 记录需要执行的任务
    Runnable firstTask;
    // 记录完成过的任务数量
    volatile long completedTasks;

    Worker(Runnable firstTask) {
        this.setState(-1);
        this.firstTask = firstTask;
        this.thread = ThreadPoolExecutor.this.getThreadFactory().newThread(this);
    }

    public void run() {
        ThreadPoolExecutor.this.runWorker(this);
    }

    protected boolean isHeldExclusively() {
        return this.getState() != 0;
    }

    protected boolean tryAcquire(int unused) {
        if (this.compareAndSetState(0, 1)) {
            this.setExclusiveOwnerThread(Thread.currentThread());
            return true;
        } else {
            return false;
        }
    }

    protected boolean tryRelease(int unused) {
        this.setExclusiveOwnerThread((Thread) null);
        this.setState(0);
        return true;
    }

    public void lock() {
        this.acquire(1);
    }

    public boolean tryLock() {
        return this.tryAcquire(1);
    }

    public void unlock() {
        this.release(1);
    }

    public boolean isLocked() {
        return this.isHeldExclusively();
    }

    void interruptIfStarted() {
        Thread t;
        if (this.getState() >= 0 && (t = this.thread) != null && !t.isInterrupted()) {
            try {
                t.interrupt();
            } catch (SecurityException var3) {
            }
        }

    }
}
```

## ForkJoinPool

普通线程池是一个任务队列，多个Worker线程共享。 WorkStealingPool：每个线程独占一个任务队列，如果自己的队列中没有任务了，回去其他线程的队列中 “拿“ 一个
WorkStealingPool是ForkJoinPool的一种对象
 Fork + Join  
* Fork 将大的任务分解
* Join 将小的任务汇总

注：
* ForkJoinPool 最适合的是计算密集型的任务，如果存在 I/O，线程间同步，sleep() 等会造成线程长时间阻塞的情况时，最好配合使用 ManagedBlocker。
* ForkJoinPool 与任务量的规模好像有关系，由于会不断分解任务，小的任务又交给其他线程来执行，可能会造成 Stack Overflow 栈溢出

 两个主要方法
* fork()方法类似于线程的Thread.start()方法，但是它不是真的启动一个线程，而是将任务放入到工作队列中。
* join()方法类似于线程的Thread.join()方法，但是它不是简单地阻塞线程，而是利用工作线程运行其它任务。当一个工作线程中调用了join()方法，它将处理其它任务，直到注意到目标子任务已经完成了。