package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import com.wmt.data.MediaItem;
import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;

public abstract class JsonParserMinimalBase extends JsonParser {
    protected static final int INT_APOSTROPHE = 39;
    protected static final int INT_ASTERISK = 42;
    protected static final int INT_BACKSLASH = 92;
    protected static final int INT_COLON = 58;
    protected static final int INT_COMMA = 44;
    protected static final int INT_CR = 13;
    protected static final int INT_LBRACKET = 91;
    protected static final int INT_LCURLY = 123;
    protected static final int INT_LF = 10;
    protected static final int INT_QUOTE = 34;
    protected static final int INT_RBRACKET = 93;
    protected static final int INT_RCURLY = 125;
    protected static final int INT_SLASH = 47;
    protected static final int INT_SPACE = 32;
    protected static final int INT_TAB = 9;
    protected static final int INT_b = 98;
    protected static final int INT_f = 102;
    protected static final int INT_n = 110;
    protected static final int INT_r = 114;
    protected static final int INT_t = 116;
    protected static final int INT_u = 117;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 11;
        }
    }

    protected JsonParserMinimalBase() {
    }

    protected JsonParserMinimalBase(int features) {
        super(features);
    }

    protected static final String _getCharDesc(int ch) {
        char c = (char) ch;
        if (Character.isISOControl(c)) {
            return "(CTRL-CHAR, code " + ch + ")";
        }
        return ch > 255 ? "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")" : "'" + c + "' (code " + ch + ")";
    }

    protected final JsonParseException _constructError(String msg, Throwable t) {
        return new JsonParseException(msg, getCurrentLocation(), t);
    }

    protected abstract void _handleEOF() throws JsonParseException;

    protected char _handleUnrecognizedCharacterEscape(char ch) throws JsonProcessingException {
        if (!isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
            if (!(ch == '\'' && isEnabled(Feature.ALLOW_SINGLE_QUOTES))) {
                _reportError("Unrecognized character escape " + _getCharDesc(ch));
            }
        }
        return ch;
    }

    protected final void _reportError(String msg) throws JsonParseException {
        throw _constructError(msg);
    }

    protected void _reportInvalidEOF() throws JsonParseException {
        _reportInvalidEOF(" in " + this._currToken);
    }

    protected void _reportInvalidEOF(String msg) throws JsonParseException {
        _reportError("Unexpected end-of-input" + msg);
    }

    protected void _reportInvalidEOFInValue() throws JsonParseException {
        _reportInvalidEOF(" in a value");
    }

    protected void _reportUnexpectedChar(int ch, String comment) throws JsonParseException {
        String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
        if (comment != null) {
            msg = msg + ": " + comment;
        }
        _reportError(msg);
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected void _throwInvalidSpace(int i) throws JsonParseException {
        _reportError("Illegal character (" + _getCharDesc((char) i) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens");
    }

    protected void _throwUnquotedSpace(int i, String ctxtDesc) throws JsonParseException {
        if (!isEnabled(Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || i >= 32) {
            _reportError("Illegal unquoted character (" + _getCharDesc((char) i) + "): has to be escaped using backslash to be included in " + ctxtDesc);
        }
    }

    protected final void _wrapError(String msg, Throwable t) throws JsonParseException {
        throw _constructError(msg, t);
    }

    public abstract void close() throws IOException;

    public abstract byte[] getBinaryValue(Base64Variant base64Variant) throws IOException, JsonParseException;

    public abstract String getCurrentName() throws IOException, JsonParseException;

    public abstract JsonStreamContext getParsingContext();

    public abstract String getText() throws IOException, JsonParseException;

    public abstract char[] getTextCharacters() throws IOException, JsonParseException;

    public abstract int getTextLength() throws IOException, JsonParseException;

    public abstract int getTextOffset() throws IOException, JsonParseException;

    public boolean getValueAsBoolean(boolean defaultValue) throws IOException, JsonParseException {
        if (this._currToken != null) {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    return getIntValue() != 0;
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    return true;
                case Type.LONG:
                case Type.DOUBLE:
                    return false;
                case INT_TAB:
                    Object value = getEmbeddedObject();
                    if (value instanceof Boolean) {
                        return ((Boolean) value).booleanValue();
                    }
                    if ("true".equals(getText().trim())) {
                        return true;
                    }
                case INT_LF:
                    if ("true".equals(getText().trim())) {
                        return true;
                    }
            }
        }
        return defaultValue;
    }

    public double getValueAsDouble(double defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case Opcodes.T_LONG:
                return getDoubleValue();
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return 1.0d;
            case Type.LONG:
            case Type.DOUBLE:
                return MediaItem.INVALID_LATLNG;
            case INT_TAB:
                Object value = getEmbeddedObject();
                return value instanceof Number ? ((Number) value).doubleValue() : defaultValue;
            case INT_LF:
                return NumberInput.parseAsDouble(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    public int getValueAsInt(int defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case Opcodes.T_LONG:
                return getIntValue();
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return 1;
            case Type.LONG:
            case Type.DOUBLE:
                return 0;
            case INT_TAB:
                Object value = getEmbeddedObject();
                return value instanceof Number ? ((Number) value).intValue() : defaultValue;
            case INT_LF:
                return NumberInput.parseAsInt(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    public long getValueAsLong(long defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case Opcodes.T_LONG:
                return getLongValue();
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return 1;
            case Type.LONG:
            case Type.DOUBLE:
                return 0;
            case INT_TAB:
                Object value = getEmbeddedObject();
                return value instanceof Number ? ((Number) value).longValue() : defaultValue;
            case INT_LF:
                return NumberInput.parseAsLong(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    public abstract boolean hasTextCharacters();

    public abstract boolean isClosed();

    public abstract JsonToken nextToken() throws IOException, JsonParseException;

    public JsonParser skipChildren() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            int open = 1;
            while (true) {
                JsonToken t = nextToken();
                if (t == null) {
                    _handleEOF();
                } else {
                    switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
                        case LocalAudioAll.SORT_BY_DATE:
                        case ClassWriter.COMPUTE_FRAMES:
                            open++;
                            break;
                        case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        case JsonWriteContext.STATUS_EXPECT_VALUE:
                            open--;
                            if (open == 0) {
                            }
                        default:
                            break;
                    }
                }
            }
        }
        return this;
    }
}