package org.dom4j.tree;

import java.util.Iterator;

public class SingleIterator<T> implements Iterator<T> {
    private boolean first;
    private T object;

    public SingleIterator(T object) {
        this.first = true;
        this.object = object;
    }

    public boolean hasNext() {
        return this.first;
    }

    public T next() {
        T answer = this.object;
        this.object = null;
        this.first = false;
        return answer;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported by this iterator");
    }
}