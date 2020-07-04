# Arrays工具类

## 常用方法汇总
- sort （**串行**排序）
- parallelSort （**并行**排序，采用多线程排序的方式， 但数组长度达到 2 ^18时（转折点）， 并行排序的时间会短）
- swap（private级别的方法....)
- binarySearch
- fill
- **asList**（最重要的）
- setAll
- stream



### asList

**注意**：这个方法返回的是Arrays的内部类ArrayList xtends AbstractList<E>    implements RandomAccess, java.io.Serializable，  但是这个内部类并没有实现 add、remove方法，如果 调用的话，会报UnsupportedOperationException。

```java
/**   将创建来的同类型参数，封装成List<T>对象返回

*/
public static <T> List<T> asList(T... a){
    return new ArrayList<>(a);
}

List<Integer> integers = Arrays.asList(1, 2, 5, 6);
integers.add(5);// 会报异常
```

---
### sort

```Java
/** a: 要排序的数组
    fromIndex: 开始下标
    toIndex：结束下标
    排序策略： 升序排序
*/
public static void sort(int[] a, int fromIndex, int toIndex)
```

---

### binarySearch

```Java
/** a:要查找的数组，并且是已经排好序的。  但并不会检查是否排好序
 	key: 要查找的数
 	return: 找到则返回下标， 找不到返回-（low + 1) 
 	如果有过个目前key的话， 返回的结果跟长度有关，因为是一碰到目标就返回了
*/  
public static int binarySearch(int[] a, int key)
```

---

### fill

```java
/** a: 要填充的数组
	fromIndex：起始下标
	toIndex：结束下标
	val： 要填充的值
*/
public static void fill(long[] a, int fromIndex, int toIndex, long val)
```

---

### setAll

```Java
/**  x: a数组的下标
	表达式a[x] * 2  表达的意思是 a[x] = a[x] * 2;
	其实这个操作就是生成的结果和下标有关系的
*/
Integer[] a = new Integer[]{1,3,100,5};
Arrays.setAll(a, x-> a[x] * 2);
for(int x : a){
       System.out.println(x);
}
```



---

### stream

待完成





