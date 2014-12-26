package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class MapSerializer extends ContainerSerializerBase<Map<?, ?>> implements ResolvableSerializer {
    protected static final JavaType UNSPECIFIED_TYPE;
    protected PropertySerializerMap _dynamicValueSerializers;
    protected final HashSet<String> _ignoredEntries;
    protected JsonSerializer<Object> _keySerializer;
    protected final JavaType _keyType;
    protected final BeanProperty _property;
    protected JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final boolean _valueTypeIsStatic;
    protected final TypeSerializer _valueTypeSerializer;

    static {
        UNSPECIFIED_TYPE = TypeFactory.unknownType();
    }

    protected MapSerializer() {
        this((HashSet) null, null, null, false, null, null, null, null);
    }

    @Deprecated
    protected MapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<Object> keySerializer, BeanProperty property) {
        this(ignoredEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, null, property);
    }

    protected MapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, BeanProperty property) {
        super(Map.class, false);
        this._property = property;
        this._ignoredEntries = ignoredEntries;
        this._keyType = keyType;
        this._valueType = valueType;
        this._valueTypeIsStatic = valueTypeIsStatic;
        this._valueTypeSerializer = vts;
        this._keySerializer = keySerializer;
        this._valueSerializer = valueSerializer;
        this._dynamicValueSerializers = PropertySerializerMap.emptyMap();
    }

    @Deprecated
    protected MapSerializer(HashSet<String> ignoredEntries, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts) {
        this(ignoredEntries, UNSPECIFIED_TYPE, valueType, valueTypeIsStatic, vts, null, null, null);
    }

    @Deprecated
    public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, BeanProperty property) {
        return construct(ignoredList, mapType, staticValueType, vts, property, null, null);
    }

    public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer) {
        JavaType valueType;
        JavaType keyType;
        HashSet<String> ignoredEntries = toSet(ignoredList);
        if (mapType == null) {
            valueType = UNSPECIFIED_TYPE;
            keyType = valueType;
        } else {
            keyType = mapType.getKeyType();
            valueType = mapType.getContentType();
        }
        if (!staticValueType) {
            staticValueType = valueType != null && valueType.isFinal();
        }
        return new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, keySerializer, valueSerializer, property);
    }

    private static HashSet<String> toSet(String[] ignoredEntries) {
        if (ignoredEntries == null || ignoredEntries.length == 0) {
            return null;
        }
        HashSet<String> result = new HashSet(ignoredEntries.length);
        String[] arr$ = ignoredEntries;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            result.add(arr$[i$]);
            i$++;
        }
        return result;
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
        if (map != result.map) {
            this._dynamicValueSerializers = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
        if (map != result.map) {
            this._dynamicValueSerializers = result.map;
        }
        return result.serializer;
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        MapSerializer ms = new MapSerializer(this._ignoredEntries, this._keyType, this._valueType, this._valueTypeIsStatic, vts, this._keySerializer, this._valueSerializer, this._property);
        if (this._valueSerializer != null) {
            ms._valueSerializer = this._valueSerializer;
        }
        return ms;
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("object", true);
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._valueTypeIsStatic && this._valueSerializer == null) {
            this._valueSerializer = provider.findValueSerializer(this._valueType, this._property);
        }
        if (this._keySerializer == null) {
            this._keySerializer = provider.findKeySerializer(this._keyType, this._property);
        }
    }

    public void serialize(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        if (!value.isEmpty()) {
            if (this._valueSerializer != null) {
                serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
            } else {
                serializeFields(value, jgen, provider);
            }
        }
        jgen.writeEndObject();
    }

    protected void serializeFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._valueTypeSerializer != null) {
            serializeTypedFields(value, jgen, provider);
        } else {
            JsonSerializer<Object> keySerializer = this._keySerializer;
            HashSet<String> ignored = this._ignoredEntries;
            SerializerProvider serializerProvider = provider;
            boolean skipNulls = !serializerProvider.isEnabled(Feature.WRITE_NULL_MAP_VALUES);
            PropertySerializerMap serializers = this._dynamicValueSerializers;
            Iterator i$ = value.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<?, ?> entry = (Entry) i$.next();
                Object valueElem = entry.getValue();
                Object keyElem = entry.getKey();
                if (keyElem == null) {
                    provider.getNullKeySerializer().serialize(null, jgen, provider);
                } else if (!(skipNulls && valueElem == null)) {
                    if (ignored == null || !ignored.contains(keyElem)) {
                        keySerializer.serialize(keyElem, jgen, provider);
                    }
                }
                if (valueElem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    Class cc = valueElem.getClass();
                    JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                    if (serializer == null) {
                        if (this._valueType.hasGenericTypes()) {
                            serializer = _findAndAddDynamic(serializers, this._valueType.forcedNarrowBy(cc), provider);
                        } else {
                            serializer = _findAndAddDynamic(serializers, cc, provider);
                        }
                        serializers = this._dynamicValueSerializers;
                    }
                    try {
                        serializer.serialize(valueElem, jgen, provider);
                    } catch (Exception e) {
                        wrapAndThrow(provider, e, value, "" + keyElem);
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void serializeFieldsUsing(java.util.Map<?, ?> r16_value, org.codehaus.jackson.JsonGenerator r17_jgen, org.codehaus.jackson.map.SerializerProvider r18_provider, org.codehaus.jackson.map.JsonSerializer<java.lang.Object> r19_ser) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.MapSerializer.serializeFieldsUsing(java.util.Map, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider, org.codehaus.jackson.map.JsonSerializer):void");
        /*
        r15 = this;
        r9 = r15._keySerializer;
        r6 = r15._ignoredEntries;
        r11 = r15._valueTypeSerializer;
        r13 = org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_NULL_MAP_VALUES;
        r0 = r18;
        r13 = r0.isEnabled(r13);
        if (r13 != 0) goto L_0x0045;
    L_0x0010:
        r10 = 1;
    L_0x0011:
        r13 = r16.entrySet();
        r5 = r13.iterator();
    L_0x0019:
        r13 = r5.hasNext();
        if (r13 == 0) goto L_0x008d;
    L_0x001f:
        r4 = r5.next();
        r4 = (java.util.Map.Entry) r4;
        r12 = r4.getValue();
        r8 = r4.getKey();
        if (r8 != 0) goto L_0x0047;
    L_0x002f:
        r13 = r18.getNullKeySerializer();
        r14 = 0;
        r0 = r17;
        r1 = r18;
        r13.serialize(r14, r0, r1);
    L_0x003b:
        if (r12 != 0) goto L_0x005b;
    L_0x003d:
        r0 = r18;
        r1 = r17;
        r0.defaultSerializeNull(r1);
        goto L_0x0019;
    L_0x0045:
        r10 = 0;
        goto L_0x0011;
    L_0x0047:
        if (r10 == 0) goto L_0x004b;
    L_0x0049:
        if (r12 == 0) goto L_0x0019;
    L_0x004b:
        if (r6 == 0) goto L_0x0053;
    L_0x004d:
        r13 = r6.contains(r8);
        if (r13 != 0) goto L_0x0019;
    L_0x0053:
        r0 = r17;
        r1 = r18;
        r9.serialize(r8, r0, r1);
        goto L_0x003b;
    L_0x005b:
        if (r11 != 0) goto L_0x0083;
    L_0x005d:
        r0 = r19;
        r1 = r17;
        r2 = r18;
        r0.serialize(r12, r1, r2);	 Catch:{ Exception -> 0x0067 }
        goto L_0x0019;
    L_0x0067:
        r3 = move-exception;
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r14 = "";
        r13 = r13.append(r14);
        r13 = r13.append(r8);
        r7 = r13.toString();
        r0 = r18;
        r1 = r16;
        r15.wrapAndThrow(r0, r3, r1, r7);
        goto L_0x0019;
    L_0x0083:
        r0 = r19;
        r1 = r17;
        r2 = r18;
        r0.serializeWithType(r12, r1, r2, r11);	 Catch:{ Exception -> 0x0067 }
        goto L_0x0019;
    L_0x008d:
        return;
        */
    }

    protected void serializeTypedFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        JsonSerializer<Object> keySerializer = this._keySerializer;
        JsonSerializer<Object> prevValueSerializer = null;
        Class<?> prevValueClass = null;
        HashSet<String> ignored = this._ignoredEntries;
        boolean skipNulls = !provider.isEnabled(Feature.WRITE_NULL_MAP_VALUES);
        Iterator i$ = value.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<?, ?> entry = (Entry) i$.next();
            Object valueElem = entry.getValue();
            Object keyElem = entry.getKey();
            if (keyElem == null) {
                provider.getNullKeySerializer().serialize(null, jgen, provider);
            } else if (!(skipNulls && valueElem == null)) {
                if (ignored == null || !ignored.contains(keyElem)) {
                    keySerializer.serialize(keyElem, jgen, provider);
                }
            }
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                JsonSerializer<Object> currSerializer;
                Class<?> cc = valueElem.getClass();
                if (cc == prevValueClass) {
                    currSerializer = prevValueSerializer;
                } else {
                    currSerializer = provider.findValueSerializer((Class)cc, this._property);
                    prevValueSerializer = currSerializer;
                    prevValueClass = cc;
                }
                try {
                    currSerializer.serializeWithType(valueElem, jgen, provider, this._valueTypeSerializer);
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, "" + keyElem);
                }
            }
        }
    }

    public void serializeWithType(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForObject(value, jgen);
        if (!value.isEmpty()) {
            if (this._valueSerializer != null) {
                serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
            } else {
                serializeFields(value, jgen, provider);
            }
        }
        typeSer.writeTypeSuffixForObject(value, jgen);
    }
}