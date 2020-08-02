package com.zju.ysoretarted.leetcode.offer;

/**
 * @Author zhongcz
 * @Date 2020/8/1 12:53
 */
public class Issue56 {

    public int singleNumber(int[] nums) {
        int[] digit = new int[32];
        for (int x : nums) {
            for (int i = 0; i < 32; ++i) {
                if (((x >> i) & 1) == 1) {
                    digit[i] = (digit[i] + 1) % 3;
                }
            }
        }
        int res = 0;
        for(int i = 0; i < 32; ++i){
            res ^= (digit[i] << i);
        }
        return res;
    }
}
