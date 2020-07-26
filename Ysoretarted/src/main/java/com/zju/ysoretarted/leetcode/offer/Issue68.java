package com.zju.ysoretarted.leetcode.offer;


import sun.reflect.generics.tree.Tree;

public class Issue68 {

    /** 普通二叉树
     *
     * @param root
     * @param p
     * @param q
     * @return
     */
   /* public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q)
            return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if(left == null) return right;
        if(right == null) return left;
        return root;
    }*/


    /**
     * 二叉搜索树
     * @param root
     * @param p
     * @param q
     * @return
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q)
            return root;
        if(p.val > q.val){
            TreeNode tmp = p;
            p = q;
            q = tmp;
        }
        if(root.val < p.val)
            return lowestCommonAncestor(root.right, p, q);
        if(root.val > q.val)
            return lowestCommonAncestor(root.left, p, q);
        return root;

    }

    private static void swap(TreeNode a, TreeNode b){
        TreeNode tmp = a;
        a = b;
        b = tmp;
    }



    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static void main(String[] args) {
        TreeNode  root = new TreeNode(2);
        TreeNode p = new TreeNode(1);
        TreeNode q = new TreeNode(3);
        root.left = p;
        root.right = q;
        System.out.println(lowestCommonAncestor(root, q, p).val);
    }

}
