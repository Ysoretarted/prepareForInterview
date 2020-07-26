package com.zju.ysoretarted.leetcode.offer;

public class Issue11 {

    public int minArray(int[] numbers) {
        int len = numbers.length;
        if(len == 0)
            return Integer.MIN_VALUE;
        int left = 0;
        int right = len - 1;
        int mid;
        while(left < right){
            mid = left + (right - left) / 2;
            if(numbers[mid] > numbers[right]){
                left = mid + 1;
            }
            else if(numbers[mid] < numbers[right]){
                right = mid;
            }else{
                right--;
            }
        }
        return numbers[left];
    }
}
