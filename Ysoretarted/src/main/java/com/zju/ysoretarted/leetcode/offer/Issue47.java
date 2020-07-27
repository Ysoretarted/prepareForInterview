package com.zju.ysoretarted.leetcode.offer;

public class Issue47 {
    public int maxValue(int[][] grid) {
        int leni = grid.length;
        int lenj = grid[0].length;
        int[][] dp = new int[leni + 1][lenj + 1];
        for(int i = 0;  i< leni; ++i){
            for(int j = 0; j < lenj; ++j){
                dp[i + 1][j +1] = Math.max(dp[i][j + 1], dp[i + 1][j]) + grid[i][j];
            }
        }
        return dp[leni][lenj];
    }

    public static void main(String[] args) {

    }
}
