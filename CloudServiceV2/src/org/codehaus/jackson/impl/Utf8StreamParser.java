package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.ConnectionInstance;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;
import org.codehaus.jackson.util.CharTypes;

public final class Utf8StreamParser extends StreamBasedParserBase {
    static final byte BYTE_LF = (byte) 10;
    private static final int[] sInputCodesLatin1;
    private static final int[] sInputCodesUtf8;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 4;
        }
    }

    static {
        sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
        sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
    }

    public Utf8StreamParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, BytesToNameCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features, in, inputBuffer, start, end, bufferRecyclable);
        this._quadBuffer = new int[16];
        this._tokenIncomplete = false;
        this._objectCodec = codec;
        this._symbols = sym;
        if (!Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features)) {
            _throwInternal();
        }
    }

    private final int _decodeBase64Escape(Base64Variant b64variant, int ch, int index) throws IOException, JsonParseException {
        if (ch != 92) {
            throw reportInvalidChar(b64variant, ch, index);
        }
        int unescaped = _decodeEscaped();
        if (unescaped <= 32 && index == 0) {
            return -1;
        }
        int bits = b64variant.decodeBase64Char(unescaped);
        if (bits >= 0) {
            return bits;
        }
        throw reportInvalidChar(b64variant, unescaped, index);
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

    private final void _finishString2(char[] outBuf, int outPtr) throws IOException, JsonParseException {
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr = this._inputPtr;
            if (ptr >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = Math.min(this._inputEnd, outBuf.length - outPtr + ptr);
            int ptr2 = ptr;
            int outPtr2 = outPtr;
            while (ptr2 < max) {
                ptr = ptr2 + 1;
                int c = inputBuffer[ptr2] & 255;
                if (codes[c] != 0) {
                    this._inputPtr = ptr;
                    if (c == 34) {
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    } else {
                        switch (codes[c]) {
                            case LocalAudioAll.SORT_BY_DATE:
                                c = _decodeEscaped();
                                outPtr = outPtr2;
                                break;
                            case ClassWriter.COMPUTE_FRAMES:
                                c = _decodeUtf8_2(c);
                                outPtr = outPtr2;
                                break;
                            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
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
                                if (c < 32) {
                                    _throwUnquotedSpace(c, "string value");
                                    outPtr = outPtr2;
                                } else {
                                    _reportInvalidChar(c);
                                    outPtr = outPtr2;
                                }
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
                    ptr2 = ptr;
                    outPtr2 = outPtr;
                }
            }
            this._inputPtr = ptr2;
            outPtr = outPtr2;
        }
    }

    private final JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken t = this._nextToken;
        this._nextToken = null;
        if (t == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (t == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = t;
        return t;
    }

    private final JsonToken _nextTokenNotInObject(int i) throws IOException, JsonParseException {
        JsonToken jsonToken;
        if (i == 34) {
            this._tokenIncomplete = true;
            jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        } else {
            switch (i) {
                case ConnectionInstance.CONNECTION_FAILED:
                case SmileConstants.TOKEN_PREFIX_KEY_SHARED_LONG:
                case Opcodes.V1_5:
                case Opcodes.V1_6:
                case Opcodes.V1_7:
                case Opcodes.CALOAD:
                case Opcodes.SALOAD:
                case Opcodes.ISTORE:
                case Opcodes.LSTORE:
                case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES:
                case Opcodes.DSTORE:
                    jsonToken = parseNumberText(i);
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.DUP_X2:
                    this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                    jsonToken = JsonToken.START_ARRAY;
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.DUP2_X1:
                case Opcodes.LUSHR:
                    _reportUnexpectedChar(i, "expected a value");
                    _matchToken(JsonToken.VALUE_TRUE);
                    jsonToken = JsonToken.VALUE_TRUE;
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.FSUB:
                    _matchToken(JsonToken.VALUE_FALSE);
                    jsonToken = JsonToken.VALUE_FALSE;
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.FDIV:
                    _matchToken(JsonToken.VALUE_NULL);
                    jsonToken = JsonToken.VALUE_NULL;
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.INEG:
                    _matchToken(JsonToken.VALUE_TRUE);
                    jsonToken = JsonToken.VALUE_TRUE;
                    this._currToken = jsonToken;
                    return jsonToken;
                case Opcodes.LSHR:
                    this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                    jsonToken = JsonToken.START_OBJECT;
                    this._currToken = jsonToken;
                    return jsonToken;
                default:
                    jsonToken = _handleUnexpectedValue(i);
                    this._currToken = jsonToken;
                    return jsonToken;
            }
        }
    }

    private final JsonToken _parseFloatText(char[] outBuf, int outPtr, int c, boolean negative, int integerPartLength) throws IOException, JsonParseException {
        int outPtr2;
        int fractLen = 0;
        boolean z = false;
        if (c == 46) {
            outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            outPtr = outPtr2;
            while (true) {
                if (this._inputPtr < this._inputEnd || loadMore()) {
                    byte[] bArr = this._inputBuffer;
                    int i = this._inputPtr;
                    this._inputPtr = i + 1;
                    c = bArr[i] & 255;
                    if (c >= 48 && c <= 57) {
                        fractLen++;
                        if (outPtr >= outBuf.length) {
                            outBuf = this._textBuffer.finishCurrentSegment();
                            outPtr = 0;
                        }
                        outPtr2 = outPtr + 1;
                        outBuf[outPtr] = (char) c;
                        outPtr = outPtr2;
                    }
                } else {
                    z = true;
                }
                if (fractLen == 0) {
                    reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
                }
            }
        }
        int expLen = 0;
        if (c == 101 || c == 69) {
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & 255;
            if (c == 45 || c == 43) {
                if (outPtr2 >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr = 0;
                } else {
                    outPtr = outPtr2;
                }
                outPtr2 = outPtr + 1;
                outBuf[outPtr] = (char) c;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                c = bArr[i] & 255;
                outPtr = outPtr2;
            } else {
                outPtr = outPtr2;
            }
            while (c <= 57 && c >= 48) {
                expLen++;
                if (outPtr >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                outPtr2 = outPtr + 1;
                outBuf[outPtr] = (char) c;
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    z = true;
                    outPtr = outPtr2;
                    break;
                } else {
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    c = bArr[i] & 255;
                    outPtr = outPtr2;
                }
            }
            if (expLen == 0) {
                reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
            }
        }
        if (!z) {
            this._inputPtr--;
        }
        this._textBuffer.setCurrentLength(outPtr);
        return resetFloat(negative, integerPartLength, fractLen, expLen);
    }

    private final JsonToken _parserNumber2(char[] outBuf, int outPtr, boolean negative, int intPartLength) throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int c = bArr[i] & 255;
                if (c <= 57 && c >= 48) {
                    if (outPtr >= outBuf.length) {
                        outBuf = this._textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    int outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    intPartLength++;
                }
                if (c == 46 || c == 101 || c == 69) {
                    return _parseFloatText(outBuf, outPtr, c, negative, intPartLength);
                }
                this._inputPtr--;
                this._textBuffer.setCurrentLength(outPtr);
                return resetInt(negative, intPartLength);
            } else {
                this._textBuffer.setCurrentLength(outPtr);
                return resetInt(negative, intPartLength);
            }
        }
    }

    private final void _skipCComment() throws IOException, JsonParseException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case Type.OBJECT:
                            _skipLF();
                            break;
                        case Opcodes.FCONST_2:
                            _skipCR();
                            break;
                        case ConnectionInstance.SERVER_RESOLUTION_UPDATE:
                            if (this._inputBuffer[this._inputPtr] == (byte) 47) {
                                this._inputPtr++;
                                return;
                            }
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                _reportInvalidEOF(" in a comment");
                return;
            }
        }
    }

    private final void _skipComment() throws IOException, JsonParseException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(Opcodes.V1_3, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i] & 255;
        if (c == 47) {
            _skipCppComment();
        } else if (c == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCppComment() throws IOException, JsonParseException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case Type.OBJECT:
                            _skipLF();
                            return;
                        case Opcodes.FCONST_2:
                            _skipCR();
                            return;
                        case ConnectionInstance.SERVER_RESOLUTION_UPDATE:
                            break;
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    private final void _skipUtf8_2(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != 128) {
            _reportInvalidOther(c & 255, this._inputPtr);
        }
    }

    private final void _skipUtf8_3(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != 128) {
            _reportInvalidOther(c & 255, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & 192) != 128) {
            _reportInvalidOther(c & 255, this._inputPtr);
        }
    }

    private final void _skipUtf8_4(int c) throws IOException, JsonParseException {
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
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255, this._inputPtr);
        }
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
    }

    private final int _skipWS() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                if (i2 > 32) {
                    if (i2 != 47) {
                        return i2;
                    }
                    _skipComment();
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            } else {
                throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
            }
        }
    }

    private final int _skipWSOrEnd() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & 255;
                if (i2 > 32) {
                    if (i2 != 47) {
                        return i2;
                    }
                    _skipComment();
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            } else {
                _handleEOF();
                return -1;
            }
        }
    }

    private final int _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return 48;
        }
        int ch = this._inputBuffer[this._inputPtr] & 255;
        if (ch < 48 || ch > 57) {
            return 48;
        }
        if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (ch != 48) {
            return ch;
        }
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return ch;
            }
            ch = this._inputBuffer[this._inputPtr] & 255;
            if (ch >= 48 && ch <= 57) {
                this._inputPtr++;
            }
            return 48;
        } while (ch == 48);
        return ch;
    }

    private final Name addName(int[] quads, int qlen, int lastQuadBytes) throws JsonParseException {
        int lastQuad;
        int byteLen = qlen << 2 - 4 + lastQuadBytes;
        if (lastQuadBytes < 4) {
            lastQuad = quads[qlen - 1];
            quads[qlen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
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
                    _reportInvalidEOF(" in field name");
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
            quads[qlen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, qlen);
    }

    private final Name findName(int q1, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        return addName(this._quadBuffer, 1, lastQuadBytes);
    }

    private final Name findName(int q1, int q2, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1, q2);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        this._quadBuffer[1] = q2;
        return addName(this._quadBuffer, ClassWriter.COMPUTE_FRAMES, lastQuadBytes);
    }

    private final Name findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes) throws JsonParseException {
        if (qlen >= quads.length) {
            quads = growArrayBy(quads, quads.length);
            this._quadBuffer = quads;
        }
        int qlen2 = qlen + 1;
        quads[qlen] = lastQuad;
        Name name = this._symbols.findName(quads, qlen2);
        return name == null ? addName(quads, qlen2, lastQuadBytes) : name;
    }

    public static int[] growArrayBy(int[] arr, int more) {
        if (arr == null) {
            return new int[more];
        }
        int[] old = arr;
        int len = arr.length;
        arr = new int[(len + more)];
        System.arraycopy(old, 0, arr, 0, len);
        return arr;
    }

    private int nextByte() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return bArr[i] & 255;
    }

    private final Name parseFieldName(int q1, int ch, int lastQuadBytes) throws IOException, JsonParseException {
        return parseEscapedFieldName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
    }

    private final Name parseFieldName(int q1, int q2, int ch, int lastQuadBytes) throws IOException, JsonParseException {
        this._quadBuffer[0] = q1;
        return parseEscapedFieldName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected byte[] _decodeBase64(org.codehaus.jackson.Base64Variant r11_b64variant) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._decodeBase64(org.codehaus.jackson.Base64Variant):byte[]");
        /*
        r10 = this;
        r9 = 3;
        r8 = 34;
        r7 = -2;
        r1 = r10._getByteArrayBuilder();
    L_0x0008:
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x0011;
    L_0x000e:
        r10.loadMoreGuaranteed();
    L_0x0011:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r4 = 32;
        if (r2 <= r4) goto L_0x0008;
    L_0x0021:
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0035;
    L_0x0027:
        if (r2 != r8) goto L_0x002e;
    L_0x0029:
        r4 = r1.toByteArray();
    L_0x002d:
        return r4;
    L_0x002e:
        r4 = 0;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
        if (r0 < 0) goto L_0x0008;
    L_0x0035:
        r3 = r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x003f;
    L_0x003c:
        r10.loadMoreGuaranteed();
    L_0x003f:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0056;
    L_0x0051:
        r4 = 1;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
    L_0x0056:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x0063;
    L_0x0060:
        r10.loadMoreGuaranteed();
    L_0x0063:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00d4;
    L_0x0075:
        if (r0 == r7) goto L_0x008e;
    L_0x0077:
        if (r2 != r8) goto L_0x0089;
    L_0x0079:
        r4 = r11.usesPadding();
        if (r4 != 0) goto L_0x0089;
    L_0x007f:
        r3 = r3 >> 4;
        r1.append(r3);
        r4 = r1.toByteArray();
        goto L_0x002d;
    L_0x0089:
        r4 = 2;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
    L_0x008e:
        if (r0 != r7) goto L_0x00d4;
    L_0x0090:
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x0099;
    L_0x0096:
        r10.loadMoreGuaranteed();
    L_0x0099:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r4 = r11.usesPaddingChar(r2);
        if (r4 != 0) goto L_0x00cd;
    L_0x00ab:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "expected padding character '";
        r4 = r4.append(r5);
        r5 = r11.getPaddingChar();
        r4 = r4.append(r5);
        r5 = "'";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r4 = r10.reportInvalidChar(r11, r2, r9, r4);
        throw r4;
    L_0x00cd:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x0008;
    L_0x00d4:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x00e1;
    L_0x00de:
        r10.loadMoreGuaranteed();
    L_0x00e1:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0115;
    L_0x00f3:
        if (r0 == r7) goto L_0x010c;
    L_0x00f5:
        if (r2 != r8) goto L_0x0108;
    L_0x00f7:
        r4 = r11.usesPadding();
        if (r4 != 0) goto L_0x0108;
    L_0x00fd:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        r4 = r1.toByteArray();
        goto L_0x002d;
    L_0x0108:
        r0 = r10._decodeBase64Escape(r11, r2, r9);
    L_0x010c:
        if (r0 != r7) goto L_0x0115;
    L_0x010e:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        goto L_0x0008;
    L_0x0115:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r1.appendThreeBytes(r3);
        goto L_0x0008;
        */
    }

    protected int _decodeCharForError(int firstByte) throws IOException, JsonParseException {
        int c = firstByte;
        if (c >= 0) {
            return c;
        }
        int needed;
        if ((c & 224) == 192) {
            c &= 31;
            needed = 1;
        } else if ((c & 240) == 224) {
            c &= 15;
            needed = ClassWriter.COMPUTE_FRAMES;
        } else if ((c & 248) == 240) {
            c &= 7;
            needed = JsonWriteContext.STATUS_OK_AFTER_SPACE;
        } else {
            _reportInvalidInitial(c & 255);
            needed = 1;
        }
        int d = nextByte();
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 1) {
            return c;
        }
        d = nextByte();
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 2) {
            return c;
        }
        d = nextByte();
        if ((d & 192) != 128) {
            _reportInvalidOther(d & 255);
        }
        return (c << 6) | (d & 63);
    }

    protected final char _decodeEscaped() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i];
        switch (c) {
            case 34:
            case Opcodes.V1_3:
            case Opcodes.DUP2:
                return (char) c;
            case Opcodes.FADD:
                return '\b';
            case Opcodes.FSUB:
                return '\f';
            case Opcodes.FDIV:
                return '\n';
            case Opcodes.FREM:
                return '\r';
            case Opcodes.INEG:
                return '\t';
            case Opcodes.LNEG:
                int value = 0;
                int i2 = 0;
                while (i2 < 4) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = bArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                    i2++;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(c));
        }
    }

    protected void _finishString() throws IOException, JsonParseException {
        int ptr = this._inputPtr;
        if (ptr >= this._inputEnd) {
            loadMoreGuaranteed();
            ptr = this._inputPtr;
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        int max = Math.min(this._inputEnd, outBuf.length + ptr);
        byte[] inputBuffer = this._inputBuffer;
        int outPtr = 0;
        while (ptr < max) {
            int c = inputBuffer[ptr] & 255;
            if (codes[c] != 0) {
                if (c == 34) {
                    this._inputPtr = ptr + 1;
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                }
                this._inputPtr = ptr;
                _finishString2(outBuf, outPtr);
            } else {
                ptr++;
                int outPtr2 = outPtr + 1;
                outBuf[outPtr] = (char) c;
                outPtr = outPtr2;
            }
        }
        this._inputPtr = ptr;
        _finishString2(outBuf, outPtr);
    }

    protected final String _getText2(JsonToken t) {
        if (t == null) {
            return null;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return this._parsingContext.getCurrentName();
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return this._textBuffer.contentsAsString();
            default:
                return t.asString();
        }
    }

    protected JsonToken _handleApostropheValue() throws IOException, JsonParseException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = this._inputPtr + outBuf.length - outPtr;
            if (max2 < max) {
                max = max2;
            }
            while (this._inputPtr < max) {
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int c = inputBuffer[i] & 255;
                if (c != 39 && codes[c] == 0) {
                    int outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                }
                if (c == 39) {
                    this._textBuffer.setCurrentLength(outPtr);
                    return JsonToken.VALUE_STRING;
                } else {
                    switch (codes[c]) {
                        case LocalAudioAll.SORT_BY_DATE:
                            if (c != 34) {
                                c = _decodeEscaped();
                            }
                            break;
                        case ClassWriter.COMPUTE_FRAMES:
                            c = _decodeUtf8_2(c);
                            break;
                        case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                            c = this._inputEnd - this._inputPtr >= 2 ? _decodeUtf8_3fast(c) : _decodeUtf8_3(c);
                            break;
                        case JsonWriteContext.STATUS_EXPECT_VALUE:
                            c = _decodeUtf8_4(c);
                            outPtr2 = outPtr + 1;
                            outBuf[outPtr] = (char) (55296 | (c >> 10));
                            if (outPtr2 >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            } else {
                                outPtr = outPtr2;
                            }
                            c = 56320 | (c & 1023);
                            break;
                        default:
                            if (c < 32) {
                                _throwUnquotedSpace(c, "string value");
                            }
                            _reportInvalidChar(c);
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
            }
        }
    }

    protected JsonToken _handleInvalidNumberStart(int ch, boolean negative) throws IOException, JsonParseException {
        double d = Double.NEGATIVE_INFINITY;
        if (ch == 73) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i];
            String match;
            if (ch == 78) {
                match = negative ? "-INF" : "+INF";
                if (_matchToken(match, JsonWriteContext.STATUS_OK_AFTER_SPACE) && isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!negative) {
                        d = Double.POSITIVE_INFINITY;
                    }
                    return resetAsNaN(match, d);
                } else {
                    _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                }
            } else if (ch == 110) {
                match = negative ? "-Infinity" : "+Infinity";
                if (_matchToken(match, JsonWriteContext.STATUS_OK_AFTER_SPACE) && isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!negative) {
                        d = Double.POSITIVE_INFINITY;
                    }
                    return resetAsNaN(match, d);
                } else {
                    _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                }
            }
        }
        reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    protected JsonToken _handleUnexpectedValue(int c) throws IOException, JsonParseException {
        byte[] bArr;
        int i;
        switch (c) {
            case 39:
                if (isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApostropheValue();
                }
                _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
            case ConnectionInstance.SERVER_SCREEN_IMAGE_UPDATE:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                return _handleInvalidNumberStart(bArr[i] & 255, false);
            case 78:
                if (_matchToken("NaN", 1) && isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return resetAsNaN("NaN", Double.NaN);
                }
                _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                _reportUnexpectedChar(bArr[i] & 255, "expected 'NaN' or a valid value");
                _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
            default:
                _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
        }
    }

    protected final Name _handleUnusualFieldName(int ch) throws IOException, JsonParseException {
        if (ch == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseApostropheFieldName();
        }
        int qlen;
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(ch, "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeUtf8JsNames();
        if (codes[ch] != 0) {
            _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int qlen2 = 0;
        while (true) {
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            ch = this._inputBuffer[this._inputPtr] & 255;
            if (codes[ch] != 0) {
                break;
            }
            this._inputPtr++;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = currQuad;
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        return name == null ? addName(quads, qlen, currQuadBytes) : name;
    }

    protected void _matchToken(JsonToken token) throws IOException, JsonParseException {
        byte[] matchBytes = token.asByteArray();
        int i = 1;
        int len = matchBytes.length;
        while (i < len) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (matchBytes[i] != this._inputBuffer[this._inputPtr]) {
                _reportInvalidToken(token.asString().substring(0, i), "'null', 'true' or 'false'");
            }
            this._inputPtr++;
            i++;
        }
    }

    protected final boolean _matchToken(String matchStr, int i) throws IOException, JsonParseException {
        int len = matchStr.length();
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in a value");
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i), "'null', 'true', 'false' or NaN");
            }
            this._inputPtr++;
            i++;
        } while (i < len);
        if ((this._inputPtr < this._inputEnd || loadMore()) && Character.isJavaIdentifierPart((char) _decodeCharForError(this._inputBuffer[this._inputPtr] & 255))) {
            this._inputPtr++;
            _reportInvalidToken(matchStr.substring(0, i), "'null', 'true', 'false' or NaN");
        }
        return true;
    }

    protected final Name _parseApostropheFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing ''' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i] & 255;
        if (ch == 39) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        int qlen;
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int[] codes = sInputCodesLatin1;
        int qlen2 = 0;
        while (ch != 39) {
            if (!(ch == 34 || codes[ch] == 0)) {
                if (ch != 92) {
                    _throwUnquotedSpace(ch, "name");
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > 127) {
                    if (currQuadBytes >= 4) {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                        qlen2 = qlen;
                    }
                    if (ch < 2048) {
                        currQuad = (currQuad << 8) | ((ch >> 6) | 192);
                        currQuadBytes++;
                        qlen = qlen2;
                    } else {
                        currQuad = (currQuad << 8) | ((ch >> 12) | 224);
                        currQuadBytes++;
                        if (currQuadBytes >= 4) {
                            if (qlen2 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen = qlen2 + 1;
                            quads[qlen2] = currQuad;
                            currQuad = 0;
                            currQuadBytes = 0;
                        } else {
                            qlen = qlen2;
                        }
                        currQuad = (currQuad << 8) | (((ch >> 6) & 63) | 128);
                        currQuadBytes++;
                    }
                    ch = (ch & 63) | 128;
                    qlen2 = qlen;
                }
            }
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & 255;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen2 >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen = qlen2 + 1;
            quads[qlen2] = currQuad;
        } else {
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        return name == null ? addName(quads, qlen, currQuadBytes) : name;
    }

    protected final Name _parseFieldName(int i) throws IOException, JsonParseException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        if (this._inputPtr + 9 > this._inputEnd) {
            return slowParseFieldName();
        }
        byte[] input = this._inputBuffer;
        int[] codes = sInputCodesLatin1;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int q = input[i2] & 255;
        if (codes[q] != 0) {
            return q == 34 ? BytesToNameCanonicalizer.getEmptyName() : parseFieldName(0, q, 0);
        } else {
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = input[i2] & 255;
            if (codes[i] != 0) {
                return i == 34 ? findName(q, 1) : parseFieldName(q, i, 1);
            } else {
                q = (q << 8) | i;
                i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                i = input[i2] & 255;
                if (codes[i] != 0) {
                    return i == 34 ? findName(q, ClassWriter.COMPUTE_FRAMES) : parseFieldName(q, i, ClassWriter.COMPUTE_FRAMES);
                } else {
                    q = (q << 8) | i;
                    i2 = this._inputPtr;
                    this._inputPtr = i2 + 1;
                    i = input[i2] & 255;
                    if (codes[i] != 0) {
                        return i == 34 ? findName(q, JsonWriteContext.STATUS_OK_AFTER_SPACE) : parseFieldName(q, i, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                    } else {
                        q = (q << 8) | i;
                        i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        i = input[i2] & 255;
                        if (codes[i] != 0) {
                            return i == 34 ? findName(q, JsonWriteContext.STATUS_EXPECT_VALUE) : parseFieldName(q, i, JsonWriteContext.STATUS_EXPECT_VALUE);
                        } else {
                            this._quad1 = q;
                            return parseMediumFieldName(i, codes);
                        }
                    }
                }
            }
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

    protected void _reportInvalidToken(String matchedPart, String msg) throws IOException, JsonParseException {
        StringBuilder sb = new StringBuilder(matchedPart);
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                char c = (char) _decodeCharForError(bArr[i]);
                if (Character.isJavaIdentifierPart(c)) {
                    this._inputPtr++;
                    sb.append(c);
                }
            }
            _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
            return;
        }
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == (byte) 10) {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected final void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected void _skipString() throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._skipString():void");
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:62)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:29)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
        /*
        r7 = this;
        r6 = 0;
        r7._tokenIncomplete = r6;
        r1 = sInputCodesUtf8;
        r2 = r7._inputBuffer;
    L_0x0007:
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        if (r4 < r3) goto L_0x004e;
    L_0x000d:
        r7.loadMoreGuaranteed();
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        r5 = r4;
    L_0x0015:
        if (r5 >= r3) goto L_0x0028;
    L_0x0017:
        r4 = r5 + 1;
        r6 = r2[r5];
        r0 = r6 & 255;
        r6 = r1[r0];
        if (r6 == 0) goto L_0x004e;
    L_0x0021:
        r7._inputPtr = r4;
        r6 = 34;
        if (r0 != r6) goto L_0x002b;
    L_0x0027:
        return;
    L_0x0028:
        r7._inputPtr = r5;
        goto L_0x0007;
    L_0x002b:
        r6 = r1[r0];
        switch(r6) {
            case 1: goto L_0x003a;
            case 2: goto L_0x003e;
            case 3: goto L_0x0042;
            case 4: goto L_0x0046;
            default: goto L_0x0030;
        };
    L_0x0030:
        r6 = 32;
        if (r0 >= r6) goto L_0x004a;
    L_0x0034:
        r6 = "string value";
        r7._throwUnquotedSpace(r0, r6);
        goto L_0x0007;
    L_0x003a:
        r7._decodeEscaped();
        goto L_0x0007;
    L_0x003e:
        r7._skipUtf8_2(r0);
        goto L_0x0007;
    L_0x0042:
        r7._skipUtf8_3(r0);
        goto L_0x0007;
    L_0x0046:
        r7._skipUtf8_4(r0);
        goto L_0x0007;
    L_0x004a:
        r7._reportInvalidChar(r0);
        goto L_0x0007;
    L_0x004e:
        r5 = r4;
        goto L_0x0015;
        */
    }

    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            if (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null) {
                _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
            }
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(b64variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException e) {
                throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + e.getMessage());
            }
        }
        return this._binaryValue;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public String getText() throws IOException, JsonParseException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_STRING) {
            return _getText2(t);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
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
            case ClassWriter.COMPUTE_FRAMES:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                return this._textBuffer.getTextBuffer();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return this._textBuffer.getTextBuffer();
            default:
                return this._currToken.asCharArray();
        }
    }

    public int getTextLength() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return this._parsingContext.getCurrentName().length();
            case ClassWriter.COMPUTE_FRAMES:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                return this._textBuffer.size();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return this._textBuffer.size();
            default:
                return this._currToken.asCharArray().length;
        }
    }

    public int getTextOffset() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case ClassWriter.COMPUTE_FRAMES:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                return this._textBuffer.getTextOffset();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return this._textBuffer.getTextOffset();
            default:
                return 0;
        }
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            this._currToken = null;
            return null;
        } else {
            this._tokenInputTotal = this._currInputProcessed + ((long) this._inputPtr) - 1;
            this._tokenInputRow = this._currInputRow;
            this._tokenInputCol = this._inputPtr - this._currInputRowStart - 1;
            this._binaryValue = null;
            JsonToken jsonToken;
            if (i == 93) {
                if (!this._parsingContext.inArray()) {
                    _reportMismatchedEndMarker(i, '}');
                }
                this._parsingContext = this._parsingContext.getParent();
                jsonToken = JsonToken.END_ARRAY;
                this._currToken = jsonToken;
                return jsonToken;
            } else if (i == 125) {
                if (!this._parsingContext.inObject()) {
                    _reportMismatchedEndMarker(i, ']');
                }
                this._parsingContext = this._parsingContext.getParent();
                jsonToken = JsonToken.END_OBJECT;
                this._currToken = jsonToken;
                return jsonToken;
            } else {
                if (this._parsingContext.expectComma()) {
                    if (i != 44) {
                        _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                    }
                    i = _skipWS();
                }
                if (!this._parsingContext.inObject()) {
                    return _nextTokenNotInObject(i);
                }
                this._parsingContext.setCurrentName(_parseFieldName(i).getName());
                this._currToken = JsonToken.FIELD_NAME;
                i = _skipWS();
                if (i != 58) {
                    _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
                }
                i = _skipWS();
                if (i == 34) {
                    this._tokenIncomplete = true;
                    this._nextToken = JsonToken.VALUE_STRING;
                    return this._currToken;
                } else {
                    JsonToken t;
                    switch (i) {
                        case ConnectionInstance.CONNECTION_FAILED:
                        case SmileConstants.TOKEN_PREFIX_KEY_SHARED_LONG:
                        case Opcodes.V1_5:
                        case Opcodes.V1_6:
                        case Opcodes.V1_7:
                        case Opcodes.CALOAD:
                        case Opcodes.SALOAD:
                        case Opcodes.ISTORE:
                        case Opcodes.LSTORE:
                        case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES:
                        case Opcodes.DSTORE:
                            t = parseNumberText(i);
                            break;
                        case Opcodes.DUP_X2:
                            t = JsonToken.START_ARRAY;
                            break;
                        case Opcodes.DUP2_X1:
                        case Opcodes.LUSHR:
                            _reportUnexpectedChar(i, "expected a value");
                            _matchToken(JsonToken.VALUE_TRUE);
                            t = JsonToken.VALUE_TRUE;
                            break;
                        case Opcodes.FSUB:
                            _matchToken(JsonToken.VALUE_FALSE);
                            t = JsonToken.VALUE_FALSE;
                            break;
                        case Opcodes.FDIV:
                            _matchToken(JsonToken.VALUE_NULL);
                            t = JsonToken.VALUE_NULL;
                            break;
                        case Opcodes.INEG:
                            _matchToken(JsonToken.VALUE_TRUE);
                            t = JsonToken.VALUE_TRUE;
                            break;
                        case Opcodes.LSHR:
                            t = JsonToken.START_OBJECT;
                            break;
                        default:
                            t = _handleUnexpectedValue(i);
                            break;
                    }
                    this._nextToken = t;
                    return this._currToken;
                }
            }
        }
    }

    protected Name parseEscapedFieldName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes) throws IOException, JsonParseException {
        int[] codes = sInputCodesLatin1;
        while (true) {
            int qlen2;
            if (codes[ch] != 0 && ch == 34) {
                break;
            }
            if (ch != 92) {
                _throwUnquotedSpace(ch, "name");
            } else {
                ch = _decodeEscaped();
            }
            if (ch > 127) {
                if (currQuadBytes >= 4) {
                    if (qlen >= quads.length) {
                        quads = growArrayBy(quads, quads.length);
                        this._quadBuffer = quads;
                    }
                    qlen2 = qlen + 1;
                    quads[qlen] = currQuad;
                    currQuad = 0;
                    currQuadBytes = 0;
                } else {
                    qlen2 = qlen;
                }
                if (ch < 2048) {
                    currQuad = (currQuad << 8) | ((ch >> 6) | 192);
                    currQuadBytes++;
                    qlen = qlen2;
                } else {
                    currQuad = (currQuad << 8) | ((ch >> 12) | 224);
                    currQuadBytes++;
                    if (currQuadBytes >= 4) {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                    } else {
                        qlen = qlen2;
                    }
                    currQuad = (currQuad << 8) | (((ch >> 6) & 63) | 128);
                    currQuadBytes++;
                }
                ch = (ch & 63) | 128;
                qlen2 = qlen;
            }
            qlen2 = qlen;
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & 255;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = currQuad;
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        return name == null ? addName(quads, qlen, currQuadBytes) : name;
    }

    protected Name parseLongFieldName(int q) throws IOException, JsonParseException {
        int[] codes = sInputCodesLatin1;
        int qlen = ClassWriter.COMPUTE_FRAMES;
        while (this._inputEnd - this._inputPtr >= 4) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & 255;
            if (codes[i2] != 0) {
                return i2 == 34 ? findName(this._quadBuffer, qlen, q, 1) : parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 1);
            } else {
                q = (q << 8) | i2;
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i] & 255;
                if (codes[i2] != 0) {
                    return i2 == 34 ? findName(this._quadBuffer, qlen, q, ClassWriter.COMPUTE_FRAMES) : parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 2);
                } else {
                    q = (q << 8) | i2;
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i] & 255;
                    if (codes[i2] != 0) {
                        return i2 == 34 ? findName(this._quadBuffer, qlen, q, JsonWriteContext.STATUS_OK_AFTER_SPACE) : parseEscapedFieldName(this._quadBuffer, qlen, q, i2, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                    } else {
                        q = (q << 8) | i2;
                        bArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        i2 = bArr[i] & 255;
                        if (codes[i2] != 0) {
                            return i2 == 34 ? findName(this._quadBuffer, qlen, q, JsonWriteContext.STATUS_EXPECT_VALUE) : parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 4);
                        } else {
                            if (qlen >= this._quadBuffer.length) {
                                this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
                            }
                            int qlen2 = qlen + 1;
                            this._quadBuffer[qlen] = q;
                            q = i2;
                            qlen = qlen2;
                        }
                    }
                }
            }
        }
        return parseEscapedFieldName(this._quadBuffer, qlen, 0, q, 0);
    }

    protected final Name parseMediumFieldName(int q2, int[] codes) throws IOException, JsonParseException {
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & 255;
        if (codes[i2] != 0) {
            return i2 == 34 ? findName(this._quad1, q2, 1) : parseFieldName(this._quad1, q2, i2, 1);
        } else {
            q2 = (q2 << 8) | i2;
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            i2 = bArr[i] & 255;
            if (codes[i2] != 0) {
                return i2 == 34 ? findName(this._quad1, q2, ClassWriter.COMPUTE_FRAMES) : parseFieldName(this._quad1, q2, i2, ClassWriter.COMPUTE_FRAMES);
            } else {
                q2 = (q2 << 8) | i2;
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i] & 255;
                if (codes[i2] != 0) {
                    return i2 == 34 ? findName(this._quad1, q2, JsonWriteContext.STATUS_OK_AFTER_SPACE) : parseFieldName(this._quad1, q2, i2, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                } else {
                    q2 = (q2 << 8) | i2;
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i] & 255;
                    if (codes[i2] != 0) {
                        return i2 == 34 ? findName(this._quad1, q2, JsonWriteContext.STATUS_EXPECT_VALUE) : parseFieldName(this._quad1, q2, i2, JsonWriteContext.STATUS_EXPECT_VALUE);
                    } else {
                        this._quadBuffer[0] = this._quad1;
                        this._quadBuffer[1] = q2;
                        return parseLongFieldName(i2);
                    }
                }
            }
        }
    }

    protected final JsonToken parseNumberText(int c) throws IOException, JsonParseException {
        int outPtr;
        int outPtr2;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        boolean negative = c == 45;
        if (negative) {
            outPtr = 0 + 1;
            outBuf[0] = '-';
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & 255;
            if (c < 48 || c > 57) {
                return _handleInvalidNumberStart(c, true);
            }
        } else {
            outPtr = 0;
        }
        if (c == 48) {
            c = _verifyNoLeadingZeroes();
        }
        outPtr2 = outPtr + 1;
        outBuf[outPtr] = (char) c;
        int intLen = 1;
        int end = this._inputPtr + outBuf.length;
        if (end > this._inputEnd) {
            end = this._inputEnd;
        }
        while (this._inputPtr < end) {
            byte[] bArr2 = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            c = bArr2[i2] & 255;
            if (c >= 48 && c <= 57) {
                intLen++;
                outPtr = outPtr2 + 1;
                outBuf[outPtr2] = (char) c;
                outPtr2 = outPtr;
            }
            if (c == 46 || c == 101 || c == 69) {
                return _parseFloatText(outBuf, outPtr2, c, negative, intLen);
            }
            this._inputPtr--;
            this._textBuffer.setCurrentLength(outPtr2);
            return resetInt(negative, intLen);
        }
        return _parserNumber2(outBuf, outPtr2, negative, intLen);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException {
        return reportInvalidChar(b64variant, ch, bindex, null);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= 32) {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ((char) ch) + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        return new IllegalArgumentException(base);
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    protected Name slowParseFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing '\"' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & 255;
        return i2 == 34 ? BytesToNameCanonicalizer.getEmptyName() : parseEscapedFieldName(this._quadBuffer, 0, 0, i2, 0);
    }
}