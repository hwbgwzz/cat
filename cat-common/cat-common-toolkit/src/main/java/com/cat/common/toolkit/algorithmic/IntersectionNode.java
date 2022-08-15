package com.cat.common.toolkit.algorithmic;

public class IntersectionNode {
    public static void main(String[] args) {

    }

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        ListNode lnA = headA, lnB = headB;

        /**
         * 当等null的时候互换指针， 在循环中让listnode的元素数量相同，这样就能找到相交点
         */
        while(lnA != lnB) {
            lnA = lnA == null ? headB : lnA.next;
            lnB = lnB == null ? headA : lnB.next;
        }

        return lnB;
    }
}
