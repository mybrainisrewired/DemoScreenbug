package org.codehaus.jackson.map.ext;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.deser.StdScalarDeserializer;
import org.codehaus.jackson.map.util.Provider;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JodaDeserializers implements Provider<StdDeserializer<?>> {

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 2;
        }
    }

    static abstract class JodaDeserializer<T> extends StdScalarDeserializer<T> {
        static final DateTimeFormatter _localDateTimeFormat;

        static {
            _localDateTimeFormat = ISODateTimeFormat.localDateOptionalTimeParser();
        }

        protected JodaDeserializer(Class<T> cls) {
            super(cls);
        }

        protected DateTime parseLocal(JsonParser jp) throws IOException, JsonProcessingException {
            String str = jp.getText().trim();
            return str.length() == 0 ? null : _localDateTimeFormat.parseDateTime(str);
        }
    }

    public static class DateMidnightDeserializer extends JodaDeserializer<DateMidnight> {
        public DateMidnightDeserializer() {
            super(DateMidnight.class);
        }

        public DateMidnight deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return new DateMidnight(year, month, day);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after DateMidnight ints");
            } else {
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        return new DateMidnight(jp.getLongValue());
                    case ClassWriter.COMPUTE_FRAMES:
                        DateTime local = parseLocal(jp);
                        return local == null ? null : local.toDateMidnight();
                    default:
                        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
                }
            }
        }
    }

    public static class DateTimeDeserializer<T extends ReadableInstant> extends JodaDeserializer<T> {
        public DateTimeDeserializer(Class<T> cls) {
            super(cls);
        }

        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return new DateTime(jp.getLongValue(), DateTimeZone.UTC);
            }
            if (t == JsonToken.VALUE_STRING) {
                String str = jp.getText().trim();
                return str.length() == 0 ? null : new DateTime(str, DateTimeZone.UTC);
            } else {
                throw ctxt.mappingException(getValueClass());
            }
        }
    }

    public static class LocalDateDeserializer extends JodaDeserializer<LocalDate> {
        public LocalDateDeserializer() {
            super(LocalDate.class);
        }

        public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return new LocalDate(year, month, day);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDate ints");
            } else {
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        return new LocalDate(jp.getLongValue());
                    case ClassWriter.COMPUTE_FRAMES:
                        DateTime local = parseLocal(jp);
                        return local == null ? null : local.toLocalDate();
                    default:
                        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, String or Number");
                }
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JodaDeserializer<LocalDateTime> {
        public LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                jp.nextToken();
                int hour = jp.getIntValue();
                jp.nextToken();
                int minute = jp.getIntValue();
                jp.nextToken();
                int second = jp.getIntValue();
                int millisecond = 0;
                if (jp.nextToken() != JsonToken.END_ARRAY) {
                    millisecond = jp.getIntValue();
                    jp.nextToken();
                }
                if (jp.getCurrentToken() == JsonToken.END_ARRAY) {
                    return new LocalDateTime(year, month, day, hour, minute, second, millisecond);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDateTime ints");
            } else {
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        return new LocalDateTime(jp.getLongValue());
                    case ClassWriter.COMPUTE_FRAMES:
                        DateTime local = parseLocal(jp);
                        return local == null ? null : local.toLocalDateTime();
                    default:
                        throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array or Number");
                }
            }
        }
    }

    public Collection<StdDeserializer<?>> provide() {
        return Arrays.asList(new StdDeserializer[]{new DateTimeDeserializer(DateTime.class), new DateTimeDeserializer(ReadableDateTime.class), new DateTimeDeserializer(ReadableInstant.class), new LocalDateDeserializer(), new LocalDateTimeDeserializer(), new DateMidnightDeserializer()});
    }
}