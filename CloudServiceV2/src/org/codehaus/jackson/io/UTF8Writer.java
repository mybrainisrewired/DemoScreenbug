package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8Writer extends Writer {
    static final int SURR1_FIRST = 55296;
    static final int SURR1_LAST = 56319;
    static final int SURR2_FIRST = 56320;
    static final int SURR2_LAST = 57343;
    protected final IOContext _context;
    OutputStream _out;
    byte[] _outBuffer;
    final int _outBufferEnd;
    int _outPtr;
    int _surrogate;

    public UTF8Writer(IOContext ctxt, OutputStream out) {
        this._surrogate = 0;
        this._context = ctxt;
        this._out = out;
        this._outBuffer = ctxt.allocWriteEncodingBuffer();
        this._outBufferEnd = this._outBuffer.length - 4;
        this._outPtr = 0;
    }

    private int convertSurrogate(int secondPart) throws IOException {
        int firstPart = this._surrogate;
        this._surrogate = 0;
        if (secondPart >= 56320 && secondPart <= 57343) {
            return 65536 + (firstPart - 55296) << 10 + secondPart - 56320;
        }
        throw new IOException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
    }

    private void throwIllegal(int code) throws IOException {
        if (code > 1114111) {
            throw new IOException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        } else if (code < 55296) {
            throw new IOException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
        } else if (code <= 56319) {
            throw new IOException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        } else {
            throw new IOException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
    }

    public Writer append(int c) throws IOException {
        write(c);
        return this;
    }

    public void close() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            OutputStream out = this._out;
            this._out = null;
            byte[] buf = this._outBuffer;
            if (buf != null) {
                this._outBuffer = null;
                this._context.releaseWriteEncodingBuffer(buf);
            }
            out.close();
            int code = this._surrogate;
            this._surrogate = 0;
            if (code > 0) {
                throwIllegal(code);
            }
        }
    }

    public void flush() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            this._out.flush();
        }
    }

    public void write(int c) throws IOException {
        if (this._surrogate > 0) {
            c = convertSurrogate(c);
        } else if (c >= 55296 && c <= 57343) {
            if (c > 56319) {
                throwIllegal(c);
            }
            this._surrogate = c;
            return;
        }
        if (this._outPtr >= this._outBufferEnd) {
            this._out.write(this._outBuffer, 0, this._outPtr);
            this._outPtr = 0;
        }
        if (c < 128) {
            byte[] bArr = this._outBuffer;
            int i = this._outPtr;
            this._outPtr = i + 1;
            bArr[i] = (byte) c;
        } else {
            int ptr = this._outPtr;
            int ptr2;
            if (c < 2048) {
                ptr2 = ptr + 1;
                this._outBuffer[ptr] = (byte) ((c >> 6) | 192);
                ptr = ptr2 + 1;
                this._outBuffer[ptr2] = (byte) ((c & 63) | 128);
            } else if (c <= 65535) {
                ptr2 = ptr + 1;
                this._outBuffer[ptr] = (byte) ((c >> 12) | 224);
                ptr = ptr2 + 1;
                this._outBuffer[ptr2] = (byte) (((c >> 6) & 63) | 128);
                ptr2 = ptr + 1;
                this._outBuffer[ptr] = (byte) ((c & 63) | 128);
                ptr = ptr2;
            } else {
                if (c > 1114111) {
                    throwIllegal(c);
                }
                ptr2 = ptr + 1;
                this._outBuffer[ptr] = (byte) ((c >> 18) | 240);
                ptr = ptr2 + 1;
                this._outBuffer[ptr2] = (byte) (((c >> 12) & 63) | 128);
                ptr2 = ptr + 1;
                this._outBuffer[ptr] = (byte) (((c >> 6) & 63) | 128);
                ptr = ptr2 + 1;
                this._outBuffer[ptr2] = (byte) ((c & 63) | 128);
            }
            this._outPtr = ptr;
        }
    }

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int off, int len) throws IOException {
        if (len >= 2) {
            int off2;
            if (this._surrogate > 0) {
                off2 = off + 1;
                len--;
                write(convertSurrogate(str.charAt(off)));
                off = off2;
            }
            int outPtr = this._outPtr;
            byte[] outBuf = this._outBuffer;
            int outBufLast = this._outBufferEnd;
            len += off;
            off2 = off;
            while (off2 < len) {
                int outPtr2;
                if (outPtr >= outBufLast) {
                    this._out.write(outBuf, 0, outPtr);
                    outPtr = 0;
                }
                off = off2 + 1;
                int c = str.charAt(off2);
                if (c < 128) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) c;
                    int maxInCount = len - off;
                    int maxOutCount = outBufLast - outPtr2;
                    if (maxInCount > maxOutCount) {
                        maxInCount = maxOutCount;
                    }
                    maxInCount += off;
                    off2 = off;
                    while (off2 < maxInCount) {
                        off = off2 + 1;
                        c = str.charAt(off2);
                        if (c >= 128) {
                            off2 = off;
                        } else {
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (byte) c;
                            outPtr2 = outPtr;
                            off2 = off;
                        }
                    }
                    outPtr = outPtr2;
                } else {
                    outPtr2 = outPtr;
                    off2 = off;
                }
                if (c < 2048) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 6) | 192);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                    off = off2;
                } else if (c < 55296 || c > 57343) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 12) | 224);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 6) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c & 63) | 128);
                } else {
                    if (c > 56319) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    this._surrogate = c;
                    if (off2 >= len) {
                        outPtr = outPtr2;
                        break;
                    } else {
                        off = off2 + 1;
                        c = convertSurrogate(str.charAt(off2));
                        if (c > 1114111) {
                            this._outPtr = outPtr2;
                            throwIllegal(c);
                        }
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (byte) ((c >> 18) | 240);
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (byte) (((c >> 12) & 63) | 128);
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (byte) (((c >> 6) & 63) | 128);
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (byte) ((c & 63) | 128);
                        outPtr = outPtr2;
                    }
                }
                off2 = off;
                break;
            }
            this._outPtr = outPtr;
        } else if (len == 1) {
            write(str.charAt(off));
        }
    }

    public void write(char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len >= 2) {
            int off2;
            if (this._surrogate > 0) {
                off2 = off + 1;
                len--;
                write(convertSurrogate(cbuf[off]));
                off = off2;
            }
            int outPtr = this._outPtr;
            byte[] outBuf = this._outBuffer;
            int outBufLast = this._outBufferEnd;
            len += off;
            off2 = off;
            while (off2 < len) {
                int outPtr2;
                if (outPtr >= outBufLast) {
                    this._out.write(outBuf, 0, outPtr);
                    outPtr = 0;
                }
                off = off2 + 1;
                int c = cbuf[off2];
                if (c < 128) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) c;
                    int maxInCount = len - off;
                    int maxOutCount = outBufLast - outPtr2;
                    if (maxInCount > maxOutCount) {
                        maxInCount = maxOutCount;
                    }
                    maxInCount += off;
                    off2 = off;
                    while (off2 < maxInCount) {
                        off = off2 + 1;
                        c = cbuf[off2];
                        if (c >= 128) {
                            off2 = off;
                        } else {
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (byte) c;
                            outPtr2 = outPtr;
                            off2 = off;
                        }
                    }
                    outPtr = outPtr2;
                } else {
                    outPtr2 = outPtr;
                    off2 = off;
                }
                if (c < 2048) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 6) | 192);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) ((c & 63) | 128);
                    outPtr = outPtr2;
                    off = off2;
                } else if (c < 55296 || c > 57343) {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c >> 12) | 224);
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (byte) (((c >> 6) & 63) | 128);
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (byte) ((c & 63) | 128);
                } else {
                    if (c > 56319) {
                        this._outPtr = outPtr2;
                        throwIllegal(c);
                    }
                    this._surrogate = c;
                    if (off2 >= len) {
                        outPtr = outPtr2;
                        break;
                    } else {
                        off = off2 + 1;
                        c = convertSurrogate(cbuf[off2]);
                        if (c > 1114111) {
                            this._outPtr = outPtr2;
                            throwIllegal(c);
                        }
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (byte) ((c >> 18) | 240);
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (byte) (((c >> 12) & 63) | 128);
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (byte) (((c >> 6) & 63) | 128);
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (byte) ((c & 63) | 128);
                        outPtr = outPtr2;
                    }
                }
                off2 = off;
                break;
            }
            this._outPtr = outPtr;
        } else if (len == 1) {
            write(cbuf[off]);
        }
    }
}