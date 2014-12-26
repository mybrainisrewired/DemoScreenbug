package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import com.wmt.util.ColorBaseSlot;
import java.io.IOException;
import java.io.OutputStream;
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
import org.codehaus.jackson.util.CharTypes;

public class Utf8Generator extends JsonGeneratorBase {
    private static final byte BYTE_0 = (byte) 48;
    private static final byte BYTE_BACKSLASH = (byte) 92;
    private static final byte BYTE_COLON = (byte) 58;
    private static final byte BYTE_COMMA = (byte) 44;
    private static final byte BYTE_LBRACKET = (byte) 91;
    private static final byte BYTE_LCURLY = (byte) 123;
    private static final byte BYTE_QUOTE = (byte) 34;
    private static final byte BYTE_RBRACKET = (byte) 93;
    private static final byte BYTE_RCURLY = (byte) 125;
    private static final byte BYTE_SPACE = (byte) 32;
    private static final byte BYTE_u = (byte) 117;
    private static final byte[] FALSE_BYTES;
    static final byte[] HEX_CHARS;
    private static final int MAX_BYTES_TO_BUFFER = 512;
    private static final byte[] NULL_BYTES;
    protected static final int SURR1_FIRST = 55296;
    protected static final int SURR1_LAST = 56319;
    protected static final int SURR2_FIRST = 56320;
    protected static final int SURR2_LAST = 57343;
    private static final byte[] TRUE_BYTES;
    protected static final int[] sOutputEscapes;
    protected boolean _bufferRecyclable;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected CharacterEscapes _characterEscapes;
    protected byte[] _entityBuffer;
    protected final IOContext _ioContext;
    protected int _maximumNonEscapedChar;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected int[] _outputEscapes;
    protected final int _outputMaxContiguous;
    protected final OutputStream _outputStream;
    protected int _outputTail;

    static {
        HEX_CHARS = CharTypes.copyHexBytes();
        NULL_BYTES = new byte[]{(byte) 110, (byte) 117, (byte) 108, (byte) 108};
        TRUE_BYTES = new byte[]{(byte) 116, (byte) 114, (byte) 117, (byte) 101};
        FALSE_BYTES = new byte[]{(byte) 102, (byte) 97, (byte) 108, (byte) 115, (byte) 101};
        sOutputEscapes = CharTypes.get7BitOutputEscapes();
    }

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out) {
        super(features, codec);
        this._outputEscapes = sOutputEscapes;
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (isEnabled(Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
    }

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable) {
        super(features, codec);
        this._outputEscapes = sOutputEscapes;
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = outputOffset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (isEnabled(Feature.ESCAPE_NON_ASCII)) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
    }

    private int _handleLongCustomEscape(byte[] outputBuffer, int outputPtr, int outputEnd, byte[] raw, int remainingChars) throws IOException, JsonGenerationException {
        int len = raw.length;
        if (outputPtr + len > outputEnd) {
            this._outputTail = outputPtr;
            _flushBuffer();
            outputPtr = this._outputTail;
            if (len > outputBuffer.length) {
                this._outputStream.write(raw, 0, len);
                return outputPtr;
            } else {
                System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
                outputPtr += len;
            }
        }
        if (remainingChars * 6 + outputPtr <= outputEnd) {
            return outputPtr;
        }
        _flushBuffer();
        return this._outputTail;
    }

    private final int _outputMultiByteChar(int ch, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        int outputPtr2;
        if (ch < 55296 || ch > 57343) {
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch >> 12) | 224);
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = (byte) (((ch >> 6) & 63) | 128);
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch & 63) | 128);
            return outputPtr2;
        } else {
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = (byte) 92;
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = (byte) 117;
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = HEX_CHARS[(ch >> 12) & 15];
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = HEX_CHARS[(ch >> 8) & 15];
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = HEX_CHARS[(ch >> 4) & 15];
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = HEX_CHARS[ch & 15];
            return outputPtr;
        }
    }

    private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen) throws IOException {
        if (ch < 55296 || ch > 57343) {
            byte[] bbuf = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 12) | 224);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) (((ch >> 6) & 63) | 128);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | 128);
            return inputOffset;
        } else {
            if (inputOffset >= inputLen) {
                _reportError("Split surrogate on writeRaw() input (last character)");
            }
            _outputSurrogates(ch, cbuf[inputOffset]);
            return inputOffset + 1;
        }
    }

    private final void _writeBytes(byte[] bytes) throws IOException {
        int len = bytes.length;
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > 512) {
                this._outputStream.write(bytes, 0, len);
                return;
            }
        }
        System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException {
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > 512) {
                this._outputStream.write(bytes, offset, len);
                return;
            }
        }
        System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private int _writeCustomEscape(byte[] outputBuffer, int outputPtr, SerializableString esc, int remainingChars) throws IOException, JsonGenerationException {
        byte[] raw = esc.asUnquotedUTF8();
        int len = raw.length;
        if (len > 6) {
            return _handleLongCustomEscape(outputBuffer, outputPtr, this._outputEnd, raw, remainingChars);
        } else {
            System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
            return outputPtr + len;
        }
    }

    private final void _writeCustomStringSegment2(char[] cbuf, int offset, int end) throws IOException, JsonGenerationException {
        if (this._outputTail + (end - offset) * 6 > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar <= 0 ? ColorBaseSlot.CB_INDEX_INVALID : this._maximumNonEscapedChar;
        CharacterEscapes customEscapes = this._characterEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            SerializableString esc;
            if (ch <= 127) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = (byte) 92;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else if (escape == -2) {
                        esc = customEscapes.getEscapeSequence(ch);
                        if (esc == null) {
                            throw new JsonGenerationException("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
                        }
                        outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                esc = customEscapes.getEscapeSequence(ch);
                if (esc != null) {
                    outputPtr2 = _writeCustomEscape(outputBuffer, outputPtr2, esc, end - offset);
                    offset2 = offset;
                } else {
                    if (ch <= 2047) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch & 63) | 128);
                        outputPtr = outputPtr2;
                    } else {
                        outputPtr = _outputMultiByteChar(ch, outputPtr2);
                    }
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private int _writeGenericEscape(int charToEscape, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        int outputPtr2 = outputPtr + 1;
        bbuf[outputPtr] = (byte) 92;
        outputPtr = outputPtr2 + 1;
        bbuf[outputPtr2] = (byte) 117;
        if (charToEscape > 255) {
            int hi = (charToEscape >> 8) & 255;
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = HEX_CHARS[hi >> 4];
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = HEX_CHARS[hi & 15];
            charToEscape &= 255;
        } else {
            outputPtr2 = outputPtr + 1;
            bbuf[outputPtr] = (byte) 48;
            outputPtr = outputPtr2 + 1;
            bbuf[outputPtr2] = (byte) 48;
        }
        outputPtr2 = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[charToEscape >> 4];
        outputPtr = outputPtr2 + 1;
        bbuf[outputPtr2] = HEX_CHARS[charToEscape & 15];
        return outputPtr;
    }

    private final void _writeLongString(String text) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        _writeStringSegments(text);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    private final void _writeLongString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        _writeStringSegments(this._charBuffer, 0, len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    private final void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, JsonWriteContext.STATUS_EXPECT_VALUE);
        this._outputTail += 4;
    }

    private final void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = (byte) 34;
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = (byte) 34;
    }

    private final void _writeQuotedLong(long l) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    private final void _writeQuotedRaw(Object value) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        writeRaw(value.toString());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    private final void _writeSegmentedRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int end = this._outputEnd;
        byte[] bbuf = this._outputBuffer;
        while (offset < len) {
            do {
                int ch = cbuf[offset];
                int i;
                if (ch >= 128) {
                    if (this._outputTail + 3 >= this._outputEnd) {
                        _flushBuffer();
                    }
                    int offset2 = offset + 1;
                    char ch2 = cbuf[offset];
                    if (ch2 < '\u0800') {
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bbuf[i] = (byte) ((ch2 >> 6) | 192);
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bbuf[i] = (byte) ((ch2 & 63) | 128);
                    } else {
                        _outputRawMultiByteChar(ch2, cbuf, offset2, len);
                    }
                    offset = offset2;
                } else {
                    if (this._outputTail >= end) {
                        _flushBuffer();
                    }
                    i = this._outputTail;
                    this._outputTail = i + 1;
                    bbuf[i] = (byte) ch;
                    offset++;
                }
            } while (offset < len);
            return;
        }
    }

    private final void _writeStringSegment(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        len += offset;
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr2 = outputPtr;
        while (offset < len) {
            int ch = cbuf[offset];
            if (ch > 127 || escCodes[ch] != 0) {
                break;
            }
            outputPtr = outputPtr2 + 1;
            outputBuffer[outputPtr2] = (byte) ch;
            offset++;
            outputPtr2 = outputPtr;
        }
        this._outputTail = outputPtr2;
        if (offset >= len) {
            return;
        }
        if (this._characterEscapes != null) {
            _writeCustomStringSegment2(cbuf, offset, len);
        } else if (this._maximumNonEscapedChar == 0) {
            _writeStringSegment2(cbuf, offset, len);
        } else {
            _writeStringSegmentASCII2(cbuf, offset, len);
        }
    }

    private final void _writeStringSegment2(char[] cbuf, int offset, int end) throws IOException, JsonGenerationException {
        if (this._outputTail + (end - offset) * 6 > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            if (ch > 127) {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | 128);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else if (escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ch;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) 92;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    offset2 = offset;
                } else {
                    outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeStringSegmentASCII2(char[] cbuf, int offset, int end) throws IOException, JsonGenerationException {
        if (this._outputTail + (end - offset) * 6 > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        int maxUnescaped = this._maximumNonEscapedChar;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            if (ch <= 127) {
                if (escCodes[ch] == 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ch;
                    outputPtr2 = outputPtr;
                    offset2 = offset;
                } else {
                    int escape = escCodes[ch];
                    if (escape > 0) {
                        outputPtr = outputPtr2 + 1;
                        outputBuffer[outputPtr2] = (byte) 92;
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) escape;
                        offset2 = offset;
                    } else {
                        outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                        offset2 = offset;
                    }
                }
            } else if (ch > maxUnescaped) {
                outputPtr2 = _writeGenericEscape(ch, outputPtr2);
                offset2 = offset;
            } else {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | 192);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | 128);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeStringSegments(String text) throws IOException, JsonGenerationException {
        int left = text.length();
        int offset = 0;
        char[] cbuf = this._charBuffer;
        while (left > 0) {
            int len = Math.min(this._outputMaxContiguous, left);
            text.getChars(offset, offset + len, cbuf, 0);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, 0, len);
            offset += len;
            left -= len;
        }
    }

    private final void _writeStringSegments(char[] cbuf, int offset, int totalLen) throws IOException, JsonGenerationException {
        int len;
        do {
            len = Math.min(this._outputMaxContiguous, totalLen);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, offset, len);
            offset += len;
        } while (totalLen - len > 0);
    }

    private final void _writeUTF8Segment(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int ptr;
        int[] escCodes = this._outputEscapes;
        int end = offset + len;
        int ptr2 = offset;
        while (ptr2 < end) {
            ptr = ptr2 + 1;
            int ch = utf8[ptr2];
            if (ch < 0 || escCodes[ch] == 0) {
                ptr2 = ptr;
            } else {
                _writeUTF8Segment2(utf8, offset, len);
                return;
            }
        }
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeUTF8Segment2(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int outputPtr = this._outputTail;
        if (len * 6 + outputPtr > this._outputEnd) {
            _flushBuffer();
            outputPtr = this._outputTail;
        }
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = this._outputEscapes;
        len += offset;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < len) {
            offset = offset2 + 1;
            byte b = utf8[offset2];
            byte ch = b;
            if (ch < 0 || escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = b;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) 92;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _writeGenericEscape(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen) throws IOException, JsonGenerationException {
        int len;
        do {
            len = Math.min(this._outputMaxContiguous, totalLen);
            _writeUTF8Segment(utf8, offset, len);
            offset += len;
        } while (totalLen - len > 0);
    }

    protected final int _decodeSurrogate(int surr1, int surr2) throws IOException {
        if (surr2 < 56320 || surr2 > 57343) {
            _reportError("Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2));
        }
        return 65536 + (surr1 - 55296) << 10 + surr2 - 56320;
    }

    protected final void _flushBuffer() throws IOException {
        int len = this._outputTail;
        if (len > 0) {
            this._outputTail = 0;
            this._outputStream.write(this._outputBuffer, 0, len);
        }
    }

    protected final void _outputSurrogates(int surr1, int surr2) throws IOException {
        int c = _decodeSurrogate(surr1, surr2);
        if (this._outputTail + 4 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c >> 18) | 240);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 12) & 63) | 128);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 6) & 63) | 128);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c & 63) | 128);
    }

    protected void _releaseBuffers() {
        byte[] buf = this._outputBuffer;
        if (buf != null && this._bufferRecyclable) {
            this._outputBuffer = null;
            this._ioContext.releaseWriteEncodingBuffer(buf);
        }
        char[] cbuf = this._charBuffer;
        if (cbuf != null) {
            this._charBuffer = null;
            this._ioContext.releaseConcatBuffer(cbuf);
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
            byte b;
            switch (status) {
                case LocalAudioAll.SORT_BY_DATE:
                    b = BYTE_COMMA;
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    b = BYTE_COLON;
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    b = BYTE_SPACE;
                    break;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = b;
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
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 92;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 110;
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

    protected final void _writeFieldName(String name) throws IOException, JsonGenerationException {
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
            int len = name.length();
            if (len <= this._charBufferLength) {
                name.getChars(0, len, this._charBuffer, 0);
                if (len <= this._outputMaxContiguous) {
                    if (this._outputTail + len > this._outputEnd) {
                        _flushBuffer();
                    }
                    _writeStringSegment(this._charBuffer, 0, len);
                } else {
                    _writeStringSegments(this._charBuffer, 0, len);
                }
            } else {
                _writeStringSegments(name);
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
        } else {
            _writeStringSegments(name);
        }
    }

    protected final void _writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        byte[] raw = name.asQuotedUTF8();
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
            int len = raw.length;
            if (this._outputTail + len + 1 < this._outputEnd) {
                System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, len);
                this._outputTail += len;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 34;
            } else {
                _writeBytes(raw);
                if (this._outputTail >= this._outputEnd) {
                    _flushBuffer();
                }
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 34;
            }
        } else {
            _writeBytes(raw);
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
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
            int len = name.length();
            if (len <= this._charBufferLength) {
                name.getChars(0, len, this._charBuffer, 0);
                if (len <= this._outputMaxContiguous) {
                    if (this._outputTail + len > this._outputEnd) {
                        _flushBuffer();
                    }
                    _writeStringSegment(this._charBuffer, 0, len);
                } else {
                    _writeStringSegments(this._charBuffer, 0, len);
                }
            } else {
                _writeStringSegments(name);
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
        } else {
            _writeStringSegments(name);
        }
    }

    protected final void _writePPFieldName(SerializableString name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        boolean addQuotes = isEnabled(Feature.QUOTE_FIELD_NAMES);
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
        }
        _writeBytes(name.asQuotedUTF8());
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 34;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8Generator.close():void");
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
        r1 = r2._outputStream;
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
        r1 = r2._outputStream;
        r1.close();
    L_0x0043:
        r2._releaseBuffers();
        return;
    L_0x0047:
        r1 = org.codehaus.jackson.JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM;
        r1 = r2.isEnabled(r1);
        if (r1 == 0) goto L_0x0043;
    L_0x004f:
        r1 = r2._outputStream;
        r1.flush();
        goto L_0x0043;
        */
    }

    public final void flush() throws IOException {
        _flushBuffer();
        if (this._outputStream != null && isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._outputStream.flush();
        }
    }

    public CharacterEscapes getCharacterEscapes() {
        return this._characterEscapes;
    }

    public int getHighestEscapedChar() {
        return this._maximumNonEscapedChar;
    }

    public Object getOutputTarget() {
        return this._outputStream;
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
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        _writeBinary(b64variant, data, offset, offset + len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
        int len = keyword.length;
        System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
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
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 93;
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
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 125;
        }
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName(name, z);
        } else {
            if (status == 1) {
                if (this._outputTail >= this._outputEnd) {
                    _flushBuffer();
                }
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 44;
            }
            _writeFieldName(name);
        }
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName(name, z);
        } else {
            if (status == 1) {
                if (this._outputTail >= this._outputEnd) {
                    _flushBuffer();
                }
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 44;
            }
            _writeFieldName(name);
        }
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName(name, z);
        } else {
            if (status == 1) {
                if (this._outputTail >= this._outputEnd) {
                    _flushBuffer();
                }
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 44;
            }
            _writeFieldName(name);
        }
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

    public void writeRaw(char ch) throws IOException, JsonGenerationException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i;
        if (ch <= '\u007f') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ch;
        } else if (ch < '\u0800') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 6) | 192);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | 128);
        } else {
            _outputRawMultiByteChar(ch, null, 0, 0);
        }
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        int start = 0;
        int len = text.length();
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(start, start + len2, buf, 0);
            writeRaw(buf, 0, len2);
            start += len2;
            len -= len2;
        }
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(offset, offset + len2, buf, 0);
            writeRaw(buf, 0, len2);
            offset += len2;
            len -= len2;
        }
    }

    public final void writeRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int len3 = len + len + len;
        if (this._outputTail + len3 <= this._outputEnd || this._outputEnd >= len3) {
            _flushBuffer();
        } else {
            _writeSegmentedRaw(cbuf, offset, len);
            return;
        }
        len += offset;
        while (offset < len) {
            do {
                int ch = cbuf[offset];
                byte[] bArr;
                int i;
                if (ch > 127) {
                    int offset2 = offset + 1;
                    char ch2 = cbuf[offset];
                    if (ch2 < '\u0800') {
                        bArr = this._outputBuffer;
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bArr[i] = (byte) ((ch2 >> 6) | 192);
                        bArr = this._outputBuffer;
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bArr[i] = (byte) ((ch2 & 63) | 128);
                    } else {
                        _outputRawMultiByteChar(ch2, cbuf, offset2, len);
                    }
                    offset = offset2;
                } else {
                    bArr = this._outputBuffer;
                    i = this._outputTail;
                    this._outputTail = i + 1;
                    bArr[i] = (byte) ch;
                    offset++;
                }
            } while (offset < len);
            return;
        }
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        _writeBytes(text, offset, length);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
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
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 91;
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
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) 123;
        }
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (text == null) {
            _writeNull();
        } else {
            int len = text.length();
            if (len > this._charBufferLength) {
                _writeLongString(text);
            } else {
                text.getChars(0, len, this._charBuffer, 0);
                if (len > this._outputMaxContiguous) {
                    _writeLongString(this._charBuffer, 0, len);
                } else {
                    if (this._outputTail + len + 2 > this._outputEnd) {
                        _flushBuffer();
                    }
                    byte[] bArr = this._outputBuffer;
                    int i = this._outputTail;
                    this._outputTail = i + 1;
                    bArr[i] = (byte) 34;
                    _writeStringSegment(this._charBuffer, 0, len);
                    bArr = this._outputBuffer;
                    i = this._outputTail;
                    this._outputTail = i + 1;
                    bArr[i] = (byte) 34;
                }
            }
        }
    }

    public final void writeString(SerializableString text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        _writeBytes(text.asQuotedUTF8());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
        } else {
            _writeStringSegments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }

    public final void writeStringField(String fieldName, String value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeString(value);
    }

    public void writeUTF8String(byte[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
        if (len <= this._outputMaxContiguous) {
            _writeUTF8Segment(text, offset, len);
        } else {
            _writeUTF8Segments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) 34;
    }
}