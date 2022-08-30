package com.cat.common.toolkit.algorithmic;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

@Slf4j
public class MinStack {

    Deque<Integer> mStack;
    Deque<Integer> minStack;

    public MinStack() {
        mStack = new LinkedList<>();
        minStack = new LinkedList<>();
        minStack.push(Integer.MAX_VALUE);
    }

    public void push(int val) {
        mStack.push(val);
        minStack.push(Math.min(minStack.peek(), val));
    }

    public void pop() {
        mStack.pop();
        minStack.pop();
    }

    public int top() {
        return mStack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack xStack = new MinStack();
        xStack.push(-2);
        xStack.push(0);
        xStack.push(-3);
        xStack.push(4);

/*        log.info("size:"+xStack.minStack.size());
        int size = xStack.minStack.size();
        for (int i=0;i<size;i++) {
            log.info("value:"+xStack.minStack.pop());
        }*/

        log.info("min:"+xStack.getMin());
        xStack.pop();
        log.info("top:"+xStack.top());
        log.info("min:"+xStack.getMin());

        xStack.pop();
        log.info("top:"+xStack.top());
        log.info("min:"+xStack.getMin());
    }
}
