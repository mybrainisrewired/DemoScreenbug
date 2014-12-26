package org.codehaus.jackson.map.util;

import java.lang.reflect.Array;
import java.util.List;

public final class ObjectBuffer {
    static final int INITIAL_CHUNK_SIZE = 12;
    static final int MAX_CHUNK_SIZE = 262144;
    static final int SMALL_CHUNK_SIZE = 16384;
    private Node _bufferHead;
    private Node _bufferTail;
    private int _bufferedEntryCount;
    private Object[] _freeBuffer;

    static final class Node {
        final Object[] _data;
        Node _next;

        public Node(Object[] data) {
            this._data = data;
        }

        public Object[] getData() {
            return this._data;
        }

        public void linkNext(Node next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }

        public Node next() {
            return this._next;
        }
    }

    protected final void _copyTo(Object resultArray, int totalSize, Object[] lastChunk, int lastChunkEntries) {
        int ptr = 0;
        Node n = this._bufferHead;
        while (n != null) {
            Object[] curr = n.getData();
            int len = curr.length;
            System.arraycopy(curr, 0, resultArray, ptr, len);
            ptr += len;
            n = n.next();
        }
        System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
        ptr += lastChunkEntries;
        if (ptr != totalSize) {
            throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
        }
    }

    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }

    public Object[] appendCompletedChunk(Object[] fullChunk) {
        Node next = new Node(fullChunk);
        if (this._bufferHead == null) {
            this._bufferTail = next;
            this._bufferHead = next;
        } else {
            this._bufferTail.linkNext(next);
            this._bufferTail = next;
        }
        int len = fullChunk.length;
        this._bufferedEntryCount += len;
        if (len < 16384) {
            len += len;
        } else {
            len += len >> 2;
        }
        return new Object[len];
    }

    public int bufferedSize() {
        return this._bufferedEntryCount;
    }

    public void completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, List<Object> resultList) {
        int i;
        Node n = this._bufferHead;
        while (n != null) {
            Object[] curr = n.getData();
            i = 0;
            int len = curr.length;
            while (i < len) {
                resultList.add(curr[i]);
                i++;
            }
            n = n.next();
        }
        i = 0;
        while (i < lastChunkEntries) {
            resultList.add(lastChunk[i]);
            i++;
        }
    }

    public Object[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries) {
        int totalSize = lastChunkEntries + this._bufferedEntryCount;
        Object[] result = new Object[totalSize];
        _copyTo(result, totalSize, lastChunk, lastChunkEntries);
        return result;
    }

    public <T> T[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, Class<T> componentType) {
        int totalSize = lastChunkEntries + this._bufferedEntryCount;
        Object[] result = (Object[]) Array.newInstance(componentType, totalSize);
        _copyTo(result, totalSize, lastChunk, lastChunkEntries);
        _reset();
        return result;
    }

    public int initialCapacity() {
        return this._freeBuffer == null ? 0 : this._freeBuffer.length;
    }

    public Object[] resetAndStart() {
        _reset();
        return this._freeBuffer == null ? new Object[12] : this._freeBuffer;
    }
}