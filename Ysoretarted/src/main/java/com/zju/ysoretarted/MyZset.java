package com.zju.ysoretarted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 维护span的意义是什么呢
 */
public class MyZset {
    public static int MAXLEVEL = 32;   // 能支持的最大层数
    private ZSkipList table;

    class ZSkipList {
        ZSkipListNode head;
        ZSkipListNode tail;
        int level;
        int length;  //记录跳表的元素
    }

    class ZSkipListNode {
        ZSkipListLevel[] level; //每层的
        ZSkipListNode pre;
        double score;
        Object obj;

        ZSkipListNode(int level) {
            this.level = createZSkipListLevel(level);
        }

        ZSkipListNode(int level, ZSkipListNode pre, double score, Object obj) {
            this.level = createZSkipListLevel(level);
            this.pre = pre;
            this.score = score;
            this.obj = obj;
        }

        private ZSkipListLevel[] createZSkipListLevel(int level) {
            ZSkipListLevel[] ret = new ZSkipListLevel[level];
            for (int i = 0; i < level; ++i) {
                ret[i] = new ZSkipListLevel();
            }
            return ret;
        }
    }

    class ZSkipListLevel {
        ZSkipListNode next;
        int span;
    }

    /**
     * worst O(N)   avg O(logN)
     * @param addScore
     * @param addObj
     * @return
     */
    public ZSkipListNode insert(double addScore, Object addObj) {
        if (table == null) {
            initTable();
        }
        ZSkipListNode[] update = createZSkipListNodeList(MAXLEVEL);//记录更新位置的前一个节点
        int[] rank = new int[MAXLEVEL];   // 第i层到更新点的 距离，rank越往下走，越大
        ZSkipListNode x;

        x = table.head;  //x 是一直 next 移的
        for (int i = table.level - 1; i >= 0; --i) {
            rank[i] = (i == table.level - 1) ? 0 : rank[i + 1];
            while (x.level[i].next != null &&
                    (x.level[i].next.score < addScore /*|| (x.level[i].next.score == addScore && cmp(x.level[i].next.obj, addObj) < 0)*/)) {
                rank[i] += x.level[i].span;
                x = x.level[i].next;
            }
            update[i] = x;
        }

        int nowLevel = generateLevelByRandom();
        if (nowLevel > table.level) {
            for (int i = table.level; i < nowLevel; ++i) {
                // System.out.println("AAA");
                rank[i] = 0;
                update[i] = table.head;
                update[i].level[i].span = table.length; // TODO   ??? 为什么不是 length 加一
            }
            table.level = nowLevel; //维护最高 level
        }
        x = new ZSkipListNode(nowLevel, null, addScore, addObj);  //pre还没维护
        for (int i = 0; i < nowLevel; ++i) {
            // System.out.println("BBB");
            x.level[i].next = update[i].level[i].next;
            update[i].level[i].next = x;

            //维护 span  ,这里要理解一下
            x.level[i].span = update[i].level[i].span + rank[i] - rank[0];  //前面两个相加是等于真实的距离
            update[i].level[i].span = rank[0] + 1 - rank[i];
        }
        // 先决条件  nowLevel < table.level
        for (int i = nowLevel; i < table.level; ++i) {
            //System.out.println("CCC");
            update[i].level[i].span++;
        }
//      插入位置是不是第一个节点
        x.pre = (update[0] == table.head) ? null : update[0];
        if (x.level[0].next != null) {           //插入位置是不是最后一个节点
            x.level[0].next.pre = x;
        } else
            table.tail = x;
        table.length++;
        return x;
    }

    private ZSkipListNode[] createZSkipListNodeList(int zskiplistMaxlevel) {
        ZSkipListNode[] ret = new ZSkipListNode[zskiplistMaxlevel];
        for (int i = 0; i < zskiplistMaxlevel; ++i) {
            ret[i] = new ZSkipListNode(zskiplistMaxlevel);
        }
        return ret;
    }

    public Object get(double score) {
        ZSkipListNode ret = getZSkipListNode(score);
        return ret == null ? null : ret.obj;
    }

    public List<ZSkipListNode> getByRange(double left, double right){
        ZSkipListNode x = table.head;
        for (int i = table.level - 1; i >= 0; --i) {
            while (x.level[i].next != null) {
                System.out.println("AAA");
                double nowScore = x.level[i].next.score;
                if (nowScore == left) {
                    ;
                } else if (nowScore < left) {
                    x = x.level[i].next;
                } else {
                    break;
                }
            }
        }
        System.out.println("BBB");
        ZSkipListNode ans = x.level[0].next;
        List<ZSkipListNode> ret = new ArrayList<>();
        while(ans != null && ans.score <= right){
            ret.add(ans);
            ans = ans.level[0].next;

        }
        for(ZSkipListNode node : ret){
            System.out.print(node.score +"\t");
        }
        System.out.println();

        for(ZSkipListNode node : ret){
            System.out.print(node.obj + "\t");
        }
        System.out.println();
        return ret;
    }

    /**
     * worst O(N)  avg O(log N)
     * @param score
     * @return
     */
    private ZSkipListNode getZSkipListNode(double score) {
        ZSkipListNode x = table.head;
        for (int i = table.level - 1; i >= 0; --i) {
            while (x.level[i].next != null) {
                double nowScore = x.level[i].next.score;
                if (nowScore == score) {
                    return x.level[i].next;
                } else if (nowScore < score) {
                    x = x.level[i].next;
                } else {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 维护 链表关系
     * span的维护
     * 维护table 的长度
     * level的最大值？？？  解决
     * 更新tail
     *  O(log N)  O(N)
     * @param score
     */
    public int delete(double score, Object obj) {
        ZSkipListNode[] update = createZSkipListNodeList(MAXLEVEL);
        ZSkipListNode x;

        x = table.head;
        for (int i = table.level - 1; i >= 0; --i) {
            // worst O(N)   avg O(logN)
            while (x.level[i].next != null &&
                    (x.level[i].next.score < score /*|| (x.level[i].next.score == addScore && cmp(x.level[i].next.obj, addObj) < 0)*/)) {
                //System.out.println("FFFF");
                x = x.level[i].next;
            }
            update[i] = x;
        }
        x = x.level[0].next;
        if (x != null && x.score == score /*&& true*/) {
            deleteNode(x, update);
            return 1;
        }
        return 0;
    }

    /**
     * 打印全表
     */
    public void printTable(){
        ZSkipListNode x = table.head.level[0].next;

        while(x != null){
            System.out.print(x.score + "  ");
            x = x.level[0].next;
        }
        System.out.println();
        x = table.head.level[0].next;
        while(x != null){
            System.out.print(x.obj + "  ");
            x = x.level[0].next;
        }
        System.out.println();
    }

    /**  O(1)
     * @param x      要删除的节点（含位置信息）
     * @param update 每一层的前置节点
     */
    private void deleteNode(ZSkipListNode x, ZSkipListNode[] update) {
        for (int i = 0; i < table.level; ++i) {
            if (update[i].level[i].next == x) {
                update[i].level[i].span += x.level[i].span - 1; //要是x是最后一个节点，不应该是 0 吗
                update[i].level[i].next = x.level[i].next;
            } else {
                // 这个分支什么时候走
                update[i].level[i].span -= 1;
            }
        }
        //更新 pre
        // 删除的点不是最后一个节点
        if (x.level[0].next != null) {
            x.level[0].next.pre = x.pre;
        } else {
            table.tail = x.pre;  //否则更新 尾指针
        }

        /**
         *  从高往下遍历， 右节点那就是最高层
         *    源码这里 是 > 1 ?? 那什么时候 层数变为 0 呢 ？？， 不过问题影响不大
         */
        while (table.level > 1 && table.head.level[table.level - 1].next == null) {
            table.level--;
        }
        table.length--;
    }

    private static int generateLevelByRandom() {
        int ret = 1;
        for (int i = 0; i < 16; ++i) {
            ret = new Random().nextInt(32);
            if (ret < 2 * (i + 1))
                break;
        }
        return ret == 0 ? 1 : ret;

    }

    private void initTable() {
        table = new ZSkipList();
        ZSkipListNode head = new ZSkipListNode(32, null, -1, null);
        head.pre = null;
        table.head = head;
        table.tail = head;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; ++i) {
            generateLevelByRandom();
        }

    }


}
