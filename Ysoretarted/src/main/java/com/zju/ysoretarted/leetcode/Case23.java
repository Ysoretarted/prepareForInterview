package com.zju.ysoretarted.leetcoe;


import java.util.PriorityQueue;
import java.util.Queue;

public class Case23 {


    /**
     *    每次循环取最小的
     * @param lists
     * @return
     */
    /*public ListNode mergeKLists(ListNode[] lists) {
        ListNode dummyHead = new ListNode(-1);
        ListNode tail = dummyHead;
        int len = lists.length;
        if(len == 0){
            return null;
        }
        boolean flag = true;
        while(flag){
            flag = false;
            int mi = -1;
            for(int i = 0; i < len; ++i){
                if(lists[i] == null)
                    continue;
                if(mi == -1){
                    mi = i;
                    flag = true;
                }
                if(lists[mi].val > lists[i].val){
                    mi = i;
                }
            }
            if(mi != -1){
                tail.next = lists[mi];
                tail = tail.next;
                lists[mi] = lists[mi].next;
            }
        }
        return dummyHead.next;
    }*/

    public ListNode mergeKLists(ListNode[] lists) {
        int len = lists.length;
        ListNode tail, dummyHead = new ListNode(-1);
        tail = dummyHead;
        if(len == 0)
            return dummyHead.next;
        Queue<ListNode> queue = new PriorityQueue<>(((o1, o2) -> o1.val - o2.val));
        for(ListNode node : lists){
            if(node != null)
                queue.add(node);
        }
        while(!queue.isEmpty()){
            ListNode node = queue.remove();
            System.out.println(node.val);
            tail.next = node;
            tail = tail.next;
            if(node.next != null){
                queue.add(node.next);
            }
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {

    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
}
