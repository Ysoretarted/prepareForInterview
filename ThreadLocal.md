ThreadLocal 简单概述

- 通过为每个线程提供一个独立的变量副本，解决了变量并发访问的冲突问题





面试问题：

https://blog.csdn.net/bjweimengshu/article/details/93147982

- 你对ThreadLoca的理解
- ThreadLocal无法解决共享对象的更新问题
- ThreadLoca引发内存泄漏



底层实现：

>Thread 类有threadLocals 这是一个 ThreadLocal.ThreadLocalMap对象。
>
>ThreadLocalMap 类里面有 Entry<T>[] table 对象。 Entry 继承了 WeakReference,  有 key ，value
>
>核心是：1. Thread.currentThread 可以获得当前调用方法的 线程t。
>
>				2. 拿出的线程不同， t.threadLocals 得到的map自然不同。   即使传进相同的ThreadLocal变量，拿出的对象和所操作的对象自然不同。

**注意：**  



核心方法：

- ThreadLocal.set()
- ThreadLocal.get();
- ThreadLocal.remove();





## set()

```Java
public void set(T value) {
        Thread t = Thread.currentThread();  
        ThreadLocalMap map = getMap(t);  // 根据当前线程， 拿到map
        if (map != null)
            // 类似hashMap 的 put
            map.set(this, value);
        else
            //传入t是为了 t.threadlocals = 赋值, vaule是为了在map上放进去
            //createMap是TheadLocal的方法， 所以下面有 <ThreadLocal, T(value) > 键值对
            // 类似HashMap的空表的初始化。
            createMap(t, value);  
    
}
```





```Java
// map.set(this, vlaue)
private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);

            for (Entry e = tab[i];
                 e != null;   // 为null 代表没有冲突
                 e = tab[i = nextIndex(i, len)]) {  //循环往右移动一格，这里可以引申出hash冲突的解决方式。   这里不像hash一样采用拉链法
                ThreadLocal<?> k = e.get();

                if (k == key) {   // 存在，覆盖旧值
                    e.value = value;
                    return;
                }

                if (k == null) {  // 这里可能是因为 弱引用被 GC了 
                    replaceStaleEntry(key, value, i);
                    return;   //跳出
                }
            }
    // 说明， 没有发生冲突， 或者中途 没有遇到k == null了（就是说节点没插进入
    // 其实这里有可能 一个table长度 都会发生冲突，  不知道这里怎么解决的， 可能是因为软引用的关系。
    // 上面不会， 还有threshold阈值起作用呢。
    
            tab[i] = new Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold)  // 这里有等于，只要清理失败（清理回收弱引用）， 只要一满就rehash()
                rehash();
}
```

```Java
// 返回离 stagleSlot 最近（往右） table[i] 是空闲的位置
private int expungeStaleEntry(int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;

            // expunge entry at staleSlot
            tab[staleSlot].value = null;
            tab[staleSlot] = null;             // 上一行有必要吗， 估计涉及GC
            size--;

            // Rehash until we encounter null
            Entry e;
            int i;
           //调整 stale节点附近（tabe[i] != null)的节点，到 本该在的点（或者遇到冲突调整一下）
            for (i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();
                if (k == null) {
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) {   // 说明该节点不在应该的位置上，  即冲突过
                        tab[i] = null;

                        // Unlike Knuth 6.4 Algorithm R, we must scan until
                        // null because multiple entries could have been stale.
                        while (tab[h] != null)
                            h = nextIndex(h, len);
                        tab[h] = e; //调成一下  冲突的点。
                    }
                }
            }
            return i;
}
```





## get()

```Java
public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
    // table为空，生成一个map， 把<this, null>放进去
        return setInitialValue();
 }
```

```Java
private T setInitialValue() {
        T value = initialValue();   // 返回null, 该方法可以被重写， protected
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)   //在此判断map是不是空
            map.set(this, value);
        else
            createMap(t, value); // 创建， 并将<this, value>放进map
        return value;
}
```



## remove()

```Java
public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);  // map的remove方法， 以this为key，删掉键值对
}
```





```Java
private void remove(ThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);  //索引
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) { // 解決冲突方法
                if (e.get() == key) {     
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
}
```

