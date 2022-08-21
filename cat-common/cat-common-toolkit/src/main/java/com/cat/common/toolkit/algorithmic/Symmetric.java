package com.cat.common.toolkit.algorithmic;

public class Symmetric {

    public static void main(String[] args) {

    }

    public boolean isSymmetric(TreeNode node) {
        return check(node, node);
    }

    public boolean check(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }

        if (node1 == null || node2 == null) {
            return false;
        }

        boolean leftHave = check(node1.left, node2.right);
        boolean rightHave = check(node1.right, node2.left);
        return node1.val == node2.val && leftHave && rightHave;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
