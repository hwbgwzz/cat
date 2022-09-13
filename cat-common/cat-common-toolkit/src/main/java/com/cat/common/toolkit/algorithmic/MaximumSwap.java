package com.cat.common.toolkit.algorithmic;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * You are given an integer num. You can swap two digits at most once to get the maximum valued number.
 *
 * Return the maximum valued number you can get.
 *   Input: num = 2736
 *   Output: 7236
 *   Explanation: Swap the number 2 and the number 7.
 */
@Slf4j
public class MaximumSwap {
    public static void main(String[] args) {
        char[] numArr = String.valueOf(2765).toCharArray();
        int[] maxIndexs = new int[numArr.length];

        int index = numArr.length - 1;
        for (int i = numArr.length - 1; i >= 0; i--) {
            if (numArr[i] > numArr[index]) index = i;
            maxIndexs[i] = index;
        }

        log.info(Arrays.toString(numArr));
        log.info(Arrays.toString(maxIndexs));

        for (int i = 0; i < numArr.length; i++) {
            if (numArr[i] != numArr[maxIndexs[i]]) {
                char temp = numArr[i];
                numArr[i] = numArr[maxIndexs[i]];
                numArr[maxIndexs[i]] = temp;
                break;
            }
        }

        log.info(""+Integer.valueOf(new String(numArr)));

    }
}
