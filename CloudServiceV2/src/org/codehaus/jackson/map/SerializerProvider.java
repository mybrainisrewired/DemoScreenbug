package org.codehaus.jackson.map;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerProvider {
    protected static final JavaType TYPE_OBJECT;
    protected final SerializationConfig _config;
    protected final Class<?> _serializationView;

    static {
        TYPE_OBJECT = TypeFactory.defaultInstance().uncheckedSimpleType(Object.class);
    }

    protected SerializerProvider(SerializationConfig config) {
        this._config = config;
        this._serializationView = config == null ? null : this._config.getSerializationView();
    }

    public abstract int cachedSerializersCount();

    public JavaType constructType(Type type) {
        return this._config.getTypeFactory().constructType(type);
    }

    public abstract void defaultSerializeDateValue(long j, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void defaultSerializeDateValue(Date date, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public final void defaultSerializeField(String fieldName, Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeFieldName(fieldName);
        if (value == null) {
            getNullValueSerializer().serialize(null, jgen, this);
        } else {
            findTypedValueSerializer(value.getClass(), true).serialize(value, jgen, this);
        }
    }

    public final void defaultSerializeNull(JsonGenerator jgen) throws IOException, JsonProcessingException {
        getNullValueSerializer().serialize(null, jgen, this);
    }

    public final void defaultSerializeValue(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        if (value == null) {
            getNullValueSerializer().serialize(null, jgen, this);
        } else {
            findTypedValueSerializer(value.getClass(), true).serialize(value, jgen, this);
        }
    }

    public abstract JsonSerializer<Object> findKeySerializer(JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonSerializer<Object> findTypedValueSerializer(Class valueType, boolean cache) throws JsonMappingException {
        return findTypedValueSerializer(valueType, cache, null);
    }

    public abstract JsonSerializer<Object> findTypedValueSerializer(Class<?> cls, boolean z, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache) throws JsonMappingException {
        return findTypedValueSerializer(valueType, cache, null);
    }

    public abstract JsonSerializer<Object> findTypedValueSerializer(JavaType javaType, boolean z, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonSerializer<Object> findValueSerializer(Class runtimeType) throws JsonMappingException {
        return findValueSerializer(runtimeType, null);
    }

    public abstract JsonSerializer<Object> findValueSerializer(Class<?> cls, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonSerializer<Object> findValueSerializer(JavaType serializationType) throws JsonMappingException {
        return findValueSerializer(serializationType, null);
    }

    public abstract JsonSerializer<Object> findValueSerializer(JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract void flushCachedSerializers();

    public abstract JsonSchema generateJsonSchema(Class<?> cls, SerializationConfig serializationConfig, SerializerFactory serializerFactory) throws JsonMappingException;

    public final SerializationConfig getConfig() {
        return this._config;
    }

    public final FilterProvider getFilterProvider() {
        return this._config.getFilterProvider();
    }

    @Deprecated
    public final JsonSerializer<Object> getKeySerializer() throws JsonMappingException {
        return findKeySerializer(TYPE_OBJECT, null);
    }

    @Deprecated
    public final JsonSerializer<Object> getKeySerializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
        return findKeySerializer(valueType, property);
    }

    public abstract JsonSerializer<Object> getNullKeySerializer();

    public abstract JsonSerializer<Object> getNullValueSerializer();

    public final Class<?> getSerializationView() {
        return this._serializationView;
    }

    public abstract JsonSerializer<Object> getUnknownTypeSerializer(Class<?> cls);

    public abstract boolean hasSerializerFor(SerializationConfig serializationConfig, Class<?> cls, SerializerFactory serializerFactory);

    public final boolean isEnabled(Feature feature) {
        return this._config.isEnabled(feature);
    }

    public abstract void serializeValue(SerializationConfig serializationConfig, JsonGenerator jsonGenerator, Object obj, SerializerFactory serializerFactory) throws IOException, JsonGenerationException;

    public abstract void serializeValue(SerializationConfig serializationConfig, JsonGenerator jsonGenerator, Object obj, JavaType javaType, SerializerFactory serializerFactory) throws IOException, JsonGenerationException;

    public abstract void setDefaultKeySerializer(JsonSerializer<Object> jsonSerializer);

    public abstract void setNullKeySerializer(JsonSerializer<Object> jsonSerializer);

    public abstract void setNullValueSerializer(JsonSerializer<Object> jsonSerializer);
}