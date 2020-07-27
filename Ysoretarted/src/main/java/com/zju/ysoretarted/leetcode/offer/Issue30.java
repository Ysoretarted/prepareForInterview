package com.zju.ysoretarted.leetcode.offer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class Issue30 {
    private Stack<Integer> stack;
    private Deque<Integer> window;
    /** initialize your data structure here. */
    public Issue30() {
        stack = new Stack<>();
        window = new LinkedList<>();
    }

    public void push(int x) {
        stack.push(x);
        while(!window.isEmpty() && x < window.getFirst())
            window.addFirst(x);

    }

    public void pop() {
        int x = stack.pop();
        if(x == window.getFirst()){
            window.removeFirst();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int min() {
        if(window.isEmpty())
            return Integer.MIN_VALUE;
        return window.getFirst();
    }
}
