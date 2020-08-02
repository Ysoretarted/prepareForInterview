package com.zju.ysoretarted.leetcode.offer;

/**
 * @Author zhongcz
 * @Date 2020/8/2 17:09
 */
public class Issue33 {

    public boolean verifyPostorder(int[] postorder) {
        return recursive(postorder, 0, postorder.length - 1);
    }

    private boolean recursive(int[] postorder, int left, int right) {
        if(left >= right){
            return true;
        }else{
            int m = left;
            while(left < right && postorder[left] < postorder[right]){
                m++;
            }
            int mid = m;
            while(m < right && postorder[m] > postorder[right]){
                m++;
            }
            return m == right && recursive(postorder, left, mid - 1)
                    && recursive(postorder, mid, right - 1);

        }
    }
}
