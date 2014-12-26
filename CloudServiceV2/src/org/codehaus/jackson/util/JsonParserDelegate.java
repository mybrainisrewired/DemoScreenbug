package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;

public class JsonParserDelegate extends JsonParser {
    protected JsonParser delegate;

    public JsonParserDelegate(JsonParser d) {
        this.delegate = d;
    }

    public boolean canUseSchema(FormatSchema schema) {
        return this.delegate.canUseSchema(schema);
    }

    public void clearCurrentToken() {
        this.delegate.clearCurrentToken();
    }

    public void close() throws IOException {
        this.delegate.close();
    }

    public JsonParser disable(Feature f) {
        this.delegate.disable(f);
        return this;
    }

    public JsonParser enable(Feature f) {
        this.delegate.enable(f);
        return this;
    }

    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        return this.delegate.getBigIntegerValue();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        return this.delegate.getBinaryValue(b64variant);
    }

    public byte getByteValue() throws IOException, JsonParseException {
        return this.delegate.getByteValue();
    }

    public ObjectCodec getCodec() {
        return this.delegate.getCodec();
    }

    public JsonLocation getCurrentLocation() {
        return this.delegate.getCurrentLocation();
    }

    public String getCurrentName() throws IOException, JsonParseException {
        return this.delegate.getCurrentName();
    }

    public JsonToken getCurrentToken() {
        return this.delegate.getCurrentToken();
    }

    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        return this.delegate.getDecimalValue();
    }

    public double getDoubleValue() throws IOException, JsonParseException {
        return this.delegate.getDoubleValue();
    }

    public float getFloatValue() throws IOException, JsonParseException {
        return this.delegate.getFloatValue();
    }

    public Object getInputSource() {
        return this.delegate.getInputSource();
    }

    public int getIntValue() throws IOException, JsonParseException {
        return this.delegate.getIntValue();
    }

    public JsonToken getLastClearedToken() {
        return this.delegate.getLastClearedToken();
    }

    public long getLongValue() throws IOException, JsonParseException {
        return this.delegate.getLongValue();
    }

    public NumberType getNumberType() throws IOException, JsonParseException {
        return this.delegate.getNumberType();
    }

    public Number getNumberValue() throws IOException, JsonParseException {
        return this.delegate.getNumberValue();
    }

    public JsonStreamContext getParsingContext() {
        return this.delegate.getParsingContext();
    }

    public short getShortValue() throws IOException, JsonParseException {
        return this.delegate.getShortValue();
    }

    public String getText() throws IOException, JsonParseException {
        return this.delegate.getText();
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        return this.delegate.getTextCharacters();
    }

    public int getTextLength() throws IOException, JsonParseException {
        return this.delegate.getTextLength();
    }

    public int getTextOffset() throws IOException, JsonParseException {
        return this.delegate.getTextOffset();
    }

    public JsonLocation getTokenLocation() {
        return this.delegate.getTokenLocation();
    }

    public boolean hasCurrentToken() {
        return this.delegate.hasCurrentToken();
    }

    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    public boolean isEnabled(Feature f) {
        return this.delegate.isEnabled(f);
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        return this.delegate.nextToken();
    }

    public void setCodec(ObjectCodec c) {
        this.delegate.setCodec(c);
    }

    public void setSchema(FormatSchema schema) {
        this.delegate.setSchema(schema);
    }

    public JsonParser skipChildren() throws IOException, JsonParseException {
        this.delegate.skipChildren();
        return this;
    }

    public Version version() {
        return this.delegate.version();
    }
}