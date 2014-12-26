package org.codehaus.jackson.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.util.BufferRecycler.CharBufferType;

public final class TextBuffer {
    static final int MAX_SEGMENT_LEN = 262144;
    static final int MIN_SEGMENT_LEN = 1000;
    static final char[] NO_CHARS;
    private final BufferRecycler _allocator;
    private char[] _currentSegment;
    private int _currentSize;
    private boolean _hasSegments;
    private char[] _inputBuffer;
    private int _inputLen;
    private int _inputStart;
    private char[] _resultArray;
    private String _resultString;
    private int _segmentSize;
    private ArrayList<char[]> _segments;

    static {
        NO_CHARS = new char[0];
    }

    public TextBuffer(BufferRecycler allocator) {
        this._hasSegments = false;
        this._allocator = allocator;
    }

    private final char[] _charArray(int len) {
        return new char[len];
    }

    private char[] buildResultArray() {
        if (this._resultString != null) {
            return this._resultString.toCharArray();
        }
        char[] result;
        if (this._inputStart < 0) {
            int size = size();
            if (size < 1) {
                return NO_CHARS;
            }
            int offset = 0;
            result = _charArray(size);
            if (this._segments != null) {
                int i = 0;
                int len = this._segments.size();
                while (i < len) {
                    char[] curr = (char[]) this._segments.get(i);
                    int currLen = curr.length;
                    System.arraycopy(curr, 0, result, offset, currLen);
                    offset += currLen;
                    i++;
                }
            }
            System.arraycopy(this._currentSegment, 0, result, offset, this._currentSize);
            return result;
        } else if (this._inputLen < 1) {
            return NO_CHARS;
        } else {
            result = _charArray(this._inputLen);
            System.arraycopy(this._inputBuffer, this._inputStart, result, 0, this._inputLen);
            return result;
        }
    }

    private final void clearSegments() {
        this._hasSegments = false;
        this._segments.clear();
        this._segmentSize = 0;
        this._currentSize = 0;
    }

    private void expand(int minNewSegmentSize) {
        if (this._segments == null) {
            this._segments = new ArrayList();
        }
        char[] curr = this._currentSegment;
        this._hasSegments = true;
        this._segments.add(curr);
        this._segmentSize += curr.length;
        int oldLen = curr.length;
        int sizeAddition = oldLen >> 1;
        if (sizeAddition < minNewSegmentSize) {
            sizeAddition = minNewSegmentSize;
        }
        curr = _charArray(Math.min(MAX_SEGMENT_LEN, oldLen + sizeAddition));
        this._currentSize = 0;
        this._currentSegment = curr;
    }

    private final char[] findBuffer(int needed) {
        return this._allocator != null ? this._allocator.allocCharBuffer(CharBufferType.TEXT_BUFFER, needed) : new char[Math.max(needed, MIN_SEGMENT_LEN)];
    }

    private void unshare(int needExtra) {
        int sharedLen = this._inputLen;
        this._inputLen = 0;
        char[] inputBuf = this._inputBuffer;
        this._inputBuffer = null;
        int start = this._inputStart;
        this._inputStart = -1;
        int needed = sharedLen + needExtra;
        if (this._currentSegment == null || needed > this._currentSegment.length) {
            this._currentSegment = findBuffer(needed);
        }
        if (sharedLen > 0) {
            System.arraycopy(inputBuf, start, this._currentSegment, 0, sharedLen);
        }
        this._segmentSize = 0;
        this._currentSize = sharedLen;
    }

    public void append(char c) {
        if (this._inputStart >= 0) {
            unshare(Segment.TOKENS_PER_SEGMENT);
        }
        this._resultString = null;
        this._resultArray = null;
        char[] curr = this._currentSegment;
        if (this._currentSize >= curr.length) {
            expand(1);
            curr = this._currentSegment;
        }
        int i = this._currentSize;
        this._currentSize = i + 1;
        curr[i] = c;
    }

    public void append(String str, int offset, int len) {
        if (this._inputStart >= 0) {
            unshare(len);
        }
        this._resultString = null;
        this._resultArray = null;
        char[] curr = this._currentSegment;
        int max = curr.length - this._currentSize;
        if (max >= len) {
            str.getChars(offset, offset + len, curr, this._currentSize);
            this._currentSize += len;
        } else {
            if (max > 0) {
                str.getChars(offset, offset + max, curr, this._currentSize);
                len -= max;
                offset += max;
            }
            expand(len);
            str.getChars(offset, offset + len, this._currentSegment, 0);
            this._currentSize = len;
        }
    }

    public void append(char[] c, int start, int len) {
        if (this._inputStart >= 0) {
            unshare(len);
        }
        this._resultString = null;
        this._resultArray = null;
        char[] curr = this._currentSegment;
        int max = curr.length - this._currentSize;
        if (max >= len) {
            System.arraycopy(c, start, curr, this._currentSize, len);
            this._currentSize += len;
        } else {
            if (max > 0) {
                System.arraycopy(c, start, curr, this._currentSize, max);
                start += max;
                len -= max;
            }
            expand(len);
            System.arraycopy(c, start, this._currentSegment, 0, len);
            this._currentSize = len;
        }
    }

    public char[] contentsAsArray() {
        char[] result = this._resultArray;
        if (result != null) {
            return result;
        }
        result = buildResultArray();
        this._resultArray = result;
        return result;
    }

    public BigDecimal contentsAsDecimal() throws NumberFormatException {
        if (this._resultArray != null) {
            return new BigDecimal(this._resultArray);
        }
        if (this._inputStart >= 0) {
            return new BigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
        }
        return this._segmentSize == 0 ? new BigDecimal(this._currentSegment, 0, this._currentSize) : new BigDecimal(contentsAsArray());
    }

    public double contentsAsDouble() throws NumberFormatException {
        return NumberInput.parseDouble(contentsAsString());
    }

    public String contentsAsString() {
        if (this._resultString == null) {
            if (this._resultArray != null) {
                this._resultString = new String(this._resultArray);
            } else if (this._inputStart < 0) {
                int segLen = this._segmentSize;
                int currLen = this._currentSize;
                if (segLen == 0) {
                    this._resultString = currLen == 0 ? "" : new String(this._currentSegment, 0, currLen);
                } else {
                    StringBuilder sb = new StringBuilder(segLen + currLen);
                    if (this._segments != null) {
                        int i = 0;
                        int len = this._segments.size();
                        while (i < len) {
                            char[] curr = (char[]) this._segments.get(i);
                            sb.append(curr, 0, curr.length);
                            i++;
                        }
                    }
                    sb.append(this._currentSegment, 0, this._currentSize);
                    this._resultString = sb.toString();
                }
            } else if (this._inputLen < 1) {
                String str = "";
                this._resultString = str;
                return str;
            } else {
                this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
            }
        }
        return this._resultString;
    }

    public final char[] emptyAndGetCurrentSegment() {
        this._inputStart = -1;
        this._currentSize = 0;
        this._inputLen = 0;
        this._inputBuffer = null;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            clearSegments();
        }
        char[] curr = this._currentSegment;
        if (curr != null) {
            return curr;
        }
        curr = findBuffer(0);
        this._currentSegment = curr;
        return curr;
    }

    public void ensureNotShared() {
        if (this._inputStart >= 0) {
            unshare(Segment.TOKENS_PER_SEGMENT);
        }
    }

    public char[] expandCurrentSegment() {
        char[] curr = this._currentSegment;
        int len = curr.length;
        this._currentSegment = _charArray(len == 262144 ? 262145 : Math.min(MAX_SEGMENT_LEN, len >> 1 + len));
        System.arraycopy(curr, 0, this._currentSegment, 0, len);
        return this._currentSegment;
    }

    public char[] finishCurrentSegment() {
        if (this._segments == null) {
            this._segments = new ArrayList();
        }
        this._hasSegments = true;
        this._segments.add(this._currentSegment);
        int oldLen = this._currentSegment.length;
        this._segmentSize += oldLen;
        char[] curr = _charArray(Math.min(oldLen >> 1 + oldLen, MAX_SEGMENT_LEN));
        this._currentSize = 0;
        this._currentSegment = curr;
        return curr;
    }

    public char[] getCurrentSegment() {
        if (this._inputStart >= 0) {
            unshare(1);
        } else {
            char[] curr = this._currentSegment;
            if (curr == null) {
                this._currentSegment = findBuffer(0);
            } else if (this._currentSize >= curr.length) {
                expand(1);
            }
        }
        return this._currentSegment;
    }

    public int getCurrentSegmentSize() {
        return this._currentSize;
    }

    public char[] getTextBuffer() {
        if (this._inputStart >= 0) {
            return this._inputBuffer;
        }
        if (this._resultArray != null) {
            return this._resultArray;
        }
        if (this._resultString == null) {
            return !this._hasSegments ? this._currentSegment : contentsAsArray();
        } else {
            char[] toCharArray = this._resultString.toCharArray();
            this._resultArray = toCharArray;
            return toCharArray;
        }
    }

    public int getTextOffset() {
        return this._inputStart >= 0 ? this._inputStart : 0;
    }

    public void releaseBuffers() {
        if (this._allocator == null) {
            resetWithEmpty();
        } else if (this._currentSegment != null) {
            resetWithEmpty();
            char[] buf = this._currentSegment;
            this._currentSegment = null;
            this._allocator.releaseCharBuffer(CharBufferType.TEXT_BUFFER, buf);
        }
    }

    public void resetWithCopy(char[] buf, int start, int len) {
        this._inputBuffer = null;
        this._inputStart = -1;
        this._inputLen = 0;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            clearSegments();
        } else if (this._currentSegment == null) {
            this._currentSegment = findBuffer(len);
        }
        this._segmentSize = 0;
        this._currentSize = 0;
        append(buf, start, len);
    }

    public void resetWithEmpty() {
        this._inputStart = -1;
        this._currentSize = 0;
        this._inputLen = 0;
        this._inputBuffer = null;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            clearSegments();
        }
    }

    public void resetWithShared(char[] buf, int start, int len) {
        this._resultString = null;
        this._resultArray = null;
        this._inputBuffer = buf;
        this._inputStart = start;
        this._inputLen = len;
        if (this._hasSegments) {
            clearSegments();
        }
    }

    public void resetWithString(String value) {
        this._inputBuffer = null;
        this._inputStart = -1;
        this._inputLen = 0;
        this._resultString = value;
        this._resultArray = null;
        if (this._hasSegments) {
            clearSegments();
        }
        this._currentSize = 0;
    }

    public void setCurrentLength(int len) {
        this._currentSize = len;
    }

    public int size() {
        if (this._inputStart >= 0) {
            return this._inputLen;
        }
        if (this._resultArray != null) {
            return this._resultArray.length;
        }
        return this._resultString != null ? this._resultString.length() : this._segmentSize + this._currentSize;
    }

    public String toString() {
        return contentsAsString();
    }
}