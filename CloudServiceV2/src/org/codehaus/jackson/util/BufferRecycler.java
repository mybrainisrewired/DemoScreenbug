package org.codehaus.jackson.util;

public class BufferRecycler {
    public static final int DEFAULT_WRITE_CONCAT_BUFFER_LEN = 2000;
    protected final byte[][] _byteBuffers;
    protected final char[][] _charBuffers;

    public enum ByteBufferType {
        READ_IO_BUFFER(4000),
        WRITE_ENCODING_BUFFER(4000),
        WRITE_CONCAT_BUFFER(2000);
        private final int size;

        private ByteBufferType(int size) {
            this.size = size;
        }
    }

    public enum CharBufferType {
        TOKEN_BUFFER(2000),
        CONCAT_BUFFER(2000),
        TEXT_BUFFER(200),
        NAME_COPY_BUFFER(200);
        private final int size;

        private CharBufferType(int size) {
            this.size = size;
        }
    }

    public BufferRecycler() {
        this._byteBuffers = new byte[ByteBufferType.values().length][];
        this._charBuffers = new char[CharBufferType.values().length][];
    }

    private final byte[] balloc(int size) {
        return new byte[size];
    }

    private final char[] calloc(int size) {
        return new char[size];
    }

    public final byte[] allocByteBuffer(ByteBufferType type) {
        int ix = type.ordinal();
        byte[] buffer = this._byteBuffers[ix];
        if (buffer == null) {
            return balloc(ByteBufferType.access$000(type));
        }
        this._byteBuffers[ix] = null;
        return buffer;
    }

    public final char[] allocCharBuffer(CharBufferType type) {
        return allocCharBuffer(type, 0);
    }

    public final char[] allocCharBuffer(CharBufferType type, int minSize) {
        if (CharBufferType.access$100(type) > minSize) {
            minSize = CharBufferType.access$100(type);
        }
        int ix = type.ordinal();
        char[] buffer = this._charBuffers[ix];
        if (buffer == null || buffer.length < minSize) {
            return calloc(minSize);
        }
        this._charBuffers[ix] = null;
        return buffer;
    }

    public final void releaseByteBuffer(ByteBufferType type, byte[] buffer) {
        this._byteBuffers[type.ordinal()] = buffer;
    }

    public final void releaseCharBuffer(CharBufferType type, char[] buffer) {
        this._charBuffers[type.ordinal()] = buffer;
    }
}