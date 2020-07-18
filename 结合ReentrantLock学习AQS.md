AQS大致思想

1. 有个原子变量 state， 记录锁的状态
2. 有Node类型的head， tail
3. Node内部类，有pre，next，thread， 大致就是能组成双向链表 大致就是想要阻塞的**线程**封装一下， 然后进入等待队里





## Node

```java
//共享锁
static final Node SHARED = new Node();
//独占锁
static final Node EXCLUSIVE = null;

// 一下是waitStatus的值，  默认0
 /** waitStatus value to indicate thread has cancelled */
static final int CANCELLED =  1;
// 线程需要被唤醒
static final int SIGNAL    = -1;
// 线程在等待某个条件
static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
static final int PROPAGATE = -3;
```





hasQueuedPredecessors()

```Java
/**当前队列中有阻塞队列 ， 返回true
当前线程是队列的队首或者 队列为为空， 返回false
*/
public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
}
```



## acquire(int arg)

```Java
public final void acquire(int arg) {
        if (!tryAcquire(arg) &&   //获取锁不成功
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) // 并且成功加入队列
            selfInterrupt();  //阻塞
}
```



## tryAcquire(int acquires)

```Java
/** return: 获得锁-> true
            未获得-> false
*/
protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {   //未上锁
                if (!hasQueuedPredecessors() &&   //判断队列为空
                    compareAndSetState(0, acquires)) {// cas更改锁的状态
                    setExclusiveOwnerThread(current);// 标记当前线程为持有锁的线程
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) { // 可重入
                int nextc = c + acquires;  //int 类型溢出
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc); //这里是自己持有锁，不用cas
                return true;
            }
            return false;
}
```






```Java
// 返回锁的状态，  是不是自由状态
public final boolean release(int arg) {
        if (tryRelease(arg)) {  //锁自由了， 要进行唤醒操作
            Node h = head;
            if (h != null && h.waitStatus != 0)  // 队列不为空，并且 waitStatus是干嘛的???
                unparkSuccessor(h);
            return true;
        }
        return false;
}
```





```Java
protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;       //标记可重入锁，是不是自由了（无锁状态）
            if (c == 0) {
                free = true;         // 无锁状态
                setExclusiveOwnerThread(null); // 清除持有锁的线程
            }
            setState(c); 
            return free;  //返回锁的状态
}
```







```Java
private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode); 
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail; //pred: 之前
        if (pred != null) {  //有队列
            node.prev = pred;    //维护节点的关系， 这里为什么不放在下面更改呢
            if (compareAndSetTail(pred, node)) {// cas,更改尾节点(其实已经改了tail的引用)
                pred.next = node; // 维护
                return node;
            }
        }
        enq(node);
        return node;
}
```

```Java
/*1. 队列为空， 先初始化一个 thread为null的Node节点（哨兵）。 并改变头尾节点， 再执行传进来的Node的入队操作。
  2. 队列不为空， 直接入队 
*/
private Node enq(final Node node) {
        for (;;) {  //cas 循环
            Node t = tail;   //下面又进行了队列为空的判断，   并发情况???
            if (t == null) { // Must initialize    队列为空，就出初始化队列
                if (compareAndSetHead(new Node()))  // 设置头尾节点
                    tail = head;
            } else {                         // 和 addWaiter的部分操作一样
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
}
```





```Java
final boolean acquireQueued(final Node node, int arg) { // 这里会有两次自旋去获取锁
        boolean failed = true;  // failed: 标记 获得锁失败
        try {
            boolean interrupted = false;  // ??? 标记是否被打断???
            for (;;) {  // 注意有循环
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {   //如何node是队头（不含哨兵） ，并且当前线程获得锁成功
                    setHead(node);   //重要！！！！这里把node的thread置为null
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
}
```





```Java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;   //waitStaus的作用???
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
}
```





## 释放锁的过程

```Java
public final boolean release(int arg) {
        if (tryRelease(arg)) {  //返回释放锁的结果，成功：true。  并设置state的值
            Node h = head;
            if (h != null && h.waitStatus != 0)  //队列不为空
                unparkSuccessor(h);    //在队列中进行唤醒操作
            return true;     //成功释放锁
        }
        return false;  //没释放锁
}
```



```Java
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())  //判断当前线程是不是持有该锁
        throw new IllegalMonitorStateException();
    boolean free = false;  //释放成功的标志
    if (c == 0) {       // 因为可重入，c还是有可能 > 0 的
        free = true;
        setExclusiveOwnerThread(null);   
    }
    setState(c);
    return free;   //返回锁的状态
}
```



```Java
private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {  //这是什么情况??
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);   //唤醒
}
```

