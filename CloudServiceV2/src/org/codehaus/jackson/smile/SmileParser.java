package org.codehaus.jackson.smile;

import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.KeyTouchInputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.impl.StreamBasedParserBase;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;

public class SmileParser extends StreamBasedParserBase {
    private static final int[] NO_INTS;
    private static final String[] NO_STRINGS;
    protected static final ThreadLocal<SoftReference<SmileBufferRecycler<String>>> _smileRecyclerRef;
    protected boolean _got32BitFloat;
    protected boolean _mayContainRawBinary;
    protected ObjectCodec _objectCodec;
    protected int _quad1;
    protected int _quad2;
    protected int[] _quadBuffer;
    protected int _seenNameCount;
    protected String[] _seenNames;
    protected int _seenStringValueCount;
    protected String[] _seenStringValues;
    protected final SmileBufferRecycler<String> _smileBufferRecycler;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    protected int _typeByte;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 4;
        }
    }

    public enum Feature {
        REQUIRE_HEADER(true);
        final boolean _defaultState;
        final int _mask;

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
            this._mask = 1 << ordinal();
        }

        public static int collectDefaults() {
            int flags = 0;
            org.codehaus.jackson.smile.SmileParser.Feature[] arr$ = values();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                org.codehaus.jackson.smile.SmileParser.Feature f = arr$[i$];
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
                i$++;
            }
            return flags;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return this._mask;
        }
    }

    static {
        NO_INTS = new int[0];
        NO_STRINGS = new String[0];
        _smileRecyclerRef = new ThreadLocal();
    }

    public SmileParser(IOContext ctxt, int parserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer sym, InputStream in, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, parserFeatures, in, inputBuffer, start, end, bufferRecyclable);
        this._tokenIncomplete = false;
        this._quadBuffer = NO_INTS;
        this._seenNames = NO_STRINGS;
        this._seenNameCount = 0;
        this._seenStringValues = null;
        this._seenStringValueCount = -1;
        this._objectCodec = codec;
        this._symbols = sym;
        this._tokenInputRow = -1;
        this._tokenInputCol = -1;
        this._smileBufferRecycler = _smileBufferRecycler();
    }

    private final String _addDecodedToSymbols(int len, String name) {
        if (len < 5) {
            return this._symbols.addName(name, this._quad1, 0).getName();
        }
        if (len < 9) {
            return this._symbols.addName(name, this._quad1, this._quad2).getName();
        }
        return this._symbols.addName(name, this._quadBuffer, (len + 3) >> 2).getName();
    }

    private final void _addSeenStringValue() throws IOException, JsonParseException {
        _finishToken();
        if (this._seenStringValueCount < this._seenStringValues.length) {
            String[] strArr = this._seenStringValues;
            int i = this._seenStringValueCount;
            this._seenStringValueCount = i + 1;
            strArr[i] = this._textBuffer.contentsAsString();
        } else {
            _expandSeenStringValues();
        }
    }

    private final void _decodeLongAscii() throws IOException, JsonParseException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            int inPtr = this._inputPtr;
            int left = this._inputEnd - inPtr;
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            left = Math.min(left, outBuf.length - outPtr);
            while (true) {
                int inPtr2 = inPtr + 1;
                byte b = this._inputBuffer[inPtr];
                if (b == (byte) -4) {
                    this._inputPtr = inPtr2;
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                } else {
                    int outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) b;
                    left--;
                    if (left <= 0) {
                        this._inputPtr = inPtr2;
                        outPtr = outPtr2;
                    } else {
                        inPtr = inPtr2;
                        outPtr = outPtr2;
                    }
                }
            }
        }
    }

    private final void _decodeLongUnicode() throws IOException, JsonParseException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr;
            int outPtr2;
            int ptr2 = this._inputPtr;
            if (ptr2 >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr2 = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = ptr2 + outBuf.length - outPtr;
            if (max2 < max) {
                max = max2;
                ptr = ptr2;
                outPtr2 = outPtr;
            } else {
                ptr = ptr2;
                outPtr2 = outPtr;
            }
            while (ptr < max) {
                ptr2 = ptr + 1;
                int c = inputBuffer[ptr] & 255;
                if (codes[c] != 0) {
                    this._inputPtr = ptr2;
                    if (c == 252) {
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    } else {
                        switch (codes[c]) {
                            case LocalAudioAll.SORT_BY_DATE:
                                c = _decodeUtf8_2(c);
                                outPtr = outPtr2;
                                break;
                            case ClassWriter.COMPUTE_FRAMES:
                                if (this._inputEnd - this._inputPtr >= 2) {
                                    c = _decodeUtf8_3fast(c);
                                    outPtr = outPtr2;
                                } else {
                                    c = _decodeUtf8_3(c);
                                    outPtr = outPtr2;
                                }
                                break;
                            case JsonWriteContext.STATUS_EXPECT_VALUE:
                                c = _decodeUtf8_4(c);
                                outPtr = outPtr2 + 1;
                                outBuf[outPtr2] = (char) (55296 | (c >> 10));
                                if (outPtr >= outBuf.length) {
                                    outBuf = this._textBuffer.finishCurrentSegment();
                                    outPtr = 0;
                                }
                                c = 56320 | (c & 1023);
                                break;
                            default:
                                _reportInvalidChar(c);
                                outPtr = outPtr2;
                                break;
                        }
                        if (outPtr >= outBuf.length) {
                            outBuf = this._textBuffer.finishCurrentSegment();
                            outPtr = 0;
                        }
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (char) c;
                        outPtr = outPtr2;
                    }
                } else {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (char) c;
                    ptr = ptr2;
                    outPtr2 = outPtr;
                }
            }
            this._inputPtr = ptr;
            outPtr = outPtr2;
        }
    }

    private final Name _decodeLongUnicodeName(int[] quads, int byteLen, int quadLen) throws IOException, JsonParseException {
        int lastQuad;
        int lastQuadBytes = byteLen & 3;
        if (lastQuadBytes < 4) {
            lastQuad = quads[quadLen - 1];
            quads[quadLen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
        } else {
            lastQuad = 0;
        }
        char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
        int ix = 0;
        int cix = 0;
        while (ix < byteLen) {
            int cix2;
            int ch = (quads[ix >> 2] >> ((3 - (ix & 3)) << 3)) & 255;
            ix++;
            if (ch > 127) {
                int needed;
                if ((ch & 224) == 192) {
                    ch &= 31;
                    needed = 1;
                } else if ((ch & 240) == 224) {
                    ch &= 15;
                    needed = ClassWriter.COMPUTE_FRAMES;
                } else if ((ch & 248) == 240) {
                    ch &= 7;
                    needed = JsonWriteContext.STATUS_OK_AFTER_SPACE;
                } else {
                    _reportInvalidInitial(ch);
                    ch = 1;
                    needed = 1;
                }
                if (ix + needed > byteLen) {
                    _reportInvalidEOF(" in long field name");
                }
                int ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                ix++;
                if ((ch2 & 192) != 128) {
                    _reportInvalidOther(ch2);
                }
                ch = (ch << 6) | (ch2 & 63);
                if (needed > 1) {
                    ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                    ix++;
                    if ((ch2 & 192) != 128) {
                        _reportInvalidOther(ch2);
                    }
                    ch = (ch << 6) | (ch2 & 63);
                    if (needed > 2) {
                        ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                        ix++;
                        if ((ch2 & 192) != 128) {
                            _reportInvalidOther(ch2 & 255);
                        }
                        ch = (ch << 6) | (ch2 & 63);
                    }
                }
                if (needed > 2) {
                    ch -= 65536;
                    if (cix >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    cix2 = cix + 1;
                    cbuf[cix] = (char) (55296 + ch >> 10);
                    ch = 56320 | (ch & 1023);
                    if (cix2 >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    cix = cix2 + 1;
                    cbuf[cix2] = (char) ch;
                }
            }
            cix2 = cix;
            if (cix2 >= cbuf.length) {
                cbuf = this._textBuffer.expandCurrentSegment();
            }
            cix = cix2 + 1;
            cbuf[cix2] = (char) ch;
        }
        String baseName = new String(cbuf, 0, cix);
        if (lastQuadBytes < 4) {
            quads[quadLen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, quadLen);
    }

    private final String _decodeShortAsciiName(int len) throws IOException, JsonParseException {
        int outPtr;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr = this._inputPtr;
        int inEnd = inPtr + len - 3;
        int inPtr2 = inPtr;
        int outPtr2 = 0;
        while (inPtr2 < inEnd) {
            outPtr = outPtr2 + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr2] = (char) inBuf[inPtr2];
            outPtr2 = outPtr + 1;
            inPtr2 = inPtr + 1;
            outBuf[outPtr] = (char) inBuf[inPtr];
            outPtr = outPtr2 + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr2] = (char) inBuf[inPtr2];
            outPtr2 = outPtr + 1;
            inPtr2 = inPtr + 1;
            outBuf[outPtr] = (char) inBuf[inPtr];
        }
        int left = len & 3;
        if (left > 0) {
            outPtr = outPtr2 + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr2] = (char) inBuf[inPtr2];
            if (left > 1) {
                outPtr2 = outPtr + 1;
                inPtr2 = inPtr + 1;
                outBuf[outPtr] = (char) inBuf[inPtr];
                if (left > 2) {
                    outPtr = outPtr2 + 1;
                    inPtr = inPtr2 + 1;
                    outBuf[outPtr2] = (char) inBuf[inPtr2];
                }
            }
            this._inputPtr = inPtr;
            this._textBuffer.setCurrentLength(len);
            return this._textBuffer.contentsAsString();
        }
        inPtr = inPtr2;
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
        return this._textBuffer.contentsAsString();
    }

    private final String _decodeShortUnicodeName(int len) throws IOException, JsonParseException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr = 0;
        while (inPtr2 < end) {
            int outPtr2;
            inPtr = inPtr2 + 1;
            int i = inBuf[inPtr2] & 255;
            int code = codes[i];
            if (code != 0) {
                switch (code) {
                    case LocalAudioAll.SORT_BY_DATE:
                        i = ((i & 31) << 6) | (inBuf[inPtr] & 63);
                        inPtr++;
                        outPtr2 = outPtr;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        i = (((i & 15) << 12) | ((inBuf[inPtr] & 63) << 6)) | (inBuf[inPtr2] & 63);
                        outPtr2 = outPtr;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        inPtr2 = inPtr + 1;
                        i = ((((i & 7) << 18) | ((inBuf[inPtr] & 63) << 12)) | ((inBuf[inPtr2] & 63) << 6)) | (inBuf[inPtr] & 63) - 65536;
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (char) (55296 | (i >> 10));
                        i = 56320 | (i & 1023);
                        inPtr = inPtr2;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i) + " in short Unicode text block");
                        break;
                }
            }
            outPtr2 = outPtr;
            outPtr = outPtr2 + 1;
            outBuf[outPtr2] = (char) i;
            inPtr2 = inPtr;
        }
        this._textBuffer.setCurrentLength(outPtr);
        return this._textBuffer.contentsAsString();
    }

    private final int _decodeUtf8_2(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        return ((c & 31) << 6) | (d & 63);
    }

    private final int _decodeUtf8_3(int c1) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_3fast(int c1) throws IOException, JsonParseException {
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_4(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        c = ((c & 7) << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        c = (c << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
        return (c << 6) | (d & 63) - 65536;
    }

    private final String[] _expandSeenNames(String[] oldShared) {
        int newSize = SmileConstants.MAX_SHARED_STRING_VALUES;
        int len = oldShared.length;
        String[] newShared;
        if (len == 0) {
            newShared = (String[]) this._smileBufferRecycler.allocSeenNamesBuffer();
            return newShared == null ? new String[64] : newShared;
        } else if (len == 1024) {
            newShared = oldShared;
            this._seenNameCount = 0;
            return newShared;
        } else {
            if (len == 64) {
                newSize = Opcodes.ACC_NATIVE;
            }
            newShared = new String[newSize];
            System.arraycopy(oldShared, 0, newShared, 0, oldShared.length);
            return newShared;
        }
    }

    private final void _expandSeenStringValues() {
        String[] newShared;
        int newSize = SmileConstants.MAX_SHARED_STRING_VALUES;
        String[] oldShared = this._seenStringValues;
        int len = oldShared.length;
        if (len == 0) {
            newShared = (String[]) this._smileBufferRecycler.allocSeenStringValuesBuffer();
            if (newShared == null) {
                newShared = new String[64];
            }
        } else if (len == 1024) {
            newShared = oldShared;
            this._seenStringValueCount = 0;
        } else {
            if (len == 64) {
                newSize = Opcodes.ACC_NATIVE;
            }
            newShared = new String[newSize];
            System.arraycopy(oldShared, 0, newShared, 0, oldShared.length);
        }
        this._seenStringValues = newShared;
        String[] strArr = this._seenStringValues;
        int i = this._seenStringValueCount;
        this._seenStringValueCount = i + 1;
        strArr[i] = this._textBuffer.contentsAsString();
    }

    private final Name _findDecodedFromSymbols(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        int inPtr;
        byte[] inBuf;
        if (len < 5) {
            inPtr = this._inputPtr;
            inBuf = this._inputBuffer;
            int q = inBuf[inPtr] & 255;
            len--;
            if (len > 0) {
                inPtr++;
                q = q << 8 + inBuf[inPtr] & 255;
                len--;
                if (len > 0) {
                    inPtr++;
                    q = q << 8 + inBuf[inPtr] & 255;
                    if (len - 1 > 0) {
                        q = q << 8 + inBuf[inPtr + 1] & 255;
                    }
                }
            }
            this._quad1 = q;
            return this._symbols.findName(q);
        } else if (len >= 9) {
            return _findDecodedMedium(len);
        } else {
            inPtr = this._inputPtr;
            inBuf = this._inputBuffer;
            inPtr++;
            inPtr++;
            inPtr++;
            int q1 = (((((inBuf[inPtr] & 255) << 8) + (inBuf[inPtr] & 255)) << 8) + (inBuf[inPtr] & 255)) << 8 + inBuf[inPtr] & 255;
            inPtr++;
            int q2 = inBuf[inPtr] & 255;
            len -= 5;
            if (len > 0) {
                inPtr++;
                q2 = q2 << 8 + inBuf[inPtr] & 255;
                len--;
                if (len > 0) {
                    inPtr++;
                    q2 = q2 << 8 + inBuf[inPtr] & 255;
                    if (len - 1 > 0) {
                        q2 = q2 << 8 + inBuf[inPtr + 1] & 255;
                    }
                }
            }
            this._quad1 = q1;
            this._quad2 = q2;
            return this._symbols.findName(q1, q2);
        }
    }

    private final Name _findDecodedMedium(int len) throws IOException, JsonParseException {
        int bufLen = (len + 3) >> 2;
        if (bufLen > this._quadBuffer.length) {
            this._quadBuffer = _growArrayTo(this._quadBuffer, bufLen);
        }
        int offset = 0;
        int inPtr = this._inputPtr;
        byte[] inBuf = this._inputBuffer;
        while (true) {
            int inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            int offset2 = offset + 1;
            this._quadBuffer[offset] = ((((((inBuf[inPtr] & 255) << 8) | (inBuf[inPtr2] & 255)) << 8) | (inBuf[inPtr] & 255)) << 8) | (inBuf[inPtr2] & 255);
            len -= 4;
            if (len <= 3) {
                break;
            }
            offset = offset2;
        }
        if (len > 0) {
            int q = inBuf[inPtr] & 255;
            len--;
            if (len > 0) {
                inPtr++;
                q = q << 8 + inBuf[inPtr] & 255;
                if (len - 1 > 0) {
                    q = q << 8 + inBuf[inPtr + 1] & 255;
                }
            }
            offset = offset2 + 1;
            this._quadBuffer[offset2] = q;
        } else {
            offset = offset2;
        }
        return this._symbols.findName(this._quadBuffer, offset);
    }

    private final void _finishBigDecimal() throws IOException, JsonParseException {
        this._numberBigDecimal = new BigDecimal(new BigInteger(_read7BitBinaryWithLength()), SmileUtil.zigzagDecode(_readUnsignedVInt()));
        this._numTypesValid = 16;
    }

    private final void _finishBigInteger() throws IOException, JsonParseException {
        this._numberBigInt = new BigInteger(_read7BitBinaryWithLength());
        this._numTypesValid = 4;
    }

    private final void _finishDouble() throws IOException, JsonParseException {
        long value = ((long) _fourBytesToInt()) << 28 + ((long) _fourBytesToInt());
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        long j = value << 7;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        value = j + ((long) bArr[i]);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        j = value << 7;
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        this._numberDouble = Double.longBitsToDouble(j + ((long) bArr[i]));
        this._numTypesValid = 8;
    }

    private final void _finishFloat() throws IOException, JsonParseException {
        int i = _fourBytesToInt();
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int i2 = i << 7;
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        this._numberDouble = (double) Float.intBitsToFloat(i2 + bArr[i3]);
        this._numTypesValid = 8;
    }

    private final void _finishInt() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int value = bArr[i];
        if (value < 0) {
            value &= 63;
        } else {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            if (i2 >= 0) {
                value = value << 7 + i2;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i];
                if (i2 >= 0) {
                    value = value << 7 + i2;
                    if (this._inputPtr >= this._inputEnd) {
                        loadMoreGuaranteed();
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i];
                    if (i2 >= 0) {
                        value = value << 7 + i2;
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        bArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        i2 = bArr[i];
                        if (i2 >= 0) {
                            _reportError("Corrupt input; 32-bit VInt extends beyond 5 data bytes");
                        }
                    }
                }
            }
            value = value << 6 + i2 & 63;
        }
        this._numberInt = SmileUtil.zigzagDecode(value);
        this._numTypesValid = 1;
    }

    private final void _finishLong() throws IOException, JsonParseException {
        long l = (long) _fourBytesToInt();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int value = bArr[i];
            if (value < 0) {
                this._numberLong = SmileUtil.zigzagDecode(l << 6 + ((long) (value & 63)));
                this._numTypesValid = 2;
                return;
            } else {
                l = l << 7 + ((long) value);
            }
        }
    }

    private final void _finishRawBinary() throws IOException, JsonParseException {
        int byteLen = _readUnsignedVInt();
        this._binaryValue = new byte[byteLen];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int ptr = 0;
        while (true) {
            int toAdd = Math.min(byteLen, this._inputEnd - this._inputPtr);
            System.arraycopy(this._inputBuffer, this._inputPtr, this._binaryValue, ptr, toAdd);
            this._inputPtr += toAdd;
            ptr += toAdd;
            byteLen -= toAdd;
            if (byteLen > 0) {
                loadMoreGuaranteed();
            } else {
                return;
            }
        }
    }

    private final int _fourBytesToInt() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int i3 = i2 << 7;
        byte[] bArr2 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        i2 = i3 + bArr2[i4];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        i3 = i2 << 7;
        bArr2 = this._inputBuffer;
        i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        i2 = i3 + bArr2[i4];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        i3 = i2 << 7;
        bArr2 = this._inputBuffer;
        i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        return i3 + bArr2[i4];
    }

    private static int[] _growArrayTo(int[] arr, int minSize) {
        int[] newArray = new int[(minSize + 4)];
        if (arr != null) {
            System.arraycopy(arr, 0, newArray, 0, arr.length);
        }
        return newArray;
    }

    private final void _handleLongFieldName() throws IOException, JsonParseException {
        byte[] inBuf = this._inputBuffer;
        int quads = 0;
        int q = 0;
        while (true) {
            int bytes;
            String name;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = inBuf[i];
            if ((byte) -4 == b) {
                bytes = 0;
            } else {
                q = b & 255;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                b = inBuf[i];
                if ((byte) -4 == b) {
                    bytes = 1;
                } else {
                    q = (q << 8) | (b & 255);
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    b = inBuf[i];
                    if ((byte) -4 == b) {
                        bytes = ClassWriter.COMPUTE_FRAMES;
                    } else {
                        q = (q << 8) | (b & 255);
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        b = inBuf[i];
                        if ((byte) -4 == b) {
                            bytes = JsonWriteContext.STATUS_OK_AFTER_SPACE;
                        } else {
                            q = (q << 8) | (b & 255);
                            if (quads >= this._quadBuffer.length) {
                                this._quadBuffer = _growArrayTo(this._quadBuffer, this._quadBuffer.length + 256);
                            }
                            int quads2 = quads + 1;
                            this._quadBuffer[quads] = q;
                            quads = quads2;
                        }
                    }
                }
            }
            int byteLen = quads << 2;
            if (bytes > 0) {
                if (quads >= this._quadBuffer.length) {
                    this._quadBuffer = _growArrayTo(this._quadBuffer, this._quadBuffer.length + 256);
                }
                quads2 = quads + 1;
                this._quadBuffer[quads] = q;
                byteLen += bytes;
                quads = quads2;
            }
            Name n = this._symbols.findName(this._quadBuffer, quads);
            if (n != null) {
                name = n.getName();
            } else {
                name = _decodeLongUnicodeName(this._quadBuffer, byteLen, quads).getName();
            }
            if (this._seenNames != null) {
                if (this._seenNameCount >= this._seenNames.length) {
                    this._seenNames = _expandSeenNames(this._seenNames);
                }
                String[] strArr = this._seenNames;
                int i2 = this._seenNameCount;
                this._seenNameCount = i2 + 1;
                strArr[i2] = name;
            }
            this._parsingContext.setCurrentName(name);
            return;
        }
    }

    private final JsonToken _handleSharedString(int index) throws IOException, JsonParseException {
        if (index >= this._seenStringValueCount) {
            _reportInvalidSharedStringValue(index);
        }
        this._textBuffer.resetWithString(this._seenStringValues[index]);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final byte[] _read7BitBinaryWithLength() throws IOException, JsonParseException {
        int ptr;
        int byteLen = _readUnsignedVInt();
        byte[] result = new byte[byteLen];
        int lastOkPtr = byteLen - 7;
        int ptr2 = 0;
        while (ptr2 <= lastOkPtr) {
            if (this._inputEnd - this._inputPtr < 8) {
                _loadToHaveAtLeast(Type.DOUBLE);
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] << 25;
            byte[] bArr2 = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            i2 += bArr2[i3] << 18;
            bArr2 = this._inputBuffer;
            i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            i2 += bArr2[i3] << 11;
            bArr2 = this._inputBuffer;
            i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            int i1 = i2 + bArr2[i3] << 4;
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            int x = bArr[i];
            i1 += x >> 3;
            i2 = (x & 7) << 21;
            bArr2 = this._inputBuffer;
            i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            i2 += bArr2[i3] << 14;
            bArr2 = this._inputBuffer;
            i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            i2 += bArr2[i3] << 7;
            bArr2 = this._inputBuffer;
            i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            int i22 = i2 + bArr2[i3];
            ptr = ptr2 + 1;
            result[ptr2] = (byte) (i1 >> 24);
            ptr2 = ptr + 1;
            result[ptr] = (byte) (i1 >> 16);
            ptr = ptr2 + 1;
            result[ptr2] = (byte) (i1 >> 8);
            ptr2 = ptr + 1;
            result[ptr] = (byte) i1;
            ptr = ptr2 + 1;
            result[ptr2] = (byte) (i22 >> 16);
            ptr2 = ptr + 1;
            result[ptr] = (byte) (i22 >> 8);
            ptr = ptr2 + 1;
            result[ptr2] = (byte) i22;
            ptr2 = ptr;
        }
        int toDecode = result.length - ptr2;
        if (toDecode > 0) {
            if (this._inputEnd - this._inputPtr < toDecode + 1) {
                _loadToHaveAtLeast(toDecode + 1);
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            int value = bArr[i];
            int i4 = 1;
            while (i4 < toDecode) {
                i2 = value << 7;
                bArr2 = this._inputBuffer;
                i3 = this._inputPtr;
                this._inputPtr = i3 + 1;
                value = i2 + bArr2[i3];
                ptr = ptr2 + 1;
                result[ptr2] = (byte) (value >> (7 - i4));
                i4++;
                ptr2 = ptr;
            }
            value <<= toDecode;
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            result[ptr2] = (byte) (bArr[i] + value);
        }
        return result;
    }

    private final int _readUnsignedVInt() throws IOException, JsonParseException {
        int value = 0;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            if (i2 < 0) {
                return value << 6 + i2 & 63;
            }
            value = value << 7 + i2;
        }
    }

    protected static final SmileBufferRecycler<String> _smileBufferRecycler() {
        SoftReference<SmileBufferRecycler<String>> ref = (SoftReference) _smileRecyclerRef.get();
        SmileBufferRecycler<String> br = ref == null ? null : (SmileBufferRecycler) ref.get();
        if (br != null) {
            return br;
        }
        br = new SmileBufferRecycler();
        _smileRecyclerRef.set(new SoftReference(br));
        return br;
    }

    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException, JsonParseException {
        _throwInternal();
        return null;
    }

    protected final void _decodeShortAsciiValue(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr = this._inputPtr;
        int end = inPtr + len;
        int outPtr = 0;
        while (inPtr < end) {
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) inBuf[inPtr];
            inPtr++;
            outPtr = outPtr2;
        }
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
    }

    protected final void _decodeShortUnicodeValue(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr = 0;
        while (inPtr2 < end) {
            int outPtr2;
            inPtr = inPtr2 + 1;
            int i = inputBuf[inPtr2] & 255;
            int code = codes[i];
            if (code != 0) {
                switch (code) {
                    case LocalAudioAll.SORT_BY_DATE:
                        i = ((i & 31) << 6) | (inputBuf[inPtr] & 63);
                        inPtr++;
                        outPtr2 = outPtr;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        i = (((i & 15) << 12) | ((inputBuf[inPtr] & 63) << 6)) | (inputBuf[inPtr2] & 63);
                        outPtr2 = outPtr;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        inPtr2 = inPtr + 1;
                        i = ((((i & 7) << 18) | ((inputBuf[inPtr] & 63) << 12)) | ((inputBuf[inPtr2] & 63) << 6)) | (inputBuf[inPtr] & 63) - 65536;
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (char) (55296 | (i >> 10));
                        i = 56320 | (i & 1023);
                        inPtr = inPtr2;
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = (char) i;
                        inPtr2 = inPtr;
                        break;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i) + " in short Unicode text block");
                        break;
                }
            }
            outPtr2 = outPtr;
            outPtr = outPtr2 + 1;
            outBuf[outPtr2] = (char) i;
            inPtr2 = inPtr;
        }
        this._textBuffer.setCurrentLength(outPtr);
    }

    protected final void _finishNumberToken(int tb) throws IOException, JsonParseException {
        tb &= 31;
        int type = tb >> 2;
        if (type == 1) {
            int subtype = tb & 3;
            if (subtype == 0) {
                _finishInt();
            } else if (subtype == 1) {
                _finishLong();
            } else if (subtype == 2) {
                _finishBigInteger();
            } else {
                _throwInternal();
            }
        } else {
            if (type == 2) {
                switch (tb & 3) {
                    case LocalAudioAll.SORT_BY_TITLE:
                        _finishFloat();
                        return;
                    case LocalAudioAll.SORT_BY_DATE:
                        _finishDouble();
                        return;
                    case ClassWriter.COMPUTE_FRAMES:
                        _finishBigDecimal();
                        return;
                }
            }
            _throwInternal();
        }
    }

    protected void _finishString() throws IOException, JsonParseException {
        _throwInternal();
    }

    protected void _finishToken() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int tb = this._typeByte;
        int type = (tb >> 5) & 7;
        if (type == 1) {
            _finishNumberToken(tb);
        } else if (type <= 3) {
            _decodeShortAsciiValue(tb & 63 + 1);
        } else if (type <= 5) {
            _decodeShortUnicodeValue(tb & 63 + 2);
        } else {
            if (type == 7) {
                switch ((tb & 31) >> 2) {
                    case LocalAudioAll.SORT_BY_TITLE:
                        _decodeLongAscii();
                        return;
                    case LocalAudioAll.SORT_BY_DATE:
                        _decodeLongUnicode();
                        return;
                    case ClassWriter.COMPUTE_FRAMES:
                        this._binaryValue = _read7BitBinaryWithLength();
                        return;
                    case Type.LONG:
                        _finishRawBinary();
                        return;
                }
            }
            _throwInternal();
        }
    }

    protected final JsonToken _handleFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i];
        this._typeByte = ch;
        int index;
        int len;
        Name n;
        String name;
        String[] strArr;
        switch ((ch >> 6) & 3) {
            case LocalAudioAll.SORT_BY_TITLE:
                switch (ch) {
                    case Opcodes.ACC_SYNCHRONIZED:
                        this._parsingContext.setCurrentName("");
                        return JsonToken.FIELD_NAME;
                    case SmileConstants.TOKEN_PREFIX_KEY_SHARED_LONG:
                    case Opcodes.V1_5:
                    case Opcodes.V1_6:
                    case Opcodes.V1_7:
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        int i2 = (ch & 3) << 8;
                        byte[] bArr2 = this._inputBuffer;
                        int i3 = this._inputPtr;
                        this._inputPtr = i3 + 1;
                        index = i2 + bArr2[i3] & 255;
                        if (index >= this._seenNameCount) {
                            _reportInvalidSharedName(index);
                        }
                        this._parsingContext.setCurrentName(this._seenNames[index]);
                        return JsonToken.FIELD_NAME;
                    case Opcodes.CALOAD:
                        _handleLongFieldName();
                        return JsonToken.FIELD_NAME;
                    default:
                        _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch) + " for expected field name (or END_OBJECT marker)");
                        return null;
                }
            case LocalAudioAll.SORT_BY_DATE:
                index = ch & 63;
                if (index >= this._seenNameCount) {
                    _reportInvalidSharedName(index);
                }
                this._parsingContext.setCurrentName(this._seenNames[index]);
                return JsonToken.FIELD_NAME;
            case ClassWriter.COMPUTE_FRAMES:
                len = ch & 63 + 1;
                n = _findDecodedFromSymbols(len);
                if (n != null) {
                    name = n.getName();
                    this._inputPtr += len;
                } else {
                    name = _addDecodedToSymbols(len, _decodeShortAsciiName(len));
                }
                if (this._seenNames != null) {
                    if (this._seenNameCount >= this._seenNames.length) {
                        this._seenNames = _expandSeenNames(this._seenNames);
                    }
                    strArr = this._seenNames;
                    i = this._seenNameCount;
                    this._seenNameCount = i + 1;
                    strArr[i] = name;
                }
                this._parsingContext.setCurrentName(name);
                return JsonToken.FIELD_NAME;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                len = ch & 63;
                if (len > 55) {
                    if (len == 59) {
                        if (!this._parsingContext.inObject()) {
                            _reportMismatchedEndMarker(Opcodes.LUSHR, ']');
                        }
                        this._parsingContext = this._parsingContext.getParent();
                        return JsonToken.END_OBJECT;
                    }
                    _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch) + " for expected field name (or END_OBJECT marker)");
                    return null;
                } else {
                    len += 2;
                    n = _findDecodedFromSymbols(len);
                    if (n != null) {
                        name = n.getName();
                        this._inputPtr += len;
                    } else {
                        name = _addDecodedToSymbols(len, _decodeShortUnicodeName(len));
                    }
                    if (this._seenNames != null) {
                        if (this._seenNameCount >= this._seenNames.length) {
                            this._seenNames = _expandSeenNames(this._seenNames);
                        }
                        strArr = this._seenNames;
                        i = this._seenNameCount;
                        this._seenNameCount = i + 1;
                        strArr[i] = name;
                    }
                    this._parsingContext.setCurrentName(name);
                    return JsonToken.FIELD_NAME;
                }
            default:
                _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch) + " for expected field name (or END_OBJECT marker)");
                return null;
        }
    }

    protected void _parseNumericValue(int expType) throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            int tb = this._typeByte;
            if (((tb >> 5) & 7) != 1) {
                _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
            }
            this._tokenIncomplete = false;
            _finishNumberToken(tb);
        }
    }

    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        String[] nameBuf = this._seenNames;
        if (nameBuf != null && nameBuf.length > 0) {
            this._seenNames = null;
            Arrays.fill(nameBuf, 0, this._seenNameCount, null);
            this._smileBufferRecycler.releaseSeenNamesBuffer(nameBuf);
        }
        String[] valueBuf = this._seenStringValues;
        if (valueBuf != null && valueBuf.length > 0) {
            this._seenStringValues = null;
            Arrays.fill(valueBuf, 0, this._seenStringValueCount, null);
            this._smileBufferRecycler.releaseSeenStringValuesBuffer(valueBuf);
        }
    }

    protected void _reportInvalidChar(int c) throws JsonParseException {
        if (c < 32) {
            _throwInvalidSpace(c);
        }
        _reportInvalidInitial(c);
    }

    protected void _reportInvalidInitial(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask, int ptr) throws JsonParseException {
        this._inputPtr = ptr;
        _reportInvalidOther(mask);
    }

    protected void _reportInvalidSharedName(int index) throws IOException {
        if (this._seenNames == null) {
            _reportError("Encountered shared name reference, even though document header explicitly declared no shared name references are included");
        }
        _reportError("Invalid shared name reference " + index + "; only got " + this._seenNameCount + " names in buffer (invalid content)");
    }

    protected void _reportInvalidSharedStringValue(int index) throws IOException {
        if (this._seenStringValues == null) {
            _reportError("Encountered shared text value reference, even though document header did not declared shared text value references may be included");
        }
        _reportError("Invalid shared text value reference " + index + "; only got " + this._seenStringValueCount + " names in buffer (invalid content)");
    }

    protected void _skip7BitBinary() throws IOException, JsonParseException {
        int origBytes = _readUnsignedVInt();
        int chunks = origBytes / 7;
        int encBytes = chunks * 8;
        origBytes -= chunks * 7;
        if (origBytes > 0) {
            encBytes += origBytes + 1;
        }
        _skipBytes(encBytes);
    }

    protected void _skipBytes(int len) throws IOException, JsonParseException {
        while (true) {
            int toAdd = Math.min(len, this._inputEnd - this._inputPtr);
            this._inputPtr += toAdd;
            if (len - toAdd > 0) {
                loadMoreGuaranteed();
            } else {
                return;
            }
        }
    }

    protected void _skipIncomplete() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int tb = this._typeByte;
        int end;
        byte[] buf;
        int i;
        switch ((tb >> 5) & 7) {
            case LocalAudioAll.SORT_BY_DATE:
                tb &= 31;
                switch (tb >> 2) {
                    case LocalAudioAll.SORT_BY_DATE:
                        switch (tb & 3) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                while (true) {
                                    end = this._inputEnd;
                                    buf = this._inputBuffer;
                                    while (this._inputPtr < end) {
                                        i = this._inputPtr;
                                        this._inputPtr = i + 1;
                                        if (buf[i] < 0) {
                                            return;
                                        }
                                    }
                                    loadMoreGuaranteed();
                                }
                                break;
                            case LocalAudioAll.SORT_BY_DATE:
                                _skipBytes(JsonWriteContext.STATUS_EXPECT_VALUE);
                                while (true) {
                                    end = this._inputEnd;
                                    buf = this._inputBuffer;
                                    while (this._inputPtr < end) {
                                        i = this._inputPtr;
                                        this._inputPtr = i + 1;
                                        if (buf[i] < 0) {
                                            return;
                                        }
                                    }
                                    loadMoreGuaranteed();
                                }
                                break;
                            case ClassWriter.COMPUTE_FRAMES:
                                _skip7BitBinary();
                            default:
                                _throwInternal();
                        }
                    case ClassWriter.COMPUTE_FRAMES:
                        switch (tb & 3) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                _skipBytes(JsonWriteContext.STATUS_EXPECT_NAME);
                            case LocalAudioAll.SORT_BY_DATE:
                                _skipBytes(Type.OBJECT);
                            case ClassWriter.COMPUTE_FRAMES:
                                _readUnsignedVInt();
                                _skip7BitBinary();
                            default:
                                _throwInternal();
                        }
                    default:
                        _throwInternal();
                }
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                _skipBytes(tb & 63 + 1);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
            case JsonWriteContext.STATUS_EXPECT_NAME:
                _skipBytes(tb & 63 + 2);
            case Type.LONG:
                switch ((tb & 31) >> 2) {
                    case LocalAudioAll.SORT_BY_TITLE:
                    case LocalAudioAll.SORT_BY_DATE:
                        while (true) {
                            end = this._inputEnd;
                            buf = this._inputBuffer;
                            while (this._inputPtr < end) {
                                i = this._inputPtr;
                                this._inputPtr = i + 1;
                                if (buf[i] == (byte) -4) {
                                    return;
                                }
                            }
                            loadMoreGuaranteed();
                        }
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                        _skip7BitBinary();
                    case Type.LONG:
                        _skipBytes(_readUnsignedVInt());
                    default:
                        _throwInternal();
                }
            default:
                _throwInternal();
        }
    }

    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            _finishToken();
        }
        if (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) {
            _reportError("Current token (" + this._currToken + ") not VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        return this._binaryValue;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public String getCurrentName() throws IOException, JsonParseException {
        return this._parsingContext.getCurrentName();
    }

    public NumberType getNumberType() throws IOException, JsonParseException {
        return this._got32BitFloat ? NumberType.FLOAT : super.getNumberType();
    }

    public String getText() throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            int tb = this._typeByte;
            int type = (tb >> 5) & 7;
            if (type == 2 || type == 3) {
                _decodeShortAsciiValue(tb & 63 + 1);
                return this._textBuffer.contentsAsString();
            } else if (type == 4 || type == 5) {
                _decodeShortUnicodeValue(tb & 63 + 2);
                return this._textBuffer.contentsAsString();
            } else {
                _finishToken();
            }
        }
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        JsonToken t = this._currToken;
        if (t == null) {
            return null;
        }
        if (t == JsonToken.FIELD_NAME) {
            return this._parsingContext.getCurrentName();
        }
        return t.isNumeric() ? getNumberValue().toString() : this._currToken.asString();
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        if (this._tokenIncomplete) {
            _finishToken();
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return this._textBuffer.getTextBuffer();
            case ClassWriter.COMPUTE_FRAMES:
                if (!this._nameCopied) {
                    String name = this._parsingContext.getCurrentName();
                    int nameLen = name.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
                    } else if (this._nameCopyBuffer.length < nameLen) {
                        this._nameCopyBuffer = new char[nameLen];
                    }
                    name.getChars(0, nameLen, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return getNumberValue().toString().toCharArray();
            default:
                return this._currToken.asCharArray();
        }
    }

    public int getTextLength() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        if (this._tokenIncomplete) {
            _finishToken();
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return this._textBuffer.size();
            case ClassWriter.COMPUTE_FRAMES:
                return this._parsingContext.getCurrentName().length();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return getNumberValue().toString().length();
            default:
                return this._currToken.asCharArray().length;
        }
    }

    public int getTextOffset() throws IOException, JsonParseException {
        return 0;
    }

    protected boolean handleSignature(boolean consumeFirstByte, boolean throwException) throws IOException, JsonParseException {
        boolean z = false;
        if (consumeFirstByte) {
            this._inputPtr++;
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if (this._inputBuffer[this._inputPtr] == (byte) 41) {
            int i = this._inputPtr + 1;
            this._inputPtr = i;
            if (i >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (this._inputBuffer[this._inputPtr] == (byte) 10) {
                i = this._inputPtr + 1;
                this._inputPtr = i;
                if (i >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                int ch = bArr[i2];
                int versionBits = (ch >> 4) & 15;
                if (versionBits != 0) {
                    _reportError("Header version number bits (0x" + Integer.toHexString(versionBits) + ") indicate unrecognized version; only 0x0 handled by parser");
                }
                if ((ch & 1) == 0) {
                    this._seenNames = null;
                    this._seenNameCount = -1;
                }
                if ((ch & 2) != 0) {
                    this._seenStringValues = NO_STRINGS;
                    this._seenStringValueCount = 0;
                }
                if ((ch & 4) != 0) {
                    z = true;
                }
                this._mayContainRawBinary = z;
                return true;
            } else if (!throwException) {
                return false;
            } else {
                _reportError("Malformed content: signature not valid, starts with 0x3a, 0x29, but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0xA");
                return false;
            }
        } else if (!throwException) {
            return false;
        } else {
            _reportError("Malformed content: signature not valid, starts with 0x3a but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0x29");
            return false;
        }
    }

    public boolean mayContainRawBinary() {
        return this._mayContainRawBinary;
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        boolean z = true;
        if (this._tokenIncomplete) {
            _skipIncomplete();
        }
        this._tokenInputTotal = this._currInputProcessed + ((long) this._inputPtr) - 1;
        this._binaryValue = null;
        JsonToken _handleFieldName;
        if (this._parsingContext.inObject() && this._currToken != JsonToken.FIELD_NAME) {
            _handleFieldName = _handleFieldName();
            this._currToken = _handleFieldName;
            return _handleFieldName;
        } else if (this._inputPtr < this._inputEnd || loadMore()) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int ch = bArr[i];
            this._typeByte = ch;
            switch ((ch >> 5) & 7) {
                case LocalAudioAll.SORT_BY_TITLE:
                    if (ch == 0) {
                        _reportError("Invalid token byte 0x00");
                    }
                    return _handleSharedString(ch - 1);
                case LocalAudioAll.SORT_BY_DATE:
                    int typeBits = ch & 31;
                    if (typeBits < 4) {
                        switch (typeBits) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                this._textBuffer.resetWithEmpty();
                                _handleFieldName = JsonToken.VALUE_STRING;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                            case LocalAudioAll.SORT_BY_DATE:
                                _handleFieldName = JsonToken.VALUE_NULL;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                            case ClassWriter.COMPUTE_FRAMES:
                                _handleFieldName = JsonToken.VALUE_FALSE;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                            default:
                                _handleFieldName = JsonToken.VALUE_TRUE;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                        }
                    } else {
                        if (typeBits < 8) {
                            if ((typeBits & 3) <= 2) {
                                this._tokenIncomplete = true;
                                this._numTypesValid = 0;
                                _handleFieldName = JsonToken.VALUE_NUMBER_INT;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                            }
                        } else if (typeBits < 12) {
                            int subtype = typeBits & 3;
                            if (subtype <= 2) {
                                this._tokenIncomplete = true;
                                this._numTypesValid = 0;
                                if (subtype != 0) {
                                    z = false;
                                }
                                this._got32BitFloat = z;
                                _handleFieldName = JsonToken.VALUE_NUMBER_FLOAT;
                                this._currToken = _handleFieldName;
                                return _handleFieldName;
                            }
                        } else if (typeBits != 26 || !handleSignature(false, false)) {
                            _reportError("Unrecognized token byte 0x3A (malformed segment header?");
                        } else if (this._currToken == null) {
                            return nextToken();
                        } else {
                            this._currToken = null;
                            return null;
                        }
                        _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch & 255) + " for expected value token");
                        return null;
                    }
                case ClassWriter.COMPUTE_FRAMES:
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    this._currToken = JsonToken.VALUE_STRING;
                    if (this._seenStringValueCount >= 0) {
                        _addSeenStringValue();
                    } else {
                        this._tokenIncomplete = true;
                    }
                    return this._currToken;
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    this._numberInt = SmileUtil.zigzagDecode(ch & 31);
                    this._numTypesValid = 1;
                    _handleFieldName = JsonToken.VALUE_NUMBER_INT;
                    this._currToken = _handleFieldName;
                    return _handleFieldName;
                case Type.LONG:
                    switch (ch & 31) {
                        case LocalAudioAll.SORT_BY_TITLE:
                        case JsonWriteContext.STATUS_EXPECT_VALUE:
                            this._tokenIncomplete = true;
                            _handleFieldName = JsonToken.VALUE_STRING;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case Type.DOUBLE:
                            this._tokenIncomplete = true;
                            _handleFieldName = JsonToken.VALUE_EMBEDDED_OBJECT;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case Opcodes.FCONST_1:
                        case Opcodes.FCONST_2:
                        case Opcodes.DCONST_0:
                        case Opcodes.DCONST_1:
                            if (this._inputPtr >= this._inputEnd) {
                                loadMoreGuaranteed();
                            }
                            int i2 = (ch & 3) << 8;
                            byte[] bArr2 = this._inputBuffer;
                            int i3 = this._inputPtr;
                            this._inputPtr = i3 + 1;
                            return _handleSharedString(i2 + bArr2[i3] & 255);
                        case Opcodes.DLOAD:
                            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                            _handleFieldName = JsonToken.START_ARRAY;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case Opcodes.ALOAD:
                            if (!this._parsingContext.inArray()) {
                                _reportMismatchedEndMarker(Opcodes.DUP2_X1, '}');
                            }
                            this._parsingContext = this._parsingContext.getParent();
                            _handleFieldName = JsonToken.END_ARRAY;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case 26:
                            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                            _handleFieldName = JsonToken.START_OBJECT;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case 27:
                            _reportError("Invalid type marker byte 0xFB in value mode (would be END_OBJECT in key mode)");
                            this._tokenIncomplete = true;
                            _handleFieldName = JsonToken.VALUE_EMBEDDED_OBJECT;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case 29:
                            this._tokenIncomplete = true;
                            _handleFieldName = JsonToken.VALUE_EMBEDDED_OBJECT;
                            this._currToken = _handleFieldName;
                            return _handleFieldName;
                        case KeyTouchInputEvent.EV_MAX:
                            this._currToken = null;
                            return null;
                        default:
                            _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch & 255) + " for expected value token");
                            return null;
                    }
                default:
                    _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch & 255) + " for expected value token");
                    return null;
            }
        } else {
            _handleEOF();
            close();
            this._currToken = null;
            return null;
        }
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }
}