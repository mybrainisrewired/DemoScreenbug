package org.codehaus.jackson.util;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.impl.JsonParserMinimalBase;
import org.codehaus.jackson.impl.JsonReadContext;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class TokenBuffer extends JsonGenerator {
    protected static final int DEFAULT_PARSER_FEATURES;
    protected int _appendOffset;
    protected boolean _closed;
    protected Segment _first;
    protected int _generatorFeatures;
    protected Segment _last;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_INTEGER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_DECIMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.LONG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 7;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 9;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 10;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 11;
            } catch (NoSuchFieldError e16) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 12;
        }
    }

    protected static final class Segment {
        public static final int TOKENS_PER_SEGMENT = 16;
        private static final JsonToken[] TOKEN_TYPES_BY_INDEX;
        protected Segment _next;
        protected long _tokenTypes;
        protected final Object[] _tokens;

        static {
            TOKEN_TYPES_BY_INDEX = new JsonToken[16];
            JsonToken[] t = JsonToken.values();
            System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(Opcodes.DCONST_1, t.length - 1));
        }

        public Segment() {
            this._tokens = new Object[16];
        }

        public Segment append(int index, JsonToken tokenType) {
            if (index < 16) {
                set(index, tokenType);
                return null;
            } else {
                this._next = new Segment();
                this._next.set(0, tokenType);
                return this._next;
            }
        }

        public Segment append(int index, JsonToken tokenType, Object value) {
            if (index < 16) {
                set(index, tokenType, value);
                return null;
            } else {
                this._next = new Segment();
                this._next.set(0, tokenType, value);
                return this._next;
            }
        }

        public Object get(int index) {
            return this._tokens[index];
        }

        public Segment next() {
            return this._next;
        }

        public void set(int index, JsonToken tokenType) {
            long typeCode = (long) tokenType.ordinal();
            if (index > 0) {
                typeCode <<= index << 2;
            }
            this._tokenTypes |= typeCode;
        }

        public void set(int index, JsonToken tokenType, Object value) {
            this._tokens[index] = value;
            long typeCode = (long) tokenType.ordinal();
            if (index > 0) {
                typeCode <<= index << 2;
            }
            this._tokenTypes |= typeCode;
        }

        public JsonToken type(int index) {
            long l = this._tokenTypes;
            if (index > 0) {
                l >>= index << 2;
            }
            return TOKEN_TYPES_BY_INDEX[((int) l) & 15];
        }
    }

    protected static final class Parser extends JsonParserMinimalBase {
        protected transient ByteArrayBuilder _byteBuilder;
        protected boolean _closed;
        protected ObjectCodec _codec;
        protected JsonLocation _location;
        protected JsonReadContext _parsingContext;
        protected Segment _segment;
        protected int _segmentPtr;

        public Parser(Segment firstSeg, ObjectCodec codec) {
            super(0);
            this._location = null;
            this._segment = firstSeg;
            this._segmentPtr = -1;
            this._codec = codec;
            this._parsingContext = JsonReadContext.createRootContext(-1, -1);
        }

        protected final void _checkIsNumber() throws JsonParseException {
            if (this._currToken == null || !this._currToken.isNumeric()) {
                throw _constructError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
            }
        }

        protected final Object _currentObject() {
            return this._segment.get(this._segmentPtr);
        }

        protected void _decodeBase64(String str, ByteArrayBuilder builder, Base64Variant b64variant) throws IOException, JsonParseException {
            int ptr = 0;
            int len = str.length();
            while (ptr < len) {
                while (true) {
                    int ptr2 = ptr + 1;
                    char ch = str.charAt(ptr);
                    if (ptr2 >= len) {
                        return;
                    } else if (ch > ' ') {
                        int bits = b64variant.decodeBase64Char(ch);
                        if (bits < 0) {
                            _reportInvalidBase64(b64variant, ch, 0, null);
                        }
                        int decodedData = bits;
                        if (ptr2 >= len) {
                            _reportBase64EOF();
                        }
                        ptr = ptr2 + 1;
                        ch = str.charAt(ptr2);
                        bits = b64variant.decodeBase64Char(ch);
                        if (bits < 0) {
                            _reportInvalidBase64(b64variant, ch, 1, null);
                        }
                        decodedData = (decodedData << 6) | bits;
                        if (ptr < len || b64variant.usesPadding()) {
                            _reportBase64EOF();
                        } else {
                            builder.append(decodedData >> 4);
                            return;
                        }
                        ptr2 = ptr + 1;
                        ch = str.charAt(ptr);
                        bits = b64variant.decodeBase64Char(ch);
                        if (bits < 0) {
                            if (bits != -2) {
                                _reportInvalidBase64(b64variant, ch, ClassWriter.COMPUTE_FRAMES, null);
                            }
                            if (ptr2 >= len) {
                                _reportBase64EOF();
                            }
                            ptr = ptr2 + 1;
                            ch = str.charAt(ptr2);
                            if (!b64variant.usesPaddingChar(ch)) {
                                _reportInvalidBase64(b64variant, ch, JsonWriteContext.STATUS_OK_AFTER_SPACE, "expected padding character '" + b64variant.getPaddingChar() + "'");
                            }
                            builder.append(decodedData >> 4);
                        } else {
                            decodedData = (decodedData << 6) | bits;
                            if (ptr2 < len || b64variant.usesPadding()) {
                                _reportBase64EOF();
                            } else {
                                builder.appendTwoBytes(decodedData >> 2);
                                return;
                            }
                            ptr = ptr2 + 1;
                            ch = str.charAt(ptr2);
                            bits = b64variant.decodeBase64Char(ch);
                            if (bits < 0) {
                                if (bits != -2) {
                                    _reportInvalidBase64(b64variant, ch, JsonWriteContext.STATUS_OK_AFTER_SPACE, null);
                                }
                                builder.appendTwoBytes(decodedData >> 2);
                            } else {
                                builder.appendThreeBytes((decodedData << 6) | bits);
                            }
                        }
                    } else {
                        ptr = ptr2;
                    }
                }
            }
        }

        protected void _handleEOF() throws JsonParseException {
            _throwInternal();
        }

        protected void _reportBase64EOF() throws JsonParseException {
            throw _constructError("Unexpected end-of-String in base64 content");
        }

        protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg) throws JsonParseException {
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
            throw _constructError(base);
        }

        public void close() throws IOException {
            if (!this._closed) {
                this._closed = true;
            }
        }

        public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof BigInteger) {
                return (BigInteger) n;
            }
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()]) {
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    return ((BigDecimal) n).toBigInteger();
                default:
                    return BigInteger.valueOf(n.longValue());
            }
        }

        public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
            if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = _currentObject();
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (this._currToken != JsonToken.VALUE_STRING) {
                throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), can not access as binary");
            }
            String str = getText();
            if (str == null) {
                return null;
            }
            ByteArrayBuilder builder = this._byteBuilder;
            if (builder == null) {
                builder = new ByteArrayBuilder(100);
                this._byteBuilder = builder;
            }
            _decodeBase64(str, builder, b64variant);
            return builder.toByteArray();
        }

        public ObjectCodec getCodec() {
            return this._codec;
        }

        public JsonLocation getCurrentLocation() {
            return this._location == null ? JsonLocation.NA : this._location;
        }

        public String getCurrentName() {
            return this._parsingContext.getCurrentName();
        }

        public BigDecimal getDecimalValue() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof BigDecimal) {
                return (BigDecimal) n;
            }
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    return BigDecimal.valueOf(n.longValue());
                case ClassWriter.COMPUTE_FRAMES:
                    return new BigDecimal((BigInteger) n);
                default:
                    return BigDecimal.valueOf(n.doubleValue());
            }
        }

        public double getDoubleValue() throws IOException, JsonParseException {
            return getNumberValue().doubleValue();
        }

        public Object getEmbeddedObject() {
            return this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT ? _currentObject() : null;
        }

        public float getFloatValue() throws IOException, JsonParseException {
            return getNumberValue().floatValue();
        }

        public int getIntValue() throws IOException, JsonParseException {
            return this._currToken == JsonToken.VALUE_NUMBER_INT ? ((Number) _currentObject()).intValue() : getNumberValue().intValue();
        }

        public long getLongValue() throws IOException, JsonParseException {
            return getNumberValue().longValue();
        }

        public NumberType getNumberType() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof Integer) {
                return NumberType.INT;
            }
            if (n instanceof Long) {
                return NumberType.LONG;
            }
            if (n instanceof Double) {
                return NumberType.DOUBLE;
            }
            if (n instanceof BigDecimal) {
                return NumberType.BIG_DECIMAL;
            }
            if (n instanceof Float) {
                return NumberType.FLOAT;
            }
            return n instanceof BigInteger ? NumberType.BIG_INTEGER : null;
        }

        public final Number getNumberValue() throws IOException, JsonParseException {
            _checkIsNumber();
            return (Number) _currentObject();
        }

        public JsonStreamContext getParsingContext() {
            return this._parsingContext;
        }

        public String getText() {
            Object ob;
            if (this._currToken == JsonToken.VALUE_STRING || this._currToken == JsonToken.FIELD_NAME) {
                ob = _currentObject();
                if (ob instanceof String) {
                    return (String) ob;
                }
                return ob != null ? ob.toString() : null;
            } else if (this._currToken == null) {
                return null;
            } else {
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
                    case Type.LONG:
                    case Type.DOUBLE:
                        ob = _currentObject();
                        return ob != null ? ob.toString() : null;
                    default:
                        return this._currToken.asString();
                }
            }
        }

        public char[] getTextCharacters() {
            String str = getText();
            return str == null ? null : str.toCharArray();
        }

        public int getTextLength() {
            String str = getText();
            return str == null ? 0 : str.length();
        }

        public int getTextOffset() {
            return 0;
        }

        public JsonLocation getTokenLocation() {
            return getCurrentLocation();
        }

        public boolean hasTextCharacters() {
            return false;
        }

        public boolean isClosed() {
            return this._closed;
        }

        public JsonToken nextToken() throws IOException, JsonParseException {
            if (this._closed || this._segment == null) {
                return null;
            }
            int i = this._segmentPtr + 1;
            this._segmentPtr = i;
            if (i >= 16) {
                this._segmentPtr = 0;
                this._segment = this._segment.next();
                if (this._segment == null) {
                    return null;
                }
            }
            this._currToken = this._segment.type(this._segmentPtr);
            if (this._currToken == JsonToken.FIELD_NAME) {
                Object ob = _currentObject();
                this._parsingContext.setCurrentName(ob instanceof String ? (String) ob : ob.toString());
            } else if (this._currToken == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
            } else if (this._currToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
            } else if (this._currToken == JsonToken.END_OBJECT || this._currToken == JsonToken.END_ARRAY) {
                this._parsingContext = this._parsingContext.getParent();
                if (this._parsingContext == null) {
                    this._parsingContext = JsonReadContext.createRootContext(-1, -1);
                }
            }
            return this._currToken;
        }

        public JsonToken peekNextToken() throws IOException, JsonParseException {
            if (this._closed) {
                return null;
            }
            Segment seg = this._segment;
            int ptr = this._segmentPtr + 1;
            if (ptr >= 16) {
                ptr = 0;
                seg = seg == null ? null : seg.next();
            }
            return seg != null ? seg.type(ptr) : null;
        }

        public void setCodec(ObjectCodec c) {
            this._codec = c;
        }

        public void setLocation(JsonLocation l) {
            this._location = l;
        }
    }

    static {
        DEFAULT_PARSER_FEATURES = Feature.collectDefaults();
    }

    public TokenBuffer(ObjectCodec codec) {
        this._objectCodec = codec;
        this._generatorFeatures = DEFAULT_PARSER_FEATURES;
        this._writeContext = JsonWriteContext.createRootContext();
        Segment segment = new Segment();
        this._last = segment;
        this._first = segment;
        this._appendOffset = 0;
    }

    protected final void _append(JsonToken type) {
        Segment next = this._last.append(this._appendOffset, type);
        if (next == null) {
            this._appendOffset++;
        } else {
            this._last = next;
            this._appendOffset = 1;
        }
    }

    protected final void _append(JsonToken type, Object value) {
        Segment next = this._last.append(this._appendOffset, type, value);
        if (next == null) {
            this._appendOffset++;
        } else {
            this._last = next;
            this._appendOffset = 1;
        }
    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
    }

    public JsonParser asParser() {
        return asParser(this._objectCodec);
    }

    public JsonParser asParser(JsonParser src) {
        Parser p = new Parser(this._first, src.getCodec());
        p.setLocation(src.getTokenLocation());
        return p;
    }

    public JsonParser asParser(ObjectCodec codec) {
        return new Parser(this._first, codec);
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException {
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeStartObject();
            case ClassWriter.COMPUTE_FRAMES:
                writeEndObject();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeStartArray();
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                writeEndArray();
            case JsonWriteContext.STATUS_EXPECT_NAME:
                writeFieldName(jp.getCurrentName());
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                if (jp.hasTextCharacters()) {
                    writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
                } else {
                    writeString(jp.getText());
                }
            case Type.LONG:
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        writeNumber(jp.getIntValue());
                    case ClassWriter.COMPUTE_FRAMES:
                        writeNumber(jp.getBigIntegerValue());
                    default:
                        writeNumber(jp.getLongValue());
                }
            case Type.DOUBLE:
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        writeNumber(jp.getDecimalValue());
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        writeNumber(jp.getFloatValue());
                    default:
                        writeNumber(jp.getDoubleValue());
                }
            case Type.ARRAY:
                writeBoolean(true);
            case Type.OBJECT:
                writeBoolean(false);
            case Opcodes.T_LONG:
                writeNull();
            case Opcodes.FCONST_1:
                writeObject(jp.getEmbeddedObject());
            default:
                throw new RuntimeException("Internal error: should never end up through this code path");
        }
    }

    public void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.FIELD_NAME) {
            writeFieldName(jp.getCurrentName());
            t = jp.nextToken();
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeStartObject();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    copyCurrentStructure(jp);
                }
                writeEndObject();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeStartArray();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    copyCurrentStructure(jp);
                }
                writeEndArray();
            default:
                copyCurrentEvent(jp);
        }
    }

    public JsonGenerator disable(JsonGenerator.Feature f) {
        this._generatorFeatures &= f.getMask() ^ -1;
        return this;
    }

    public JsonGenerator enable(JsonGenerator.Feature f) {
        this._generatorFeatures |= f.getMask();
        return this;
    }

    public void flush() throws IOException {
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    public boolean isClosed() {
        return this._closed;
    }

    public boolean isEnabled(JsonGenerator.Feature f) {
        return (this._generatorFeatures & f.getMask()) != 0;
    }

    public void serialize(JsonGenerator jgen) throws IOException, JsonGenerationException {
        Segment segment = this._first;
        int ptr = -1;
        while (true) {
            ptr++;
            if (ptr >= 16) {
                ptr = 0;
                segment = segment.next();
                if (segment == null) {
                    return;
                }
            }
            JsonToken t = segment.type(ptr);
            if (t != null) {
                Object ob;
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        jgen.writeStartObject();
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                        jgen.writeEndObject();
                        break;
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        jgen.writeStartArray();
                        break;
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        jgen.writeEndArray();
                        break;
                    case JsonWriteContext.STATUS_EXPECT_NAME:
                        ob = segment.get(ptr);
                        if (ob instanceof SerializableString) {
                            jgen.writeFieldName((SerializableString) ob);
                        } else {
                            jgen.writeFieldName((String) ob);
                        }
                        break;
                    case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        ob = segment.get(ptr);
                        if (ob instanceof SerializableString) {
                            jgen.writeString((SerializableString) ob);
                        } else {
                            jgen.writeString((String) ob);
                        }
                        break;
                    case Type.LONG:
                        Number n = (Number) segment.get(ptr);
                        if (n instanceof BigInteger) {
                            jgen.writeNumber((BigInteger) n);
                        } else if (n instanceof Long) {
                            jgen.writeNumber(n.longValue());
                        } else {
                            jgen.writeNumber(n.intValue());
                        }
                        break;
                    case Type.DOUBLE:
                        Object n2 = segment.get(ptr);
                        if (n2 instanceof BigDecimal) {
                            jgen.writeNumber((BigDecimal) n2);
                        } else if (n2 instanceof Float) {
                            jgen.writeNumber(((Float) n2).floatValue());
                        } else if (n2 instanceof Double) {
                            jgen.writeNumber(((Double) n2).doubleValue());
                        } else if (n2 == null) {
                            jgen.writeNull();
                        } else if (n2 instanceof String) {
                            jgen.writeNumber((String) n2);
                        } else {
                            throw new JsonGenerationException("Unrecognized value type for VALUE_NUMBER_FLOAT: " + n2.getClass().getName() + ", can not serialize");
                        }
                    case Type.ARRAY:
                        jgen.writeBoolean(true);
                        break;
                    case Type.OBJECT:
                        jgen.writeBoolean(false);
                        break;
                    case Opcodes.T_LONG:
                        jgen.writeNull();
                        break;
                    case Opcodes.FCONST_1:
                        jgen.writeObject(segment.get(ptr));
                        break;
                    default:
                        throw new RuntimeException("Internal error: should never end up through this code path");
                }
            } else {
                return;
            }
        }
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[TokenBuffer: ");
        JsonParser jp = asParser();
        int count = 0;
        while (true) {
            try {
                JsonToken t = jp.nextToken();
                if (t == null) {
                    if (count >= 100) {
                        sb.append(" ... (truncated ").append(count - 100).append(" entries)");
                    }
                    sb.append(']');
                    return sb.toString();
                } else {
                    if (count < 100) {
                        if (count > 0) {
                            sb.append(", ");
                        }
                        sb.append(t.toString());
                    }
                    count++;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return this;
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        byte[] copy = new byte[len];
        System.arraycopy(data, offset, copy, 0, len);
        writeObject(copy);
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _append(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
    }

    public final void writeEndArray() throws IOException, JsonGenerationException {
        _append(JsonToken.END_ARRAY);
        JsonWriteContext c = this._writeContext.getParent();
        if (c != null) {
            this._writeContext = c;
        }
    }

    public final void writeEndObject() throws IOException, JsonGenerationException {
        _append(JsonToken.END_OBJECT);
        JsonWriteContext c = this._writeContext.getParent();
        if (c != null) {
            this._writeContext = c;
        }
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name);
    }

    public void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name.getValue());
    }

    public void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name.getValue());
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NULL);
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
    }

    public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException {
        if (dec == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_NUMBER_FLOAT, dec);
        }
    }

    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        if (v == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_NUMBER_INT, v);
        }
    }

    public void writeObject(Object value) throws IOException, JsonProcessingException {
        _append(JsonToken.VALUE_EMBEDDED_OBJECT, value);
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public final void writeStartArray() throws IOException, JsonGenerationException {
        _append(JsonToken.START_ARRAY);
        this._writeContext = this._writeContext.createChildArrayContext();
    }

    public final void writeStartObject() throws IOException, JsonGenerationException {
        _append(JsonToken.START_OBJECT);
        this._writeContext = this._writeContext.createChildObjectContext();
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        if (text == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_STRING, text);
        }
    }

    public void writeString(SerializableString text) throws IOException, JsonGenerationException {
        if (text == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_STRING, text);
        }
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        writeString(new String(text, offset, len));
    }

    public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException {
        _append(JsonToken.VALUE_EMBEDDED_OBJECT, rootNode);
    }

    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }
}