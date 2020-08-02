package com.zju.ysoretarted.leetcode.offer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhongcz
 * @Date 2020/8/2 17:52
 */
public class Issue36 {

    static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right) {
            val = _val;
            left = _left;
            right = _right;
        }
    }

    ;

    public static Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        List<Node> ret = recursive(root);
        Node left = ret.get(0);
        Node right = ret.get(1);
        System.out.println(left.val);
        System.out.println(right.val);
        left.left = right;
        right.right = left;
        return left;
    }

    private static List<Node> recursive(Node root) {
        if(root == null){
            return null;
        }
        List<Node> left = recursive(root.left);
        List<Node> right = recursive(root.right);
        Node L = left == null ? root : left.get(1);
        Node R = right == null ? root : right.get(0);
        if(root != L){
            root.left = L;
            L.right = root;
        }
        if(root != R){
            root.right = R;
            R.left = root;
        }
        List<Node> ans = new ArrayList<>();
        //这里返回要注意一下
        ans.add(left == null ? root : left.get(0));
        ans.add(right == null ? root : right.get(1));
        return ans;
    }


    public static void main(String[] args) {
        Node node4 = new Node(4);
        Node node2 = new Node(2);
        Node node1 = new Node(1);
        Node node5 = new Node(5);

        node4.left = node2;
        node4.right = node5;

        node2.left = node1;

        Node node = treeToDoublyList(node4);
        System.out.println();
    }
}


