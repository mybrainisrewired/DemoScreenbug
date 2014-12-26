package org.codehaus.jackson;

import com.wmt.data.MediaItem;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.type.TypeReference;

public abstract class JsonParser implements Closeable, Versioned {
    private static final int MAX_BYTE_I = 127;
    private static final int MAX_SHORT_I = 32767;
    private static final int MIN_BYTE_I = -128;
    private static final int MIN_SHORT_I = -32768;
    protected JsonToken _currToken;
    protected int _features;
    protected JsonToken _lastClearedToken;

    public enum Feature {
        AUTO_CLOSE_SOURCE(true),
        ALLOW_COMMENTS(false),
        ALLOW_UNQUOTED_FIELD_NAMES(false),
        ALLOW_SINGLE_QUOTES(false),
        ALLOW_UNQUOTED_CONTROL_CHARS(false),
        ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false),
        ALLOW_NUMERIC_LEADING_ZEROS(false),
        ALLOW_NON_NUMERIC_NUMBERS(false),
        INTERN_FIELD_NAMES(true),
        CANONICALIZE_FIELD_NAMES(true);
        final boolean _defaultState;

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        public static int collectDefaults() {
            int flags = 0;
            org.codehaus.jackson.JsonParser.Feature[] arr$ = values();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                org.codehaus.jackson.JsonParser.Feature f = arr$[i$];
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

        public boolean enabledIn(int flags) {
            return (getMask() & flags) != 0;
        }

        public int getMask() {
            return 1 << ordinal();
        }
    }

    public enum NumberType {
        INT,
        LONG,
        BIG_INTEGER,
        FLOAT,
        DOUBLE,
        BIG_DECIMAL
    }

    protected JsonParser() {
    }

    protected JsonParser(int features) {
        this._features = features;
    }

    protected JsonParseException _constructError(String msg) {
        return new JsonParseException(msg, getCurrentLocation());
    }

    public boolean canUseSchema(FormatSchema schema) {
        return false;
    }

    public void clearCurrentToken() {
        if (this._currToken != null) {
            this._lastClearedToken = this._currToken;
            this._currToken = null;
        }
    }

    public abstract void close() throws IOException;

    public JsonParser configure(Feature f, boolean state) {
        if (state) {
            enableFeature(f);
        } else {
            disableFeature(f);
        }
        return this;
    }

    public JsonParser disable(Feature f) {
        this._features &= f.getMask() ^ -1;
        return this;
    }

    public void disableFeature(Feature f) {
        disable(f);
    }

    public JsonParser enable(Feature f) {
        this._features |= f.getMask();
        return this;
    }

    public void enableFeature(Feature f) {
        enable(f);
    }

    public abstract BigInteger getBigIntegerValue() throws IOException, JsonParseException;

    public byte[] getBinaryValue() throws IOException, JsonParseException {
        return getBinaryValue(Base64Variants.getDefaultVariant());
    }

    public abstract byte[] getBinaryValue(Base64Variant base64Variant) throws IOException, JsonParseException;

    public boolean getBooleanValue() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_TRUE) {
            return true;
        }
        if (this._currToken == JsonToken.VALUE_FALSE) {
            return false;
        }
        throw new JsonParseException("Current token (" + this._currToken + ") not of boolean type", getCurrentLocation());
    }

    public byte getByteValue() throws IOException, JsonParseException {
        int value = getIntValue();
        if (value >= -128 && value <= 127) {
            return (byte) value;
        }
        throw _constructError("Numeric value (" + getText() + ") out of range of Java byte");
    }

    public abstract ObjectCodec getCodec();

    public abstract JsonLocation getCurrentLocation();

    public abstract String getCurrentName() throws IOException, JsonParseException;

    public JsonToken getCurrentToken() {
        return this._currToken;
    }

    public abstract BigDecimal getDecimalValue() throws IOException, JsonParseException;

    public abstract double getDoubleValue() throws IOException, JsonParseException;

    public Object getEmbeddedObject() throws IOException, JsonParseException {
        return null;
    }

    public abstract float getFloatValue() throws IOException, JsonParseException;

    public Object getInputSource() {
        return null;
    }

    public abstract int getIntValue() throws IOException, JsonParseException;

    public JsonToken getLastClearedToken() {
        return this._lastClearedToken;
    }

    public abstract long getLongValue() throws IOException, JsonParseException;

    public abstract NumberType getNumberType() throws IOException, JsonParseException;

    public abstract Number getNumberValue() throws IOException, JsonParseException;

    public abstract JsonStreamContext getParsingContext();

    public short getShortValue() throws IOException, JsonParseException {
        int value = getIntValue();
        if (value >= -32768 && value <= 32767) {
            return (short) value;
        }
        throw _constructError("Numeric value (" + getText() + ") out of range of Java short");
    }

    public abstract String getText() throws IOException, JsonParseException;

    public abstract char[] getTextCharacters() throws IOException, JsonParseException;

    public abstract int getTextLength() throws IOException, JsonParseException;

    public abstract int getTextOffset() throws IOException, JsonParseException;

    public abstract JsonLocation getTokenLocation();

    public boolean getValueAsBoolean() throws IOException, JsonParseException {
        return getValueAsBoolean(false);
    }

    public boolean getValueAsBoolean(boolean defaultValue) throws IOException, JsonParseException {
        return defaultValue;
    }

    public double getValueAsDouble() throws IOException, JsonParseException {
        return getValueAsDouble(MediaItem.INVALID_LATLNG);
    }

    public double getValueAsDouble(double defaultValue) throws IOException, JsonParseException {
        return defaultValue;
    }

    public int getValueAsInt() throws IOException, JsonParseException {
        return getValueAsInt(0);
    }

    public int getValueAsInt(int defaultValue) throws IOException, JsonParseException {
        return defaultValue;
    }

    public long getValueAsLong() throws IOException, JsonParseException {
        return (long) getValueAsInt(0);
    }

    public long getValueAsLong(long defaultValue) throws IOException, JsonParseException {
        return defaultValue;
    }

    public boolean hasCurrentToken() {
        return this._currToken != null;
    }

    public boolean hasTextCharacters() {
        return false;
    }

    public abstract boolean isClosed();

    public boolean isEnabled(Feature f) {
        return (this._features & f.getMask()) != 0;
    }

    public boolean isExpectedStartArrayToken() {
        return getCurrentToken() == JsonToken.START_ARRAY;
    }

    public final boolean isFeatureEnabled(Feature f) {
        return isEnabled(f);
    }

    public abstract JsonToken nextToken() throws IOException, JsonParseException;

    public JsonToken nextValue() throws IOException, JsonParseException {
        JsonToken t = nextToken();
        return t == JsonToken.FIELD_NAME ? nextToken() : t;
    }

    public <T> T readValueAs(Class valueType) throws IOException, JsonProcessingException {
        ObjectCodec codec = getCodec();
        if (codec != null) {
            return codec.readValue(this, valueType);
        }
        throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into Java objects");
    }

    public <T> T readValueAs(TypeReference valueTypeRef) throws IOException, JsonProcessingException {
        ObjectCodec codec = getCodec();
        if (codec != null) {
            return codec.readValue(this, valueTypeRef);
        }
        throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into Java objects");
    }

    public JsonNode readValueAsTree() throws IOException, JsonProcessingException {
        ObjectCodec codec = getCodec();
        if (codec != null) {
            return codec.readTree(this);
        }
        throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into JsonNode tree");
    }

    public int releaseBuffered(OutputStream out) throws IOException {
        return -1;
    }

    public int releaseBuffered(Writer w) throws IOException {
        return -1;
    }

    public abstract void setCodec(ObjectCodec objectCodec);

    public void setFeature(Feature f, boolean state) {
        configure(f, state);
    }

    public void setSchema(FormatSchema schema) {
        throw new UnsupportedOperationException("Parser of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
    }

    public abstract JsonParser skipChildren() throws IOException, JsonParseException;

    public Version version() {
        return Version.unknownVersion();
    }
}