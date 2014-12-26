package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public final class ContainerSerializers {

    public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T> implements ResolvableSerializer {
        protected PropertySerializerMap _dynamicSerializers;
        protected JsonSerializer<Object> _elementSerializer;
        protected final JavaType _elementType;
        protected final BeanProperty _property;
        protected final boolean _staticTyping;
        protected final TypeSerializer _valueTypeSerializer;

        @Deprecated
        protected AsArraySerializer(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            this(cls, et, staticTyping, vts, property, null);
        }

        protected AsArraySerializer(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> elementSerializer) {
            boolean z = false;
            super(cls, false);
            this._elementType = et;
            if (staticTyping || (et != null && et.isFinal())) {
                z = true;
            }
            this._staticTyping = z;
            this._valueTypeSerializer = vts;
            this._property = property;
            this._elementSerializer = elementSerializer;
            this._dynamicSerializers = PropertySerializerMap.emptyMap();
        }

        protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class type, SerializerProvider provider) throws JsonMappingException {
            SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return result.serializer;
        }

        protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
            SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return result.serializer;
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode o = createSchemaNode("array", true);
            JavaType contentType = null;
            if (typeHint != null) {
                contentType = provider.constructType(typeHint).getContentType();
                if (contentType == null && typeHint instanceof ParameterizedType) {
                    Type[] typeArgs = ((ParameterizedType) typeHint).getActualTypeArguments();
                    if (typeArgs.length == 1) {
                        contentType = provider.constructType(typeArgs[0]);
                    }
                }
            }
            if (contentType == null && this._elementType != null) {
                contentType = this._elementType;
            }
            if (contentType != null) {
                JsonNode schemaNode = null;
                if (contentType.getRawClass() != Object.class) {
                    JsonSerializer<Object> ser = provider.findValueSerializer(contentType, this._property);
                    if (ser instanceof SchemaAware) {
                        schemaNode = ((SchemaAware) ser).getSchema(provider, null);
                    }
                }
                if (schemaNode == null) {
                    schemaNode = JsonSchema.getDefaultSchemaNode();
                }
                o.put("items", schemaNode);
            }
            return o;
        }

        public void resolve(SerializerProvider provider) throws JsonMappingException {
            if (this._staticTyping && this._elementType != null && this._elementSerializer == null) {
                this._elementSerializer = provider.findValueSerializer(this._elementType, this._property);
            }
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
    public static class CollectionSerializer extends org.codehaus.jackson.map.ser.ContainerSerializers.AsArraySerializer<Collection<?>> {
        @Deprecated
        public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            this(elemType, staticTyping, vts, property, null);
        }

        public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
            super(Collection.class, elemType, staticTyping, vts, property, valueSerializer);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ContainerSerializers.CollectionSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            } else {
                Iterator<?> it = value.iterator();
                if (it.hasNext()) {
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    TypeSerializer typeSer = this._valueTypeSerializer;
                    int i = 0;
                    do {
                        try {
                            Object elem = it.next();
                            if (elem == null) {
                                provider.defaultSerializeNull(jgen);
                            } else {
                                Class<?> cc = elem.getClass();
                                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                                if (serializer == null) {
                                    if (this._elementType.hasGenericTypes()) {
                                        serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                                    } else {
                                        serializer = _findAndAddDynamic(serializers, cc, provider);
                                    }
                                    serializers = this._dynamicSerializers;
                                }
                                if (typeSer == null) {
                                    serializer.serialize(elem, jgen, provider);
                                } else {
                                    serializer.serializeWithType(elem, jgen, provider, typeSer);
                                }
                            }
                            i++;
                        } catch (Exception e) {
                            wrapAndThrow(provider, e, value, i);
                        }
                    } while (it.hasNext());
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void serializeContentsUsing(java.util.Collection<?> r7_value, org.codehaus.jackson.JsonGenerator r8_jgen, org.codehaus.jackson.map.SerializerProvider r9_provider, org.codehaus.jackson.map.JsonSerializer<java.lang.Object> r10_ser) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.ContainerSerializers.CollectionSerializer.serializeContentsUsing(java.util.Collection, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider, org.codehaus.jackson.map.JsonSerializer):void");
            /*
            r6 = this;
            r3 = r7.iterator();
            r5 = r3.hasNext();
            if (r5 == 0) goto L_0x001e;
        L_0x000a:
            r4 = r6._valueTypeSerializer;
            r2 = 0;
        L_0x000d:
            r1 = r3.next();
            if (r1 != 0) goto L_0x001f;
        L_0x0013:
            r9.defaultSerializeNull(r8);	 Catch:{ Exception -> 0x0025 }
        L_0x0016:
            r2 = r2 + 1;
        L_0x0018:
            r5 = r3.hasNext();
            if (r5 != 0) goto L_0x000d;
        L_0x001e:
            return;
        L_0x001f:
            if (r4 != 0) goto L_0x002a;
        L_0x0021:
            r10.serialize(r1, r8, r9);	 Catch:{ Exception -> 0x0025 }
            goto L_0x0016;
        L_0x0025:
            r0 = move-exception;
            r6.wrapAndThrow(r9, r0, r7, r2);
            goto L_0x0018;
        L_0x002a:
            r10.serializeWithType(r1, r8, r9, r4);	 Catch:{ Exception -> 0x0025 }
            goto L_0x0016;
            */
        }
    }

    public static class EnumSetSerializer extends org.codehaus.jackson.map.ser.ContainerSerializers.AsArraySerializer<EnumSet<? extends Enum<?>>> {
        public EnumSetSerializer(JavaType elemType, BeanProperty property) {
            super(EnumSet.class, elemType, true, null, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            JsonSerializer<Object> enumSer = this._elementSerializer;
            Iterator i$ = value.iterator();
            while (i$.hasNext()) {
                Enum<?> en = (Enum) i$.next();
                if (enumSer == null) {
                    enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
                }
                enumSer.serialize(en, jgen, provider);
            }
        }
    }

    @JacksonStdImpl
    public static class IndexedListSerializer extends org.codehaus.jackson.map.ser.ContainerSerializers.AsArraySerializer<List<?>> {
        public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
            super(List.class, elemType, staticTyping, vts, property, valueSerializer);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ContainerSerializers.IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
        }

        public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            } else if (this._valueTypeSerializer != null) {
                serializeTypedContents(value, jgen, provider);
            } else {
                int len = value.size();
                if (len != 0) {
                    int i = 0;
                    try {
                        PropertySerializerMap serializers = this._dynamicSerializers;
                        while (i < len) {
                            Object elem = value.get(i);
                            if (elem == null) {
                                provider.defaultSerializeNull(jgen);
                            } else {
                                Class<?> cc = elem.getClass();
                                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                                if (serializer == null) {
                                    if (this._elementType.hasGenericTypes()) {
                                        serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                                    } else {
                                        serializer = _findAndAddDynamic(serializers, cc, provider);
                                    }
                                    serializers = this._dynamicSerializers;
                                }
                                serializer.serialize(elem, jgen, provider);
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        wrapAndThrow(provider, e, value, i);
                    }
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void serializeContentsUsing(java.util.List<?> r6_value, org.codehaus.jackson.JsonGenerator r7_jgen, org.codehaus.jackson.map.SerializerProvider r8_provider, org.codehaus.jackson.map.JsonSerializer<java.lang.Object> r9_ser) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.ContainerSerializers.IndexedListSerializer.serializeContentsUsing(java.util.List, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider, org.codehaus.jackson.map.JsonSerializer):void");
            /*
            r5 = this;
            r3 = r6.size();
            if (r3 != 0) goto L_0x0007;
        L_0x0006:
            return;
        L_0x0007:
            r4 = r5._valueTypeSerializer;
            r2 = 0;
        L_0x000a:
            if (r2 >= r3) goto L_0x0006;
        L_0x000c:
            r1 = r6.get(r2);
            if (r1 != 0) goto L_0x0018;
        L_0x0012:
            r8.defaultSerializeNull(r7);	 Catch:{ Exception -> 0x001e }
        L_0x0015:
            r2 = r2 + 1;
            goto L_0x000a;
        L_0x0018:
            if (r4 != 0) goto L_0x0023;
        L_0x001a:
            r9.serialize(r1, r7, r8);	 Catch:{ Exception -> 0x001e }
            goto L_0x0015;
        L_0x001e:
            r0 = move-exception;
            r5.wrapAndThrow(r8, r0, r6, r2);
            goto L_0x0015;
        L_0x0023:
            r9.serializeWithType(r1, r7, r8, r4);	 Catch:{ Exception -> 0x001e }
            goto L_0x0015;
            */
        }

        public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int len = value.size();
            if (len != 0) {
                int i = 0;
                try {
                    TypeSerializer typeSer = this._valueTypeSerializer;
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        Object elem = value.get(i);
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            Class<?> cc = elem.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, cc, provider);
                                }
                                serializers = this._dynamicSerializers;
                            }
                            serializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                        i++;
                    }
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, i);
                }
            }
        }
    }

    @JacksonStdImpl
    public static class IterableSerializer extends org.codehaus.jackson.map.ser.ContainerSerializers.AsArraySerializer<Iterable<?>> {
        public IterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Iterable.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ContainerSerializers.IterableSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Iterable<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    Object elem = it.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        JsonSerializer<Object> currSerializer;
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class)cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        } else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (it.hasNext());
            }
        }
    }

    @JacksonStdImpl
    public static class IteratorSerializer extends org.codehaus.jackson.map.ser.ContainerSerializers.AsArraySerializer<Iterator<?>> {
        public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Iterator.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new org.codehaus.jackson.map.ser.ContainerSerializers.IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    Object elem = value.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        JsonSerializer<Object> currSerializer;
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class)cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        } else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (value.hasNext());
            }
        }
    }

    private ContainerSerializers() {
    }

    public static ContainerSerializerBase<?> collectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        return new CollectionSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }

    public static JsonSerializer<?> enumSetSerializer(JavaType enumType, BeanProperty property) {
        return new EnumSetSerializer(enumType, property);
    }

    public static ContainerSerializerBase<?> indexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        return new IndexedListSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }

    public static ContainerSerializerBase<?> iterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IterableSerializer(elemType, staticTyping, vts, property);
    }

    public static ContainerSerializerBase<?> iteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IteratorSerializer(elemType, staticTyping, vts, property);
    }
}