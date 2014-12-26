package org.codehaus.jackson;

import com.wmt.data.MediaItem;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonParser.NumberType;

public abstract class JsonNode implements Iterable<JsonNode> {
    protected static final List<JsonNode> NO_NODES;
    protected static final List<String> NO_STRINGS;

    static {
        NO_NODES = Collections.emptyList();
        NO_STRINGS = Collections.emptyList();
    }

    protected JsonNode() {
    }

    public abstract JsonToken asToken();

    public abstract boolean equals(Object obj);

    public abstract JsonNode findParent(String str);

    public final List<JsonNode> findParents(String fieldName) {
        List<JsonNode> result = findParents(fieldName, null);
        return result == null ? Collections.emptyList() : result;
    }

    public abstract List<JsonNode> findParents(String str, List<JsonNode> list);

    public abstract JsonNode findPath(String str);

    public abstract JsonNode findValue(String str);

    public final List<JsonNode> findValues(String fieldName) {
        List<JsonNode> result = findValues(fieldName, null);
        return result == null ? Collections.emptyList() : result;
    }

    public abstract List<JsonNode> findValues(String str, List<JsonNode> list);

    public final List<String> findValuesAsText(String fieldName) {
        List<String> result = findValuesAsText(fieldName, null);
        return result == null ? Collections.emptyList() : result;
    }

    public abstract List<String> findValuesAsText(String str, List<String> list);

    public JsonNode get(int index) {
        return null;
    }

    public JsonNode get(String fieldName) {
        return null;
    }

    public BigInteger getBigIntegerValue() {
        return BigInteger.ZERO;
    }

    public byte[] getBinaryValue() throws IOException {
        return null;
    }

    public boolean getBooleanValue() {
        return false;
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.ZERO;
    }

    public double getDoubleValue() {
        return MediaItem.INVALID_LATLNG;
    }

    @Deprecated
    public final JsonNode getElementValue(int index) {
        return get(index);
    }

    public Iterator<JsonNode> getElements() {
        return NO_NODES.iterator();
    }

    public Iterator<String> getFieldNames() {
        return NO_STRINGS.iterator();
    }

    @Deprecated
    public final JsonNode getFieldValue(String fieldName) {
        return get(fieldName);
    }

    public Iterator<Entry<String, JsonNode>> getFields() {
        return Collections.emptyList().iterator();
    }

    public int getIntValue() {
        return 0;
    }

    public long getLongValue() {
        return 0;
    }

    public abstract NumberType getNumberType();

    public Number getNumberValue() {
        return null;
    }

    @Deprecated
    public final JsonNode getPath(int index) {
        return path(index);
    }

    @Deprecated
    public final JsonNode getPath(String fieldName) {
        return path(fieldName);
    }

    public String getTextValue() {
        return null;
    }

    public boolean getValueAsBoolean() {
        return getValueAsBoolean(false);
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return defaultValue;
    }

    public double getValueAsDouble() {
        return getValueAsDouble(MediaItem.INVALID_LATLNG);
    }

    public double getValueAsDouble(double defaultValue) {
        return defaultValue;
    }

    public int getValueAsInt() {
        return getValueAsInt(0);
    }

    public int getValueAsInt(int defaultValue) {
        return defaultValue;
    }

    public long getValueAsLong() {
        return getValueAsLong(0);
    }

    public long getValueAsLong(long defaultValue) {
        return defaultValue;
    }

    public abstract String getValueAsText();

    public boolean has(int index) {
        return get(index) != null;
    }

    public boolean has(String fieldName) {
        return get(fieldName) != null;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isBigDecimal() {
        return false;
    }

    public boolean isBigInteger() {
        return false;
    }

    public boolean isBinary() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isContainerNode() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isFloatingPointNumber() {
        return false;
    }

    public boolean isInt() {
        return false;
    }

    public boolean isIntegralNumber() {
        return false;
    }

    public boolean isLong() {
        return false;
    }

    public boolean isMissingNode() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPojo() {
        return false;
    }

    public boolean isTextual() {
        return false;
    }

    public boolean isValueNode() {
        return false;
    }

    public final Iterator<JsonNode> iterator() {
        return getElements();
    }

    public abstract JsonNode path(int i);

    public abstract JsonNode path(String str);

    public int size() {
        return 0;
    }

    public abstract String toString();

    public abstract JsonParser traverse();

    public JsonNode with(String propertyName) {
        throw new UnsupportedOperationException("JsonNode not of type ObjectNode (but " + getClass().getName() + "), can not call with() on it");
    }

    @Deprecated
    public abstract void writeTo(JsonGenerator jsonGenerator) throws IOException, JsonGenerationException;
}