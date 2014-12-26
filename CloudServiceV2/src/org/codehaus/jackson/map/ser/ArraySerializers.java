package org.codehaus.jackson.map.ser;

import com.clouds.util.SMSConstant;
import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.ObjectNode;

public final class ArraySerializers {

    @JacksonStdImpl
    public static final class ByteArraySerializer extends SerializerBase<byte[]> {
        public ByteArraySerializer() {
            super(byte[].class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("string"));
            return o;
        }

        public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeBinary(value);
        }

        public void serializeWithType(byte[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForScalar(value, jgen);
            jgen.writeBinary(value);
            typeSer.writeTypeSuffixForScalar(value, jgen);
        }
    }

    @JacksonStdImpl
    public static final class CharArraySerializer extends SerializerBase<char[]> {
        public CharArraySerializer() {
            super(char[].class);
        }

        private final void _writeArrayContents(JsonGenerator jgen, char[] value) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeString(value, i, 1);
                i++;
            }
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            JsonNode itemSchema = createSchemaNode("string");
            itemSchema.put("type", "string");
            o.put("items", itemSchema);
            return o;
        }

        public void serialize(char[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                jgen.writeStartArray();
                _writeArrayContents(jgen, value);
                jgen.writeEndArray();
            } else {
                jgen.writeString(value, 0, value.length);
            }
        }

        public void serializeWithType(char[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                typeSer.writeTypePrefixForArray(value, jgen);
                _writeArrayContents(jgen, value);
                typeSer.writeTypeSuffixForArray(value, jgen);
            } else {
                typeSer.writeTypePrefixForScalar(value, jgen);
                jgen.writeString(value, 0, value.length);
                typeSer.writeTypeSuffixForScalar(value, jgen);
            }
        }
    }

    public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T> {
        protected final BeanProperty _property;
        protected final TypeSerializer _valueTypeSerializer;

        protected AsArraySerializer(Class<T> cls, TypeSerializer vts, BeanProperty property) {
            super(cls);
            this._valueTypeSerializer = vts;
            this._property = property;
        }

        public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeStartArray();
            serializeContents(value, jgen, provider);
            jgen.writeEndArray();
        }

        protected abstract void serializeContents(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException;

        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForArray(value, jgen);
            serializeContents(value, jgen, provider);
            typeSer.writeTypeSuffixForArray(value, jgen);
        }
    }

    @JacksonStdImpl
    public static final class BooleanArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<boolean[]> {
        public BooleanArraySerializer() {
            super(boolean[].class, null, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("boolean"));
            return o;
        }

        public void serializeContents(boolean[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeBoolean(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class DoubleArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<double[]> {
        public DoubleArraySerializer() {
            super(double[].class, null, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode(SMSConstant.S_number));
            return o;
        }

        public void serializeContents(double[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeNumber(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class FloatArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<float[]> {
        public FloatArraySerializer() {
            this(null);
        }

        public FloatArraySerializer(TypeSerializer vts) {
            super(float[].class, vts, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ArraySerializers.FloatArraySerializer(vts);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode(SMSConstant.S_number));
            return o;
        }

        public void serializeContents(float[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeNumber(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class IntArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<int[]> {
        public IntArraySerializer() {
            super(int[].class, null, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("integer"));
            return o;
        }

        public void serializeContents(int[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeNumber(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class LongArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<long[]> {
        public LongArraySerializer() {
            this(null);
        }

        public LongArraySerializer(TypeSerializer vts) {
            super(long[].class, vts, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ArraySerializers.LongArraySerializer(vts);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode(SMSConstant.S_number, true));
            return o;
        }

        public void serializeContents(long[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeNumber(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class ShortArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<short[]> {
        public ShortArraySerializer() {
            this(null);
        }

        public ShortArraySerializer(TypeSerializer vts) {
            super(short[].class, vts, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ArraySerializers.ShortArraySerializer(vts);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("integer"));
            return o;
        }

        public void serializeContents(short[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                jgen.writeNumber(value[i]);
                i++;
            }
        }
    }

    @JacksonStdImpl
    public static final class StringArraySerializer extends org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer<String[]> implements ResolvableSerializer {
        protected JsonSerializer<Object> _elementSerializer;

        public StringArraySerializer(BeanProperty prop) {
            super(String[].class, null, prop);
        }

        private void serializeContentsSlow(String[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            int i = 0;
            int len = value.length;
            while (i < len) {
                if (value[i] == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    ser.serialize(value[i], jgen, provider);
                }
                i++;
            }
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("string"));
            return o;
        }

        public void resolve(SerializerProvider provider) throws JsonMappingException {
            JsonSerializer<Object> ser = provider.findValueSerializer(String.class, this._property);
            if (ser != null && ser.getClass().getAnnotation(JacksonStdImpl.class) == null) {
                this._elementSerializer = ser;
            }
        }

        public void serializeContents(String[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int len = value.length;
            if (len != 0) {
                if (this._elementSerializer != null) {
                    serializeContentsSlow(value, jgen, provider, this._elementSerializer);
                } else {
                    int i = 0;
                    while (i < len) {
                        if (value[i] == null) {
                            jgen.writeNull();
                        } else {
                            jgen.writeString(value[i]);
                        }
                        i++;
                    }
                }
            }
        }
    }

    private ArraySerializers() {
    }
}