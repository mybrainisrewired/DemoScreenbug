package org.codehaus.jackson.map.ser;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.TimeZone;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualSerializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer.None;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.ArraySerializers.BooleanArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.ByteArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.CharArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.DoubleArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.FloatArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.IntArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.LongArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.ShortArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.StringArraySerializer;
import org.codehaus.jackson.map.ser.StdSerializers.BooleanSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.CalendarSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.DoubleSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.FloatSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.IntLikeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.IntegerSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.LongSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.NumberSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SerializableSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SerializableWithTypeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SqlDateSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SqlTimeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.StringSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.TokenBufferSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.UtilDateSerializer;
import org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer;
import org.codehaus.jackson.map.ser.impl.InetAddressSerializer;
import org.codehaus.jackson.map.ser.impl.ObjectArraySerializer;
import org.codehaus.jackson.map.ser.impl.StringCollectionSerializer;
import org.codehaus.jackson.map.ser.impl.TimeZoneSerializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

public abstract class BasicSerializerFactory extends SerializerFactory {
    protected static final HashMap<String, JsonSerializer<?>> _arraySerializers;
    protected static final HashMap<String, JsonSerializer<?>> _concrete;
    protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy;

    static {
        _concrete = new HashMap();
        _concreteLazy = new HashMap();
        _concrete.put(String.class.getName(), new StringSerializer());
        ToStringSerializer sls = ToStringSerializer.instance;
        _concrete.put(StringBuffer.class.getName(), sls);
        _concrete.put(StringBuilder.class.getName(), sls);
        _concrete.put(Character.class.getName(), sls);
        _concrete.put(Character.TYPE.getName(), sls);
        _concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
        _concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
        JsonSerializer<?> intS = new IntegerSerializer();
        _concrete.put(Integer.class.getName(), intS);
        _concrete.put(Integer.TYPE.getName(), intS);
        _concrete.put(Long.class.getName(), LongSerializer.instance);
        _concrete.put(Long.TYPE.getName(), LongSerializer.instance);
        _concrete.put(Byte.class.getName(), IntLikeSerializer.instance);
        _concrete.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        _concrete.put(Short.class.getName(), IntLikeSerializer.instance);
        _concrete.put(Short.TYPE.getName(), IntLikeSerializer.instance);
        _concrete.put(Float.class.getName(), FloatSerializer.instance);
        _concrete.put(Float.TYPE.getName(), FloatSerializer.instance);
        _concrete.put(Double.class.getName(), DoubleSerializer.instance);
        _concrete.put(Double.TYPE.getName(), DoubleSerializer.instance);
        JsonSerializer<?> ns = new NumberSerializer();
        _concrete.put(BigInteger.class.getName(), ns);
        _concrete.put(BigDecimal.class.getName(), ns);
        _concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
        _concrete.put(Date.class.getName(), UtilDateSerializer.instance);
        _concrete.put(java.sql.Date.class.getName(), new SqlDateSerializer());
        _concrete.put(Time.class.getName(), new SqlTimeSerializer());
        _concrete.put(Timestamp.class.getName(), UtilDateSerializer.instance);
        Iterator i$ = new JdkSerializers().provide().iterator();
        while (i$.hasNext()) {
            Entry<Class<?>, Object> en = (Entry) i$.next();
            Object value = en.getValue();
            if (value instanceof JsonSerializer) {
                _concrete.put(((Class) en.getKey()).getName(), (JsonSerializer) value);
            } else if (value instanceof Class) {
                _concreteLazy.put(((Class) en.getKey()).getName(), (Class) value);
            } else {
                throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
            }
        }
        _concreteLazy.put(TokenBuffer.class.getName(), TokenBufferSerializer.class);
        _arraySerializers = new HashMap();
        _arraySerializers.put(boolean[].class.getName(), new BooleanArraySerializer());
        _arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
        _arraySerializers.put(char[].class.getName(), new CharArraySerializer());
        _arraySerializers.put(short[].class.getName(), new ShortArraySerializer());
        _arraySerializers.put(int[].class.getName(), new IntArraySerializer());
        _arraySerializers.put(long[].class.getName(), new LongArraySerializer());
        _arraySerializers.put(float[].class.getName(), new FloatArraySerializer());
        _arraySerializers.put(double[].class.getName(), new DoubleArraySerializer());
    }

    protected BasicSerializerFactory() {
    }

    protected static JsonSerializer<Object> findContentSerializer(SerializationConfig config, Annotated a, BeanProperty property) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<? extends JsonSerializer<?>> serClass = intr.findContentSerializer(a);
        if ((serClass == null || serClass == None.class) && property != null) {
            serClass = intr.findContentSerializer(property.getMember());
        }
        return (serClass == null || serClass == None.class) ? null : config.serializerInstance(a, serClass);
    }

    protected static JsonSerializer<Object> findKeySerializer(SerializationConfig config, Annotated a, BeanProperty property) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<? extends JsonSerializer<?>> serClass = intr.findKeySerializer(a);
        if ((serClass == null || serClass == None.class) && property != null) {
            serClass = intr.findKeySerializer(property.getMember());
        }
        return (serClass == null || serClass == None.class) ? null : config.serializerInstance(a, serClass);
    }

    protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(SerializationConfig config, Annotated a, T type) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (!type.isContainerType()) {
            return type;
        }
        Class<?> keyClass = intr.findSerializationKeyType(a, type.getKeyType());
        if (keyClass == null || type instanceof MapType) {
            try {
                type = ((MapType) type).widenKey(keyClass);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Illegal key-type annotation: type " + type + " is not a Map type");
        }
        Class<?> cc = intr.findSerializationContentType(a, type.getContentType());
        if (cc == null) {
            return type;
        }
        try {
            return type.widenContentsBy(cc);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + e2.getMessage());
        }
    }

    protected JsonSerializer<?> buildArraySerializer(SerializationConfig config, ArrayType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Class<?> raw = type.getRawClass();
        if (String[].class == raw) {
            return new StringArraySerializer(property);
        }
        JsonSerializer<?> ser = (JsonSerializer) _arraySerializers.get(raw.getName());
        return ser == null ? new ObjectArraySerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer) : ser;
    }

    protected JsonSerializer<?> buildCollectionLikeSerializer(SerializationConfig config, CollectionLikeType type, BeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Iterator i$ = customSerializers().iterator();
        while (i$.hasNext()) {
            JsonSerializer<?> ser = ((Serializers) i$.next()).findCollectionLikeSerializer(config, type, beanDesc, property, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }

    protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig config, CollectionType type, BeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Iterator i$ = customSerializers().iterator();
        while (i$.hasNext()) {
            JsonSerializer<?> ser = ((Serializers) i$.next()).findCollectionSerializer(config, type, beanDesc, property, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        Class<?> raw = type.getRawClass();
        if (EnumSet.class.isAssignableFrom(raw)) {
            return buildEnumSetSerializer(config, type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
        }
        Class<?> elementRaw = type.getContentType().getRawClass();
        return isIndexedList(raw) ? elementRaw == String.class ? new IndexedStringListSerializer(property) : ContainerSerializers.indexedListSerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer) : elementRaw == String.class ? new StringCollectionSerializer(property) : ContainerSerializers.collectionSerializer(type.getContentType(), staticTyping, elementTypeSerializer, property, elementValueSerializer);
    }

    public JsonSerializer<?> buildContainerSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        TypeSerializer elementTypeSerializer = createTypeSerializer(config, type.getContentType(), property);
        if (elementTypeSerializer != null) {
            staticTyping = false;
        } else if (!staticTyping) {
            staticTyping = usesStaticTyping(config, beanDesc, elementTypeSerializer, property);
        }
        JsonSerializer<Object> elementValueSerializer = findContentSerializer(config, beanDesc.getClassInfo(), property);
        if (type.isMapLikeType()) {
            MapLikeType mlt = (MapLikeType) type;
            JsonSerializer<Object> keySerializer = findKeySerializer(config, beanDesc.getClassInfo(), property);
            if (!mlt.isTrueMapType()) {
                return buildMapLikeSerializer(config, mlt, beanDesc, property, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
            }
            return buildMapSerializer(config, (MapType) mlt, beanDesc, property, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
        } else if (type.isCollectionLikeType()) {
            CollectionLikeType clt = (CollectionLikeType) type;
            if (!clt.isTrueCollectionType()) {
                return buildCollectionLikeSerializer(config, clt, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
            }
            return buildCollectionSerializer(config, (CollectionType) clt, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
        } else if (!type.isArrayType()) {
            return null;
        } else {
            return buildArraySerializer(config, (ArrayType) type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer);
        }
    }

    protected JsonSerializer<?> buildEnumMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        JavaType keyType = type.getKeyType();
        EnumValues enumValues = null;
        if (keyType.isEnumType()) {
            enumValues = EnumValues.construct(keyType.getRawClass(), config.getAnnotationIntrospector());
        }
        return new EnumMapSerializer(type.getContentType(), staticTyping, enumValues, elementTypeSerializer, property, elementValueSerializer);
    }

    protected JsonSerializer<?> buildEnumSetSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        JavaType enumType = type.getContentType();
        if (!enumType.isEnumType()) {
            enumType = null;
        }
        return ContainerSerializers.enumSetSerializer(enumType, property);
    }

    protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.unknownType();
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iterableSerializer(valueType, usesStaticTyping(config, beanDesc, vts, property), vts, property);
    }

    protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.unknownType();
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iteratorSerializer(valueType, usesStaticTyping(config, beanDesc, vts, property), vts, property);
    }

    protected JsonSerializer<?> buildMapLikeSerializer(SerializationConfig config, MapLikeType type, BeanDescription beanDesc, BeanProperty property, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Iterator i$ = customSerializers().iterator();
        while (i$.hasNext()) {
            JsonSerializer<?> ser = ((Serializers) i$.next()).findMapLikeSerializer(config, type, beanDesc, property, keySerializer, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }

    protected JsonSerializer<?> buildMapSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        Iterator i$ = customSerializers().iterator();
        while (i$.hasNext()) {
            JsonSerializer<?> ser = ((Serializers) i$.next()).findMapSerializer(config, type, beanDesc, property, keySerializer, elementTypeSerializer, elementValueSerializer);
            if (ser != null) {
                return ser;
            }
        }
        return EnumMap.class.isAssignableFrom(type.getRawClass()) ? buildEnumMapSerializer(config, type, beanDesc, property, staticTyping, elementTypeSerializer, elementValueSerializer) : MapSerializer.construct(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()), type, staticTyping, elementTypeSerializer, property, keySerializer, elementValueSerializer);
    }

    public abstract JsonSerializer<Object> createSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public TypeSerializer createTypeSerializer(MapperConfig config, JavaType baseType, BeanProperty property) {
        AnnotatedClass ac = ((BasicBeanDescription) config.introspectClassAnnotations(baseType.getRawClass())).getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
        } else {
            subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
        }
        return b == null ? null : b.buildTypeSerializer(config, baseType, subtypes, property);
    }

    protected abstract Iterable<Serializers> customSerializers();

    public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) throws JsonMappingException {
        Class<?> type = javaType.getRawClass();
        if (Iterator.class.isAssignableFrom(type)) {
            return buildIteratorSerializer(config, javaType, beanDesc, property, staticTyping);
        }
        if (Iterable.class.isAssignableFrom(type)) {
            return buildIterableSerializer(config, javaType, beanDesc, property, staticTyping);
        }
        return CharSequence.class.isAssignableFrom(type) ? ToStringSerializer.instance : null;
    }

    public final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) {
        String clsName = type.getRawClass().getName();
        JsonSerializer<?> ser = (JsonSerializer) _concrete.get(clsName);
        if (ser != null) {
            return ser;
        }
        Class<? extends JsonSerializer<?>> serClass = (Class) _concreteLazy.get(clsName);
        if (serClass == null) {
            return null;
        }
        try {
            return (JsonSerializer) serClass.newInstance();
        } catch (Exception e) {
            Exception e2 = e;
            throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e2.getMessage(), e2);
        }
    }

    public final JsonSerializer<?> findSerializerByPrimaryType(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property, boolean staticTyping) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (JsonSerializable.class.isAssignableFrom(raw)) {
            return JsonSerializableWithType.class.isAssignableFrom(raw) ? SerializableWithTypeSerializer.instance : SerializableSerializer.instance;
        } else {
            AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
            if (valueMethod != null) {
                Method m = valueMethod.getAnnotated();
                if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                    ClassUtil.checkAndFixAccess(m);
                }
                return new JsonValueSerializer(m, findSerializerFromAnnotation(config, valueMethod, property), property);
            } else if (InetAddress.class.isAssignableFrom(raw)) {
                return InetAddressSerializer.instance;
            } else {
                if (TimeZone.class.isAssignableFrom(raw)) {
                    return TimeZoneSerializer.instance;
                }
                if (Number.class.isAssignableFrom(raw)) {
                    return NumberSerializer.instance;
                }
                if (Enum.class.isAssignableFrom(raw)) {
                    return EnumSerializer.construct(raw, config, beanDesc);
                }
                if (Calendar.class.isAssignableFrom(raw)) {
                    return CalendarSerializer.instance;
                }
                return Date.class.isAssignableFrom(raw) ? UtilDateSerializer.instance : null;
            }
        }
    }

    protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated a, BeanProperty property) throws JsonMappingException {
        Object serDef = config.getAnnotationIntrospector().findSerializer(a);
        if (serDef == null) {
            return null;
        }
        JsonSerializer<Object> ser;
        if (serDef instanceof JsonSerializer) {
            ser = (JsonSerializer) serDef;
            return ser instanceof ContextualSerializer ? ((ContextualSerializer) ser).createContextual(config, property) : ser;
        } else if (serDef instanceof Class) {
            Class<?> cls = (Class) serDef;
            if (JsonSerializer.class.isAssignableFrom(cls)) {
                ser = config.serializerInstance(a, cls);
                return ser instanceof ContextualSerializer ? ((ContextualSerializer) ser).createContextual(config, property) : ser;
            } else {
                throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonSerializer>");
            }
        } else {
            throw new IllegalStateException("AnnotationIntrospector returned value of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
        }
    }

    public final JsonSerializer<?> getNullSerializer() {
        return NullSerializer.instance;
    }

    protected boolean isIndexedList(Class<?> cls) {
        return RandomAccess.class.isAssignableFrom(cls);
    }

    protected <T extends JavaType> T modifyTypeByAnnotation(SerializationConfig config, Annotated a, T type) {
        Class<?> superclass = config.getAnnotationIntrospector().findSerializationType(a);
        if (superclass != null) {
            try {
                type = type.widenBy(superclass);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to widen type " + type + " with concrete-type annotation (value " + superclass.getName() + "), method '" + a.getName() + "': " + e.getMessage());
            }
        }
        return modifySecondaryTypesByAnnotation(config, a, type);
    }

    protected boolean usesStaticTyping(SerializationConfig config, BasicBeanDescription beanDesc, TypeSerializer typeSer, BeanProperty property) {
        if (typeSer != null) {
            return false;
        }
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Typing t = intr.findSerializationTyping(beanDesc.getClassInfo());
        if (t != null) {
            if (t == Typing.STATIC) {
                return true;
            }
        } else if (config.isEnabled(Feature.USE_STATIC_TYPING)) {
            return true;
        }
        if (property == null) {
            return false;
        }
        JavaType type = property.getType();
        if (!type.isContainerType()) {
            return false;
        }
        if (intr.findSerializationContentType(property.getMember(), property.getType()) != null) {
            return true;
        }
        return type instanceof MapType && intr.findSerializationKeyType(property.getMember(), property.getType()) != null;
    }
}