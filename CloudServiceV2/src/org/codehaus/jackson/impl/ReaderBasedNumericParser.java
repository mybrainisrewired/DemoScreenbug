package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;

public abstract class ReaderBasedNumericParser extends ReaderBasedParserBase {
    public ReaderBasedNumericParser(IOContext pc, int features, Reader r) {
        super(pc, features, r);
    }

    private final char _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return '0';
        }
        char ch = this._inputBuffer[this._inputPtr];
        if (ch < '0' || ch > '9') {
            return '0';
        }
        if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (ch != '0') {
            return ch;
        }
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return ch;
            }
            ch = this._inputBuffer[this._inputPtr];
            if (ch >= '0' && ch <= '9') {
                this._inputPtr++;
            }
            return '0';
        } while (ch == '0');
        return ch;
    }

    private final JsonToken parseNumberText2(boolean negative) throws IOException, JsonParseException {
        int outPtr;
        char c;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int outPtr2 = 0;
        if (negative) {
            outPtr = 0 + 1;
            outBuf[0] = '-';
            outPtr2 = outPtr;
        }
        int intLen = 0;
        if (this._inputPtr < this._inputEnd) {
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            c = cArr[i];
        } else {
            c = getNextChar("No digit following minus sign");
        }
        if (c == '0') {
            c = _verifyNoLeadingZeroes();
        }
        boolean z = false;
        while (c >= '0' && c <= '9') {
            intLen++;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr2 = 0;
            }
            outPtr = outPtr2 + 1;
            outBuf[outPtr2] = c;
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                c = '\u0000';
                z = true;
                break;
            } else {
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                c = cArr[i];
                outPtr2 = outPtr;
            }
        }
        outPtr = outPtr2;
        if (intLen == 0) {
            reportInvalidNumber("Missing integer part (next char " + _getCharDesc(c) + ")");
        }
        int fractLen = 0;
        if (c == '.') {
            outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            while (true) {
                if (this._inputPtr < this._inputEnd || loadMore()) {
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    c = cArr[i];
                    if (c >= '0' && c <= '9') {
                        fractLen++;
                        if (outPtr2 >= outBuf.length) {
                            outBuf = this._textBuffer.finishCurrentSegment();
                            outPtr2 = 0;
                        }
                        outPtr = outPtr2 + 1;
                        outBuf[outPtr2] = c;
                        outPtr2 = outPtr;
                    }
                } else {
                    z = true;
                }
                if (fractLen == 0) {
                    reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
                }
            }
        } else {
            outPtr2 = outPtr;
        }
        int expLen = 0;
        if (c == 'e' || c == 'E') {
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr2 = 0;
            }
            outPtr = outPtr2 + 1;
            outBuf[outPtr2] = c;
            if (this._inputPtr < this._inputEnd) {
                cArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                c = cArr[i];
            } else {
                c = getNextChar("expected a digit for number exponent");
            }
            if (c == '-' || c == '+') {
                if (outPtr >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr2 = 0;
                } else {
                    outPtr2 = outPtr;
                }
                outPtr = outPtr2 + 1;
                outBuf[outPtr2] = c;
                if (this._inputPtr < this._inputEnd) {
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    c = cArr[i];
                } else {
                    c = getNextChar("expected a digit for number exponent");
                }
                outPtr2 = outPtr;
            } else {
                outPtr2 = outPtr;
            }
            while (c <= '9' && c >= '0') {
                expLen++;
                if (outPtr2 >= outBuf.length) {
                    outBuf = this._textBuffer.finishCurrentSegment();
                    outPtr2 = 0;
                }
                outPtr = outPtr2 + 1;
                outBuf[outPtr2] = c;
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    z = true;
                    outPtr2 = outPtr;
                    break;
                } else {
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    c = cArr[i];
                    outPtr2 = outPtr;
                }
            }
            if (expLen == 0) {
                reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
            }
        }
        if (!z) {
            this._inputPtr--;
        }
        this._textBuffer.setCurrentLength(outPtr2);
        return reset(negative, intLen, fractLen, expLen);
    }

    protected JsonToken _handleInvalidNumberStart(int ch, boolean negative) throws IOException, JsonParseException {
        double d = Double.NEGATIVE_INFINITY;
        if (ch == 73) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = cArr[i];
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final org.codehaus.jackson.JsonToken parseNumberText(int r15_ch) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.ReaderBasedNumericParser.parseNumberText(int):org.codehaus.jackson.JsonToken");
        /*
        r14 = this;
        r13 = 45;
        r9 = 1;
        r12 = 57;
        r11 = 48;
        if (r15 != r13) goto L_0x0021;
    L_0x0009:
        r5 = r9;
    L_0x000a:
        r6 = r14._inputPtr;
        r8 = r6 + -1;
        r2 = r14._inputEnd;
        if (r5 == 0) goto L_0x0036;
    L_0x0012:
        r10 = r14._inputEnd;
        if (r6 < r10) goto L_0x0023;
    L_0x0016:
        if (r5 == 0) goto L_0x001a;
    L_0x0018:
        r8 = r8 + 1;
    L_0x001a:
        r14._inputPtr = r8;
        r9 = r14.parseNumberText2(r5);
    L_0x0020:
        return r9;
    L_0x0021:
        r5 = 0;
        goto L_0x000a;
    L_0x0023:
        r10 = r14._inputBuffer;
        r7 = r6 + 1;
        r15 = r10[r6];
        if (r15 > r12) goto L_0x002d;
    L_0x002b:
        if (r15 >= r11) goto L_0x0035;
    L_0x002d:
        r14._inputPtr = r7;
        r9 = r14._handleInvalidNumberStart(r15, r9);
        r6 = r7;
        goto L_0x0020;
    L_0x0035:
        r6 = r7;
    L_0x0036:
        if (r15 == r11) goto L_0x0016;
    L_0x0038:
        r3 = 1;
    L_0x0039:
        r9 = r14._inputEnd;
        if (r6 >= r9) goto L_0x0016;
    L_0x003d:
        r9 = r14._inputBuffer;
        r7 = r6 + 1;
        r15 = r9[r6];
        if (r15 < r11) goto L_0x0047;
    L_0x0045:
        if (r15 <= r12) goto L_0x0050;
    L_0x0047:
        r1 = 0;
        r9 = 46;
        if (r15 != r9) goto L_0x0066;
    L_0x004c:
        if (r7 < r2) goto L_0x0054;
    L_0x004e:
        r6 = r7;
        goto L_0x0016;
    L_0x0050:
        r3 = r3 + 1;
        r6 = r7;
        goto L_0x0039;
    L_0x0054:
        r9 = r14._inputBuffer;
        r6 = r7 + 1;
        r15 = r9[r7];
        if (r15 < r11) goto L_0x005e;
    L_0x005c:
        if (r15 <= r12) goto L_0x0073;
    L_0x005e:
        if (r1 != 0) goto L_0x0065;
    L_0x0060:
        r9 = "Decimal point not followed by a digit";
        r14.reportUnexpectedNumberChar(r15, r9);
    L_0x0065:
        r7 = r6;
    L_0x0066:
        r0 = 0;
        r9 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r15 == r9) goto L_0x006f;
    L_0x006b:
        r9 = 69;
        if (r15 != r9) goto L_0x00a4;
    L_0x006f:
        if (r7 < r2) goto L_0x0077;
    L_0x0071:
        r6 = r7;
        goto L_0x0016;
    L_0x0073:
        r1 = r1 + 1;
        r7 = r6;
        goto L_0x004c;
    L_0x0077:
        r9 = r14._inputBuffer;
        r6 = r7 + 1;
        r15 = r9[r7];
        if (r15 == r13) goto L_0x0083;
    L_0x007f:
        r9 = 43;
        if (r15 != r9) goto L_0x00b8;
    L_0x0083:
        if (r6 >= r2) goto L_0x0016;
    L_0x0085:
        r9 = r14._inputBuffer;
        r7 = r6 + 1;
        r15 = r9[r6];
    L_0x008b:
        if (r15 > r12) goto L_0x009d;
    L_0x008d:
        if (r15 < r11) goto L_0x009d;
    L_0x008f:
        r0 = r0 + 1;
        if (r7 < r2) goto L_0x0095;
    L_0x0093:
        r6 = r7;
        goto L_0x0016;
    L_0x0095:
        r9 = r14._inputBuffer;
        r6 = r7 + 1;
        r15 = r9[r7];
        r7 = r6;
        goto L_0x008b;
    L_0x009d:
        if (r0 != 0) goto L_0x00a4;
    L_0x009f:
        r9 = "Exponent indicator not followed by a digit";
        r14.reportUnexpectedNumberChar(r15, r9);
    L_0x00a4:
        r6 = r7;
        r6 = r6 + -1;
        r14._inputPtr = r6;
        r4 = r6 - r8;
        r9 = r14._textBuffer;
        r10 = r14._inputBuffer;
        r9.resetWithShared(r10, r8, r4);
        r9 = r14.reset(r5, r3, r1, r0);
        goto L_0x0020;
    L_0x00b8:
        r7 = r6;
        goto L_0x008b;
        */
    }
}