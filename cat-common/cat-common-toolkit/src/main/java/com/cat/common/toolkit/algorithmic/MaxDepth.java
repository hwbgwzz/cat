package com.cat.common.toolkit.algorithmic;

/**
 * 给定一个二叉树，找出其最大深度。
 *
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * 说明: 叶子节点是指没有子节点的节点。

 */
public class MaxDepth {
      public static void main(String[] args) {

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

    public int maxDepth(TreeNode node) {
          if (node == null) {
              return 0;
          }
          int left = maxDepth(node.left);
          int right = maxDepth(node.right);
          return Math.max(left,right) + 1;
    }
}
