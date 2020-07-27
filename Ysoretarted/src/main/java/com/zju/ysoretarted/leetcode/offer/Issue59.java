package com.zju.ysoretarted.leetcode.offer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Issue59 {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int len = nums.length;
        if(len == 0)
            return new int[0];
        int left = -k + 1;
        int right = 0;
        int ans[] = new int[len - k + 1];
        int index = 0;
        Deque<Integer> window = new LinkedList<>();
        for(; right < len; ){
            if(left >= 1){
                if(nums[left - 1] == window.getFirst())
                    window.remove();
            }
            int tmp = nums[right++];
            while(!window.isEmpty() && tmp > window.getLast()){
                window.removeLast();
            }
            window.addLast(tmp);
            if(left >= 0)
                ans[index++] = window.getFirst();
            left++;
        }
        return ans;

    }
}
