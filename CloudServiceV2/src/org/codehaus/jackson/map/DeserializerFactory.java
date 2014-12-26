package org.codehaus.jackson.map;

import java.lang.reflect.Type;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public abstract class DeserializerFactory {
    protected static final Deserializers[] NO_DESERIALIZERS;

    public static abstract class Config {
        public abstract Iterable<AbstractTypeResolver> abstractTypeResolvers();

        public abstract Iterable<BeanDeserializerModifier> deserializerModifiers();

        public abstract Iterable<Deserializers> deserializers();

        public abstract boolean hasAbstractTypeResolvers();

        public abstract boolean hasDeserializerModifiers();

        public abstract boolean hasDeserializers();

        public abstract boolean hasKeyDeserializers();

        public abstract Iterable<KeyDeserializers> keyDeserializers();

        public abstract org.codehaus.jackson.map.DeserializerFactory.Config withAbstractTypeResolver(AbstractTypeResolver abstractTypeResolver);

        public abstract org.codehaus.jackson.map.DeserializerFactory.Config withAdditionalDeserializers(Deserializers deserializers);

        public abstract org.codehaus.jackson.map.DeserializerFactory.Config withAdditionalKeyDeserializers(KeyDeserializers keyDeserializers);

        public abstract org.codehaus.jackson.map.DeserializerFactory.Config withDeserializerModifier(BeanDeserializerModifier beanDeserializerModifier);
    }

    static {
        NO_DESERIALIZERS = new Deserializers[0];
    }

    public abstract JsonDeserializer<?> createArrayDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, ArrayType arrayType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, ArrayType type, DeserializerProvider p) throws JsonMappingException {
        return createArrayDeserializer(config, p, type, null);
    }

    public abstract JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, JavaType type, DeserializerProvider p) throws JsonMappingException {
        return createBeanDeserializer(config, p, type, null);
    }

    public abstract JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, CollectionType collectionType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, CollectionType type, DeserializerProvider p) throws JsonMappingException {
        return createCollectionDeserializer(config, p, type, null);
    }

    public abstract JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, CollectionLikeType collectionLikeType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, Type enumClass, DeserializerProvider p) throws JsonMappingException {
        return createEnumDeserializer(config, p, TypeFactory.type(enumClass), null);
    }

    public abstract JsonDeserializer<?> createEnumDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public KeyDeserializer createKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        return null;
    }

    public abstract JsonDeserializer<?> createMapDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, MapType mapType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, MapType type, DeserializerProvider p) throws JsonMappingException {
        return createMapDeserializer(config, p, type, null);
    }

    public abstract JsonDeserializer<?> createMapLikeDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, MapLikeType mapLikeType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, Type nodeClass, DeserializerProvider p) throws JsonMappingException {
        return createTreeDeserializer(config, p, TypeFactory.type(nodeClass), null);
    }

    public abstract JsonDeserializer<?> createTreeDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    @Deprecated
    public final TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType) {
        return findTypeDeserializer(config, baseType, null);
    }

    public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property) {
        return null;
    }

    public abstract Config getConfig();

    public final DeserializerFactory withAbstractTypeResolver(AbstractTypeResolver resolver) {
        return withConfig(getConfig().withAbstractTypeResolver(resolver));
    }

    public final DeserializerFactory withAdditionalDeserializers(Deserializers additional) {
        return withConfig(getConfig().withAdditionalDeserializers(additional));
    }

    public final DeserializerFactory withAdditionalKeyDeserializers(KeyDeserializers additional) {
        return withConfig(getConfig().withAdditionalKeyDeserializers(additional));
    }

    public abstract DeserializerFactory withConfig(Config config);

    public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier modifier) {
        return withConfig(getConfig().withDeserializerModifier(modifier));
    }
}