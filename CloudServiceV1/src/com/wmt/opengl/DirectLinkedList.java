package com.wmt.opengl;

public final class DirectLinkedList<E> {
    private Entry<E> mHead;
    private int mSize;
    private Entry<E> mTail;

    public static final class Entry<E> {
        public boolean inserted;
        public com.wmt.opengl.DirectLinkedList.Entry<E> next;
        public com.wmt.opengl.DirectLinkedList.Entry<E> previous;
        public final E value;

        public Entry(E value) {
            this.previous = null;
            this.next = null;
            this.inserted = false;
            this.value = value;
        }
    }

    public DirectLinkedList() {
        this.mSize = 0;
    }

    public void add(Entry<E> entry) {
        Entry<E> tail = this.mTail;
        if (tail != null) {
            tail.next = entry;
            entry.previous = tail;
        } else {
            this.mHead = entry;
        }
        this.mTail = entry;
        entry.inserted = true;
        this.mSize++;
    }

    public void clear() {
        this.mHead = null;
        this.mTail = null;
        this.mSize = 0;
    }

    public Entry<E> getHead() {
        return this.mHead;
    }

    public Entry<E> getTail() {
        return this.mTail;
    }

    public boolean isEmpty() {
        return this.mSize == 0;
    }

    public Entry<E> remove(Entry<E> entry) {
        Entry<E> previous = entry.previous;
        Entry<E> next = entry.next;
        if (next != null) {
            next.previous = previous;
            entry.next = null;
        } else {
            this.mTail = previous;
        }
        if (previous != null) {
            previous.next = next;
            entry.previous = null;
        } else {
            this.mHead = next;
        }
        entry.inserted = false;
        this.mSize--;
        if (this.mSize < 0) {
            this.mSize = 0;
        }
        return next;
    }

    public int size() {
        return this.mSize;
    }
}