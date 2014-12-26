package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.android.MMAdView;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat enUsFormat;
    private final DateFormat iso8601Format;
    private final DateFormat localFormat;

    static {
        FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                return typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
            }
        };
    }

    public DateTypeAdapter() {
        this.enUsFormat = DateFormat.getDateTimeInstance(MMAdView.TRANSITION_UP, MMAdView.TRANSITION_UP, Locale.US);
        this.localFormat = DateFormat.getDateTimeInstance(MMAdView.TRANSITION_UP, MMAdView.TRANSITION_UP);
        this.iso8601Format = buildIso8601Format();
    }

    private static DateFormat buildIso8601Format() {
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return iso8601Format;
    }

    private synchronized Date deserializeToDate(String json) {
        Date parse;
        try {
            parse = this.localFormat.parse(json);
        } catch (ParseException e) {
            try {
                parse = this.enUsFormat.parse(json);
            } catch (ParseException e2) {
                try {
                    parse = this.iso8601Format.parse(json);
                } catch (ParseException e3) {
                    throw new JsonSyntaxException(json, e3);
                } catch (Throwable th) {
                }
            }
        }
        return parse;
    }

    public Date read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.NULL) {
            return deserializeToDate(in.nextString());
        }
        in.nextNull();
        return null;
    }

    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(this.enUsFormat.format(value));
        }
    }
}