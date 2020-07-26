package com.zju.ysoretarted.leetcode.offer;

import java.util.Stack;

public class Issue31 {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int len = pushed.length;
        if(len == 0)
            return true;
        int in = 0;
        int out = 0;
        while(in < len || out < len){
            if(stack.isEmpty() || stack.peek() != popped[out]){
                if(in < len)
                    stack.push(pushed[in++]);
                else return false;
                continue;
            }
            else{
                stack.pop();
                out++;
            }
        }
        return stack.isEmpty();
    }
}
