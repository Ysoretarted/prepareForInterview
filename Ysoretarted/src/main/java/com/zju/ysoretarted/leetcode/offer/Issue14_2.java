package com.zju.ysoretarted.leetcode.offer;

/**
 * @Author zhongcz
 * @Date 2020/8/2 17:33
 */
public class Issue14_2 {
    private static final int MOD = (int) 1e9 + 7;

    public int cuttingRope(int n) {
        if (n <= 3) {
            return n - 1;
        }
        int a = n / 3;
        int b = n % 3;
        long ret = quickPow(3, a - 1);
        System.out.println(ret);
        if (b == 0) {
            return (int) (ret * 3 % MOD);
        }
        if (b == 1) {
            return (int) (ret * 4 % MOD);
        }
        return (int) (ret * 6 % MOD);

    }

    private int quickPow(long x, int n) {
        long res = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                res = res * x % MOD;
            }
            x = x * x % MOD;
            n >>= 1;
        }
        return (int) res;
    }
}
