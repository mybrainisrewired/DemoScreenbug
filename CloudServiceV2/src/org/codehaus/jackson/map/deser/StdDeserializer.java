package org.codehaus.jackson.map.deser;

import com.wmt.data.LocalAudioAll;
import com.wmt.data.MediaItem;
import com.wmt.remotectrl.ConnectionInstance;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

public abstract class StdDeserializer<T> extends JsonDeserializer<T> {
    protected final Class<?> _valueClass;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 3;
        }
    }

    public static final class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean> {
        public AtomicBooleanDeserializer() {
            super(AtomicBoolean.class);
        }

        public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
        }
    }

    public static class AtomicReferenceDeserializer extends StdScalarDeserializer<AtomicReference<?>> implements ResolvableDeserializer {
        protected final BeanProperty _property;
        protected final JavaType _referencedType;
        protected JsonDeserializer<?> _valueDeserializer;

        public AtomicReferenceDeserializer(JavaType referencedType, BeanProperty property) {
            super(AtomicReference.class);
            this._referencedType = referencedType;
            this._property = property;
        }

        public AtomicReference<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new AtomicReference(this._valueDeserializer.deserialize(jp, ctxt));
        }

        public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
            this._valueDeserializer = provider.findValueDeserializer(config, this._referencedType, this._property);
        }
    }

    @JacksonStdImpl
    public static class BigDecimalDeserializer extends StdScalarDeserializer<BigDecimal> {
        public BigDecimalDeserializer() {
            super(BigDecimal.class);
        }

        public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                return jp.getDecimalValue();
            }
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                if (text.length() == 0) {
                    return null;
                }
                try {
                    return new BigDecimal(text);
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
                }
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    public static class BigIntegerDeserializer extends StdScalarDeserializer<BigInteger> {
        public BigIntegerDeserializer() {
            super(BigInteger.class);
        }

        public BigInteger deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                    case ClassWriter.COMPUTE_FRAMES:
                        return BigInteger.valueOf(jp.getLongValue());
                }
            } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
                return jp.getDecimalValue().toBigInteger();
            } else {
                if (t != JsonToken.VALUE_STRING) {
                    throw ctxt.mappingException(this._valueClass);
                }
            }
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            try {
                return new BigInteger(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
            }
        }
    }

    @JacksonStdImpl
    public static class CalendarDeserializer extends StdScalarDeserializer<Calendar> {
        Class<? extends Calendar> _calendarClass;

        public CalendarDeserializer() {
            this(null);
        }

        public CalendarDeserializer(Class<? extends Calendar> cc) {
            super(Calendar.class);
            this._calendarClass = cc;
        }

        public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            if (this._calendarClass == null) {
                return ctxt.constructCalendar(d);
            }
            try {
                Calendar c = (Calendar) this._calendarClass.newInstance();
                c.setTimeInMillis(d.getTime());
                return c;
            } catch (Exception e) {
                throw ctxt.instantiationException(this._calendarClass, e);
            }
        }
    }

    @JacksonStdImpl
    public static final class ClassDeserializer extends StdScalarDeserializer<Class<?>> {
        public ClassDeserializer() {
            super(Class.class);
        }

        public Class<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
                String className = jp.getText();
                if (className.indexOf(Opcodes.V1_2) < 0 && "int".equals(className)) {
                    return Integer.TYPE;
                }
                if ("long".equals(className)) {
                    return Long.TYPE;
                }
                if ("float".equals(className)) {
                    return Float.TYPE;
                }
                if ("double".equals(className)) {
                    return Double.TYPE;
                }
                if ("boolean".equals(className)) {
                    return Boolean.TYPE;
                }
                if ("byte".equals(className)) {
                    return Byte.TYPE;
                }
                if ("char".equals(className)) {
                    return Character.TYPE;
                }
                if ("short".equals(className)) {
                    return Short.TYPE;
                }
                if ("void".equals(className)) {
                    return Void.TYPE;
                }
                try {
                    return Class.forName(jp.getText());
                } catch (ClassNotFoundException e) {
                    throw ctxt.instantiationException(this._valueClass, e);
                }
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    public static final class NumberDeserializer extends StdScalarDeserializer<Number> {
        public NumberDeserializer() {
            super(Number.class);
        }

        public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS) ? jp.getBigIntegerValue() : jp.getNumberValue();
            } else {
                if (t == JsonToken.VALUE_NUMBER_FLOAT) {
                    return ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS) ? jp.getDecimalValue() : Double.valueOf(jp.getDoubleValue());
                } else {
                    if (t == JsonToken.VALUE_STRING) {
                        String text = jp.getText().trim();
                        try {
                            if (text.indexOf(Opcodes.V1_2) >= 0) {
                                return ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS) ? new BigDecimal(text) : new Double(text);
                            } else {
                                if (ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS)) {
                                    return new BigInteger(text);
                                }
                                long value = Long.parseLong(text);
                                return (value > 2147483647L || value < -2147483648L) ? Long.valueOf(value) : Integer.valueOf((int) value);
                            }
                        } catch (IllegalArgumentException e) {
                            throw ctxt.weirdStringException(this._valueClass, "not a valid number");
                        }
                    } else {
                        throw ctxt.mappingException(this._valueClass);
                    }
                }
            }
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                case ClassWriter.COMPUTE_FRAMES:
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    return deserialize(jp, ctxt);
                default:
                    return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
            }
        }
    }

    protected static abstract class PrimitiveOrWrapperDeserializer<T> extends StdScalarDeserializer<T> {
        final T _nullValue;

        protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl) {
            super(vc);
            this._nullValue = nvl;
        }

        public final T getNullValue() {
            return this._nullValue;
        }
    }

    public static class SqlDateDeserializer extends StdScalarDeserializer<java.sql.Date> {
        public SqlDateDeserializer() {
            super(java.sql.Date.class);
        }

        public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Date d = _parseDate(jp, ctxt);
            return d == null ? null : new java.sql.Date(d.getTime());
        }
    }

    public static class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement> {
        public StackTraceElementDeserializer() {
            super(StackTraceElement.class);
        }

        public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
                String className = "";
                String methodName = "";
                String fileName = "";
                int lineNumber = -1;
                while (true) {
                    JsonToken t = jp.nextValue();
                    if (t == JsonToken.END_OBJECT) {
                        return new StackTraceElement(className, methodName, fileName, lineNumber);
                    }
                    String propName = jp.getCurrentName();
                    if ("className".equals(propName)) {
                        className = jp.getText();
                    } else if ("fileName".equals(propName)) {
                        fileName = jp.getText();
                    } else if ("lineNumber".equals(propName)) {
                        if (t.isNumeric()) {
                            lineNumber = jp.getIntValue();
                        } else {
                            throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'lineNumber'");
                        }
                    } else if ("methodName".equals(propName)) {
                        methodName = jp.getText();
                    } else if (!"nativeMethod".equals(propName)) {
                        handleUnknownProperty(jp, ctxt, this._valueClass, propName);
                    }
                }
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    public static final class StringDeserializer extends StdScalarDeserializer<String> {
        public StringDeserializer() {
            super(String.class);
        }

        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken curr = jp.getCurrentToken();
            if (curr == JsonToken.VALUE_STRING) {
                return jp.getText();
            }
            if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                return ob instanceof byte[] ? Base64Variants.getDefaultVariant().encode((byte[]) ob, false) : ob.toString();
            } else if (curr.isScalarValue()) {
                return jp.getText();
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return deserialize(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer> {
        public TokenBufferDeserializer() {
            super(TokenBuffer.class);
        }

        public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            TokenBuffer tb = new TokenBuffer(jp.getCodec());
            tb.copyCurrentStructure(jp);
            return tb;
        }
    }

    @JacksonStdImpl
    public static final class BooleanDeserializer extends PrimitiveOrWrapperDeserializer<Boolean> {
        public BooleanDeserializer(Class<Boolean> cls, Boolean nvl) {
            super(cls, nvl);
        }

        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseBoolean(jp, ctxt);
        }

        public Boolean deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return _parseBoolean(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class ByteDeserializer extends PrimitiveOrWrapperDeserializer<Byte> {
        public ByteDeserializer(Class<Byte> cls, Byte nvl) {
            super(cls, nvl);
        }

        public Byte deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            int value = _parseIntPrimitive(jp, ctxt);
            if (value >= -128 && value <= 127) {
                return Byte.valueOf((byte) value);
            }
            throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 8-bit value");
        }
    }

    @JacksonStdImpl
    public static final class CharacterDeserializer extends PrimitiveOrWrapperDeserializer<Character> {
        public CharacterDeserializer(Class<Character> cls, Character nvl) {
            super(cls, nvl);
        }

        public Character deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                int value = jp.getIntValue();
                if (value >= 0 && value <= 65535) {
                    return Character.valueOf((char) value);
                }
            } else if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText();
                if (text.length() == 1) {
                    return Character.valueOf(text.charAt(0));
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    public static final class DoubleDeserializer extends PrimitiveOrWrapperDeserializer<Double> {
        public DoubleDeserializer(Class<Double> cls, Double nvl) {
            super(cls, nvl);
        }

        public Double deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseDouble(jp, ctxt);
        }

        public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return _parseDouble(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class FloatDeserializer extends PrimitiveOrWrapperDeserializer<Float> {
        public FloatDeserializer(Class<Float> cls, Float nvl) {
            super(cls, nvl);
        }

        public Float deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseFloat(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class IntegerDeserializer extends PrimitiveOrWrapperDeserializer<Integer> {
        public IntegerDeserializer(Class<Integer> cls, Integer nvl) {
            super(cls, nvl);
        }

        public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseInteger(jp, ctxt);
        }

        public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return _parseInteger(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class LongDeserializer extends PrimitiveOrWrapperDeserializer<Long> {
        public LongDeserializer(Class<Long> cls, Long nvl) {
            super(cls, nvl);
        }

        public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseLong(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class ShortDeserializer extends PrimitiveOrWrapperDeserializer<Short> {
        public ShortDeserializer(Class<Short> cls, Short nvl) {
            super(cls, nvl);
        }

        public Short deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return _parseShort(jp, ctxt);
        }
    }

    protected StdDeserializer(Class<?> vc) {
        this._valueClass = vc;
    }

    protected StdDeserializer(JavaType valueType) {
        this._valueClass = valueType == null ? null : valueType.getRawClass();
    }

    protected static final double parseDouble(String numStr) throws NumberFormatException {
        return NumberInput.NASTY_SMALL_DOUBLE.equals(numStr) ? Double.MIN_NORMAL : Double.parseDouble(numStr);
    }

    protected final Boolean _parseBoolean(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return jp.getIntValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
        } else {
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                if ("true".equals(text)) {
                    return Boolean.TRUE;
                }
                if ("false".equals(text) || text.length() == 0) {
                    return Boolean.FALSE;
                }
                throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    protected final boolean _parseBooleanPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return true;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return false;
        }
        if (t == JsonToken.VALUE_NULL) {
            return false;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return jp.getIntValue() != 0;
        } else {
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                if ("true".equals(text)) {
                    return true;
                }
                if ("false".equals(text) || text.length() == 0) {
                    return Boolean.FALSE.booleanValue();
                }
                throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    protected Date _parseDate(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        try {
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return new Date(jp.getLongValue());
            }
            if (t == JsonToken.VALUE_STRING) {
                String str = jp.getText().trim();
                return str.length() == 0 ? null : ctxt.parseDate(str);
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        } catch (IllegalArgumentException e) {
            throw ctxt.weirdStringException(this._valueClass, "not a valid representation (error: " + e.getMessage() + ")");
        }
    }

    protected final Double _parseDouble(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Double.valueOf(jp.getDoubleValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            switch (text.charAt(0)) {
                case ConnectionInstance.CONNECTION_FAILED:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Double.valueOf(Double.NEGATIVE_INFINITY);
                    }
                case 'I':
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Double.valueOf(Double.POSITIVE_INFINITY);
                    }
                case 'N':
                    if ("NaN".equals(text)) {
                        return Double.valueOf(Double.NaN);
                    }
            }
            try {
                return Double.valueOf(parseDouble(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Double value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return null;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final double _parseDoublePrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getDoubleValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return MediaItem.INVALID_LATLNG;
            }
            switch (text.charAt(0)) {
                case ConnectionInstance.CONNECTION_FAILED:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Double.NEGATIVE_INFINITY;
                    }
                case 'I':
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Double.POSITIVE_INFINITY;
                    }
                case 'N':
                    if ("NaN".equals(text)) {
                        return Double.NaN;
                    }
            }
            try {
                return parseDouble(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid double value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return MediaItem.INVALID_LATLNG;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final Float _parseFloat(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Float.valueOf(jp.getFloatValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            switch (text.charAt(0)) {
                case ConnectionInstance.CONNECTION_FAILED:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Float.valueOf(Float.NEGATIVE_INFINITY);
                    }
                case 'I':
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Float.valueOf(Float.POSITIVE_INFINITY);
                    }
                case 'N':
                    if ("NaN".equals(text)) {
                        return Float.valueOf(Float.NaN);
                    }
            }
            try {
                return Float.valueOf(Float.parseFloat(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Float value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return null;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final float _parseFloatPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getFloatValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return 0.0f;
            }
            switch (text.charAt(0)) {
                case ConnectionInstance.CONNECTION_FAILED:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Float.NEGATIVE_INFINITY;
                    }
                case 'I':
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Float.POSITIVE_INFINITY;
                    }
                case 'N':
                    if ("NaN".equals(text)) {
                        return Float.NaN;
                    }
            }
            try {
                return Float.parseFloat(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid float value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return 0.0f;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final int _parseIntPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getIntValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            try {
                int len = text.length();
                if (len <= 9) {
                    return len != 0 ? NumberInput.parseInt(text) : 0;
                } else {
                    long l = Long.parseLong(text);
                    if (l >= -2147483648L && l <= 2147483647L) {
                        return (int) l;
                    }
                    throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
                }
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid int value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return 0;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final Integer _parseInteger(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Integer.valueOf(jp.getIntValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            try {
                int len = text.length();
                if (len <= 9) {
                    return len != 0 ? Integer.valueOf(NumberInput.parseInt(text)) : null;
                } else {
                    long l = Long.parseLong(text);
                    if (l >= -2147483648L && l <= 2147483647L) {
                        return Integer.valueOf((int) l);
                    }
                    throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of Integer (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
                }
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Integer value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return null;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final Long _parseLong(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Long.valueOf(jp.getLongValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            try {
                return Long.valueOf(NumberInput.parseLong(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Long value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return null;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final long _parseLongPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getLongValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return 0;
            }
            try {
                return NumberInput.parseLong(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid long value");
            }
        } else if (t == JsonToken.VALUE_NULL) {
            return 0;
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }

    protected final Short _parseShort(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Short.valueOf(jp.getShortValue());
        }
        int value = _parseIntPrimitive(jp, ctxt);
        if (value >= -32768 && value <= 32767) {
            return Short.valueOf((short) value);
        }
        throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
    }

    protected final short _parseShortPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        int value = _parseIntPrimitive(jp, ctxt);
        if (value >= -32768 && value <= 32767) {
            return (short) value;
        }
        throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    protected JsonDeserializer<Object> findDeserializer(DeserializationConfig config, DeserializerProvider provider, JavaType type, BeanProperty property) throws JsonMappingException {
        return provider.findValueDeserializer(config, type, property);
    }

    public Class<?> getValueClass() {
        return this._valueClass;
    }

    public JavaType getValueType() {
        return null;
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object instanceOrClass, String propName) throws IOException, JsonProcessingException {
        if (instanceOrClass == null) {
            instanceOrClass = getValueClass();
        }
        if (!ctxt.handleUnknownProperty(jp, this, instanceOrClass, propName)) {
            reportUnknownProperty(ctxt, instanceOrClass, propName);
            jp.skipChildren();
        }
    }

    protected boolean isDefaultSerializer(JsonDeserializer<?> deserializer) {
        return (deserializer == null || deserializer.getClass().getAnnotation(JacksonStdImpl.class) == null) ? false : true;
    }

    protected void reportUnknownProperty(DeserializationContext ctxt, Object instanceOrClass, String fieldName) throws IOException, JsonProcessingException {
        if (ctxt.isEnabled(Feature.FAIL_ON_UNKNOWN_PROPERTIES)) {
            throw ctxt.unknownFieldException(instanceOrClass, fieldName);
        }
    }
}