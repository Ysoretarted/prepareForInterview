package com.zju.ysoretarted.leetcode.offer;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Issue49 {

    public int nthUglyNumber(int n) {
        Queue<Long> q = new PriorityQueue<>();
        q.add(1L);
        Set<Long> set = new HashSet<>();
        set.add(1L);
        while(n-- >= 1){
            long x = q.remove();
            if(n == 0)
                return ((int) x);
            if(!set.contains(x * 2)){
                set.add(x * 2);
                q.add(x * 2);
            }

            if(!set.contains(x * 3)){
                set.add(x * 3);
                q.add(x * 3);
            }
            if(!set.contains(x * 5)){
                set.add(x * 5);
                q.add(x * 5);
            }
        }
        return Integer.MIN_VALUE;
    }
}
