package com.qican.ygj.utils;


public class BaseArray {
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static final int ARRAY_INITIAL_CAPACITY = 12;
    
    protected int mHandlerCount = 0;
    protected Object[] mHandlers = new Object[ARRAY_INITIAL_CAPACITY];
    
    public int getCount()
    {
    	return mHandlerCount;
    }
    
    public Object getItem(int position)
    {
    	if (position >= 0 && position < getCount()) {
    		return mHandlers[position];
    	}
    	
    	return null;
    }
    
    // 缩放数组
    public void addInArray(Object msgHandler, int index) {
    	Object[] children = mHandlers;
        final int count = mHandlerCount;
        final int size = children.length;
        if (index == count) {
            if (size == count) {
            	mHandlers = new Object[size + ARRAY_CAPACITY_INCREMENT];
                System.arraycopy(children, 0, mHandlers, 0, size);
                children = mHandlers;
            }
            children[mHandlerCount++] = msgHandler;
        } else if (index < count) {
            if (size == count) {
            	mHandlers = new Object[size + ARRAY_CAPACITY_INCREMENT];
                System.arraycopy(children, 0, mHandlers, 0, index);
                System.arraycopy(children, index, mHandlers, index + 1, count - index);
                children = mHandlers;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = msgHandler;
            mHandlerCount++;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
        }
    }
    
    // This method also sets the child's mParent to null
    public void removeFromArray(int index) {
        final Object[] children = mHandlers;
        final int count = mHandlerCount;
        if (index == count - 1) {
            children[--mHandlerCount] = null;
        } else if (index >= 0 && index < count) {
            System.arraycopy(children, index + 1, children, index, count - index - 1);
            children[--mHandlerCount] = null;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public int indexOfChild(Object msgHandler) {
        final int count = mHandlerCount;
        final Object[] children = mHandlers;
        for (int i = 0; i < count; i++) {
            if (children[i] == msgHandler) {
                return i;
            }
        }
        return -1;
    }
}
