package com.millennialmedia.google.gson;

import com.millennialmedia.google.gson.internal.bind.JsonTreeReader;
import com.millennialmedia.google.gson.internal.bind.JsonTreeWriter;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter<T> {
    public final T fromJson(Reader in) throws IOException {
        return read(new JsonReader(in));
    }

    public final T fromJson(String json) throws IOException {
        return fromJson(new StringReader(json));
    }

    public final T fromJsonTree(JsonElement jsonTree) {
        try {
            return read(new JsonTreeReader(jsonTree));
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>() {
            public T read(JsonReader reader) throws IOException {
                if (reader.peek() != JsonToken.NULL) {
                    return TypeAdapter.this.read(reader);
                }
                reader.nextNull();
                return null;
            }

            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    TypeAdapter.this.write(out, value);
                }
            }
        };
    }

    public abstract T read(JsonReader jsonReader) throws IOException;

    public final String toJson(T value) throws IOException {
        StringWriter stringWriter = new StringWriter();
        toJson(stringWriter, value);
        return stringWriter.toString();
    }

    public final void toJson(Writer out, T value) throws IOException {
        write(new JsonWriter(out), value);
    }

    public final JsonElement toJsonTree(T value) {
        try {
            JsonTreeWriter jsonWriter = new JsonTreeWriter();
            write(jsonWriter, value);
            return jsonWriter.get();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    public abstract void write(JsonWriter jsonWriter, T t) throws IOException;
}