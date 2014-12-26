package org.codehaus.jackson.map.ext;

import com.clouds.util.SMSConstant;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.map.util.Provider;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JodaSerializers implements Provider<Entry<Class<?>, JsonSerializer<?>>> {
    static final HashMap<Class<?>, JsonSerializer<?>> _serializers;

    protected static abstract class JodaSerializer<T> extends SerializerBase<T> {
        static final DateTimeFormatter _localDateFormat;
        static final DateTimeFormatter _localDateTimeFormat;

        static {
            _localDateTimeFormat = ISODateTimeFormat.dateTime();
            _localDateFormat = ISODateTimeFormat.date();
        }

        protected JodaSerializer(Class cls) {
            super(cls);
        }

        protected String printLocalDate(ReadableInstant dateValue) throws IOException, JsonProcessingException {
            return _localDateFormat.print(dateValue);
        }

        protected String printLocalDate(ReadablePartial dateValue) throws IOException, JsonProcessingException {
            return _localDateFormat.print(dateValue);
        }

        protected String printLocalDateTime(ReadablePartial dateValue) throws IOException, JsonProcessingException {
            return _localDateTimeFormat.print(dateValue);
        }
    }

    public static final class DateMidnightSerializer extends JodaSerializer<DateMidnight> {
        public DateMidnightSerializer() {
            super(DateMidnight.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }

        public void serialize(DateMidnight dt, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeEndArray();
            } else {
                jgen.writeString(printLocalDate(dt));
            }
        }
    }

    public static final class DateTimeSerializer extends JodaSerializer<DateTime> {
        public DateTimeSerializer() {
            super(DateTime.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? SMSConstant.S_number : "string", true);
        }

        public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeNumber(value.getMillis());
            } else {
                jgen.writeString(value.toString());
            }
        }
    }

    public static final class LocalDateSerializer extends JodaSerializer<LocalDate> {
        public LocalDateSerializer() {
            super(LocalDate.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }

        public void serialize(LocalDate dt, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeEndArray();
            } else {
                jgen.writeString(printLocalDate(dt));
            }
        }
    }

    public static final class LocalDateTimeSerializer extends JodaSerializer<LocalDateTime> {
        public LocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }

        public void serialize(LocalDateTime dt, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeNumber(dt.hourOfDay().get());
                jgen.writeNumber(dt.minuteOfHour().get());
                jgen.writeNumber(dt.secondOfMinute().get());
                jgen.writeNumber(dt.millisOfSecond().get());
                jgen.writeEndArray();
            } else {
                jgen.writeString(printLocalDateTime(dt));
            }
        }
    }

    static {
        _serializers = new HashMap();
        _serializers.put(DateTime.class, new DateTimeSerializer());
        _serializers.put(LocalDateTime.class, new LocalDateTimeSerializer());
        _serializers.put(LocalDate.class, new LocalDateSerializer());
        _serializers.put(DateMidnight.class, new DateMidnightSerializer());
    }

    public Collection<Entry<Class<?>, JsonSerializer<?>>> provide() {
        return _serializers.entrySet();
    }
}