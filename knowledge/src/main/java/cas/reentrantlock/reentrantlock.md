# ReentrantLock

可重入锁

ReentrantLock底层实现是AQS，AQS是一个抽象类，其中许多抽象方法要子类来实现的。

在ReentrantLock中，使用了一个内部类`Sync`来实现了`AbstractQueuedSynchronizer`

```java
    abstract static class Sync extends AbstractQueuedSynchronizer {
    private static final long serialVersionUID = -5179523762034025860L;

    /**
     * 这里的lock方法是给子类提供的实现非公平锁的接口
     */
    abstract void lock();

    /**
     * 这个是非公平的tryLock方法的实现，acquires在tryLock调用时，传递的是1，这就意味着
     * 如果锁对象被当前线程成功获取之后，会将state使用CAS的方式改成1
     * 同样，如果当前线程已经获得了这把锁了，那么本次获得锁就是一个重入的过程，会将state+=acquires
     * 这里设置就不需要使用CAS操作了，因为这里本来就只有持有锁的线程才能到达
     */
    final boolean nonfairTryAcquire(int acquires) {
        // 获取当前线程
        final Thread current = Thread.currentThread();
        // 获取state
        int c = getState();
        // 看看该锁对象是不是没有被获取过
        if (c == 0) {
            // 如果当前锁没有被获取过，使用CAS的方式来设置state
            // 如果设置成功了，说明则获取了到了该锁，如果在设置的时候失败了，可能是其他线程抢先获得了这把锁
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        // 如果锁对象已被线程持有，这里判断一下是不是本线程持有的，如果是就允许重入
        else if (current == getExclusiveOwnerThread()) {
            // 这部代码都只会是独占锁的线程来执行的，不会产生线程安全的问题
            int nextc = c + acquires;
            // overflow 应该是防止超过int的最大值，导致溢出了
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }

    /**
     * 尝试释放锁
     * @param releases
     * @return
     */
    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        // 如果当前线程不是锁的独占线程的话，没有资格来释放锁，抛出异常
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
        // 看看如果将state-releases是不是0，如果是0说明该锁被释放了，其他线程可以来获取了
        // 同理，这里的操作也只能是当前独占锁的线程进行，不会产生线程安全问题
        if (c == 0) {
            free = true;
            // 将独占线程设为null
            setExclusiveOwnerThread(null);
        }
        // 设置state
        setState(c);
        return free;
    }

    /**
     * 判断当前线程是不是独占线程
     * @return
     */
    protected final boolean isHeldExclusively() {
        return getExclusiveOwnerThread() == Thread.currentThread();
    }

    /**
     * 条件对象，这里就和Condition联系起来了
     * @return
     */
    final ConditionObject newCondition() {
        return new ConditionObject();
    }

    // 还有一些方法暂时不介绍了
}

```

这个重写了AbstractQueuedSynchronizer的Sync类其实是一个抽象父类，留出了lock接口供子类来实现了，ReentrantLock既可以使用公平锁，
也可以使用非公平锁，就是有两个子类继承了这个Sync类。我们可以看一下这两个子类的如何实现了公平锁与非公平锁

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    /**
     * Performs lock.  Try immediate barge, backing up to normal
     * acquire on failure.
     */
    final void lock() {
        // 非公平锁在进行lock的时候，先使用CAS操作判断一下能不能加锁，可以的话，state设1，独占线程设为当前线程
        // 这里获取不到锁 是有线程持有了锁，但是需要判断一下是不是当前线程持有了这把锁 这个在acquire中进行判断
        if (compareAndSetState(0, 1))
            setExclusiveOwnerThread(Thread.currentThread());
        else
        // 如果CAS设置state失败的话，就调用acquire方法来尝试 可能是本线程持有了该锁，这样的话需要重入测试
        // 注意这里的参数是1
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }

    /**
     * 这个nonfairTryAcquire是ReentrantLock的方法，这里放在这里只不过是为了方便查看而已
     * @param acquires
     * @return
     */
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        } else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}

/**
 * Sync object for fair locks
 */
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;

    final void lock() {
        acquire(1);
    }

    /**
     * Fair version of tryAcquire.  Don't grant access unless
     * recursive call or no waiters or is first.
     */
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        } else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```

公平锁与非公平锁的实现大概差别就在于 公平锁有个`hasQueuedPredecessors`的过程，先判断等待队列中，是不是有比当前线程更靠前的节点。
如果存在的话，那就看一下当前线程是不是锁的独占线程，是的话重入一下。不是的话，加锁失败。如果队列中不存在比当前线程更靠前的节点了。
那就直接尝试获取锁：对state利用CAS的方式+1,然后根据



