package com.cat.common.toolkit.algorithmic;

import java.util.Arrays;

/**
 * 给定一个大小为 n 的数组 nums ，返回其中的多数元素。多数元素是指在数组中出现次数 大于 ⌊ n/2 ⌋ 的元素。
 *
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 *
 */
public class MajorityElement {
    public static void main(String[] args) {

    }

    public static int majorityElement_one(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2 ];
    }

    public static int majorityElement_two(int[] nums) {
         int count = 0;
         Integer  candidate = null;

         for (int num : nums) {
             if (count == 0) {
                 candidate = num;
             }
             count += (num == candidate) ? 1 : -1;
         }
         return candidate.intValue();
    }
}
