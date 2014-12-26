package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;

    static {
        FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                return typeToken.getRawType() == Time.class ? new TimeTypeAdapter() : null;
            }
        };
    }

    public TimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }

    public synchronized Time read(JsonReader in) throws IOException {
        Time time;
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                time = null;
            } else {
                time = new Time(this.format.parse(in.nextString()).getTime());
            }
        } catch (ParseException e) {
            throw new JsonSyntaxException(e);
        } catch (Throwable th) {
        }
        return time;
    }

    public synchronized void write(JsonWriter out, Time value) throws IOException {
        out.value(value == null ? null : this.format.format(value));
    }
}