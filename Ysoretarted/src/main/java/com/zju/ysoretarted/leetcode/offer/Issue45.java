package com.zju.ysoretarted.leetcode.offer;

import java.util.PriorityQueue;
import java.util.Queue;

public class Issue45 {
    public String minNumber(int[] nums) {
        Queue<String> queue = new PriorityQueue<>(String::compareTo);
        for(int x : nums){
            queue.add(Integer.valueOf(x).toString());
        }
        StringBuilder ans = new StringBuilder();
        while(!queue.isEmpty()){
            ans.append(queue.remove());
        }
        return ans.toString();
    }
}
