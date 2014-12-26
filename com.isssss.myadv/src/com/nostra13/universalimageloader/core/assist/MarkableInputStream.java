package com.nostra13.universalimageloader.core.assist;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class MarkableInputStream extends InputStream {
    private long defaultMark;
    private final InputStream in;
    private long limit;
    private long offset;
    private long reset;

    public MarkableInputStream(InputStream in) {
        this.defaultMark = -1;
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        this.in = in;
    }

    private void setLimit(long limit) {
        try {
            if (this.reset >= this.offset || this.offset > this.limit) {
                this.reset = this.offset;
                this.in.mark((int) (limit - this.offset));
            } else {
                this.in.reset();
                this.in.mark((int) (limit - this.reset));
                skip(this.reset, this.offset);
            }
            this.limit = limit;
        } catch (IOException e) {
            throw new IllegalStateException(new StringBuilder("Unable to mark: ").append(e).toString());
        }
    }

    private void skip(long current, long target) throws IOException {
        while (current < target) {
            current += this.in.skip(target - current);
        }
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    public void mark(int readLimit) {
        this.defaultMark = savePosition(readLimit);
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public int read() throws IOException {
        int result = this.in.read();
        if (result != -1) {
            this.offset++;
        }
        return result;
    }

    public int read(byte[] buffer) throws IOException {
        int count = this.in.read(buffer);
        if (count != -1) {
            this.offset += (long) count;
        }
        return count;
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        int count = this.in.read(buffer, offset, length);
        if (count != -1) {
            this.offset += (long) count;
        }
        return count;
    }

    public void reset() throws IOException {
        reset(this.defaultMark);
    }

    public void reset(long token) throws IOException {
        if (this.offset > this.limit || token < this.reset) {
            throw new IOException("Cannot reset");
        }
        this.in.reset();
        skip(this.reset, token);
        this.offset = token;
    }

    public long savePosition(int readLimit) {
        long offsetLimit = this.offset + ((long) readLimit);
        if (this.limit < offsetLimit) {
            setLimit(offsetLimit);
        }
        return this.offset;
    }

    public long skip(long byteCount) throws IOException {
        long skipped = this.in.skip(byteCount);
        this.offset += skipped;
        return skipped;
    }
}