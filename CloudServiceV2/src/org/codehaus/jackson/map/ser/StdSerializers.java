package org.codehaus.jackson.map.ser;

import com.clouds.util.SMSConstant;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSerializableSchema;
import org.codehaus.jackson.util.TokenBuffer;

public class StdSerializers {

    @JacksonStdImpl
    public static final class SerializableSerializer extends SerializerBase<JsonSerializable> {
        protected static final org.codehaus.jackson.map.ser.StdSerializers.SerializableSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.SerializableSerializer();
        }

        private SerializableSerializer() {
            super(JsonSerializable.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.type(typeHint).getRawClass();
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put("type", schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            return objectNode;
        }

        public void serialize(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen, provider);
        }

        public final void serializeWithType(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            if (value instanceof JsonSerializableWithType) {
                ((JsonSerializableWithType) value).serializeWithType(jgen, provider, typeSer);
            } else {
                serialize(value, jgen, provider);
            }
        }
    }

    @JacksonStdImpl
    public static final class SerializableWithTypeSerializer extends SerializerBase<JsonSerializableWithType> {
        protected static final org.codehaus.jackson.map.ser.StdSerializers.SerializableWithTypeSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.SerializableWithTypeSerializer();
        }

        private SerializableWithTypeSerializer() {
            super(JsonSerializableWithType.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.rawClass(typeHint);
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put("type", schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            return objectNode;
        }

        public void serialize(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen, provider);
        }

        public final void serializeWithType(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            value.serializeWithType(jgen, provider, typeSer);
        }
    }

    @JacksonStdImpl
    public static final class TokenBufferSerializer extends SerializerBase<TokenBuffer> {
        public TokenBufferSerializer() {
            super(TokenBuffer.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("any", true);
        }

        public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen);
        }

        public final void serializeWithType(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForScalar(value, jgen);
            serialize(value, jgen, provider);
            typeSer.writeTypeSuffixForScalar(value, jgen);
        }
    }

    @JacksonStdImpl
    public static final class CalendarSerializer extends ScalarSerializerBase<Calendar> {
        public static final org.codehaus.jackson.map.ser.StdSerializers.CalendarSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.CalendarSerializer();
        }

        public CalendarSerializer() {
            super(Calendar.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? SMSConstant.S_number : "string", true);
        }

        public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            provider.defaultSerializeDateValue(value.getTimeInMillis(), jgen);
        }
    }

    @JacksonStdImpl
    public static final class FloatSerializer extends ScalarSerializerBase<Float> {
        static final org.codehaus.jackson.map.ser.StdSerializers.FloatSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.FloatSerializer();
        }

        public FloatSerializer() {
            super(Float.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(SMSConstant.S_number, true);
        }

        public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.floatValue());
        }
    }

    @JacksonStdImpl
    public static final class IntLikeSerializer extends ScalarSerializerBase<Number> {
        static final org.codehaus.jackson.map.ser.StdSerializers.IntLikeSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.IntLikeSerializer();
        }

        public IntLikeSerializer() {
            super(Number.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }

        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.intValue());
        }
    }

    @JacksonStdImpl
    public static final class LongSerializer extends ScalarSerializerBase<Long> {
        static final org.codehaus.jackson.map.ser.StdSerializers.LongSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.LongSerializer();
        }

        public LongSerializer() {
            super(Long.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(SMSConstant.S_number, true);
        }

        public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.longValue());
        }
    }

    protected static abstract class NonTypedScalarSerializer<T> extends ScalarSerializerBase<T> {
        protected NonTypedScalarSerializer(Class<T> t) {
            super(t);
        }

        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            serialize(value, jgen, provider);
        }
    }

    @JacksonStdImpl
    public static final class NumberSerializer extends ScalarSerializerBase<Number> {
        public static final org.codehaus.jackson.map.ser.StdSerializers.NumberSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.NumberSerializer();
        }

        public NumberSerializer() {
            super(Number.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(SMSConstant.S_number, true);
        }

        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value instanceof BigDecimal) {
                jgen.writeNumber((BigDecimal) value);
            } else if (value instanceof BigInteger) {
                jgen.writeNumber((BigInteger) value);
            } else if (value instanceof Integer) {
                jgen.writeNumber(value.intValue());
            } else if (value instanceof Long) {
                jgen.writeNumber(value.longValue());
            } else if (value instanceof Double) {
                jgen.writeNumber(value.doubleValue());
            } else if (value instanceof Float) {
                jgen.writeNumber(value.floatValue());
            } else if (value instanceof Byte || value instanceof Short) {
                jgen.writeNumber(value.intValue());
            } else {
                jgen.writeNumber(value.toString());
            }
        }
    }

    @JacksonStdImpl
    public static final class SqlDateSerializer extends ScalarSerializerBase<Date> {
        public SqlDateSerializer() {
            super(Date.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }

        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value.toString());
        }
    }

    @JacksonStdImpl
    public static final class SqlTimeSerializer extends ScalarSerializerBase<Time> {
        public SqlTimeSerializer() {
            super(Time.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }

        public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value.toString());
        }
    }

    @JacksonStdImpl
    public static final class UtilDateSerializer extends ScalarSerializerBase<java.util.Date> {
        public static final org.codehaus.jackson.map.ser.StdSerializers.UtilDateSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.UtilDateSerializer();
        }

        public UtilDateSerializer() {
            super(java.util.Date.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? SMSConstant.S_number : "string", true);
        }

        public void serialize(java.util.Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            provider.defaultSerializeDateValue(value, jgen);
        }
    }

    @JacksonStdImpl
    public static final class BooleanSerializer extends NonTypedScalarSerializer<Boolean> {
        final boolean _forPrimitive;

        public BooleanSerializer(boolean forPrimitive) {
            super(Boolean.class);
            this._forPrimitive = forPrimitive;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("boolean", !this._forPrimitive);
        }

        public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeBoolean(value.booleanValue());
        }
    }

    @JacksonStdImpl
    public static final class DoubleSerializer extends NonTypedScalarSerializer<Double> {
        static final org.codehaus.jackson.map.ser.StdSerializers.DoubleSerializer instance;

        static {
            instance = new org.codehaus.jackson.map.ser.StdSerializers.DoubleSerializer();
        }

        public DoubleSerializer() {
            super(Double.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(SMSConstant.S_number, true);
        }

        public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.doubleValue());
        }
    }

    @JacksonStdImpl
    public static final class IntegerSerializer extends NonTypedScalarSerializer<Integer> {
        public IntegerSerializer() {
            super(Integer.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }

        public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.intValue());
        }
    }

    @JacksonStdImpl
    public static final class StringSerializer extends NonTypedScalarSerializer<String> {
        public StringSerializer() {
            super(String.class);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }

        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value);
        }
    }

    protected StdSerializers() {
    }
}