**```ThreadPoolExecutor```  源码解读**



通过**线程池**获得

```java
ExecutorService threadPool = Executors.newFixedThreadPool(5);//一池5线程，执行长期任务，性能好
ExecutorService threadPool1 = Executors.newSingleThreadExecutor();  // 一池一线程，适应一个任务一个任务执行的场景
ExecutorService threadPool2 = Executors.newCachedThreadPool();  // 一池N线程，会根据任务调整，  适应很多短期异步的小程序或者负载较轻的服务。	

/**
上面的3种,在工作中，你用哪一个
答：
	一个都不用，自己手写线程池(使用自己自定义的)。
	1. FixedThreadPool、SingleThreadExecutor的请求队列为LinkedBlockingQueue，长度为Integer.MAX_VALUE,可能导致大量的请求，导致OOM
	2. CachedThreadPool、ScheduledThreadPool,允许创建的线程为Integer.MAX_VALUE,可能会创建大量的线程，导致OOM。
*/
```

以上三个线程池都是通过``` ThreadPoolExecutor```这个类初始化的，这类**重要**

## 参数含义

线程池的7大参数含义：

1. ```corePoolsize```：核心线程数
2. ```maximumPoolSize```：线程池最多能容纳的最大线程数
3. ```keepAliveTime```：多余的空闲线程（超过核心线程数，且当前没有任务），当空闲时间达到keepAliveTime时，多余的空闲线程会被销毁，直到只剩下corePoolsize的线程数为止。
4. ```unit```：keepAliveTime的时间单位
5. ```workQueue```：任务队列（是一个阻塞队列），存储被提交但尚未被执行的任务
6. ```threadFactory```：表示创建线程的线程工厂，   一般默认
7. ```handler```：拒绝策略，表示workQueue满了（提前 线程数达到最大线程数），执行拒绝策略（有4种）



拒绝策略：（4种），下面4种是JDK内置的拒绝策略（均实现了RejectedExecutionHandler接口）

1. AbortPolicy(默认)：直接抛出RejectExecutionException异常阻止系统正常运行。
2. CallerRunsPolicy：“调用者运行”一种调节机制，该策略不会放弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
3. DiscardOldestPolicy：抛弃队列中等待最久的任务，然后把当前任务加入队列中，再尝试提交当前任务。
4. DiscardPolicy：直接丢弃任务，没有任何处理也不抛出异常。  如果允许任务丢失的话，这是最好的方案。

如何合理配置线程池的参数：（配置最大的线程数  or  核心线程数）

```java
Runtime.getRuntime().availableProcessors(); //获取CPU核数
```

1. CPU密集型任务配置尽可能少的线程数：    一般公式 = CPU核数 + 1个线程  的线程池。

2. IO密集型：

   1. 由于IO密集型任务并不是一直在执行任务，则应配置尽可能多的线程 = CPU核数 * 2；

   2. IO密集型时，大部分线程都阻塞，故需多配置线程数：

      参考公式 ：   = CPU核数/(1 - 阻塞系数)        阻塞系数 ： 0.8 ~ 0.9

线程池的优势：  1. 线程是稀缺资源，如果无限制的创建，不仅会消耗资源还会降低系统的稳定性。

2.提高响应速度。 当任务到达时可以不需要等待线程创建就能立即执行。

3.重用已存在的线程，来减少创建和销毁的开销，能够减少cpu切换从而提高性能。





```java
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;     // 29
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;   //后29位全是1

    private static final int RUNNING    = -1 << COUNT_BITS; //1111 …… 1111 <<29 = 1110 00
    private static final int SHUTDOWN   =  0 << COUNT_BITS; // 高3位000
    private static final int STOP       =  1 << COUNT_BITS; // 高3位001
    private static final int TIDYING    =  2 << COUNT_BITS; // 高3位010
    private static final int TERMINATED =  3 << COUNT_BITS; // 高3位011

    private static int runStateOf(int c)     { return c & ~CAPACITY; } //获取运行状态
    private static int workerCountOf(int c)  { return c & CAPACITY; } //获取活动线程数
    private static int ctlOf(int rs, int wc) { return rs | wc; }//获取运行状态和活动线程数的值
```



>线程池的状态：
>
>1. RUNNING：接受新任务，并且处理阻塞队列中的任务
>2. SHUTDOWN：不接受新任务，但处理队列中的任务
>3. STOP：不接受新任务，不处理队列中的任务，    并且中断在执行的任务。
>4. TIDYING（整理状态）：当所有的任务已终止，ctl记录的”任务数量”为0，线程池会变为TIDYING状态
>5. TERMINATED：线程池彻底终止

> 状态转换：
>
> 1. RUNNING -> SHUTDOWN： 调用```shutdown()```
> 2. RUNNING or SHUTDOWN) -> STOP:   调用```shutdownNow()```
> 3. SHUTDOWN -> TIDYING:    阻塞队列为空并且线程池中执行的任务也为空时，就会由 SHUTDOWN -> TIDYING
> 4. STOP -> TIDYING：   线程池中执行的任务为空。
> 5. TIDYING -> TERMINATED：  当钩子函数 ```terminated()```执行完成

##  参数含义

> 构造函数的七大参数：
>
>
>1. ```corePoolsize```：核心线程数
>2. ```maximumPoolSize```：线程池最多能容纳的最大线程数
>3. ```keepAliveTime```：多余的空闲线程（超过核心线程数，且当前没有任务），当空闲时间达到```keepAliveTime```时，多余的空闲线程会被销毁。
>4. ```unit```：```keepAliveTime```的时间单位
>5. ```workQueue```：任务队列（是一个阻塞队列），存储被提交但尚未被执行的任务
>6. ```threadFactory```：表示创建线程的线程工厂，   一般默认为 ```Executors.defaultThreadFactory()```
>7. ```handler```：拒绝策略，表示```workQueue```满了（提前 线程数达到最大线程数），执行拒绝策略（有4种）

**注意**：核心和非核心线程其实是等价的。

**任务提交的方式：**

1. ```java
   public void execute() //提交任务无返回值
   ```

2. ```java
   public Future<?> submit() // 任务执行后有返回值
   ```

   ---





各个函数解读：

```java
public void execute(Runnable command) {
        if (command == null)                          
            throw new NullPointerException();
        int c = ctl.get();
        if (workerCountOf(c) < corePoolSize) {  //当前线程池的线程数< corePoolsize 
         //addWorker中的第二个参数表示是不是核心线程
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        //如果当前线程池是运行状态并且任务添加到队列成功
        if (isRunning(c) && workQueue.offer(command)) {
            //double check 如果不是运行状态，移除之前入队的command
            // 然后执行拒绝策略
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))    // 这里什么时候移除失败?????
                reject(command);
            //获取线程池中的线程数，如果为0，则执行addWorker方法       为什么要为0呢？？？
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
    // 这里说明 1. 线程池已经不是RUNNING状态
    // 2. 或者，线程池是RUNNING状态，但workerCount > corePoolSize并且 workQueue阻塞队列已满
    //  那么就再次调用addWorker方法（添加非核心线程），失败则执行拒绝策略
        else if (!addWorker(command, false))
            reject(command);
    }
```



```java
private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            /**  如果rs >= SHUTDOWN，则表示此时不再接收新任务.
            接着只要下面三个条件有一个 不 满足，返回false
              1.  rs == SHUTDOWN，这时表示关闭状态，不再接受新提交的任务，但却
         可以继续处理阻塞队列中已保存的任务。
              2. fistTask为空
              3. 阻塞列队不为空   不为空为什么返回false呢                  ???????????
            */
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                   firstTask == null &&
                   ! workQueue.isEmpty()))
                return false;

            for (;;) {
                int wc = workerCountOf(c);
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))   //判断工作线程是否超出
                    return false;
                // 尝试增加ctl的数量，成功则跳出第一个循环
                if (compareAndIncrementWorkerCount(c))
                    break retry;
                //也就是说这里增加失败了，重新
                c = ctl.get(); 
                //如果当前的运行状态！=rs（先前获取的，也就是期间改变了，则返回第一个for循环，继续尝试。
                if (runStateOf(c) != rs)
                    continue retry;
            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);   //fistTask封装成  Worker
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();      //上锁的目的 ????????
                try {
                    int rs = runStateOf(ctl.get());
                    /** rs<SHUTDOWN表示是RUNNING状态
            如果rs是RUNNING状态  或者rs是SHUTDOWN状态并且 firstTask为null，向线程池中添加线程。
            因为在SHUTDOWN时不会在添加新的任务，但还是会执行workQueue中的任务
                    */
                    if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                        if (t.isAlive())    //  这里为啥要检测 t的状态???
                            throw new IllegalThreadStateException();
                        workers.add(w);
                        int s = workers.size();
                        // 维护线程池中存在过的线程数的最大数量
                        if (s > largestPoolSize)
                            largestPoolSize = s;
                        workerAdded = true;   //表明成功添加一个Worker
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start();   //因为Worker实现了Runnable，所以会调用Worker的run()
                    workerStarted = true;   //表明worker启动
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }
```

---





```java
final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock(); // allow interrupts             //  允许中断??????????
        boolean completedAbruptly = true;
        try {
            //getTask()从阻塞队列中取任务
            while (task != null || (task = getTask()) != null) {
                w.lock();
                // If pool is stopping, ensure thread is interrupted;
                // if not, ensure thread is not interrupted.  This
                // requires a recheck in second case to deal with
                // shutdownNow race while clearing interrupt
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() &&
                      runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
                try {
                    beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    w.completedTasks++;
                    w.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
            processWorkerExit(w, completedAbruptly);
        }
    }
```

```Java
private Runnable getTask() {
        boolean timedOut = false; // Did the last poll() time out?

        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }

            int wc = workerCountOf(c);

            // Are workers subject to culling?
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

            if ((wc > maximumPoolSize || (timed && timedOut))
                && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }

            try {
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
       }
}
```



```Java
//用来销毁线程的
private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
            decrementWorkerCount();

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            completedTaskCount += w.completedTasks;
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }

        tryTerminate();

        int c = ctl.get();
        if (runStateLessThan(c, STOP)) {
            if (!completedAbruptly) {
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                if (min == 0 && ! workQueue.isEmpty())
                    min = 1;
                if (workerCountOf(c) >= min)
                    return; // replacement not needed
            }
            addWorker(null, false);
        }
}
```

