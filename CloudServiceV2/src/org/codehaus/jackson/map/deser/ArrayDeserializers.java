package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ArrayBuilders.BooleanBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ByteBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.DoubleBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.FloatBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.IntBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.LongBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ShortBuilder;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

public class ArrayDeserializers {
    static final ArrayDeserializers instance;
    HashMap<JavaType, JsonDeserializer<Object>> _allDeserializers;

    static abstract class ArrayDeser<T> extends StdDeserializer<T> {
        protected ArrayDeser(Class cls) {
            super(cls);
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
        }
    }

    @JacksonStdImpl
    static final class BooleanDeser extends ArrayDeser<boolean[]> {
        public BooleanDeser() {
            super(boolean[].class);
        }

        private final boolean[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new boolean[]{_parseBooleanPrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
            boolean[] chunk = (boolean[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                boolean value = _parseBooleanPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (boolean[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class ByteDeser extends ArrayDeser<byte[]> {
        public ByteDeser() {
            super(byte[].class);
        }

        private final byte[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                byte value;
                JsonToken t = jp.getCurrentToken();
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    value = jp.getByteValue();
                } else if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                } else {
                    value = (byte) 0;
                }
                return new byte[]{value};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                return jp.getBinaryValue(ctxt.getBase64Variant());
            }
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
            byte[] chunk = (byte[]) builder.resetAndStart();
            int ix = 0;
            while (true) {
                t = jp.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return (byte[]) builder.completeAndClearBuffer(chunk, ix);
                }
                byte value;
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    value = jp.getByteValue();
                } else if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                } else {
                    value = (byte) 0;
                }
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
        }
    }

    @JacksonStdImpl
    static final class CharDeser extends ArrayDeser<char[]> {
        public CharDeser() {
            super(char[].class);
        }

        public char[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                char[] buffer = jp.getTextCharacters();
                int offset = jp.getTextOffset();
                int len = jp.getTextLength();
                char[] result = new char[len];
                System.arraycopy(buffer, offset, result, 0, len);
                return result;
            } else if (jp.isExpectedStartArrayToken()) {
                StringBuilder sb = new StringBuilder(64);
                while (true) {
                    t = jp.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        return sb.toString().toCharArray();
                    }
                    if (t != JsonToken.VALUE_STRING) {
                        throw ctxt.mappingException(Character.TYPE);
                    }
                    String str = jp.getText();
                    if (str.length() != 1) {
                        throw JsonMappingException.from(jp, "Can not convert a JSON String of length " + str.length() + " into a char element of char array");
                    }
                    sb.append(str.charAt(0));
                }
            } else {
                if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                    Object ob = jp.getEmbeddedObject();
                    if (ob == null) {
                        return null;
                    }
                    if (ob instanceof char[]) {
                        return (char[]) ob;
                    }
                    if (ob instanceof String) {
                        return ((String) ob).toCharArray();
                    }
                    if (ob instanceof byte[]) {
                        return Base64Variants.getDefaultVariant().encode((byte[]) ob, false).toCharArray();
                    }
                }
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    static final class DoubleDeser extends ArrayDeser<double[]> {
        public DoubleDeser() {
            super(double[].class);
        }

        private final double[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new double[]{_parseDoublePrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
            double[] chunk = (double[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                double value = _parseDoublePrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (double[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class FloatDeser extends ArrayDeser<float[]> {
        public FloatDeser() {
            super(float[].class);
        }

        private final float[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new float[]{_parseFloatPrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public float[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
            float[] chunk = (float[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                float value = _parseFloatPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (float[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class IntDeser extends ArrayDeser<int[]> {
        public IntDeser() {
            super(int[].class);
        }

        private final int[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new int[]{_parseIntPrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public int[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
            int[] chunk = (int[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                int value = _parseIntPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (int[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class LongDeser extends ArrayDeser<long[]> {
        public LongDeser() {
            super(long[].class);
        }

        private final long[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new long[]{_parseLongPrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public long[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
            long[] chunk = (long[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                long value = _parseLongPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (long[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class ShortDeser extends ArrayDeser<short[]> {
        public ShortDeser() {
            super(short[].class);
        }

        private final short[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new short[]{_parseShortPrimitive(jp, ctxt)};
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
            short[] chunk = (short[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                short value = _parseShortPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (short[]) builder.completeAndClearBuffer(chunk, ix);
        }
    }

    @JacksonStdImpl
    static final class StringDeser extends ArrayDeser<String[]> {
        public StringDeser() {
            super(String[].class);
        }

        private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                String[] strArr = new String[1];
                strArr[0] = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : jp.getText();
                return strArr;
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }

        public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            while (true) {
                JsonToken t = jp.nextToken();
                if (t != JsonToken.END_ARRAY) {
                    String value = t == JsonToken.VALUE_NULL ? null : jp.getText();
                    if (ix >= chunk.length) {
                        chunk = buffer.appendCompletedChunk(chunk);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                } else {
                    String[] result = (String[]) buffer.completeAndClearBuffer(chunk, ix, String.class);
                    ctxt.returnObjectBuffer(buffer);
                    return result;
                }
            }
        }
    }

    static {
        instance = new ArrayDeserializers();
    }

    private ArrayDeserializers() {
        this._allDeserializers = new HashMap();
        add(Boolean.TYPE, new BooleanDeser());
        add(Byte.TYPE, new ByteDeser());
        add(Short.TYPE, new ShortDeser());
        add(Integer.TYPE, new IntDeser());
        add(Long.TYPE, new LongDeser());
        add(Float.TYPE, new FloatDeser());
        add(Double.TYPE, new DoubleDeser());
        add(String.class, new StringDeser());
        add(Character.TYPE, new CharDeser());
    }

    private void add(Type cls, JsonDeserializer<?> deser) {
        this._allDeserializers.put(TypeFactory.defaultInstance().constructType(cls), deser);
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> getAll() {
        return instance._allDeserializers;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }
}