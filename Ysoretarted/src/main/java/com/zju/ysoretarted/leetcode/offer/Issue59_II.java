package com.zju.ysoretarted.leetcode.offer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Issue59_II {
    private Queue<Integer> queue;
    private Deque<Integer> window;
    public Issue59_II() {
        queue = new LinkedList<>();
    }

    public int max_value() {
        if(queue.isEmpty())
            return -1;
        return window.getFirst();
    }

    public void push_back(int value) {
        if(window.isEmpty()){
            window.addLast(value);
        }else{
            while(!window.isEmpty() && value > window.getLast())
                window.removeLast();
            window.addLast(value);
        }
        queue.add(value);
    }

    public int pop_front() {
        if(queue.isEmpty())
            return -1;
        int x = queue.remove();
        if(x == window.getFirst()){
            window.removeFirst();
        }
        return x;
    }
}
