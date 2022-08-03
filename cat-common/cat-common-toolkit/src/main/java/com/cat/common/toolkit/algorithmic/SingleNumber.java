package com.cat.common.toolkit.algorithmic;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 给一個整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
 * 复杂度分析
 *
 * 时间复杂度：O(n)O(n)，其中 nn 是数组的长度。
 *
 * 空间复杂度：O(n)O(n)。哈希映射中包含最多 \lfloor n/3 \rfloor + 1⌊n/3⌋+1 个元素，即需要的空间为 O(n)O(n)。
 */
@Slf4j
public class SingleNumber {
    public static void main(String[] args) {
        int nums[] = {2,2,3,2};
        log.info("仅出现一次的元素是：{}",singleNumber(nums));
    }

    public static int singleNumber(int[] nums) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        for (int num : nums) {
            resultMap.put(num, resultMap.getOrDefault(num, 0) + 1);
        }

        int sn = -1;
        for (Map.Entry<Integer, Integer> map : resultMap.entrySet()) {
            int key = map.getKey(), value = map.getValue();
            if (value == 1) {
                sn = key;
                break;
            }
        }

        return sn;
    }
}
