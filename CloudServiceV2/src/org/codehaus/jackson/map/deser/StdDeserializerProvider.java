package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.ContextualKeyDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class StdDeserializerProvider extends DeserializerProvider {
    static final HashMap<JavaType, KeyDeserializer> _keyDeserializers;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
    protected DeserializerFactory _factory;
    protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers;

    protected static final class WrappedDeserializer extends JsonDeserializer<Object> {
        final JsonDeserializer<Object> _deserializer;
        final TypeDeserializer _typeDeserializer;

        public WrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<Object> deser) {
            this._typeDeserializer = typeDeser;
            this._deserializer = deser;
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return this._deserializer.deserializeWithType(jp, ctxt, this._typeDeserializer);
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
        }
    }

    static {
        _keyDeserializers = StdKeyDeserializers.constructAll();
    }

    public StdDeserializerProvider() {
        this(BeanDeserializerFactory.instance);
    }

    public StdDeserializerProvider(DeserializerFactory f) {
        this._cachedDeserializers = new ConcurrentHashMap(64, 0.75f, 2);
        this._incompleteDeserializers = new HashMap(8);
        this._factory = f;
    }

    protected JsonDeserializer<Object> _createAndCache2(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> jsonDeserializer = null;
        try {
            JsonDeserializer<Object> deser = _createDeserializer(config, type, property);
            if (deser == null) {
                return jsonDeserializer;
            }
            boolean isResolvable = deser instanceof ResolvableDeserializer;
            boolean addToCache = deser.getClass() == BeanDeserializer.class;
            if (!addToCache && config.isEnabled(Feature.USE_ANNOTATIONS)) {
                AnnotationIntrospector aintr = config.getAnnotationIntrospector();
                Boolean cacheAnn = aintr.findCachability(AnnotatedClass.construct(deser.getClass(), aintr, jsonDeserializer));
                if (cacheAnn != null) {
                    addToCache = cacheAnn.booleanValue();
                }
            }
            if (isResolvable) {
                this._incompleteDeserializers.put(type, deser);
                _resolveDeserializer(config, (ResolvableDeserializer) deser);
                this._incompleteDeserializers.remove(type);
            }
            if (!addToCache) {
                return deser;
            }
            this._cachedDeserializers.put(type, deser);
            return deser;
        } catch (IllegalArgumentException e) {
            IllegalArgumentException iae = e;
            throw new JsonMappingException(iae.getMessage(), jsonDeserializer, iae);
        }
    }

    protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        synchronized (this._incompleteDeserializers) {
            int count;
            try {
                JsonDeserializer<Object> deser = _findCachedDeserializer(type);
                if (deser != null) {
                    return deser;
                } else {
                    count = this._incompleteDeserializers.size();
                    if (count > 0) {
                        deser = this._incompleteDeserializers.get(type);
                        if (deser != null) {
                            return deser;
                        }
                    }
                    JsonDeserializer<Object> _createAndCache2 = _createAndCache2(config, type, property);
                    if (count == 0) {
                        if (this._incompleteDeserializers.size() > 0) {
                            this._incompleteDeserializers.clear();
                        }
                    }
                    return _createAndCache2;
                }
            } catch (Throwable th) {
            }
        }
    }

    protected JsonDeserializer<Object> _createDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (type.isEnumType()) {
            return this._factory.createEnumDeserializer(config, this, type, property);
        }
        if (type.isContainerType() && type.isArrayType()) {
            return this._factory.createArrayDeserializer(config, this, (ArrayType) type, property);
        }
        if (type.isMapLikeType()) {
            MapLikeType mlt = (MapLikeType) type;
            return mlt.isTrueMapType() ? this._factory.createMapDeserializer(config, this, (MapType) mlt, property) : this._factory.createMapLikeDeserializer(config, this, mlt, property);
        } else if (type.isCollectionLikeType()) {
            CollectionLikeType clt = (CollectionLikeType) type;
            return clt.isTrueCollectionType() ? this._factory.createCollectionDeserializer(config, this, (CollectionType) clt, property) : this._factory.createCollectionLikeDeserializer(config, this, clt, property);
        }
        return JsonNode.class.isAssignableFrom(type.getRawClass()) ? this._factory.createTreeDeserializer(config, this, type, property) : this._factory.createBeanDeserializer(config, this, type, property);
    }

    protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type) {
        return (JsonDeserializer) this._cachedDeserializers.get(type);
    }

    protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type) throws JsonMappingException {
        throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
    }

    protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type) throws JsonMappingException {
        if (ClassUtil.isConcrete(type.getRawClass())) {
            throw new JsonMappingException("Can not find a Value deserializer for type " + type);
        }
        throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
    }

    protected void _resolveDeserializer(DeserializationConfig config, ResolvableDeserializer ser) throws JsonMappingException {
        ser.resolve(config, this);
    }

    public int cachedDeserializersCount() {
        return this._cachedDeserializers.size();
    }

    public KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        KeyDeserializer kd = this._factory.createKeyDeserializer(config, type, property);
        if (kd == null) {
            Class<?> raw = type.getRawClass();
            if (raw == String.class || raw == Object.class) {
                return null;
            }
            KeyDeserializer kdes = (KeyDeserializer) _keyDeserializers.get(type);
            if (kdes != null) {
                return kdes;
            }
            if (type.isEnumType()) {
                return StdKeyDeserializers.constructEnumKeyDeserializer(config, type);
            }
            kdes = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
            if (kdes != null) {
                return kdes;
            }
            if (kd == null) {
                return _handleUnknownKeyDeserializer(type);
            }
        }
        if (kd instanceof ContextualKeyDeserializer) {
            kd = ((ContextualKeyDeserializer) kd).createContextual(config, property);
        }
        return kd;
    }

    public JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = findValueDeserializer(config, type, property);
        TypeDeserializer typeDeser = this._factory.findTypeDeserializer(config, type, property);
        return typeDeser != null ? new WrappedDeserializer(typeDeser, deser) : deser;
    }

    public JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType propertyType, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
        if (deser != null) {
            if (deser instanceof ContextualDeserializer) {
                deser = ((ContextualDeserializer) deser).createContextual(config, property);
            }
            return deser;
        } else {
            deser = _createAndCacheValueDeserializer(config, propertyType, property);
            if (deser == null) {
                deser = _handleUnknownValueDeserializer(propertyType);
            }
            if (deser instanceof ContextualDeserializer) {
                deser = ((ContextualDeserializer) deser).createContextual(config, property);
            }
            return deser;
        }
    }

    public void flushCachedDeserializers() {
        this._cachedDeserializers.clear();
    }

    public boolean hasValueDeserializerFor(DeserializationConfig config, JavaType type) {
        JsonDeserializer<Object> deser = _findCachedDeserializer(type);
        if (deser == null) {
            try {
                deser = _createAndCacheValueDeserializer(config, type, null);
            } catch (Exception e) {
                return false;
            }
        }
        return deser != null;
    }

    public DeserializerProvider withAbstractTypeResolver(AbstractTypeResolver resolver) {
        this._factory = this._factory.withAbstractTypeResolver(resolver);
        return this;
    }

    public DeserializerProvider withAdditionalDeserializers(Deserializers d) {
        this._factory = this._factory.withAdditionalDeserializers(d);
        return this;
    }

    public DeserializerProvider withAdditionalKeyDeserializers(KeyDeserializers d) {
        this._factory = this._factory.withAdditionalKeyDeserializers(d);
        return this;
    }

    public DeserializerProvider withDeserializerModifier(BeanDeserializerModifier modifier) {
        this._factory = this._factory.withDeserializerModifier(modifier);
        return this;
    }
}