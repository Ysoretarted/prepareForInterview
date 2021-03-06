## 一条查询语句是如何执行的

MySQL 的逻辑架构图，如图所示

<img src="C:\Users\86177\Desktop\秋招准备\prepareForInterview\Ysoretarted\图片\MySQL 的逻辑架构图.png" style="zoom:50%;" />

### 查询缓存

建议你不要使用查询缓存。

原因：

- 查询缓存的失效非常频繁，只要有对一个表的更新，这个表上所有的查询缓存都会被清空。想想也知道，有数据的更新了，那缓存的数据就是过时的了。为了保证准确，肯定得清空。

MySQL 8.0 版本直接将查询缓存的整块功能删掉。



连接器和执行器阶段都有对权限的验证。

**区别**：





## 一条更新语句是如何执行的

redo log 和 binlog的区别：

-  redo log 是 InnoDB 引擎特有的；binlog 是 MySQL 的 Server 层实现的，所有引擎都可以
  使用。
-  redo log 是物理日志，记录的是“在某个数据页上做了什么修改”；binlog 是逻辑日志，记 录的是这个语句的原始逻辑，比如“给 ID=2 这一行的 c 字段加 1 ”。
-  redo log 是循环写的，空间固定会用完；binlog 是可以追加写入的。“追加写”是指 binlog
  文件写到一定大小后会切换到下一个，并不会覆盖以前的日志。





## 第4讲  索引

聚簇索引、非聚簇索引的区别：

- 聚簇索引：又称**主键索引**，叶子结点存放的是一整行数据
- 非： 又称**非主键索引**（又称二级索引），叶子节点存放的是主键的值



InnoDB的索引树， 大概是1200叉树，  一般4层， 就有17亿个数据。



不用索引有自己的一颗索引树

**用非主键索引查询时， 查到主键的值， 还要在主键索引书上查找一次**，这个过程称为**回表**



**页分裂：**插入一条新纪录，要维护索引树的时候，  要插入的记录所在的**数据页**已经满了，这时就要申请一个新的数据页，然后移动部分数据

疑问:**（ 新的数据页的指针是怎么维护的，  如果后面还有数据页，是怎么操作的   P64）**



页合并：



### 自增主键的优点

每次主键都是递增的，都是追加操作，不会涉及到页分裂，不用移动数据



主键长度越小，普通索引的叶子结点就越小，占用的空间就越小



#### 什么情况下业务字段适合作为主键：

- 只有一个索引
- 该索引是唯一索引

这样可以将这个索引设置成主键





## 覆盖索引和联合索引的区别




## 第6讲 

 set global readonly=true  和 FTWRL（Flush Table With Read Lock） 的区别：

- 在有些系统中，readonly 的值会被用来做其他逻辑，比如用来判断一个库是主库还是 备库。因此，修改 global 变量的方式影响面更大，不建议使用。 
- 在异常处理机制上有差异。如果执行 FTWRL 命令之后**由于客户端发生异常断开**，那 么 MySQL 会自动**释放**这个**全局锁**，整个库回到可以正常更新的状态。而将整个库设置为 readonly 之后，如果客户端发生异常，则数据库就会一直保持 readonly 状态，这样会导致整
  个库长时间处于不可写状态，风险较高。



## 第7讲



**两阶段锁**：

在 InnoDB 事务中，行锁是在需要的时候才加上的，但并不是不需要了就立刻释放，而是要等到事务结束时才释放。这个就是**两阶段锁**协议。



发生死锁的解决办法：

- 直接进入等待，直到超时。这个超时时间可以通过参数 innodb_lock_wait_timeout 来设置。
- 发起死锁检测，发现死锁后，主动回滚死锁链条中的某一个事务，让其他事 务得以继续执行。将参数 innodb_deadlock_detect 设置为 on，表示开启这个逻辑。







InnoDB 的数据是按数据页为单位来读写的。也就是说，当需要读一条记录的时候， 并不是将这个记录本身从磁盘读出来，而是以页为单位，将其整体读入内存。在 InnoDB 中，**每 个数据页的大小默认是 16KB**。