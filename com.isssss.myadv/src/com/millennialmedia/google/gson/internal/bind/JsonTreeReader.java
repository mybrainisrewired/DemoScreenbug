package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.google.gson.JsonArray;
import com.millennialmedia.google.gson.JsonElement;
import com.millennialmedia.google.gson.JsonNull;
import com.millennialmedia.google.gson.JsonObject;
import com.millennialmedia.google.gson.JsonPrimitive;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class JsonTreeReader extends JsonReader {
    private static final Object SENTINEL_CLOSED;
    private static final Reader UNREADABLE_READER;
    private final List<Object> stack;

    static {
        UNREADABLE_READER = new Reader() {
            public void close() throws IOException {
                throw new AssertionError();
            }

            public int read(char[] buffer, int offset, int count) throws IOException {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new Object();
    }

    public JsonTreeReader(JsonElement element) {
        super(UNREADABLE_READER);
        this.stack = new ArrayList();
        this.stack.add(element);
    }

    private void expect(JsonToken expected) throws IOException {
        if (peek() != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + peek());
        }
    }

    private Object peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }

    private Object popStack() {
        return this.stack.remove(this.stack.size() - 1);
    }

    public void beginArray() throws IOException {
        expect(JsonToken.BEGIN_ARRAY);
        this.stack.add(((JsonArray) peekStack()).iterator());
    }

    public void beginObject() throws IOException {
        expect(JsonToken.BEGIN_OBJECT);
        this.stack.add(((JsonObject) peekStack()).entrySet().iterator());
    }

    public void close() throws IOException {
        this.stack.clear();
        this.stack.add(SENTINEL_CLOSED);
    }

    public void endArray() throws IOException {
        expect(JsonToken.END_ARRAY);
        popStack();
        popStack();
    }

    public void endObject() throws IOException {
        expect(JsonToken.END_OBJECT);
        popStack();
        popStack();
    }

    public boolean hasNext() throws IOException {
        JsonToken token = peek();
        return (token == JsonToken.END_OBJECT || token == JsonToken.END_ARRAY) ? false : true;
    }

    public boolean nextBoolean() throws IOException {
        expect(JsonToken.BOOLEAN);
        return ((JsonPrimitive) popStack()).getAsBoolean();
    }

    public double nextDouble() throws IOException {
        JsonToken token = peek();
        if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
            double result = ((JsonPrimitive) peekStack()).getAsDouble();
            if (isLenient() || !(Double.isNaN(result) || Double.isInfinite(result))) {
                popStack();
                return result;
            } else {
                throw new NumberFormatException("JSON forbids NaN and infinities: " + result);
            }
        } else {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
    }

    public int nextInt() throws IOException {
        JsonToken token = peek();
        if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
            int result = ((JsonPrimitive) peekStack()).getAsInt();
            popStack();
            return result;
        } else {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
    }

    public long nextLong() throws IOException {
        JsonToken token = peek();
        if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
            long result = ((JsonPrimitive) peekStack()).getAsLong();
            popStack();
            return result;
        } else {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
    }

    public String nextName() throws IOException {
        expect(JsonToken.NAME);
        Entry<?, ?> entry = (Entry) ((Iterator) peekStack()).next();
        this.stack.add(entry.getValue());
        return (String) entry.getKey();
    }

    public void nextNull() throws IOException {
        expect(JsonToken.NULL);
        popStack();
    }

    public String nextString() throws IOException {
        JsonToken token = peek();
        if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
            return ((JsonPrimitive) popStack()).getAsString();
        }
        throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + token);
    }

    public JsonToken peek() throws IOException {
        if (this.stack.isEmpty()) {
            return JsonToken.END_DOCUMENT;
        }
        Object o = peekStack();
        if (o instanceof Iterator) {
            boolean isObject = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            Iterator<?> iterator = (Iterator) o;
            if (!iterator.hasNext()) {
                return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            } else {
                if (isObject) {
                    return JsonToken.NAME;
                }
                this.stack.add(iterator.next());
                return peek();
            }
        } else if (o instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        } else {
            if (o instanceof JsonArray) {
                return JsonToken.BEGIN_ARRAY;
            }
            if (o instanceof JsonPrimitive) {
                JsonPrimitive primitive = (JsonPrimitive) o;
                if (primitive.isString()) {
                    return JsonToken.STRING;
                }
                if (primitive.isBoolean()) {
                    return JsonToken.BOOLEAN;
                }
                if (primitive.isNumber()) {
                    return JsonToken.NUMBER;
                }
                throw new AssertionError();
            } else if (o instanceof JsonNull) {
                return JsonToken.NULL;
            } else {
                if (o == SENTINEL_CLOSED) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new AssertionError();
            }
        }
    }

    public void promoteNameToValue() throws IOException {
        expect(JsonToken.NAME);
        Entry<?, ?> entry = (Entry) ((Iterator) peekStack()).next();
        this.stack.add(entry.getValue());
        this.stack.add(new JsonPrimitive((String) entry.getKey()));
    }

    public void skipValue() throws IOException {
        if (peek() == JsonToken.NAME) {
            nextName();
        } else {
            popStack();
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}