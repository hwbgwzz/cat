package com.cat.common.toolkit.algorithmic;

import com.cat.common.toolkit.json.JSON;
import lombok.extern.slf4j.Slf4j;


/**
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
 *
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 *
 * 你可以按任意顺序返回答案。
 */
@Slf4j
public class TwoSum {
    public static void main(String[] args) {
        log.info(
                JSON.toFormatJSONString(
                        towSum(new int[]{11,15,2,7}, 9)
                )
        );
    }

    public static int[] towSum(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }
}
