package com.mopub.common.util;

public class Numbers {
    private Numbers() {
    }

    public static Double parseDouble(Object value) throws ClassCastException {
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException e) {
                throw new ClassCastException(new StringBuilder("Unable to parse ").append(value).append(" as double.").toString());
            }
        }
        throw new ClassCastException(new StringBuilder("Unable to parse ").append(value).append(" as double.").toString());
    }
}