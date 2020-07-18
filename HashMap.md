**HashMap**的源码解读

https://www.bilibili.com/video/av75133052?p=1

# <center> 1.7 实现：  数组 + 链表</center> 

## 各种函数解读：

```java
//这个是Integer类的方法，   得到一个比i第一个小的2^n的数
public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
}
```

```java
// number - 1 是为了防止像8这种刚刚是2^n的情况出现，然后返回16
//  返回第一个比number大或等于的(2^n的数)
private static int roundUpToPowerOf2(int number) {
        // assert number >= 0 : "number must be non-negative";
        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
}
```

```java
// 一开始扩充表的时候，传进来的其实是threshold，这个其实是刚开始构造函数的initialCapacity
// 这里有对 threshold阈值进行赋值， 这里初始化的时候table的长度已经是2^n了
private void inflateTable(int toSize) {
        // Find a power of 2 >= toSize
        int capacity = roundUpToPowerOf2(toSize);
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        table = new Entry[capacity];
        initHashSeedAsNeeded(capacity);
}
```

```java
// 判断当前HashMap所存的键值对数是不是等于大于阈值，并且新的键值对要插入的地方有值了（发生冲突），则进行扩容，然后更新要插入的数组下标
// 否则直接用头插法插入该键值对
void addEntry(int hash, K key, V value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
 }
```

```java
// 当表的长度已经达到MAXIMUM_CAPACITY时，便不再进行 扩容 操作，只更新阈值为整数最大值
// 否则开辟一个 2*当前表的length的空间，进行转移操作
void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
}
```

```java
//   因为是采用头插法，因此在新的table的的链表是跟原先比的逆序的(部分逆序，下标被重新indexFor了)
// newInd = oldInd 或者 oldInd + (扩容前的数组长度)
void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {                                 //遍历数组
            while(null != e) {                                       //遍历链表
                Entry<K,V> next = e.next;
                if (rehash) {                                        // 通常情况都是false
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }
```

```java
//根据hash值，找所对应的的数组下标，用&代替%效率更高， h&(length - 1)实现%前期length为2^n， 这也就说明了  length为啥一定要为2^n
static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length-1);
}
```

## 流程

1. 初始化：
   ```java
    threshold = initialCapacity;// 一开始并不是 capacity * loadFactor
   ```

2. put操作 :   如果表是空的（并不是判断是否为null，而是判断是不是**空表**``` table == EMPTY_TABLE```），则进行扩充表。        如果key为null，则进行``` putForNullKey(value) ``` 操作（key为null的始终对应下标为0）。  然后判断要插的key是不是重复的，重复则新值覆盖，旧值返回。  否则用头插法插入该键值对```addEntry(hash, key, value, i)```  ，返回null（这里有点小特别）

3. get操作 ： ①如果```key```为null， 如果```size```为0返回```null```，否则在```table[0]```的链表上遍历找```key```为```null```的，找到返回，找不到返回```null```。

   ​					②```key```不为```null```, 如果```size```为0返回```null```，则遍历table[i]上的链表，进行判断```if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))```,    ```key!=null```是为了防止在```table[0]```上遍历。找到返回，否则返回```null```。

## 问题：

1.  为啥用链表实现：哈希值冲突的时候，可以存多个元素
2. 为什么初始化的容量要是 2^n:  因为在存值算下标indexFor()的时候是 hash & （length - 1 ），实现取模的效果。
3. 插入方法：头插法（效率方便考虑，因为是链表）
4. ```key```可以为```null```
5. 为什么需要扩容：当数组长度一定时，存的元素元多，数组上面的链表长度就有可能很长，这样会影响get效率。
6. **什么时候扩容**： 当要进行一次put操作的时候，当前HashMp的size大于 等于threshold，并且发生了冲突（算出来的数组下标已经存过值了）

**扩容**：1. 不重新hash的情况下，  原本一条链表上的元素的顺序会被倒置(但没啥影响)

**多线程扩容的情况下可能倒置循环链表的出现，倒置```cpu```资源耗尽**







# <center>1.8实现：数组 +链表+红黑树</center>

为啥要引进红黑树： 当链表过长的时候会影响查询效率，故将链表转成红黑树







```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0) //数组为null或者为空，则进行初始化
            n = (tab = resize()).length;                   
        if ((p = tab[i = (n - 1) & hash]) == null)   //table[indx]为null，则创建一个，并赋值
            tab[i] = newNode(hash, key, value, null);    //第四个参数为null，因为是最后一个节点
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&   //判断table[ind]上链表的第一个点的key是不是和重复，重复则返回
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode) //判断table[ind] 上是链表还是树
                // 按照红黑树的插法插入
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {             //p是table[ind]上的第一个节点，且已经保证不为null
                for (int binCount = 0; ; ++binCount) {    //binCount用来判断链表上的节点数
                    if ((e = p.next) == null) {         //遍历到链表的最后一个节点
                        p.next = newNode(hash, key, value, null);  //插入
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st TREEIFY_THRESHOLD = 8
                            treeifyBin(tab, hash);  //数目大于阈值，进行数化
                        break;
                    }
                    // 当前遍历的节点的key和要插入的key一样，则跳出循环 
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key     // 对重复的key进行的操作
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null) //根据onlyIfAbsent来确定要不要覆盖旧值
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
```

```java
//将Node链表转化成TreeNode链表
final void treeifyBin(Node<K,V>[] tab, int hash) {//hash:(n-1)&hash算出要进行树化的数组下标
        int n, index; Node<K,V> e;
    // 数组为null 或者数组的长度小于 64，就不树化， 而进行resize()
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {//当table[index]上有节点才树化
            TreeNode<K,V> hd = null, tl = null;   // head记录头节点,t1记录尾节点
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null); //Node<K,V> 转化成TreeNode
                if (tl == null)               //下面其实就是将Node链表转化成TreeNode链表 
                    hd = p;              
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;                      //因为replacementTreeNode时 next就为null
            } while ((e = e.next) != null);     //因此跳出循环后就不用再给尾节点的next赋为null了
            if ((tab[index] = hd) != null)    //将新生成的链表的头节点赋值给 table[inx]
                hd.treeify(tab);            //进行调整，调整成红黑树
        }
}
```

```java
//  树化操作
//红黑树上的节点调用该方法，TreeNode继承了LinkedHashMap.Entry<K<V>，从而继承了HashMap.Node<K,V>
final void treeify(Node<K,V>[] tab) {
            TreeNode<K,V> root = null;
            for (TreeNode<K,V> x = this, next; x != null; x = next) { //遍历table[ind]上的TreeNode的链表
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;       //插入的地方都是叶子节点故  左右子节点均为null
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                }
                else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K,V> p = root;;) {   //遍历红黑树，给了root根节点
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)  //判断待插入的节点的hash和当前节点hash值的大小
                            dir = -1;           //从而判断查找方向
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                  (kc = comparableClassFor(k)) == null) ||
                                 (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K,V> xp = p;       //xp 当前层的节点
                        // 找到尽头了（找到叶子节点了），插入
                        if ((p = (dir <= 0) ? p.left : p.right) == null) { //p下一层的节点
                            x.parent = xp;             //上下层建立联系
                            if (dir <= 0)        
                                xp.left = x;
                            else
                                xp.right = x;
                            root = balanceInsertion(root, x);   //调整树形，来平衡
                            break;
                        }
                    }
                }
            }
            moveRootToFront(tab, root);     //使table[ind] 为root节点
        }
```

```java
//调整数型来平衡的 root更节点，   x：刚插入的节点
static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
                                                    TreeNode<K,V> x) {
            x.red = true;                                //将新插入的节点标记为红色
            for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
                if ((xp = x.parent) == null) { //如果 X 是根结点(root)，则标记为黑色
                    x.red = false;
                    return x;
                }        //xp:x的父亲节点    xxp:x的父亲的父亲节点
                else if (!xp.red || (xpp = xp.parent) == null)
                    return root;
                if (xp == (xppl = xpp.left)) {    //如果父亲节点为左节点  ， 第一个左
                    if ((xppr = xpp.right) != null && xppr.red) {   //uncle颜色为红色
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {                          //uncle颜色为黑色
                        if (x == xp.right) {                 //左右情况
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }                                 //左左情况
                        if (xp != null) {   
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                }
                else {                  //父亲节点是右节点
                    if (xppl != null && xppl.red) {       //uncle为红
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.left) {          //右左情况
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }                  //右右情况	
                        if (xp != null) {            
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }
```



```java
   //在红黑树上插值，TreeNode<K,V>的方法，   h：要插入的键值对的hash值
final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                                       int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K,V> root = (parent != null) ? root() : this;    //获取红黑树的根节点
            for (TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                if ((ph = p.hash) > h)       //根据hash值的大小，判断往左还是往右查找
                    dir = -1;
                else if (ph < h)
                    dir = 1;         
                //  剩下的其实就是hash相等的情况
                //   如果key和要插入的k相等
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;                     //节点重复，返回冲突节点
                else if ((kc == null &&                             //这里是key不相等的情况
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null &&
                             (q = ch.find(h, k, kc)) != null) ||
                            ((ch = p.right) != null &&
                             (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {   //这里在判断的时候改变了p，促进循环。     如果为true的话，说明已经是叶子节点了，要进行插值了
                    Node<K,V> xpn = xp.next;
                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)             //判断在左节点还是右节点插入
                        xp.left = x;
                    else
                        xp.right = x;·
                    xp.next = x;                      //这里为什么还要对next和pre进行赋值呢
                    x.parent = x.prev = xp;      //建立父子节点的关系
                    if (xpn != null)
                        ((TreeNode<K,V>)xpn).prev = x;
                    // 这里有两个操作，  1. 在树上插值之后要进行维护，调整平衡
                    // 将root赋值给table[ind], ind可以根据root的hash算出来
                    moveRootToFront(tab, balanceInsertion(root, x)); //这里有两个操作！！
                    return null;
                }
            }
 }
```

```java
final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {              
            if (oldCap >= MAXIMUM_CAPACITY) {//数组长度>=2^30次方时不扩容，把阈值设为int的最大值
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {   //遍历table[i]
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)         // 只有一个节点的话，直接copy，因为节点类型不确定，所以在红黑树上的操作还是 维护了  双向链表的联系。                            
                        newTab[e.hash & (newCap - 1)] = e; 
                    else if (e instanceof TreeNode)    //否则，如果是红黑树的节点的话，树分离法
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;   //lower,higher,一条链表分成两个链表（单向链表）？？？ 为啥单向
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {   //因为oldCap只有一位为1
                                if (loTail == null)    //尾部为null，说明没有头节点
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;       //更新尾节点
                            }
                            else {
                                if (hiTail == null)   //同理
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;     //截断
                            newTab[j] = loHead;    
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;   // ind + oldcap
                        }
                    }
                }
            }
        }
        return newTab;
    }
```

```java
final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
            TreeNode<K,V> b = this;         //this: TreeNode<K,V>,等于table[ind]上的头节点
            // Relink into lo and hi lists, preserving order
            TreeNode<K,V> loHead = null, loTail = null;
            TreeNode<K,V> hiHead = null, hiTail = null;     //拆分成两条双向链表
            int lc = 0, hc = 0;
            for (TreeNode<K,V> e = b, next; e != null; e = next) {   
                next = (TreeNode<K,V>)e.next;
                e.next = null;
                if ((e.hash & bit) == 0) {          //  一条链表
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;                     //记链表上的节点数，看要不要树化，或者非树化
                }
                else {
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);     //非树化
                else {
                    tab[index] = loHead;
                    if (hiHead != null) // (else is already treeified)优化的地方，说明只有一条链，元素没少
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);    //bit:oldCap,偏移量
                else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)      //同理
                        hiHead.treeify(tab);
                }
            }
        }
```



parent和pre的区别：

这两个都是TreeNode的属性， parent主要是在红黑树有用，  pre是将Node链表改成TreeNode双向链表起作用







put过程：

1. 判断表是不是null或者长度为0，   然后进行resize初始化

2. >1. 获得桶的位置， 如果桶为null（就是判断桶有没有元素）。  没有进行插入。 
   >2. 否则， 判断是什么类型， TreeNode 还是 Node
   >   1. TreeNode 则进行红黑树的插入
   >   2. Node， 进行链表的插入方法。  查完之后要判断链表上的元素是不是大于等于8个。     大于就要进行树化。  先treeifyBin操作将Node链表转换成TreeNode双向链表。  再进行treeify将 TreeNode双向链表转化成  红黑树。  再讲table[i] = root（红黑树的根）





resize（）过程：

1. 如果桶上是Node链表，  进行一般操作（因为链表元素个数 <=7),扩容过程，元素个数只会减少
2. 如果是红黑树（树上的元素个数不确定）， rehash之后，个数还是超过了 （6. 8 界限）。所以要进行相应的操作。

>1. 进行split操作 (此时这里只用到链表有关的属性， pre next)（可以想象成还是一个链表）。  先拆成两条双向链表。 
>2. 分别判断他们的长度。  <=6 进行untreeify操作，弄成Node单向链表。   否则（>6)，进行treeif要操作，搞成红黑树。
>
>