package com.zju.ysoretarted.leetcode.offer;

import java.util.Collections;

public class Issue46 {
    public static int translateNum(int num) {
        if(num == 0)
            return 1;
        int[] digit = new int[32];
        int ind = 0;
        while(num > 0){
            digit[ind++] = num % 10;
            num = num / 10;
        }

        int a = 1;
        int b = 1;
        int c = -1;
        System.out.println(ind);
        for(int i = ind - 2; i >= 0; i --){
            int t = digit[i + 1] * 10 + digit[i];
            c = b;
            if(t <= 25){
                c += a;
            }
            a = b;
            b = c;
            System.out.println(c);
        }
        return c;
    }

    public static void main(String[] args) {
        translateNum(12258);
    }
}
