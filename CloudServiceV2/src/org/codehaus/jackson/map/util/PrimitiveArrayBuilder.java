package org.codehaus.jackson.map.util;

public abstract class PrimitiveArrayBuilder<T> {
    static final int INITIAL_CHUNK_SIZE = 12;
    static final int MAX_CHUNK_SIZE = 262144;
    static final int SMALL_CHUNK_SIZE = 16384;
    Node<T> _bufferHead;
    Node<T> _bufferTail;
    int _bufferedEntryCount;
    T _freeBuffer;

    static final class Node<T> {
        final T _data;
        final int _dataLength;
        Node<T> _next;

        public Node(T data, int dataLen) {
            this._data = data;
            this._dataLength = dataLen;
        }

        public int copyData(T dst, int ptr) {
            System.arraycopy(this._data, 0, dst, ptr, this._dataLength);
            return ptr + this._dataLength;
        }

        public T getData() {
            return this._data;
        }

        public void linkNext(Node<T> next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }

        public Node<T> next() {
            return this._next;
        }
    }

    protected PrimitiveArrayBuilder() {
    }

    protected abstract T _constructArray(int i);

    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }

    public final T appendCompletedChunk(T fullChunk, int fullChunkLength) {
        Node<T> next = new Node(fullChunk, fullChunkLength);
        if (this._bufferHead == null) {
            this._bufferTail = next;
            this._bufferHead = next;
        } else {
            this._bufferTail.linkNext(next);
            this._bufferTail = next;
        }
        this._bufferedEntryCount += fullChunkLength;
        int nextLen = fullChunkLength;
        if (nextLen < 16384) {
            nextLen += nextLen;
        } else {
            nextLen += nextLen >> 2;
        }
        return _constructArray(nextLen);
    }

    public T completeAndClearBuffer(T lastChunk, int lastChunkEntries) {
        int totalSize = lastChunkEntries + this._bufferedEntryCount;
        T resultArray = _constructArray(totalSize);
        int ptr = 0;
        Node<T> n = this._bufferHead;
        while (n != null) {
            ptr = n.copyData(resultArray, ptr);
            n = n.next();
        }
        System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
        ptr += lastChunkEntries;
        if (ptr == totalSize) {
            return resultArray;
        }
        throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
    }

    public T resetAndStart() {
        _reset();
        return this._freeBuffer == null ? _constructArray(INITIAL_CHUNK_SIZE) : this._freeBuffer;
    }
}