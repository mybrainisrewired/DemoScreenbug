package com.millennialmedia.google.gson.stream;

import com.google.android.gms.location.LocationRequest;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.google.gson.internal.JsonReaderInternalAccess;
import com.millennialmedia.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer;
    private final Reader in;
    private boolean lenient;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos;
    private int[] stack;
    private int stackSize;

    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
            public void promoteNameToValue(JsonReader reader) throws IOException {
                if (reader instanceof JsonTreeReader) {
                    ((JsonTreeReader) reader).promoteNameToValue();
                } else {
                    int p = reader.peeked;
                    if (p == 0) {
                        p = reader.doPeek();
                    }
                    if (p == 13) {
                        reader.peeked = PEEKED_DOUBLE_QUOTED;
                    } else if (p == 12) {
                        reader.peeked = PEEKED_SINGLE_QUOTED;
                    } else if (p == 14) {
                        reader.peeked = PEEKED_UNQUOTED;
                    } else {
                        throw new IllegalStateException("Expected a name but was " + reader.peek() + " " + " at line " + reader.getLineNumber() + " column " + reader.getColumnNumber());
                    }
                }
            }
        };
    }

    public JsonReader(Reader in) {
        this.lenient = false;
        this.buffer = new char[1024];
        this.pos = 0;
        this.limit = 0;
        this.lineNumber = 0;
        this.lineStart = 0;
        this.peeked = 0;
        this.stack = new int[32];
        this.stackSize = 0;
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + 1;
        iArr[i] = 6;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace(true);
        this.pos--;
        if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || fillBuffer(NON_EXECUTE_PREFIX.length)) {
            int i = PEEKED_NONE;
            while (i < NON_EXECUTE_PREFIX.length) {
                if (this.buffer[this.pos + i] == NON_EXECUTE_PREFIX[i]) {
                    i++;
                } else {
                    return;
                }
            }
            this.pos += NON_EXECUTE_PREFIX.length;
        }
    }

    private int doPeek() throws IOException {
        int peekStack = this.stack[this.stackSize - 1];
        if (peekStack == 1) {
            this.stack[this.stackSize - 1] = 2;
        } else if (peekStack == 2) {
            switch (nextNonWhitespace(true)) {
                case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    break;
                case 59:
                    checkLenient();
                    break;
                case 93:
                    this.peeked = 4;
                    return PEEKED_END_ARRAY;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else if (peekStack == 3 || peekStack == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (peekStack == 5) {
                switch (nextNonWhitespace(true)) {
                    case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                        break;
                    case 59:
                        checkLenient();
                        break;
                    case 125:
                        this.peeked = 2;
                        return 2;
                    default:
                        throw syntaxError("Unterminated object");
                }
            }
            int c = nextNonWhitespace(true);
            switch (c) {
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    this.peeked = 13;
                    return PEEKED_DOUBLE_QUOTED_NAME;
                case ApiEventType.API_MRAID_PAUSE_AUDIO:
                    checkLenient();
                    this.peeked = 12;
                    return PEEKED_SINGLE_QUOTED_NAME;
                case 125:
                    if (peekStack != 5) {
                        this.peeked = 2;
                        return 2;
                    } else {
                        throw syntaxError("Expected name");
                    }
                default:
                    checkLenient();
                    this.pos--;
                    if (isLiteral((char) c)) {
                        this.peeked = 14;
                        return PEEKED_UNQUOTED_NAME;
                    } else {
                        throw syntaxError("Expected name");
                    }
            }
        } else if (peekStack == 4) {
            this.stack[this.stackSize - 1] = 5;
            switch (nextNonWhitespace(true)) {
                case 58:
                    break;
                case 61:
                    checkLenient();
                    if ((this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) && this.buffer[this.pos] == '>') {
                        this.pos++;
                    }
                    break;
                default:
                    throw syntaxError("Expected ':'");
            }
        } else if (peekStack == 6) {
            if (this.lenient) {
                consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        } else if (peekStack == 7) {
            if (nextNonWhitespace(false) == -1) {
                this.peeked = 17;
                return PEEKED_EOF;
            } else {
                checkLenient();
                this.pos--;
            }
        } else if (peekStack == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
        switch (nextNonWhitespace(true)) {
            case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                if (this.stackSize == 1) {
                    checkLenient();
                }
                this.peeked = 9;
                return PEEKED_DOUBLE_QUOTED;
            case ApiEventType.API_MRAID_PAUSE_AUDIO:
                checkLenient();
                this.peeked = 8;
                return PEEKED_SINGLE_QUOTED;
            case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
            case 59:
                if (peekStack == 1 || peekStack == 2) {
                    checkLenient();
                    this.pos--;
                    this.peeked = 7;
                    return 7;
                } else {
                    throw syntaxError("Unexpected value");
                }
            case 91:
                this.peeked = 3;
                return PEEKED_BEGIN_ARRAY;
            case 93:
                if (peekStack == 1) {
                    this.peeked = 4;
                    return PEEKED_END_ARRAY;
                }
                checkLenient();
                this.pos--;
                this.peeked = 7;
                return 7;
            case 123:
                this.peeked = 1;
                return 1;
            default:
                this.pos--;
                if (this.stackSize == 1) {
                    checkLenient();
                }
                int result = peekKeyword();
                if (result != 0) {
                    return result;
                }
                result = peekNumber();
                if (result != 0) {
                    return result;
                }
                if (isLiteral(this.buffer[this.pos])) {
                    checkLenient();
                    this.peeked = 10;
                    return PEEKED_UNQUOTED;
                } else {
                    throw syntaxError("Expected value");
                }
        }
    }

    private boolean fillBuffer(int minimum) throws IOException {
        char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, PEEKED_NONE, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            int total = this.in.read(buffer, this.limit, buffer.length - this.limit);
            if (total == -1) {
                return false;
            }
            this.limit += total;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == '\ufeff') {
                this.pos++;
                this.lineStart++;
                minimum++;
            }
        } while (this.limit < minimum);
        return true;
    }

    private int getColumnNumber() {
        return this.pos - this.lineStart + 1;
    }

    private int getLineNumber() {
        return this.lineNumber + 1;
    }

    private boolean isLiteral(char c) throws IOException {
        switch (c) {
            case PEEKED_DOUBLE_QUOTED:
            case PEEKED_UNQUOTED:
            case PEEKED_SINGLE_QUOTED_NAME:
            case PEEKED_DOUBLE_QUOTED_NAME:
            case ApiEventType.API_MRAID_PLAY_AUDIO:
            case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
            case ':':
            case '[':
            case ']':
            case '{':
            case '}':
                return false;
            case ApiEventType.API_MRAID_IS_AUDIO_MUTED:
            case ApiEventType.API_MRAID_PAUSE_VIDEO:
            case ';':
            case '=':
            case '\\':
                checkLenient();
                return false;
            default:
                return true;
        }
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        while (true) {
            if (p == l) {
                this.pos = p;
                if (fillBuffer(PEEKED_BEGIN_OBJECT)) {
                    p = this.pos;
                    l = this.limit;
                } else if (!throwOnEof) {
                    return -1;
                } else {
                    throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber());
                }
            }
            int p2 = p + 1;
            int c = buffer[p];
            if (c == 10) {
                this.lineNumber++;
                this.lineStart = p2;
                p = p2;
            } else if (c == 32 || c == 13) {
                p = p2;
            } else if (c == 9) {
                p = p2;
            } else if (c == 47) {
                this.pos = p2;
                if (p2 == l) {
                    this.pos--;
                    boolean charsLoaded = fillBuffer(PEEKED_END_OBJECT);
                    this.pos++;
                    if (!charsLoaded) {
                        return c;
                    }
                }
                checkLenient();
                switch (buffer[this.pos]) {
                    case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                        this.pos++;
                        if (skipTo("*/")) {
                            p = this.pos + 2;
                            l = this.limit;
                        } else {
                            throw syntaxError("Unterminated comment");
                        }
                    case ApiEventType.API_MRAID_PAUSE_VIDEO:
                        this.pos++;
                        skipToEndOfLine();
                        p = this.pos;
                        l = this.limit;
                        break;
                    default:
                        return c;
                }
            } else if (c == 35) {
                this.pos = p2;
                checkLenient();
                skipToEndOfLine();
                p = this.pos;
                l = this.limit;
            } else {
                this.pos = p2;
                return c;
            }
        }
    }

    private String nextQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        StringBuilder builder = new StringBuilder();
        do {
            int p = this.pos;
            int l = this.limit;
            int start = p;
            int p2 = p;
            while (p2 < l) {
                p = p2 + 1;
                char c = buffer[p2];
                if (c == quote) {
                    this.pos = p;
                    builder.append(buffer, start, p - start - 1);
                    return builder.toString();
                } else {
                    if (c == '\\') {
                        this.pos = p;
                        builder.append(buffer, start, p - start - 1);
                        builder.append(readEscapeCharacter());
                        p = this.pos;
                        l = this.limit;
                        start = p;
                    } else if (c == '\n') {
                        this.lineNumber++;
                        this.lineStart = p;
                    }
                    p2 = p;
                }
            }
            builder.append(buffer, start, p2 - start);
            this.pos = p2;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() throws IOException {
        StringBuilder builder = null;
        int i = PEEKED_NONE;
        while (true) {
            String result;
            if (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED:
                    case PEEKED_UNQUOTED:
                    case PEEKED_SINGLE_QUOTED_NAME:
                    case PEEKED_DOUBLE_QUOTED_NAME:
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                    case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                        break;
                    case ApiEventType.API_MRAID_IS_AUDIO_MUTED:
                    case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i++;
                        break;
                }
            } else if (i >= this.buffer.length) {
                if (builder == null) {
                    builder = new StringBuilder();
                }
                builder.append(this.buffer, this.pos, i);
                this.pos += i;
                i = PEEKED_NONE;
                if (!fillBuffer(PEEKED_BEGIN_OBJECT)) {
                }
            } else if (fillBuffer(i + 1)) {
            }
            if (builder == null) {
                result = new String(this.buffer, this.pos, i);
            } else {
                builder.append(this.buffer, this.pos, i);
                result = builder.toString();
            }
            this.pos += i;
            return result;
        }
    }

    private int peekKeyword() throws IOException {
        String keyword;
        int peeking;
        char c = this.buffer[this.pos];
        String keywordUpper;
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = PEEKED_TRUE;
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = PEEKED_FALSE;
        } else if (c != 'n' && c != 'N') {
            return 0;
        } else {
            keyword = "null";
            keywordUpper = "NULL";
            peeking = PEEKED_NULL;
        }
        int length = keyword.length();
        int i = PEEKED_BEGIN_OBJECT;
        while (i < length) {
            if (this.pos + i >= this.limit && !fillBuffer(i + 1)) {
                return 0;
            }
            c = this.buffer[this.pos + i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                return 0;
            }
            i++;
        }
        if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        this.peeked = peeking;
        return peeking;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int peekNumber() throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.google.gson.stream.JsonReader.peekNumber():int");
        /*
        r17 = this;
        r0 = r17;
        r1 = r0.buffer;
        r0 = r17;
        r10 = r0.pos;
        r0 = r17;
        r5 = r0.limit;
        r11 = 0;
        r7 = 0;
        r3 = 1;
        r6 = 0;
        r4 = 0;
    L_0x0012:
        r13 = r10 + r4;
        if (r13 != r5) goto L_0x0050;
    L_0x0016:
        r13 = r1.length;
        if (r4 != r13) goto L_0x001b;
    L_0x0019:
        r13 = 0;
    L_0x001a:
        return r13;
    L_0x001b:
        r13 = r4 + 1;
        r0 = r17;
        r13 = r0.fillBuffer(r13);
        if (r13 != 0) goto L_0x0048;
    L_0x0025:
        r13 = 2;
        if (r6 != r13) goto L_0x00da;
    L_0x0028:
        if (r3 == 0) goto L_0x00da;
    L_0x002a:
        r13 = -9223372036854775808;
        r13 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r13 != 0) goto L_0x0032;
    L_0x0030:
        if (r7 == 0) goto L_0x00da;
    L_0x0032:
        if (r7 == 0) goto L_0x00d7;
    L_0x0034:
        r0 = r17;
        r0.peekedLong = r11;
        r0 = r17;
        r13 = r0.pos;
        r13 = r13 + r4;
        r0 = r17;
        r0.pos = r13;
        r13 = 15;
        r0 = r17;
        r0.peeked = r13;
        goto L_0x001a;
    L_0x0048:
        r0 = r17;
        r10 = r0.pos;
        r0 = r17;
        r5 = r0.limit;
    L_0x0050:
        r13 = r10 + r4;
        r2 = r1[r13];
        switch(r2) {
            case 43: goto L_0x0077;
            case 45: goto L_0x0069;
            case 46: goto L_0x0088;
            case 69: goto L_0x007e;
            case 101: goto L_0x007e;
            default: goto L_0x0057;
        };
    L_0x0057:
        r13 = 48;
        if (r2 < r13) goto L_0x005f;
    L_0x005b:
        r13 = 57;
        if (r2 <= r13) goto L_0x008f;
    L_0x005f:
        r0 = r17;
        r13 = r0.isLiteral(r2);
        if (r13 == 0) goto L_0x0025;
    L_0x0067:
        r13 = 0;
        goto L_0x001a;
    L_0x0069:
        if (r6 != 0) goto L_0x0070;
    L_0x006b:
        r7 = 1;
        r6 = 1;
    L_0x006d:
        r4 = r4 + 1;
        goto L_0x0012;
    L_0x0070:
        r13 = 5;
        if (r6 != r13) goto L_0x0075;
    L_0x0073:
        r6 = 6;
        goto L_0x006d;
    L_0x0075:
        r13 = 0;
        goto L_0x001a;
    L_0x0077:
        r13 = 5;
        if (r6 != r13) goto L_0x007c;
    L_0x007a:
        r6 = 6;
        goto L_0x006d;
    L_0x007c:
        r13 = 0;
        goto L_0x001a;
    L_0x007e:
        r13 = 2;
        if (r6 == r13) goto L_0x0084;
    L_0x0081:
        r13 = 4;
        if (r6 != r13) goto L_0x0086;
    L_0x0084:
        r6 = 5;
        goto L_0x006d;
    L_0x0086:
        r13 = 0;
        goto L_0x001a;
    L_0x0088:
        r13 = 2;
        if (r6 != r13) goto L_0x008d;
    L_0x008b:
        r6 = 3;
        goto L_0x006d;
    L_0x008d:
        r13 = 0;
        goto L_0x001a;
    L_0x008f:
        r13 = 1;
        if (r6 == r13) goto L_0x0094;
    L_0x0092:
        if (r6 != 0) goto L_0x009a;
    L_0x0094:
        r13 = r2 + -48;
        r13 = -r13;
        r11 = (long) r13;
        r6 = 2;
        goto L_0x006d;
    L_0x009a:
        r13 = 2;
        if (r6 != r13) goto L_0x00ca;
    L_0x009d:
        r13 = 0;
        r13 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r13 != 0) goto L_0x00a6;
    L_0x00a3:
        r13 = 0;
        goto L_0x001a;
    L_0x00a6:
        r13 = 10;
        r13 = r13 * r11;
        r15 = r2 + -48;
        r15 = (long) r15;
        r8 = r13 - r15;
        r13 = -922337203685477580; // 0xf333333333333334 float:4.1723254E-8 double:-8.390303882365713E246;
        r13 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r13 > 0) goto L_0x00c4;
    L_0x00b7:
        r13 = -922337203685477580; // 0xf333333333333334 float:4.1723254E-8 double:-8.390303882365713E246;
        r13 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r13 != 0) goto L_0x00c8;
    L_0x00c0:
        r13 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1));
        if (r13 >= 0) goto L_0x00c8;
    L_0x00c4:
        r13 = 1;
    L_0x00c5:
        r3 = r3 & r13;
        r11 = r8;
        goto L_0x006d;
    L_0x00c8:
        r13 = 0;
        goto L_0x00c5;
    L_0x00ca:
        r13 = 3;
        if (r6 != r13) goto L_0x00cf;
    L_0x00cd:
        r6 = 4;
        goto L_0x006d;
    L_0x00cf:
        r13 = 5;
        if (r6 == r13) goto L_0x00d5;
    L_0x00d2:
        r13 = 6;
        if (r6 != r13) goto L_0x006d;
    L_0x00d5:
        r6 = 7;
        goto L_0x006d;
    L_0x00d7:
        r11 = -r11;
        goto L_0x0034;
    L_0x00da:
        r13 = 2;
        if (r6 == r13) goto L_0x00e3;
    L_0x00dd:
        r13 = 4;
        if (r6 == r13) goto L_0x00e3;
    L_0x00e0:
        r13 = 7;
        if (r6 != r13) goto L_0x00ef;
    L_0x00e3:
        r0 = r17;
        r0.peekedNumberLength = r4;
        r13 = 16;
        r0 = r17;
        r0.peeked = r13;
        goto L_0x001a;
    L_0x00ef:
        r13 = 0;
        goto L_0x001a;
        */
    }

    private void push(int newTop) {
        if (this.stackSize == this.stack.length) {
            int[] newStack = new int[(this.stackSize * 2)];
            System.arraycopy(this.stack, PEEKED_NONE, newStack, PEEKED_NONE, this.stackSize);
            this.stack = newStack;
        }
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + 1;
        iArr[i] = newTop;
    }

    private char readEscapeCharacter() throws IOException {
        if (this.pos != this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
            char[] cArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            char escaped = cArr[i];
            switch (escaped) {
                case PEEKED_UNQUOTED:
                    this.lineNumber++;
                    this.lineStart = this.pos;
                    return escaped;
                case 'b':
                    return '\b';
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                    return '\f';
                case 'n':
                    return '\n';
                case 'r':
                    return '\r';
                case 't':
                    return '\t';
                case 'u':
                    if (this.pos + 4 <= this.limit || fillBuffer(PEEKED_END_ARRAY)) {
                        char result = '\u0000';
                        int i2 = this.pos;
                        int end = i2 + 4;
                        while (i2 < end) {
                            char c = this.buffer[i2];
                            result = (char) (result << 4);
                            if (c >= '0' && c <= '9') {
                                result = (char) (c - 48 + result);
                            } else if (c < 'a' || c > 'f') {
                                if (c >= 'A' && c <= 'F') {
                                    result = (char) (c - 65 + 10 + result);
                                }
                                throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                            } else {
                                result = (char) (c - 97 + 10 + result);
                            }
                            i2++;
                        }
                        this.pos += 4;
                        return result;
                    } else {
                        throw syntaxError("Unterminated escape sequence");
                    }
                default:
                    return escaped;
            }
        } else {
            throw syntaxError("Unterminated escape sequence");
        }
    }

    private void skipQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        do {
            int p = this.pos;
            int l = this.limit;
            int p2 = p;
            while (p2 < l) {
                p = p2 + 1;
                char c = buffer[p2];
                if (c == quote) {
                    this.pos = p;
                    return;
                } else {
                    if (c == '\\') {
                        this.pos = p;
                        readEscapeCharacter();
                        p = this.pos;
                        l = this.limit;
                    } else if (c == '\n') {
                        this.lineNumber++;
                        this.lineStart = p;
                    }
                    p2 = p;
                }
            }
            this.pos = p2;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private boolean skipTo(String toFind) throws IOException {
        while (true) {
            if (this.pos + toFind.length() > this.limit && !fillBuffer(toFind.length())) {
                return false;
            }
            if (this.buffer[this.pos] == '\n') {
                this.lineNumber++;
                this.lineStart = this.pos + 1;
            } else {
                int c = PEEKED_NONE;
                while (c < toFind.length()) {
                    if (this.buffer[this.pos + c] == toFind.charAt(c)) {
                        c++;
                    }
                }
                return true;
            }
            this.pos++;
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = cArr[i];
                if (c == '\n') {
                    this.lineNumber++;
                    this.lineStart = this.pos;
                    return;
                }
            } else {
                return;
            }
        } while (c != '\r');
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int i = PEEKED_NONE;
            while (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED:
                    case PEEKED_UNQUOTED:
                    case PEEKED_SINGLE_QUOTED_NAME:
                    case PEEKED_DOUBLE_QUOTED_NAME:
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                    case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                        break;
                    case ApiEventType.API_MRAID_IS_AUDIO_MUTED:
                    case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i++;
                        break;
                }
                this.pos += i;
                return;
            }
            this.pos += i;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
    }

    private IOException syntaxError(String message) throws IOException {
        throw new MalformedJsonException(message + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    public void beginArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 3) {
            push(PEEKED_BEGIN_OBJECT);
            this.peeked = 0;
        } else {
            throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public void beginObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 1) {
            push(PEEKED_BEGIN_ARRAY);
            this.peeked = 0;
        } else {
            throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    public void endArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 4) {
            this.stackSize--;
            this.peeked = 0;
        } else {
            throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public void endObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 2) {
            this.stackSize--;
            this.peeked = 0;
        } else {
            throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public boolean hasNext() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        return (i == 2 || i == 4) ? false : true;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public boolean nextBoolean() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 5) {
            this.peeked = 0;
            return true;
        } else if (p == 6) {
            this.peeked = 0;
            return false;
        } else {
            throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public double nextDouble() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            return (double) this.peekedLong;
        } else {
            if (p == 16) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            } else if (p == 8 || p == 9) {
                this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
            } else if (p == 10) {
                this.peekedString = nextUnquotedValue();
            } else if (p != 11) {
                throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peeked = 11;
            double result = Double.parseDouble(this.peekedString);
            if (this.lenient || !(Double.isNaN(result) || Double.isInfinite(result))) {
                this.peekedString = null;
                this.peeked = 0;
                return result;
            } else {
                throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
        }
    }

    public int nextInt() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        int result;
        if (p == 15) {
            result = (int) this.peekedLong;
            if (this.peekedLong != ((long) result)) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peeked = 0;
            return result;
        } else {
            if (p == 16) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            } else if (p == 8 || p == 9) {
                this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
                try {
                    result = Integer.parseInt(this.peekedString);
                    this.peeked = 0;
                    return result;
                } catch (NumberFormatException e) {
                }
            } else {
                throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peeked = 11;
            double asDouble = Double.parseDouble(this.peekedString);
            result = (int) asDouble;
            if (((double) result) != asDouble) {
                throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peekedString = null;
            this.peeked = 0;
            return result;
        }
    }

    public long nextLong() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            return this.peekedLong;
        } else {
            long result;
            if (p == 16) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            } else if (p == 8 || p == 9) {
                this.peekedString = nextQuotedValue(p == 8 ? '\'' : '\"');
                try {
                    result = Long.parseLong(this.peekedString);
                    this.peeked = 0;
                    return result;
                } catch (NumberFormatException e) {
                }
            } else {
                throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peeked = 11;
            double asDouble = Double.parseDouble(this.peekedString);
            result = (long) asDouble;
            if (((double) result) != asDouble) {
                throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber());
            }
            this.peekedString = null;
            this.peeked = 0;
            return result;
        }
    }

    public String nextName() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 14) {
            result = nextUnquotedValue();
        } else if (p == 12) {
            result = nextQuotedValue('\'');
        } else if (p == 13) {
            result = nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = 0;
        return result;
    }

    public void nextNull() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 7) {
            this.peeked = 0;
        } else {
            throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
    }

    public String nextString() throws IOException {
        String result;
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        if (p == 10) {
            result = nextUnquotedValue();
        } else if (p == 8) {
            result = nextQuotedValue('\'');
        } else if (p == 9) {
            result = nextQuotedValue('\"');
        } else if (p == 11) {
            result = this.peekedString;
            this.peekedString = null;
        } else if (p == 15) {
            result = Long.toString(this.peekedLong);
        } else if (p == 16) {
            result = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
        }
        this.peeked = 0;
        return result;
    }

    public JsonToken peek() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = doPeek();
        }
        switch (p) {
            case PEEKED_BEGIN_OBJECT:
                return JsonToken.BEGIN_OBJECT;
            case PEEKED_END_OBJECT:
                return JsonToken.END_OBJECT;
            case PEEKED_BEGIN_ARRAY:
                return JsonToken.BEGIN_ARRAY;
            case PEEKED_END_ARRAY:
                return JsonToken.END_ARRAY;
            case PEEKED_TRUE:
            case PEEKED_FALSE:
                return JsonToken.BOOLEAN;
            case PEEKED_NULL:
                return JsonToken.NULL;
            case PEEKED_SINGLE_QUOTED:
            case PEEKED_DOUBLE_QUOTED:
            case PEEKED_UNQUOTED:
            case PEEKED_BUFFERED:
                return JsonToken.STRING;
            case PEEKED_SINGLE_QUOTED_NAME:
            case PEEKED_DOUBLE_QUOTED_NAME:
            case PEEKED_UNQUOTED_NAME:
                return JsonToken.NAME;
            case PEEKED_LONG:
            case PEEKED_NUMBER:
                return JsonToken.NUMBER;
            case PEEKED_EOF:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    public final void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public void skipValue() throws IOException {
        int count = PEEKED_NONE;
        do {
            int p = this.peeked;
            if (p == 0) {
                p = doPeek();
            }
            if (p == 3) {
                push(PEEKED_BEGIN_OBJECT);
                count++;
            } else if (p == 1) {
                push(PEEKED_BEGIN_ARRAY);
                count++;
            } else if (p == 4) {
                this.stackSize--;
                count--;
            } else if (p == 2) {
                this.stackSize--;
                count--;
            } else if (p == 14 || p == 10) {
                skipUnquotedValue();
            } else if (p == 8 || p == 12) {
                skipQuotedValue('\'');
            } else if (p == 9 || p == 13) {
                skipQuotedValue('\"');
            } else if (p == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (count != 0);
    }

    public String toString() {
        return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
    }
}