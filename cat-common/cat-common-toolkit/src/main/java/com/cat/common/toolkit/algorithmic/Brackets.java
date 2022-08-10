package com.cat.common.toolkit.algorithmic;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 *
 * 有效字符串需满足：
 *
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 *
 */
public class Brackets {
    public static void main(String[] args) {
        isValid("([]){}()[]{");
    }

    public static boolean isValid(String s) {
        int length = s.length();
        if (length % 2 == 1) {
            return false;
        }

        Map<Character, Character> charMap = new HashMap<Character, Character>(){
            {
                put(')','(');
                put(']','[');
                put('}','{');
            }
        };

        Deque<Character> deque = new LinkedList<Character>();

        for (int i = 0; i<length; i++) {
            char ch = s.charAt(i);
            if (charMap.containsKey(ch)) {
                if (deque.isEmpty() || deque.peek() != charMap.get(ch)) {
                    return false;
                }
                deque.pop(); //配对的就移除
            } else {
                deque.push(ch);
            }
        }
        return deque.isEmpty(); //为空就是括号全部配对
    }
}
