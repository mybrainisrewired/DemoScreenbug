package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.ConnectionInstance;
import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class ReaderBasedParser extends ReaderBasedNumericParser {
    protected ObjectCodec _objectCodec;
    protected final CharsToNameCanonicalizer _symbols;
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

    public ReaderBasedParser(IOContext ioCtxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st) {
        super(ioCtxt, features, r);
        this._tokenIncomplete = false;
        this._objectCodec = codec;
        this._symbols = st;
    }

    private final int _decodeBase64Escape(Base64Variant b64variant, char ch, int index) throws IOException, JsonParseException {
        if (ch != '\\') {
            throw reportInvalidChar(b64variant, ch, index);
        }
        char unescaped = _decodeEscaped();
        if (unescaped <= ' ' && index == 0) {
            return -1;
        }
        int bits = b64variant.decodeBase64Char(unescaped);
        if (bits >= 0) {
            return bits;
        }
        throw reportInvalidChar(b64variant, unescaped, index);
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

    private String _parseFieldName2(int startPtr, int hash, char endChar) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing '" + ((char) endChar) + "' for name");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= endChar && i2 == endChar) {
                    this._textBuffer.setCurrentLength(outPtr);
                    TextBuffer tb = this._textBuffer;
                    return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
                } else if (i2 < ' ') {
                    _throwUnquotedSpace(i2, "name");
                }
            }
            hash = hash * 31 + i2;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            } else {
                outPtr = outPtr2;
            }
        }
    }

    private String _parseUnusualFieldName2(int startPtr, int hash, int[] codes) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        char maxCode = codes.length;
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char c = this._inputBuffer[this._inputPtr];
                char i = c;
                if (i <= maxCode) {
                    if (codes[i] != 0) {
                    }
                } else if (!Character.isJavaIdentifierPart(c)) {
                }
                this._inputPtr++;
                hash = hash * 31 + i;
                int outPtr2 = outPtr + 1;
                outBuf[outPtr] = c;
                if (outPtr2 >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr = 0;
                } else {
                    outPtr = outPtr2;
                }
            }
            this._textBuffer.setCurrentLength(outPtr);
            TextBuffer tb = this._textBuffer;
            return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
        }
    }

    private final void _skipCComment() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
                if (i2 <= 42) {
                    if (i2 == 42) {
                        if (this._inputPtr < this._inputEnd || loadMore()) {
                            if (this._inputBuffer[this._inputPtr] == '/') {
                                this._inputPtr++;
                                return;
                            }
                        }
                    } else if (i2 < 32) {
                        if (i2 == 10) {
                            _skipLF();
                        } else if (i2 == 13) {
                            _skipCR();
                        } else if (i2 != 9) {
                            _throwInvalidSpace(i2);
                        }
                    }
                }
            }
            _reportInvalidEOF(" in a comment");
            return;
        }
    }

    private final void _skipComment() throws IOException, JsonParseException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(Opcodes.V1_3, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        if (c == '/') {
            _skipCppComment();
        } else if (c == '*') {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCppComment() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
                if (i2 < 32 && i2 == 10) {
                    _skipLF();
                    return;
                } else if (i2 == 13) {
                    _skipCR();
                    return;
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            } else {
                return;
            }
        }
    }

    private final int _skipWS() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
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
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected byte[] _decodeBase64(org.codehaus.jackson.Base64Variant r11_b64variant) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.ReaderBasedParser._decodeBase64(org.codehaus.jackson.Base64Variant):byte[]");
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
        r2 = r4[r5];
        r4 = 32;
        if (r2 <= r4) goto L_0x0008;
    L_0x001f:
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0033;
    L_0x0025:
        if (r2 != r8) goto L_0x002c;
    L_0x0027:
        r4 = r1.toByteArray();
    L_0x002b:
        return r4;
    L_0x002c:
        r4 = 0;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
        if (r0 < 0) goto L_0x0008;
    L_0x0033:
        r3 = r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x003d;
    L_0x003a:
        r10.loadMoreGuaranteed();
    L_0x003d:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r2 = r4[r5];
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0052;
    L_0x004d:
        r4 = 1;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
    L_0x0052:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x005f;
    L_0x005c:
        r10.loadMoreGuaranteed();
    L_0x005f:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r2 = r4[r5];
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00cc;
    L_0x006f:
        if (r0 == r7) goto L_0x0088;
    L_0x0071:
        if (r2 != r8) goto L_0x0083;
    L_0x0073:
        r4 = r11.usesPadding();
        if (r4 != 0) goto L_0x0083;
    L_0x0079:
        r3 = r3 >> 4;
        r1.append(r3);
        r4 = r1.toByteArray();
        goto L_0x002b;
    L_0x0083:
        r4 = 2;
        r0 = r10._decodeBase64Escape(r11, r2, r4);
    L_0x0088:
        if (r0 != r7) goto L_0x00cc;
    L_0x008a:
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x0093;
    L_0x0090:
        r10.loadMoreGuaranteed();
    L_0x0093:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r2 = r4[r5];
        r4 = r11.usesPaddingChar(r2);
        if (r4 != 0) goto L_0x00c5;
    L_0x00a3:
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
    L_0x00c5:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x0008;
    L_0x00cc:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r10._inputPtr;
        r5 = r10._inputEnd;
        if (r4 < r5) goto L_0x00d9;
    L_0x00d6:
        r10.loadMoreGuaranteed();
    L_0x00d9:
        r4 = r10._inputBuffer;
        r5 = r10._inputPtr;
        r6 = r5 + 1;
        r10._inputPtr = r6;
        r2 = r4[r5];
        r0 = r11.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x010b;
    L_0x00e9:
        if (r0 == r7) goto L_0x0102;
    L_0x00eb:
        if (r2 != r8) goto L_0x00fe;
    L_0x00ed:
        r4 = r11.usesPadding();
        if (r4 != 0) goto L_0x00fe;
    L_0x00f3:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        r4 = r1.toByteArray();
        goto L_0x002b;
    L_0x00fe:
        r0 = r10._decodeBase64Escape(r11, r2, r9);
    L_0x0102:
        if (r0 != r7) goto L_0x010b;
    L_0x0104:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        goto L_0x0008;
    L_0x010b:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r1.appendThreeBytes(r3);
        goto L_0x0008;
        */
    }

    protected final char _decodeEscaped() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        switch (c) {
            case '\"':
            case Opcodes.V1_3:
            case Opcodes.DUP2:
                return c;
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
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = cArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                    i2++;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape(c);
        }
    }

    protected void _finishString() throws IOException, JsonParseException {
        int ptr = this._inputPtr;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch >= maxCode || codes[ch] == 0) {
                    ptr++;
                } else if (ch == 34) {
                    this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
                    this._inputPtr = ptr + 1;
                    return;
                }
            } while (ptr < inputLen);
        }
        this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
        this._inputPtr = ptr;
        _finishString2();
    }

    protected void _finishString2() throws IOException, JsonParseException {
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= '\"' && i2 == '\"') {
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                } else if (i2 < ' ') {
                    _throwUnquotedSpace(i2, "string value");
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
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

    protected final JsonToken _handleApostropheValue() throws IOException, JsonParseException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= '\'' && i2 == '\'') {
                    this._textBuffer.setCurrentLength(outPtr);
                    return JsonToken.VALUE_STRING;
                } else if (i2 < ' ') {
                    _throwUnquotedSpace(i2, "string value");
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
    }

    protected final JsonToken _handleUnexpectedValue(int i) throws IOException, JsonParseException {
        char[] cArr;
        int i2;
        switch (i) {
            case 39:
                if (isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApostropheValue();
                }
                _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
            case ConnectionInstance.SERVER_SCREEN_IMAGE_UPDATE:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                cArr = this._inputBuffer;
                i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                return _handleInvalidNumberStart(cArr[i2], false);
            case 78:
                if (_matchToken("NaN", 1) && isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return resetAsNaN("NaN", Double.NaN);
                }
                _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                cArr = this._inputBuffer;
                i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                _reportUnexpectedChar(cArr[i2], "expected 'NaN' or a valid value");
                _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
            default:
                _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
                return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final java.lang.String _handleUnusualFieldName(int r12_i) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.ReaderBasedParser._handleUnusualFieldName(int):java.lang.String");
        /*
        r11 = this;
        r8 = 39;
        if (r12 != r8) goto L_0x0011;
    L_0x0004:
        r8 = org.codehaus.jackson.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
        r8 = r11.isEnabled(r8);
        if (r8 == 0) goto L_0x0011;
    L_0x000c:
        r8 = r11._parseApostropheFieldName();
    L_0x0010:
        return r8;
    L_0x0011:
        r8 = org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
        r8 = r11.isEnabled(r8);
        if (r8 != 0) goto L_0x001e;
    L_0x0019:
        r8 = "was expecting double-quote to start field name";
        r11._reportUnexpectedChar(r12, r8);
    L_0x001e:
        r1 = org.codehaus.jackson.util.CharTypes.getInputCodeLatin1JsNames();
        r5 = r1.length;
        if (r12 >= r5) goto L_0x005d;
    L_0x0025:
        r8 = r1[r12];
        if (r8 != 0) goto L_0x005b;
    L_0x0029:
        r8 = 48;
        if (r12 < r8) goto L_0x0031;
    L_0x002d:
        r8 = 57;
        if (r12 <= r8) goto L_0x005b;
    L_0x0031:
        r2 = 1;
    L_0x0032:
        if (r2 != 0) goto L_0x0039;
    L_0x0034:
        r8 = "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name";
        r11._reportUnexpectedChar(r12, r8);
    L_0x0039:
        r6 = r11._inputPtr;
        r3 = 0;
        r4 = r11._inputEnd;
        if (r6 >= r4) goto L_0x0083;
    L_0x0040:
        r8 = r11._inputBuffer;
        r0 = r8[r6];
        if (r0 >= r5) goto L_0x0063;
    L_0x0046:
        r8 = r1[r0];
        if (r8 == 0) goto L_0x007b;
    L_0x004a:
        r8 = r11._inputPtr;
        r7 = r8 + -1;
        r11._inputPtr = r6;
        r8 = r11._symbols;
        r9 = r11._inputBuffer;
        r10 = r6 - r7;
        r8 = r8.findSymbol(r9, r7, r10, r3);
        goto L_0x0010;
    L_0x005b:
        r2 = 0;
        goto L_0x0032;
    L_0x005d:
        r8 = (char) r12;
        r2 = java.lang.Character.isJavaIdentifierPart(r8);
        goto L_0x0032;
    L_0x0063:
        r8 = (char) r0;
        r8 = java.lang.Character.isJavaIdentifierPart(r8);
        if (r8 != 0) goto L_0x007b;
    L_0x006a:
        r8 = r11._inputPtr;
        r7 = r8 + -1;
        r11._inputPtr = r6;
        r8 = r11._symbols;
        r9 = r11._inputBuffer;
        r10 = r6 - r7;
        r8 = r8.findSymbol(r9, r7, r10, r3);
        goto L_0x0010;
    L_0x007b:
        r8 = r3 * 31;
        r3 = r8 + r0;
        r6 = r6 + 1;
        if (r6 < r4) goto L_0x0040;
    L_0x0083:
        r8 = r11._inputPtr;
        r7 = r8 + -1;
        r11._inputPtr = r6;
        r8 = r11._parseUnusualFieldName2(r7, r3, r1);
        goto L_0x0010;
        */
    }

    protected void _matchToken(JsonToken token) throws IOException, JsonParseException {
        String matchStr = token.asString();
        int i = 1;
        int len = matchStr.length();
        while (i < len) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in a value");
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i), "'null', 'true' or 'false'");
            }
            this._inputPtr++;
            i++;
        }
    }

    protected final String _parseApostropheFieldName() throws IOException, JsonParseException {
        int start;
        int ptr = this._inputPtr;
        int hash = 0;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch != 39) {
                    if (ch < maxCode && codes[ch] != 0) {
                        break;
                    }
                    hash = hash * 31 + ch;
                    ptr++;
                } else {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
            } while (ptr < inputLen);
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseFieldName2(start, hash, 39);
    }

    protected final String _parseFieldName(int i) throws IOException, JsonParseException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        int start;
        int ptr = this._inputPtr;
        int hash = 0;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch >= maxCode || codes[ch] == 0) {
                    hash = hash * 31 + ch;
                    ptr++;
                } else if (ch == 34) {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
            } while (ptr < inputLen);
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseFieldName2(start, hash, 34);
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == '\n') {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected final void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected void _skipString() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int inputPtr = this._inputPtr;
        int inputLen = this._inputEnd;
        char[] inputBuffer = this._inputBuffer;
        while (true) {
            if (inputPtr >= inputLen) {
                this._inputPtr = inputPtr;
                if (!loadMore()) {
                    _reportInvalidEOF(": was expecting closing quote for a string value");
                }
                inputPtr = this._inputPtr;
                inputLen = this._inputEnd;
            }
            int inputPtr2 = inputPtr + 1;
            char i = inputBuffer[inputPtr];
            if (i <= '\\' && i == '\\') {
                this._inputPtr = inputPtr2;
                char c = _decodeEscaped();
                inputPtr = this._inputPtr;
                inputLen = this._inputEnd;
            } else if (i <= '\"' && i == '\"') {
                this._inputPtr = inputPtr2;
                return;
            } else if (i < ' ') {
                this._inputPtr = inputPtr2;
                _throwUnquotedSpace(i, "string value");
            }
            inputPtr = inputPtr2;
        }
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

    public final String getText() throws IOException, JsonParseException {
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
                boolean inObject = this._parsingContext.inObject();
                if (inObject) {
                    this._parsingContext.setCurrentName(_parseFieldName(i));
                    this._currToken = JsonToken.FIELD_NAME;
                    i = _skipWS();
                    if (i != 58) {
                        _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
                    }
                    i = _skipWS();
                }
                switch (i) {
                    case 34:
                        this._tokenIncomplete = true;
                        jsonToken = JsonToken.VALUE_STRING;
                        break;
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
                        break;
                    case Opcodes.DUP_X2:
                        if (!inObject) {
                            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                        }
                        jsonToken = JsonToken.START_ARRAY;
                        break;
                    case Opcodes.DUP2_X1:
                    case Opcodes.LUSHR:
                        _reportUnexpectedChar(i, "expected a value");
                        _matchToken(JsonToken.VALUE_TRUE);
                        jsonToken = JsonToken.VALUE_TRUE;
                        break;
                    case Opcodes.FSUB:
                        _matchToken(JsonToken.VALUE_FALSE);
                        jsonToken = JsonToken.VALUE_FALSE;
                        break;
                    case Opcodes.FDIV:
                        _matchToken(JsonToken.VALUE_NULL);
                        jsonToken = JsonToken.VALUE_NULL;
                        break;
                    case Opcodes.INEG:
                        _matchToken(JsonToken.VALUE_TRUE);
                        jsonToken = JsonToken.VALUE_TRUE;
                        break;
                    case Opcodes.LSHR:
                        if (!inObject) {
                            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                        }
                        jsonToken = JsonToken.START_OBJECT;
                        break;
                    default:
                        jsonToken = _handleUnexpectedValue(i);
                        break;
                }
                if (inObject) {
                    this._nextToken = jsonToken;
                    return this._currToken;
                } else {
                    this._currToken = jsonToken;
                    return jsonToken;
                }
            }
        }
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex) throws IllegalArgumentException {
        return reportInvalidChar(b64variant, ch, bindex, null);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        return new IllegalArgumentException(base);
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }
}