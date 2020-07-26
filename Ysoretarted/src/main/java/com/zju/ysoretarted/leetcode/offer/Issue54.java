package com.zju.ysoretarted.leetcode.offer;

public class Issue54 {
    private int cnt = 0;
    private int ans = Integer.MIN_VALUE;
    public int kthLargest(TreeNode root, int k) {
        dfs(root, k);
        return ans;
    }

    private void dfs(TreeNode root, int k) {
        if(root == null)
            return ;
        dfs(root.right, k);
        cnt++;
        if(cnt == k){
            ans = root.val;
            return ;
        }
        if(cnt > k)
            return ;
        dfs(root.left, k);
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

}
