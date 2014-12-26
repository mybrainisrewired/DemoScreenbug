package com.google.android.gms.common.data;

import com.google.android.gms.internal.fq;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class a<T> implements Iterator<T> {
    private int BC;
    private final DataBuffer<T> mDataBuffer;

    public a(DataBuffer<T> dataBuffer) {
        this.mDataBuffer = (DataBuffer) fq.f(dataBuffer);
        this.BC = -1;
    }

    public boolean hasNext() {
        return this.BC < this.mDataBuffer.getCount() + -1;
    }

    public T next() {
        if (hasNext()) {
            DataBuffer dataBuffer = this.mDataBuffer;
            int i = this.BC + 1;
            this.BC = i;
            return dataBuffer.get(i);
        } else {
            throw new NoSuchElementException("Cannot advance the iterator beyond " + this.BC);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}