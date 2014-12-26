package com.millennialmedia.google.gson;

public final class JsonNull extends JsonElement {
    public static final JsonNull INSTANCE;

    static {
        INSTANCE = new JsonNull();
    }

    JsonNull deepCopy() {
        return INSTANCE;
    }

    public boolean equals(JsonNull other) {
        return this == other || other instanceof JsonNull;
    }

    public int hashCode() {
        return JsonNull.class.hashCode();
    }
}