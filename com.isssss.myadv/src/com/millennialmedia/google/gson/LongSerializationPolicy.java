package com.millennialmedia.google.gson;

public enum LongSerializationPolicy {
    DEFAULT {
        public JsonElement serialize(Number value) {
            return new JsonPrimitive(value);
        }
    },
    STRING {
        public JsonElement serialize(Long value) {
            return new JsonPrimitive(String.valueOf(value));
        }
    };

    public abstract JsonElement serialize(Long l);
}