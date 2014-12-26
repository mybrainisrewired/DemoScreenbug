package org.codehaus.jackson.map;

import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerFactory {

    public static abstract class Config {
        public abstract boolean hasKeySerializers();

        public abstract boolean hasSerializerModifiers();

        public abstract boolean hasSerializers();

        public abstract Iterable<Serializers> keySerializers();

        public abstract Iterable<BeanSerializerModifier> serializerModifiers();

        public abstract Iterable<Serializers> serializers();

        public abstract org.codehaus.jackson.map.SerializerFactory.Config withAdditionalKeySerializers(Serializers serializers);

        public abstract org.codehaus.jackson.map.SerializerFactory.Config withAdditionalSerializers(Serializers serializers);

        public abstract org.codehaus.jackson.map.SerializerFactory.Config withSerializerModifier(BeanSerializerModifier beanSerializerModifier);
    }

    public abstract JsonSerializer<Object> createKeySerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonSerializer<Object> createSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonSerializer<Object> createSerializer(JavaType type, SerializationConfig config) {
        try {
            return createSerializer(config, type, null);
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e);
        }
    }

    public abstract TypeSerializer createTypeSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final TypeSerializer createTypeSerializer(JavaType baseType, SerializationConfig config) {
        try {
            return createTypeSerializer(config, baseType, null);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Config getConfig();

    public final SerializerFactory withAdditionalKeySerializers(Serializers additional) {
        return withConfig(getConfig().withAdditionalKeySerializers(additional));
    }

    public final SerializerFactory withAdditionalSerializers(Serializers additional) {
        return withConfig(getConfig().withAdditionalSerializers(additional));
    }

    public abstract SerializerFactory withConfig(Config config);

    public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier) {
        return withConfig(getConfig().withSerializerModifier(modifier));
    }
}