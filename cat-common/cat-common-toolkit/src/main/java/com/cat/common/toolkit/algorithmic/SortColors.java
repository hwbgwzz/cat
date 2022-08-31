package com.cat.common.toolkit.algorithmic;

import java.util.Arrays;

/**
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *
 * 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 *
 * 输入：nums = [2,0,2,1,1,0]
 * 输出：[0,0,1,1,2,2]
 */
public class SortColors {
    public static void sortColors(int[] nums) {
        int[] sortNums = new int[nums.length];
        int lIndex=0, hIndex = 0;
        for (int i=0;i<nums.length;i++) {
            if (nums[i] == 2) {
                lIndex = lIndex == 0 ? nums.length -1 : --lIndex;
                sortNums[lIndex] = nums[i];
            } else if (nums[i] == 0) {
                sortNums[hIndex++] = nums[i];
            }
        }


        for (int i=0;i<nums.length;i++) {
            if (nums[i] == 1) {
                sortNums[hIndex++] = nums[i];
            }
        }
        for (int i=0;i<sortNums.length;i++) {
            nums[i] = sortNums[i];
        }
        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,2,2,2,2,0,0,0,1,1};
        sortColors(nums);
    }
}
