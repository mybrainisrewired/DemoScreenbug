package org.codehaus.jackson.map;

import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.type.JavaType;

public abstract class DeserializerProvider {
    protected DeserializerProvider() {
    }

    public abstract int cachedDeserializersCount();

    @Deprecated
    public final KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType keyType) throws JsonMappingException {
        return findKeyDeserializer(config, keyType, null);
    }

    public abstract KeyDeserializer findKeyDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type) throws JsonMappingException {
        return findTypedValueDeserializer(config, type, null);
    }

    public abstract JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<Object> findValueDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType type, JavaType referrer, String refPropName) throws JsonMappingException {
        return findValueDeserializer(config, type, (BeanProperty) 0);
    }

    public abstract void flushCachedDeserializers();

    public abstract boolean hasValueDeserializerFor(DeserializationConfig deserializationConfig, JavaType javaType);

    public abstract DeserializerProvider withAbstractTypeResolver(AbstractTypeResolver abstractTypeResolver);

    public abstract DeserializerProvider withAdditionalDeserializers(Deserializers deserializers);

    public abstract DeserializerProvider withAdditionalKeyDeserializers(KeyDeserializers keyDeserializers);

    public abstract DeserializerProvider withDeserializerModifier(BeanDeserializerModifier beanDeserializerModifier);
}