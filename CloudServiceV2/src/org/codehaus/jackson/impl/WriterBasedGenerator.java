package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import com.wmt.util.ColorBaseSlot;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.util.CharTypes;

public final class WriterBasedGenerator extends JsonGeneratorBase {
    protected static final char[] HEX_CHARS;
    protected static final int SHORT_WRITE = 32;
    protected static final int[] sOutputEscapes;
    protected CharacterEscapes _characterEscapes;
    protected SerializableString _currentEscape;
    protected char[] _entityBuffer;
    protected final IOContext _ioContext;
    protected int _maximumNonEscapedChar;
    protected char[] _outputBuffer;
    protected int _outputEnd;
    protected int[] _outputEscapes;
    protected int _outputHead;
    protected int _outputTail;
    protected final Writer _writer;

    static {
        HEX_CHARS = CharTypes.copyHexChars();
        sOutputEscapes = CharTypes.get7BitOutputEscapes();
    }

    public WriterBasedGenerator(IOContext ctxt, int features, ObjectCodec codec, Writer w) {
        super(features, codec);
        this._outputEscapes = sOutputEscapes;
        this._outputHead = 0;
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._writer = w;
        this._outputBuffer = ctxt.allocConcatBuffer();
        this._outputEnd = this._outputBuffer.length;
        if (isEnabled(Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
    }

    private char[] _allocateEntityBuffer() {
        char[] buf = new char[14];
        buf[0] = '\\';
        buf[2] = '\\';
        buf[3] = 'u';
        buf[4] = '0';
        buf[5] = '0';
        buf[8] = '\\';
        buf[9] = 'u';
        this._entityBuffer = buf;
        return buf;
    }

    private final void _appendCharacterEscape(char ch, int escCode) throws IOException, JsonGenerationException {
        if (escCode >= 0) {
            if (this._outputTail + 2 > this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\\';
            cArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = (char) escCode;
        } else if (escCode != -2) {
            if (this._outputTail + 2 > this._outputEnd) {
                _flushBuffer();
            }
            int ptr = this._outputTail;
            char[] buf = this._outputBuffer;
            int ptr2 = ptr + 1;
            buf[ptr] = '\\';
            ptr = ptr2 + 1;
            buf[ptr2] = 'u';
            if (ch > '\u00ff') {
                int hi = (ch >> 8) & 255;
                ptr2 = ptr + 1;
                buf[ptr] = HEX_CHARS[hi >> 4];
                ptr = ptr2 + 1;
                buf[ptr2] = HEX_CHARS[hi & 15];
                ch = (char) (ch & 255);
            } else {
                ptr2 = ptr + 1;
                buf[ptr] = '0';
                ptr = ptr2 + 1;
                buf[ptr2] = '0';
            }
            ptr2 = ptr + 1;
            buf[ptr] = HEX_CHARS[ch >> 4];
            buf[ptr2] = HEX_CHARS[ch & 15];
            this._outputTail = ptr2;
        } else {
            String escape;
            if (this._currentEscape == null) {
                escape = this._characterEscapes.getEscapeSequence(ch).getValue();
            } else {
                escape = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            int len = escape.length();
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
                if (len > this._outputEnd) {
                    this._writer.write(escape);
                    return;
                }
            }
            escape.getChars(0, len, this._outputBuffer, this._outputTail);
            this._outputTail += len;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int _prependOrWriteCharacterEscape(char[] r10_buffer, int r11_ptr, int r12_end, char r13_ch, int r14_escCode) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.WriterBasedGenerator._prependOrWriteCharacterEscape(char[], int, int, char, int):int");
        /*
        r9 = this;
        if (r14 < 0) goto L_0x0028;
    L_0x0002:
        r6 = 1;
        if (r11 <= r6) goto L_0x0014;
    L_0x0005:
        if (r11 >= r12) goto L_0x0014;
    L_0x0007:
        r11 = r11 + -2;
        r6 = 92;
        r10[r11] = r6;
        r6 = r11 + 1;
        r7 = (char) r14;
        r10[r6] = r7;
    L_0x0012:
        r5 = r11;
    L_0x0013:
        return r5;
    L_0x0014:
        r0 = r9._entityBuffer;
        if (r0 != 0) goto L_0x001c;
    L_0x0018:
        r0 = r9._allocateEntityBuffer();
    L_0x001c:
        r6 = 1;
        r7 = (char) r14;
        r0[r6] = r7;
        r6 = r9._writer;
        r7 = 0;
        r8 = 2;
        r6.write(r0, r7, r8);
        goto L_0x0012;
    L_0x0028:
        r6 = -2;
        if (r14 == r6) goto L_0x00e1;
    L_0x002b:
        r6 = 5;
        if (r11 <= r6) goto L_0x0080;
    L_0x002e:
        if (r11 >= r12) goto L_0x0080;
    L_0x0030:
        r11 = r11 + -6;
        r5 = r11 + 1;
        r6 = 92;
        r10[r11] = r6;
        r11 = r5 + 1;
        r6 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r10[r5] = r6;
        r6 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r13 <= r6) goto L_0x0073;
    L_0x0042:
        r6 = r13 >> 8;
        r2 = r6 & 255;
        r5 = r11 + 1;
        r6 = HEX_CHARS;
        r7 = r2 >> 4;
        r6 = r6[r7];
        r10[r11] = r6;
        r11 = r5 + 1;
        r6 = HEX_CHARS;
        r7 = r2 & 15;
        r6 = r6[r7];
        r10[r5] = r6;
        r6 = r13 & 255;
        r13 = (char) r6;
    L_0x005d:
        r5 = r11 + 1;
        r6 = HEX_CHARS;
        r7 = r13 >> 4;
        r6 = r6[r7];
        r10[r11] = r6;
        r6 = HEX_CHARS;
        r7 = r13 & 15;
        r6 = r6[r7];
        r10[r5] = r6;
        r11 = r5 + -5;
    L_0x0071:
        r5 = r11;
        goto L_0x0013;
    L_0x0073:
        r5 = r11 + 1;
        r6 = 48;
        r10[r11] = r6;
        r11 = r5 + 1;
        r6 = 48;
        r10[r5] = r6;
        goto L_0x005d;
    L_0x0080:
        r0 = r9._entityBuffer;
        if (r0 != 0) goto L_0x0088;
    L_0x0084:
        r0 = r9._allocateEntityBuffer();
    L_0x0088:
        r6 = r9._outputTail;
        r9._outputHead = r6;
        r6 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r13 <= r6) goto L_0x00c7;
    L_0x0090:
        r6 = r13 >> 8;
        r2 = r6 & 255;
        r4 = r13 & 255;
        r6 = 10;
        r7 = HEX_CHARS;
        r8 = r2 >> 4;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = 11;
        r7 = HEX_CHARS;
        r8 = r2 & 15;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = 12;
        r7 = HEX_CHARS;
        r8 = r4 >> 4;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = 13;
        r7 = HEX_CHARS;
        r8 = r4 & 15;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = r9._writer;
        r7 = 8;
        r8 = 6;
        r6.write(r0, r7, r8);
        goto L_0x0071;
    L_0x00c7:
        r6 = 6;
        r7 = HEX_CHARS;
        r8 = r13 >> 4;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = 7;
        r7 = HEX_CHARS;
        r8 = r13 & 15;
        r7 = r7[r8];
        r0[r6] = r7;
        r6 = r9._writer;
        r7 = 2;
        r8 = 6;
        r6.write(r0, r7, r8);
        goto L_0x0071;
    L_0x00e1:
        r6 = r9._currentEscape;
        if (r6 != 0) goto L_0x00ff;
    L_0x00e5:
        r6 = r9._characterEscapes;
        r6 = r6.getEscapeSequence(r13);
        r1 = r6.getValue();
    L_0x00ef:
        r3 = r1.length();
        if (r11 < r3) goto L_0x0109;
    L_0x00f5:
        if (r11 >= r12) goto L_0x0109;
    L_0x00f7:
        r11 = r11 - r3;
        r6 = 0;
        r1.getChars(r6, r3, r10, r11);
    L_0x00fc:
        r5 = r11;
        goto L_0x0013;
    L_0x00ff:
        r6 = r9._currentEscape;
        r1 = r6.getValue();
        r6 = 0;
        r9._currentEscape = r6;
        goto L_0x00ef;
    L_0x0109:
        r6 = r9._writer;
        r6.write(r1);
        goto L_0x00fc;
        */
    }

    private final void _prependOrWriteCharacterEscape(char ch, int escCode) throws IOException, JsonGenerationException {
        int ptr;
        char[] buf;
        if (escCode >= 0) {
            if (this._outputTail >= 2) {
                ptr = this._outputTail - 2;
                this._outputHead = ptr;
                int ptr2 = ptr + 1;
                this._outputBuffer[ptr] = '\\';
                this._outputBuffer[ptr2] = (char) escCode;
            } else {
                buf = this._entityBuffer;
                if (buf == null) {
                    buf = _allocateEntityBuffer();
                }
                this._outputHead = this._outputTail;
                buf[1] = (char) escCode;
                this._writer.write(buf, 0, ClassWriter.COMPUTE_FRAMES);
            }
        } else if (escCode == -2) {
            String escape;
            if (this._currentEscape == null) {
                escape = this._characterEscapes.getEscapeSequence(ch).getValue();
            } else {
                escape = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            int len = escape.length();
            if (this._outputTail >= len) {
                ptr = this._outputTail - len;
                this._outputHead = ptr;
                escape.getChars(0, len, this._outputBuffer, ptr);
            } else {
                this._outputHead = this._outputTail;
                this._writer.write(escape);
            }
        } else if (this._outputTail >= 6) {
            buf = this._outputBuffer;
            ptr = this._outputTail - 6;
            this._outputHead = ptr;
            buf[ptr] = '\\';
            ptr++;
            buf[ptr] = 'u';
            if (ch > '\u00ff') {
                hi = (ch >> 8) & 255;
                ptr++;
                buf[ptr] = HEX_CHARS[hi >> 4];
                ptr++;
                buf[ptr] = HEX_CHARS[hi & 15];
                ch = (char) (ch & 255);
            } else {
                ptr++;
                buf[ptr] = '0';
                ptr++;
                buf[ptr] = '0';
            }
            ptr++;
            buf[ptr] = HEX_CHARS[ch >> 4];
            buf[ptr + 1] = HEX_CHARS[ch & 15];
        } else {
            buf = this._entityBuffer;
            if (buf == null) {
                buf = _allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            if (ch > '\u00ff') {
                hi = (ch >> 8) & 255;
                int lo = ch & 255;
                buf[10] = HEX_CHARS[hi >> 4];
                buf[11] = HEX_CHARS[hi & 15];
                buf[12] = HEX_CHARS[lo >> 4];
                buf[13] = HEX_CHARS[lo & 15];
                this._writer.write(buf, Type.DOUBLE, FragmentManagerImpl.ANIM_STYLE_FADE_EXIT);
            } else {
                buf[6] = HEX_CHARS[ch >> 4];
                buf[7] = HEX_CHARS[ch & 15];
                this._writer.write(buf, ClassWriter.COMPUTE_FRAMES, FragmentManagerImpl.ANIM_STYLE_FADE_EXIT);
            }
        }
    }

    private void _writeLongString(String text) throws IOException, JsonGenerationException {
        _flushBuffer();
        int textLen = text.length();
        int offset = 0;
        do {
            int segmentLen;
            int max = this._outputEnd;
            if (offset + max > textLen) {
                segmentLen = textLen - offset;
            } else {
                segmentLen = max;
            }
            text.getChars(offset, offset + segmentLen, this._outputBuffer, 0);
            if (this._characterEscapes != null) {
                _writeSegmentCustom(segmentLen);
            } else if (this._maximumNonEscapedChar != 0) {
                _writeSegmentASCII(segmentLen, this._maximumNonEscapedChar);
            } else {
                _writeSegment(segmentLen);
            }
            offset += segmentLen;
        } while (offset < textLen);
    }

    private final void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        int ptr = this._outputTail;
        char[] buf = this._outputBuffer;
        buf[ptr] = 'n';
        ptr++;
        buf[ptr] = 'u';
        ptr++;
        buf[ptr] = 'l';
        ptr++;
        buf[ptr] = 'l';
        this._outputTail = ptr + 1;
    }

    private final void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr[i2] = '\"';
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        cArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr[i2] = '\"';
    }

    private final void _writeQuotedLong(long l) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        cArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
    }

    private final void _writeQuotedRaw(Object value) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        writeRaw(value.toString());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        cArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
    }

    private final void _writeSegment(int end) throws IOException, JsonGenerationException {
        int[] escCodes = this._outputEscapes;
        char escLen = escCodes.length;
        int ptr = 0;
        int start = 0;
        while (ptr < end) {
            char c;
            do {
                c = this._outputBuffer[ptr];
                if (c < escLen && escCodes[c] != 0) {
                    break;
                }
                ptr++;
            } while (ptr < end);
            int flushLen = ptr - start;
            if (flushLen > 0) {
                this._writer.write(this._outputBuffer, start, flushLen);
                if (ptr >= end) {
                    return;
                }
            }
            ptr++;
            start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, escCodes[c]);
        }
    }

    private final void _writeSegmentASCII(int end, char maxNonEscaped) throws IOException, JsonGenerationException {
        int[] escCodes = this._outputEscapes;
        char escLimit = Math.min(escCodes.length, this._maximumNonEscapedChar + 1);
        int ptr = 0;
        int i = 0;
        int start = 0;
        while (ptr < end) {
            char c;
            do {
                c = this._outputBuffer[ptr];
                if (c < escLimit) {
                    i = escCodes[c];
                    if (i != 0) {
                        break;
                    }
                    ptr++;
                } else {
                    if (c > maxNonEscaped) {
                        i = -1;
                        break;
                    }
                    ptr++;
                }
            } while (ptr < end);
            int flushLen = ptr - start;
            if (flushLen > 0) {
                this._writer.write(this._outputBuffer, start, flushLen);
                if (ptr >= end) {
                    return;
                }
            }
            ptr++;
            start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, i);
        }
    }

    private final void _writeSegmentCustom(int end) throws IOException, JsonGenerationException {
        int[] escCodes = this._outputEscapes;
        char maxNonEscaped = this._maximumNonEscapedChar < 1 ? ColorBaseSlot.CB_INDEX_INVALID : this._maximumNonEscapedChar;
        char escLimit = Math.min(escCodes.length, this._maximumNonEscapedChar + 1);
        CharacterEscapes customEscapes = this._characterEscapes;
        int ptr = 0;
        int i = 0;
        int start = 0;
        while (ptr < end) {
            char c;
            do {
                c = this._outputBuffer[ptr];
                if (c < escLimit) {
                    i = escCodes[c];
                    if (i != 0) {
                        break;
                    }
                    ptr++;
                } else if (c > maxNonEscaped) {
                    i = -1;
                    break;
                } else {
                    SerializableString escapeSequence = customEscapes.getEscapeSequence(c);
                    this._currentEscape = escapeSequence;
                    if (escapeSequence != null) {
                        i = CharacterEscapes.ESCAPE_CUSTOM;
                        break;
                    }
                    ptr++;
                }
            } while (ptr < end);
            int flushLen = ptr - start;
            if (flushLen > 0) {
                this._writer.write(this._outputBuffer, start, flushLen);
                if (ptr >= end) {
                    return;
                }
            }
            ptr++;
            start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, i);
        }
    }

    private void _writeString(String text) throws IOException, JsonGenerationException {
        int len = text.length();
        if (len > this._outputEnd) {
            _writeLongString(text);
        } else {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            text.getChars(0, len, this._outputBuffer, this._outputTail);
            if (this._characterEscapes != null) {
                _writeStringCustom(len);
            } else if (this._maximumNonEscapedChar != 0) {
                _writeStringASCII(len, this._maximumNonEscapedChar);
            } else {
                _writeString2(len);
            }
        }
    }

    private final void _writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        if (this._characterEscapes != null) {
            _writeStringCustom(text, offset, len);
        } else if (this._maximumNonEscapedChar != 0) {
            _writeStringASCII(text, offset, len, this._maximumNonEscapedChar);
        } else {
            len += offset;
            int[] escCodes = this._outputEscapes;
            char escLen = escCodes.length;
            while (offset < len) {
                char c;
                int offset2;
                int start = offset;
                do {
                    c = text[offset];
                    if (c < escLen && escCodes[c] != 0) {
                        offset2 = offset;
                        break;
                    } else {
                        offset++;
                    }
                } while (offset < len);
                offset2 = offset;
                int newAmount = offset2 - start;
                if (newAmount < 32) {
                    if (this._outputTail + newAmount > this._outputEnd) {
                        _flushBuffer();
                    }
                    if (newAmount > 0) {
                        System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
                        this._outputTail += newAmount;
                    }
                } else {
                    _flushBuffer();
                    this._writer.write(text, start, newAmount);
                }
                if (offset2 >= len) {
                    return;
                } else {
                    offset = offset2 + 1;
                    c = text[offset2];
                    _appendCharacterEscape(c, escCodes[c]);
                }
            }
        }
    }

    private void _writeString2(int len) throws IOException, JsonGenerationException {
        int end = this._outputTail + len;
        int[] escCodes = this._outputEscapes;
        char escLen = escCodes.length;
        while (this._outputTail < end) {
            int i;
            do {
                char c = this._outputBuffer[this._outputTail];
                if (c >= escLen || escCodes[c] == 0) {
                    i = this._outputTail + 1;
                    this._outputTail = i;
                } else {
                    int flushLen = this._outputTail - this._outputHead;
                    if (flushLen > 0) {
                        this._writer.write(this._outputBuffer, this._outputHead, flushLen);
                    }
                    char[] cArr = this._outputBuffer;
                    int i2 = this._outputTail;
                    this._outputTail = i2 + 1;
                    c = cArr[i2];
                    _prependOrWriteCharacterEscape(c, escCodes[c]);
                }
            } while (i < end);
            return;
        }
    }

    private void _writeStringASCII(int len, char maxNonEscaped) throws IOException, JsonGenerationException {
        int end = this._outputTail + len;
        int[] escCodes = this._outputEscapes;
        char escLimit = Math.min(escCodes.length, this._maximumNonEscapedChar + 1);
        while (this._outputTail < end) {
            int i;
            do {
                int escCode;
                char c = this._outputBuffer[this._outputTail];
                if (c < escLimit) {
                    escCode = escCodes[c];
                    if (escCode != 0) {
                    }
                    i = this._outputTail + 1;
                    this._outputTail = i;
                } else {
                    if (c > maxNonEscaped) {
                        escCode = -1;
                    }
                    i = this._outputTail + 1;
                    this._outputTail = i;
                }
                int flushLen = this._outputTail - this._outputHead;
                if (flushLen > 0) {
                    this._writer.write(this._outputBuffer, this._outputHead, flushLen);
                }
                this._outputTail++;
                _prependOrWriteCharacterEscape(c, escCode);
            } while (i < end);
            return;
        }
    }

    private final void _writeStringASCII(char[] text, int offset, int len, char maxNonEscaped) throws IOException, JsonGenerationException {
        len += offset;
        int[] escCodes = this._outputEscapes;
        char escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
        int i = 0;
        while (offset < len) {
            int start = offset;
            do {
                char c = text[offset];
                if (c < escLimit) {
                    i = escCodes[c];
                    if (i != 0) {
                        break;
                    }
                    offset++;
                } else {
                    if (c > maxNonEscaped) {
                        i = -1;
                        break;
                    }
                    offset++;
                }
            } while (offset < len);
            int newAmount = offset - start;
            if (newAmount < 32) {
                if (this._outputTail + newAmount > this._outputEnd) {
                    _flushBuffer();
                }
                if (newAmount > 0) {
                    System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
                    this._outputTail += newAmount;
                }
            } else {
                _flushBuffer();
                this._writer.write(text, start, newAmount);
            }
            if (offset < len) {
                offset++;
                _appendCharacterEscape(c, i);
            } else {
                return;
            }
        }
    }

    private void _writeStringCustom(int len) throws IOException, JsonGenerationException {
        int end = this._outputTail + len;
        int[] escCodes = this._outputEscapes;
        char maxNonEscaped = this._maximumNonEscapedChar < 1 ? ColorBaseSlot.CB_INDEX_INVALID : this._maximumNonEscapedChar;
        char escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
        CharacterEscapes customEscapes = this._characterEscapes;
        while (this._outputTail < end) {
            int i;
            do {
                int escCode;
                char c = this._outputBuffer[this._outputTail];
                if (c < escLimit) {
                    escCode = escCodes[c];
                    if (escCode != 0) {
                    }
                    i = this._outputTail + 1;
                    this._outputTail = i;
                } else if (c > maxNonEscaped) {
                    escCode = -1;
                } else {
                    SerializableString escapeSequence = customEscapes.getEscapeSequence(c);
                    this._currentEscape = escapeSequence;
                    if (escapeSequence != null) {
                        escCode = CharacterEscapes.ESCAPE_CUSTOM;
                    }
                    i = this._outputTail + 1;
                    this._outputTail = i;
                }
                int flushLen = this._outputTail - this._outputHead;
                if (flushLen > 0) {
                    this._writer.write(this._outputBuffer, this._outputHead, flushLen);
                }
                this._outputTail++;
                _prependOrWriteCharacterEscape(c, escCode);
            } while (i < end);
            return;
        }
    }

    private final void _writeStringCustom(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        len += offset;
        int[] escCodes = this._outputEscapes;
        char maxNonEscaped = this._maximumNonEscapedChar < 1 ? ColorBaseSlot.CB_INDEX_INVALID : this._maximumNonEscapedChar;
        char escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
        CharacterEscapes customEscapes = this._characterEscapes;
        int i = 0;
        while (offset < len) {
            int start = offset;
            do {
                char c = text[offset];
                if (c < escLimit) {
                    i = escCodes[c];
                    if (i != 0) {
                        break;
                    }
                    offset++;
                } else if (c > maxNonEscaped) {
                    i = -1;
                    break;
                } else {
                    SerializableString escapeSequence = customEscapes.getEscapeSequence(c);
                    this._currentEscape = escapeSequence;
                    if (escapeSequence != null) {
                        i = CharacterEscapes.ESCAPE_CUSTOM;
                        break;
                    }
                    offset++;
                }
            } while (offset < len);
            int newAmount = offset - start;
            if (newAmount < 32) {
                if (this._outputTail + newAmount > this._outputEnd) {
                    _flushBuffer();
                }
                if (newAmount > 0) {
                    System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
                    this._outputTail += newAmount;
                }
            } else {
                _flushBuffer();
                this._writer.write(text, start, newAmount);
            }
            if (offset < len) {
                offset++;
                _appendCharacterEscape(c, i);
            } else {
                return;
            }
        }
    }

    private void writeRawLong(String text) throws IOException, JsonGenerationException {
        int room = this._outputEnd - this._outputTail;
        text.getChars(0, room, this._outputBuffer, this._outputTail);
        this._outputTail += room;
        _flushBuffer();
        int offset = room;
        int len = text.length() - room;
        while (len > this._outputEnd) {
            int amount = this._outputEnd;
            text.getChars(offset, offset + amount, this._outputBuffer, 0);
            this._outputHead = 0;
            this._outputTail = amount;
            _flushBuffer();
            offset += amount;
            len -= amount;
        }
        text.getChars(offset, offset + len, this._outputBuffer, 0);
        this._outputHead = 0;
        this._outputTail = len;
    }

    protected final void _flushBuffer() throws IOException {
        int len = this._outputTail - this._outputHead;
        if (len > 0) {
            int offset = this._outputHead;
            this._outputHead = 0;
            this._outputTail = 0;
            this._writer.write(this._outputBuffer, offset, len);
        }
    }

    protected void _releaseBuffers() {
        char[] buf = this._outputBuffer;
        if (buf != null) {
            this._outputBuffer = null;
            this._ioContext.releaseConcatBuffer(buf);
        }
    }

    protected final void _verifyPrettyValueWrite(String typeMsg, int status) throws IOException, JsonGenerationException {
        switch (status) {
            case LocalAudioAll.SORT_BY_TITLE:
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                } else if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                }
            case LocalAudioAll.SORT_BY_DATE:
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
            case ClassWriter.COMPUTE_FRAMES:
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
            default:
                _cantHappen();
        }
    }

    protected final void _verifyValueWrite(String typeMsg) throws IOException, JsonGenerationException {
        int status = this._writeContext.writeValue();
        if (status == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            char c;
            switch (status) {
                case LocalAudioAll.SORT_BY_DATE:
                    c = ',';
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    c = ':';
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    c = ' ';
                    break;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = c;
            this._outputTail++;
        } else {
            _verifyPrettyValueWrite(typeMsg, status);
        }
    }

    protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd) throws IOException, JsonGenerationException {
        int safeInputEnd = inputEnd - 3;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        int inputPtr2 = inputPtr;
        while (inputPtr2 <= safeInputEnd) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            this._outputTail = b64variant.encodeBase64Chunk((((input[inputPtr2] << 8) | (input[inputPtr] & 255)) << 8) | (input[inputPtr2] & 255), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                char[] cArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = '\\';
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = 'n';
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr2 = inputPtr;
        }
        int inputLeft = inputEnd - inputPtr2;
        if (inputLeft > 0) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            int b24 = input[inputPtr2] << 16;
            if (inputLeft == 2) {
                b24 |= (input[inputPtr] & 255) << 8;
                inputPtr++;
            }
            this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
        }
    }

    protected void _writeFieldName(String name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name, commaBefore);
        } else {
            char[] cArr;
            int i;
            if (this._outputTail + 1 >= this._outputEnd) {
                _flushBuffer();
            }
            if (commaBefore) {
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = ',';
            }
            if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = '\"';
                _writeString(name);
                if (this._outputTail >= this._outputEnd) {
                    _flushBuffer();
                }
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = '\"';
            } else {
                _writeString(name);
            }
        }
    }

    public void _writeFieldName(SerializableString name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(name, commaBefore);
        } else {
            char[] cArr;
            int i;
            if (this._outputTail + 1 >= this._outputEnd) {
                _flushBuffer();
            }
            if (commaBefore) {
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = ',';
            }
            char[] quoted = name.asQuotedChars();
            if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
                cArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                cArr[i] = '\"';
                int qlen = quoted.length;
                if (this._outputTail + qlen + 1 >= this._outputEnd) {
                    writeRaw(quoted, 0, qlen);
                    if (this._outputTail >= this._outputEnd) {
                        _flushBuffer();
                    }
                    cArr = this._outputBuffer;
                    i = this._outputTail;
                    this._outputTail = i + 1;
                    cArr[i] = '\"';
                } else {
                    System.arraycopy(quoted, 0, this._outputBuffer, this._outputTail, qlen);
                    this._outputTail += qlen;
                    cArr = this._outputBuffer;
                    i = this._outputTail;
                    this._outputTail = i + 1;
                    cArr[i] = '\"';
                }
            } else {
                writeRaw(quoted, 0, quoted.length);
            }
        }
    }

    protected final void _writePPFieldName(String name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
            _writeString(name);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            cArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
        } else {
            _writeString(name);
        }
    }

    protected final void _writePPFieldName(SerializableString name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        char[] quoted = name.asQuotedChars();
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
            writeRaw(quoted, 0, quoted.length);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            cArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
        } else {
            writeRaw(quoted, 0, quoted.length);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.WriterBasedGenerator.close():void");
        /*
        r2 = this;
        super.close();
        r1 = r2._outputBuffer;
        if (r1 == 0) goto L_0x0027;
    L_0x0007:
        r1 = org.codehaus.jackson.JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT;
        r1 = r2.isEnabled(r1);
        if (r1 == 0) goto L_0x0027;
    L_0x000f:
        r0 = r2.getOutputContext();
        r1 = r0.inArray();
        if (r1 == 0) goto L_0x001d;
    L_0x0019:
        r2.writeEndArray();
        goto L_0x000f;
    L_0x001d:
        r1 = r0.inObject();
        if (r1 == 0) goto L_0x0027;
    L_0x0023:
        r2.writeEndObject();
        goto L_0x000f;
    L_0x0027:
        r2._flushBuffer();
        r1 = r2._writer;
        if (r1 == 0) goto L_0x0043;
    L_0x002e:
        r1 = r2._ioContext;
        r1 = r1.isResourceManaged();
        if (r1 != 0) goto L_0x003e;
    L_0x0036:
        r1 = org.codehaus.jackson.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
        r1 = r2.isEnabled(r1);
        if (r1 == 0) goto L_0x0047;
    L_0x003e:
        r1 = r2._writer;
        r1.close();
    L_0x0043:
        r2._releaseBuffers();
        return;
    L_0x0047:
        r1 = org.codehaus.jackson.JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM;
        r1 = r2.isEnabled(r1);
        if (r1 == 0) goto L_0x0043;
    L_0x004f:
        r1 = r2._writer;
        r1.flush();
        goto L_0x0043;
        */
    }

    public final void flush() throws IOException {
        _flushBuffer();
        if (this._writer != null && isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._writer.flush();
        }
    }

    public CharacterEscapes getCharacterEscapes() {
        return this._characterEscapes;
    }

    public int getHighestEscapedChar() {
        return this._maximumNonEscapedChar;
    }

    public Object getOutputTarget() {
        return this._writer;
    }

    public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
        this._characterEscapes = esc;
        if (esc == null) {
            this._outputEscapes = sOutputEscapes;
        } else {
            this._outputEscapes = esc.getEscapeCodesForAscii();
        }
        return this;
    }

    public JsonGenerator setHighestNonEscapedChar(int charCode) {
        if (charCode < 0) {
            charCode = 0;
        }
        this._maximumNonEscapedChar = charCode;
        return this;
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        _writeBinary(b64variant, data, offset, offset + len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        cArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        int ptr = this._outputTail;
        char[] buf = this._outputBuffer;
        if (state) {
            buf[ptr] = 't';
            ptr++;
            buf[ptr] = 'r';
            ptr++;
            buf[ptr] = 'u';
            ptr++;
            buf[ptr] = 'e';
        } else {
            buf[ptr] = 'f';
            ptr++;
            buf[ptr] = 'a';
            ptr++;
            buf[ptr] = 'l';
            ptr++;
            buf[ptr] = 's';
            ptr++;
            buf[ptr] = 'e';
        }
        this._outputTail = ptr + 1;
    }

    public final void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = ']';
        }
        this._writeContext = this._writeContext.getParent();
    }

    public final void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '}';
        }
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status != 1) {
            z = false;
        }
        _writeFieldName(name, z);
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status != 1) {
            z = false;
        }
        _writeFieldName(name, z);
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (status != 1) {
            z = false;
        }
        _writeFieldName(name, z);
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _verifyValueWrite("write null value");
        _writeNull();
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        if (!this._cfgNumbersAsStrings) {
            if (!((Double.isNaN(d) || Double.isInfinite(d)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
                _verifyValueWrite("write number");
                writeRaw(String.valueOf(d));
                return;
            }
        }
        writeString(String.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        if (!this._cfgNumbersAsStrings) {
            if (!((Float.isNaN(f) || Float.isInfinite(f)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
                _verifyValueWrite("write number");
                writeRaw(String.valueOf(f));
                return;
            }
        }
        writeString(String.valueOf(f));
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._outputTail + 11 >= this._outputEnd) {
            _flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            _writeQuotedInt(i);
        } else {
            this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        }
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedLong(l);
        } else {
            if (this._outputTail + 21 >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        }
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(encodedValue);
        } else {
            writeRaw(encodedValue);
        }
    }

    public void writeNumber(BigDecimal value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeNumber(BigInteger value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = c;
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        int len = text.length();
        int room = this._outputEnd - this._outputTail;
        if (room == 0) {
            _flushBuffer();
            room = this._outputEnd - this._outputTail;
        }
        if (room >= len) {
            text.getChars(0, len, this._outputBuffer, this._outputTail);
            this._outputTail += len;
        } else {
            writeRawLong(text);
        }
    }

    public void writeRaw(String text, int start, int len) throws IOException, JsonGenerationException {
        int room = this._outputEnd - this._outputTail;
        if (room < len) {
            _flushBuffer();
            room = this._outputEnd - this._outputTail;
        }
        if (room >= len) {
            text.getChars(start, start + len, this._outputBuffer, this._outputTail);
            this._outputTail += len;
        } else {
            writeRawLong(text.substring(start, start + len));
        }
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        if (len < 32) {
            if (len > this._outputEnd - this._outputTail) {
                _flushBuffer();
            }
            System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
        } else {
            _flushBuffer();
            this._writer.write(text, offset, len);
        }
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public final void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '[';
        }
    }

    public final void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '{';
        }
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (text == null) {
            _writeNull();
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
            _writeString(text);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            cArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
        }
    }

    public final void writeString(SerializableString sstr) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        char[] text = sstr.asQuotedChars();
        int len = text.length;
        if (len < 32) {
            if (len > this._outputEnd - this._outputTail) {
                _flushBuffer();
            }
            System.arraycopy(text, 0, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
        } else {
            _flushBuffer();
            this._writer.write(text, 0, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        cArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        _writeString(text, offset, len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        cArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
    }

    public final void writeStringField(String fieldName, String value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeString(value);
    }

    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }
}