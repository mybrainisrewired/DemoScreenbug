package com.millennialmedia.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class LazilyParsedNumber extends Number {
    private final String value;

    public LazilyParsedNumber(String value) {
        this.value = value;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new BigDecimal(this.value);
    }

    public double doubleValue() {
        return Double.parseDouble(this.value);
    }

    public float floatValue() {
        return Float.parseFloat(this.value);
    }

    public int intValue() {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            try {
                return (int) Long.parseLong(this.value);
            } catch (NumberFormatException e2) {
                return new BigInteger(this.value).intValue();
            }
        }
    }

    public long longValue() {
        try {
            return Long.parseLong(this.value);
        } catch (NumberFormatException e) {
            return new BigInteger(this.value).longValue();
        }
    }

    public String toString() {
        return this.value;
    }
}