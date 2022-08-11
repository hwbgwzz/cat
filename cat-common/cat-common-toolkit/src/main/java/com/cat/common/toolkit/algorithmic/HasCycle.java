package com.cat.common.toolkit.algorithmic;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 *
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。注意：pos 不作为参数进行传递 。仅仅是为了标识链表的实际情况。
 *
 * 如果链表中存在环 ，则返回 true 。 否则，返回 false 。
 */
public class HasCycle {
    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(1);

        ListNode listNode2 = new ListNode(2);

        ListNode listNode3 = new ListNode(3);

        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode1;

        hasCycle(listNode1);
    }

    public static boolean hasCycle(ListNode head) {
        List<ListNode> existsList = new ArrayList<ListNode>();
        while(head != null) {
            if (existsList.contains(head)) {
                System.out.println(head.val);
                return true;
            }
            existsList.add(head);
            head = head.next;
        }
        return false;
    }
}

class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
  }
