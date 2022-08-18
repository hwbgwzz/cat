package com.cat.common.toolkit.algorithmic;

import java.util.ArrayList;
import java.util.List;

/**
 * 给单链表的头节点 head ，反转链表，并返回反转后的链表。
 */
public class ReverseList {
    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(1);

        ListNode listNode2 = new ListNode(2);

        ListNode listNode3 = new ListNode(3);

        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = null;

        ListNode testNode = reverseList2(listNode1);

        while (testNode != null) {
            System.out.println(testNode.val);
            testNode = testNode.next;
        }
    }

    public static ListNode reverseList2(ListNode head) {
        ListNode pre = null;
         while (head != null) {
             ListNode next = head.next;
             head.next =pre;
             pre = head;
             head = next;
         }
         return pre;
    }

    public static ListNode reverseList(ListNode head) {
        if (head == null) {
            return head;
        }
        List<ListNode> splistNode = new ArrayList<>(10);
        while (head != null) {
            splistNode.add(head);
            head = head.next;
        }
        //Collections.reverse(splistNode);

        for (int i = splistNode.size() - 1 ; i>=0; ) {
            if (i >= 1) {
                splistNode.get(i).next = splistNode.get(--i);
            } else {
                splistNode.get(i--).next = null;
            }
        }

        return splistNode.get(splistNode.size() - 1);
    }

    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
