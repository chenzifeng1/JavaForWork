# AQS深度解析

1. state
2. 双向等待链表
3. varHandle : 普通属性原子性操作 2. 效率比反射要高，直接操作二进制码

## ReentrantLock.lock()

可重入锁源码：

1. `reentrantLock.lock();`
   ReentrantLock的lock 方法调用了 `Sync.aquire(1)`
   ```java
    public class ReentrantLock implements Lock, Serializable{
        public void lock() {
            this.sync.acquire(1);
        }
    }
   ```
2. Sync是ReentrantLock的一个内部类，继承了AbstractQueuedSynchronizer类 （AQS）
   `aquire(1)`方法实际上是调用父类`AbstractQueueSynchronizer`的`aquire()`
    ```java
    public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
       
        public final void acquire(int arg) {
            if (!this.tryAcquire(arg) && this.acquireQueued(this.addWaiter(AbstractQueuedSynchronizer.Node.EXCLUSIVE), arg)) {
                selfInterrupt();
            }
        }
       protected boolean tryAcquire(int arg) {
            throw new UnsupportedOperationException();  
        }
    }
    ```
   这个方法主要进行了三个操作：
    1. 尝试获得锁 `tryAcquire()`
    2. 如果获取不到锁就将该线程加入等待队列 `addWaiter()`
    3. 加入等待队列之后，让队列中的线程获取锁 `acquireQueued()`

3. `tryAcquire()`方法在`AbstractQueuedSynchronizer`类的实现就是抛出一个异常，但是实际运行时我们调用的是
   `AbstractQueuedSynchronizer`的子类`FairSync`/`NonfairSync`实现的`tryAquire()` 这两个类是在`ReentrantLock`的内部类，
   两者的区别就是一个是公平锁，一个是非公平锁。 我们先看一下非公平锁的实现：
   ```java
   static final class NonfairSync extends ReentrantLock.Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        NonfairSync() {}

        protected final boolean tryAcquire(int acquires) {
            return this.nonfairTryAcquire(acquires);
        }
    }
   ```
   而`nofairTryAcquire(acquires)`又是父类`Sync`中的方法，这样我们就看一下这个方法实现：
   ```java
   abstract static class Sync extends AbstractQueuedSynchronizer {
        @ReservedStackAccess
        final boolean nonfairTryAcquire(int acquires) {
            Thread current = Thread.currentThread();
            int c = this.getState();
            if (c == 0) {
                if (this.compareAndSetState(0, acquires)) {
                    this.setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == this.getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }

                this.setState(nextc);
                return true;
            }
            return false;
        }
   }
   ```
   这个方法逻辑如下：
    1. 获取到当前线程
    2. 获取state,这个state是AQS中一个很关键的属性，不同的子类继承`AbstractQueuedSynchronizer`时，写入的state是不一样的。
       这个state可以看作允许获得锁的个数。比如ReentrantLock是排他锁，那么state就是1，表示只允许一个线程持有锁。
    3. 如果c为0，表示当前没有线程持有锁，那么当前线程就通过一个CAS操作把state设为1，表示获取这把锁了。如果操作成功，就进行
       `setExclusiveOwnerThread(current)` 表示把当前线程设置为获取锁的线程（专有所有者）。 如果当前线程就是`exclusiveOwnerThread`
       ,那么就把state做+1操作，这个就是可重入锁，自己拥有锁时仍可以继续获得锁。

4. 如果state既不等于0，而当前线程又不是`exclusiveOwnerThread`,也就意味着该线程暂时拿不到锁，所以把它放入阻塞队列。 `addWaiter`：
    ```java
      public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable{
         
       private AbstractQueuedSynchronizer.Node addWaiter(AbstractQueuedSynchronizer.Node mode) {
            AbstractQueuedSynchronizer.Node node = new AbstractQueuedSynchronizer.Node(mode);
      
            AbstractQueuedSynchronizer.Node oldTail;
            do {
               while(true) {
                  oldTail = this.tail;
                  if (oldTail != null) {
                     node.setPrevRelaxed(oldTail);
                     break;
                  }
      
                  this.initializeSyncQueue();
               }
            } while(!this.compareAndSetTail(oldTail, node));
      
            oldTail.next = node;
            return node;
         }
      }
   ```
   这个方法就是将当前线程加到等待队列的尾部： 这里有一个双重循环，内循环是判断tail是否为空，如果为空则调用`initializeSyncQueue`,
   这个方法是进行一个CAS操作，将当前节点从Null变为一个虚拟队列的节点。这个方法成功执行之后下一次循环就会跳出内层循环。 外层循环的主要操作就是通过CAS操作把尾部设为新的节点。

5. 当新的节点已经再尾部时，就会执行`acquireQueued`。
   ```java
   public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
        final boolean acquireQueued(AbstractQueuedSynchronizer.Node node, int arg) {
           boolean interrupted = false;
   
           try {
               while(true) {
                    //这里是获取p的前置节点
                   AbstractQueuedSynchronizer.Node p = node.predecessor();
                   if (p == this.head && this.tryAcquire(arg)) {
                       this.setHead(node);
                       p.next = null;
                       return interrupted;
                   }
   
                   if (shouldParkAfterFailedAcquire(p, node)) {
                       interrupted |= this.parkAndCheckInterrupt();
                   }
               }
           } catch (Throwable var5) {
               this.cancelAcquire(node);
               if (interrupted) {
                   selfInterrupt();
               }
   
               throw var5;
           }
      }
   }
   ```
   这端代码比较明了，就是从node开始不断获取前置节点p，如果前置节点p是头节点那么就尝试获得锁（tryAcquire会根据是否为公平锁来进行竞争），如果p成功获得了锁，那么就返回。

   通过ReentrantLock加锁的代码可以得出AQS的无锁操作是如何实现线程安全的。

## ReentrantLock.unlock()

```java
   public class ReentrantLock implements Lock, Serializable {
    public void unlock() {
        this.sync.release(1);
    }

}
```

1. 与lock一样，unlock调用的sync.release(1)也是Sync父类AbstractQueuedSynchronized的方法.
   ```java
   public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
        
        public final boolean release(int arg) {
           if (this.tryRelease(arg)) {
               AbstractQueuedSynchronizer.Node h = this.head;
               if (h != null && h.waitStatus != 0) {
                   this.unparkSuccessor(h);
               }
   
               return true;
           } else {
               return false;
           }
        }
   }
   ```
   我们主要看看`tryRelease`方法的逻辑，这个方法是`AbstractQueuedSynchronizer`调用子类的重写方法。 既然我们是看的`ReentrantLock`的`unlock`
   方法,那么自然是ReentrantLock的实现
   ```java
   public class ReentrantLock implements Lock, Serializable{
        @ReservedStackAccess
        protected final boolean tryRelease(int releases) {
            int c = this.getState() - releases;
            if (Thread.currentThread() != this.getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            } else {
                boolean free = false;
                if (c == 0) {
                    free = true;
                    this.setExclusiveOwnerThread((Thread)null);
                }

                this.setState(c);
                return free;
            }
        }
    }
   ```
   既然是释放锁，那么肯定要把state的值做减操作，同时能够释放锁的必然是拥有锁的`exclusiveOwnerThread`类，否则就出现异常了。
   释放锁过程很简单：1. 将`exclusiveOwnerThread`设为null 2. 将state设为减去releases的值。为何在tryReleases的时候不需要使用CAS操作,
   猜测因为没必要，在state!=0时，其他线程也无法访问到exclusiveOwnerThread，这些变量是线程安全的。
   

      
   