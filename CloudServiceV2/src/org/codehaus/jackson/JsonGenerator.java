package org.codehaus.jackson;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.SerializedString;

public abstract class JsonGenerator implements Closeable, Versioned {
    protected PrettyPrinter _cfgPrettyPrinter;

    public enum Feature {
        AUTO_CLOSE_TARGET(true),
        AUTO_CLOSE_JSON_CONTENT(true),
        QUOTE_FIELD_NAMES(true),
        QUOTE_NON_NUMERIC_NUMBERS(true),
        WRITE_NUMBERS_AS_STRINGS(false),
        FLUSH_PASSED_TO_STREAM(true),
        ESCAPE_NON_ASCII(false);
        final boolean _defaultState;
        final int _mask;

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
            this._mask = 1 << ordinal();
        }

        public static int collectDefaults() {
            int flags = 0;
            org.codehaus.jackson.JsonGenerator.Feature[] arr$ = values();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                org.codehaus.jackson.JsonGenerator.Feature f = arr$[i$];
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

    protected JsonGenerator() {
    }

    public boolean canUseSchema(FormatSchema schema) {
        return false;
    }

    public abstract void close() throws IOException;

    public JsonGenerator configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public abstract void copyCurrentEvent(JsonParser jsonParser) throws IOException, JsonProcessingException;

    public abstract void copyCurrentStructure(JsonParser jsonParser) throws IOException, JsonProcessingException;

    public abstract JsonGenerator disable(Feature feature);

    @Deprecated
    public void disableFeature(Feature f) {
        disable(f);
    }

    public abstract JsonGenerator enable(Feature feature);

    @Deprecated
    public void enableFeature(Feature f) {
        enable(f);
    }

    public abstract void flush() throws IOException;

    public CharacterEscapes getCharacterEscapes() {
        return null;
    }

    public abstract ObjectCodec getCodec();

    public int getHighestEscapedChar() {
        return 0;
    }

    public abstract JsonStreamContext getOutputContext();

    public Object getOutputTarget() {
        return null;
    }

    public abstract boolean isClosed();

    public abstract boolean isEnabled(Feature feature);

    @Deprecated
    public boolean isFeatureEnabled(Feature f) {
        return isEnabled(f);
    }

    public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
        return this;
    }

    public abstract JsonGenerator setCodec(ObjectCodec objectCodec);

    @Deprecated
    public void setFeature(Feature f, boolean state) {
        configure(f, state);
    }

    public JsonGenerator setHighestNonEscapedChar(int charCode) {
        return this;
    }

    public JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
        this._cfgPrettyPrinter = pp;
        return this;
    }

    public void setSchema(FormatSchema schema) {
        throw new UnsupportedOperationException("Generator of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
    }

    public abstract JsonGenerator useDefaultPrettyPrinter();

    public Version version() {
        return Version.unknownVersion();
    }

    public final void writeArrayFieldStart(String fieldName) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeStartArray();
    }

    public abstract void writeBinary(Base64Variant base64Variant, byte[] bArr, int i, int i2) throws IOException, JsonGenerationException;

    public void writeBinary(byte[] data) throws IOException, JsonGenerationException {
        writeBinary(Base64Variants.getDefaultVariant(), data, 0, data.length);
    }

    public void writeBinary(byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        writeBinary(Base64Variants.getDefaultVariant(), data, offset, len);
    }

    public final void writeBinaryField(String fieldName, byte[] data) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeBinary(data);
    }

    public abstract void writeBoolean(boolean z) throws IOException, JsonGenerationException;

    public final void writeBooleanField(String fieldName, boolean value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeBoolean(value);
    }

    public abstract void writeEndArray() throws IOException, JsonGenerationException;

    public abstract void writeEndObject() throws IOException, JsonGenerationException;

    public abstract void writeFieldName(String str) throws IOException, JsonGenerationException;

    public void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        writeFieldName(name.getValue());
    }

    public void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        writeFieldName(name.getValue());
    }

    public abstract void writeNull() throws IOException, JsonGenerationException;

    public final void writeNullField(String fieldName) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNull();
    }

    public abstract void writeNumber(double d) throws IOException, JsonGenerationException;

    public abstract void writeNumber(float f) throws IOException, JsonGenerationException;

    public abstract void writeNumber(int i) throws IOException, JsonGenerationException;

    public abstract void writeNumber(long j) throws IOException, JsonGenerationException;

    public abstract void writeNumber(String str) throws IOException, JsonGenerationException, UnsupportedOperationException;

    public abstract void writeNumber(BigDecimal bigDecimal) throws IOException, JsonGenerationException;

    public abstract void writeNumber(BigInteger bigInteger) throws IOException, JsonGenerationException;

    public final void writeNumberField(String fieldName, double value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, float value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, int value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, long value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public final void writeNumberField(String fieldName, BigDecimal value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    public abstract void writeObject(Object obj) throws IOException, JsonProcessingException;

    public final void writeObjectField(String fieldName, Object pojo) throws IOException, JsonProcessingException {
        writeFieldName(fieldName);
        writeObject(pojo);
    }

    public final void writeObjectFieldStart(String fieldName) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeStartObject();
    }

    public abstract void writeRaw(char c) throws IOException, JsonGenerationException;

    public abstract void writeRaw(String str) throws IOException, JsonGenerationException;

    public abstract void writeRaw(String str, int i, int i2) throws IOException, JsonGenerationException;

    public abstract void writeRaw(char[] cArr, int i, int i2) throws IOException, JsonGenerationException;

    public abstract void writeRawUTF8String(byte[] bArr, int i, int i2) throws IOException, JsonGenerationException;

    public abstract void writeRawValue(String str) throws IOException, JsonGenerationException;

    public abstract void writeRawValue(String str, int i, int i2) throws IOException, JsonGenerationException;

    public abstract void writeRawValue(char[] cArr, int i, int i2) throws IOException, JsonGenerationException;

    public abstract void writeStartArray() throws IOException, JsonGenerationException;

    public abstract void writeStartObject() throws IOException, JsonGenerationException;

    public abstract void writeString(String str) throws IOException, JsonGenerationException;

    public void writeString(SerializableString text) throws IOException, JsonGenerationException {
        writeString(text.getValue());
    }

    public abstract void writeString(char[] cArr, int i, int i2) throws IOException, JsonGenerationException;

    public void writeStringField(String fieldName, String value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeString(value);
    }

    public abstract void writeTree(JsonNode jsonNode) throws IOException, JsonProcessingException;

    public abstract void writeUTF8String(byte[] bArr, int i, int i2) throws IOException, JsonGenerationException;
}