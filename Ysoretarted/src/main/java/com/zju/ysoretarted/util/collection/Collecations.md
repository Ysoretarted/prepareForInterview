# Collections

参考资料：

[https://www.cnblogs.com/sunhaoyu/p/5909196.html]: collections常用方法以及测试样例



## 与Arrays的区别：

Arrays对针对数组的，如 T[];  而Collections是针对各种集合的，比如List<T>, Collection<T>,Queue<T>



方法列表：

- sort
- shuffle
- binarySearch
- max
- min
- replaceAll
- reverse
- rotate
- copy
- fill





## swap（疑问待解决）

<font face="黑体" color=red size=3>这里为什么要 ``` final List  l = list```;呢，  直接在list上操作会怎么样呢。</font>

```Java
public static void swap(List<?> list, int i, int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        final List l = list;     
        l.set(i, l.set(j, l.get(i)));
    }
```





## sort

- 方法一

```Java
/**    默认的排序规则  Comparable的compareTo()方法
*/
public static <T extends Comparable<? super T>> void sort(List<T> list) {
     list.sort(null);
}
```

- 方法二

```Java
/**   提供一个Compartor比较器 作为排序规则
*/
public static <T> void sort(List<T> list, Comparator<? super T> c) {
      list.sort(c);
}
```



通过源码发现，```Colletions.sort()``` 底层是通过调用 ```Arrays.sort() ```实现的



## shuffle

英文名字就是洗牌的意思，顾名思义就是随机的意思



```Java

/**  扫盲：LinkList的get(i)方法 是先判断index是否大于长度的一半来选择正序或者逆序来遍历实现的
	主要思想： 就是利用 数组的课随机读写的性质， 加上循环和rand函数，实现乱序
	首先判断数组长度  < 5(LinkList 数组太长的话，会影响get效率）,或者 是RandomAccess的实例（支持随机读写的特性)， 然后根据主要思想实现。
	否则，将List转换成数组，在实现乱序排序，最会再讲数组转化成List
*/
public static void shuffle(List<?> list, Random rnd) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i=size; i>1; i--)
                swap(list, i-1, rnd.nextInt(i));
        } else {
            Object arr[] = list.toArray();

            // Shuffle array
            for (int i=size; i>1; i--)
                swap(arr, i-1, rnd.nextInt(i));

            // Dump array back into list
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator it = list.listIterator();
            for (int i=0; i<arr.length; i++) {
                it.next();
                it.set(arr[i]);
            }
      }
 }
```



## binarySearch

首先判断是否支持随机读写 或者长度 < 5000

​	是， 采取index（下标查找方法) **这个就和普通查找方法类似**

​	不是，则采取iterator（迭代器查找方法）


```Java
public static <T>
    int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        if (list instanceof RandomAccess || list.size()<BINARYSEARCH_THRESHOLD)
            return Collections.indexedBinarySearch(list, key);
        else
            return Collections.iteratorBinarySearch(list, key);
    }
```



## replaceAll

替换批量元素为某元素,若要替换的值存在刚返回true,反之返回false

```Java
public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal)
```



```Java
if (list.get(i)==null) 
if (oldVal.equals(list.get(i)))    //这里要注意odlVal 为null or 其他时，判断相等的表达不一样
```



## rotate

这个方法的思想挺好的， 时间复杂度 O(n)

核心思想： 按着distance间距， 一个个换

nMoved：统计和已经交换过的元素的个数，  和size比较作为跳出循环的条件



```Java
private static <T> void rotate1(List<T> list, int distance) {
        int size = list.size();
        if (size == 0)
            return;
        distance = distance % size;
        if (distance < 0)
            distance += size;
        if (distance == 0)
            return;

        for (int cycleStart = 0, nMoved = 0; nMoved != size; cycleStart++) {
            T displaced = list.get(cycleStart);  // 一开始记录要交换的元素
            int i = cycleStart;
            do {
                i += distance;
                if (i >= size)
                    i -= size;
                displaced = list.set(i, displaced);  //精髓，旧元素换到新位置，并记录新位置的元素
                nMoved ++;  //交换个数+1
            } while (i != cycleStart); //判断是否到达循环的起点，避免死循环
        }
```



## copy

```Java
/** dest: 目标集合
	src: 源集合
	要求 目标集合的长度  > src 长度
*/
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        int srcSize = src.size();
        if (srcSize > dest.size())   
            throw new IndexOutOfBoundsException("Source does not fit in dest");

        if (srcSize < COPY_THRESHOLD ||
            (src instanceof RandomAccess && dest instanceof RandomAccess)) {
            for (int i=0; i<srcSize; i++)
                dest.set(i, src.get(i));
        } else {
            ListIterator<? super T> di=dest.listIterator();
            ListIterator<? extends T> si=src.listIterator();
            for (int i=0; i<srcSize; i++) {
                di.next();
                di.set(si.next());
            }
        }
    }
```

