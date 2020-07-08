package com.zju.ysoretarted.designPattern.callback;

/**
 * @author zcz
 * @CreateTime 2020/7/6 8:54
 */
public class MyServer {

    public void response(MyCallBack callBack) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("服务端对客户端的请求进行相应");
        callBack.process();
    }
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        return dfs(A, B);
    }

    private boolean dfs(TreeNode a, TreeNode b) {
        if(b == null)
            return true;
        if(a == null)
            return false;
        if(a == b)
            return true;
        if(a.val == b.val)
            return dfs(a.left, b.left) && dfs(a.right, b.right);
        return dfs(a.left, b) || dfs(a.right, b);
    }

    public ListNode reverseList(ListNode head) {
        ListNode nhead = null;
        ListNode tmp = null;
        while(head != null){
            tmp = head.next;
            head.next = nhead;
            nhead = head;
            head = tmp;
        }
        return nhead;
    }
    public ListNode getKthFromEnd(ListNode head, int k) {
        ListNode tmp = head;
        if(head == null)
            return null;
        int step = 0;
        while(head != null){
            step++;
            head = head.next;
        }
        if(step < k)
            return null;
        ListNode ret = tmp;
        while(head != null){
            head = head.next;
            tmp = tmp.next;
        }
        return tmp;
    }

    public static void main(String[] args) {

    }


     public class ListNode {
         int val;
         ListNode next;
         ListNode(int x) { val = x; }
     }

     public class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
         TreeNode(int x) { val = x; }
     }

}
