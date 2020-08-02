package com.zju.ysoretarted.leetcode.offer;

/**
 * @Author zhongcz
 * @Date 2020/7/30 19:10
 */
public class Issue43 {

    public static int countDigitOne(int n) {
        int digit = 1;
        int low = 0;
        int cur = 0;
        int high;
        int res = 0;
        while (n > 0) {
            cur = n % 10;
            high = n / 10;
            if (cur == 0) {
                res += high * digit;
            }
            if (cur == 1) {
                res += high * digit + low + 1;
            }
            if (cur > 1) {
                res += (high + 1) * digit;
            }
            low = n % 10 * digit + low;
            digit *= 10;
            n = n / 10;

            System.out.println(res);
        }
        return res;
    }

    public static void main(String[] args) {
        countDigitOne(12);
    }
}
