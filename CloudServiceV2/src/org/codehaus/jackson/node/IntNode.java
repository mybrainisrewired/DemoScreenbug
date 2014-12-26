package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.map.SerializerProvider;

public final class IntNode extends NumericNode {
    private static final IntNode[] CANONICALS;
    static final int MAX_CANONICAL = 10;
    static final int MIN_CANONICAL = -1;
    final int _value;

    static {
        CANONICALS = new IntNode[12];
        int i = 0;
        while (i < 12) {
            CANONICALS[i] = new IntNode(i - 1);
            i++;
        }
    }

    public IntNode(int v) {
        this._value = v;
    }

    public static IntNode valueOf(int i) {
        return (i > 10 || i < -1) ? new IntNode(i) : CANONICALS[i + 1];
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }

    public boolean equals(IntNode o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        return ((IntNode) o)._value == this._value;
    }

    public BigInteger getBigIntegerValue() {
        return BigInteger.valueOf((long) this._value);
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf((long) this._value);
    }

    public double getDoubleValue() {
        return (double) this._value;
    }

    public int getIntValue() {
        return this._value;
    }

    public long getLongValue() {
        return (long) this._value;
    }

    public NumberType getNumberType() {
        return NumberType.INT;
    }

    public Number getNumberValue() {
        return Integer.valueOf(this._value);
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return this._value != 0;
    }

    public String getValueAsText() {
        return NumberOutput.toString(this._value);
    }

    public int hashCode() {
        return this._value;
    }

    public boolean isInt() {
        return true;
    }

    public boolean isIntegralNumber() {
        return true;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNumber(this._value);
    }
}