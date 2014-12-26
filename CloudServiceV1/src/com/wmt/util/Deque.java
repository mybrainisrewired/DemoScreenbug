package com.wmt.util;

public final class Deque<E> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private E[] mArray;
    private int mHead;
    private int mSize;
    private int mTail;

    public Deque() {
        this.mHead = 0;
        this.mTail = 0;
        this.mSize = 0;
        this.mArray = new Object[16];
    }

    public Deque(int initialCapacity) {
        this.mHead = 0;
        this.mTail = 0;
        this.mSize = 0;
        this.mArray = new Object[initialCapacity];
    }

    private void expand() {
        E[] array = this.mArray;
        int head = this.mHead;
        int capacity = array.length;
        int rightSize = capacity - head;
        Object[] newArray = new Object[(capacity << 1)];
        System.arraycopy(array, head, newArray, 0, rightSize);
        System.arraycopy(array, 0, newArray, rightSize, head);
        this.mArray = newArray;
        this.mHead = 0;
        this.mTail = capacity;
    }

    public void addFirst(E e) {
        E[] array = this.mArray;
        int head = (this.mHead - 1) & (array.length - 1);
        this.mHead = head;
        array[head] = e;
        if (head == this.mTail) {
            expand();
        }
        this.mSize++;
    }

    public void addLast(E e) {
        E[] array = this.mArray;
        int tail = this.mTail;
        array[tail] = e;
        tail = (tail + 1) & (array.length - 1);
        this.mTail = tail;
        if (this.mHead == tail) {
            expand();
        }
        this.mSize++;
    }

    public void clear() {
        E[] array = this.mArray;
        int head = this.mHead;
        int tail = this.mTail;
        if (head != tail) {
            int mask = array.length - 1;
            do {
                array[head] = null;
                head = (head + 1) & mask;
            } while (head != tail);
            this.mHead = 0;
            this.mTail = 0;
        }
        this.mSize = 0;
    }

    public E get(int index) {
        E[] array = this.mArray;
        if (index < size()) {
            return array[(this.mHead + index) & (array.length - 1)];
        }
        throw new IndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }

    public E pollFirst() {
        E[] array = this.mArray;
        int head = this.mHead;
        E result = array[head];
        if (result == null) {
            return null;
        }
        array[head] = null;
        this.mHead = (head + 1) & (array.length - 1);
        this.mSize--;
        return result;
    }

    public E pollLast() {
        E[] array = this.mArray;
        int tail = (this.mTail - 1) & (array.length - 1);
        E result = array[tail];
        if (result == null) {
            return null;
        }
        array[tail] = null;
        this.mTail = tail;
        this.mSize--;
        return result;
    }

    public int size() {
        return this.mSize;
    }
}