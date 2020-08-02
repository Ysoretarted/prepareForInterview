package com.zju.ysoretarted.leetcode.offer;

/**
 * @Author zhongcz
 * @Date 2020/7/30 16:50
 */
public class Issue26 {

    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (B == null || A == null) {
            return false;
        }
        return dfs(A, B);
    }


    private boolean dfs(TreeNode a, TreeNode b) {
        System.out.println(a.val);
        if(a == null){
            return false;
        }
        if(a.val == b.val){
            boolean ret = judge(a, b);
            if(ret){
                return true;
            }
        }
        /*if(a.left != null){
            boolean ret = dfs(a.left, b);
            if(ret){
                return true;
            }
        }
        if(a.right != null){
            boolean ret = dfs(a.right, b);
            if(ret){
                return true;
            }
        }
        return false;*/
        return dfs(a.left, b) || dfs(a.right, b);
    }
    private boolean judge(TreeNode a, TreeNode b) {
        if(b == null){
            return true;
        }
        if(a == null){
            return false;
        }
        System.out.println(a.val + "   " + b.val);
        if(a.val != b.val){
            return false;
        }
        return judge(a.left, b.left) && judge(a.right, b.right);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

}





