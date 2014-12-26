package org.codehaus.jackson.map.module;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;

public class SimpleSerializers implements Serializers {
    protected HashMap<ClassKey, JsonSerializer<?>> _classMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;

    public SimpleSerializers() {
        this._classMappings = null;
        this._interfaceMappings = null;
    }

    private void _addSerializer(Class<?> cls, JsonSerializer<?> ser) {
        ClassKey key = new ClassKey(cls);
        if (cls.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap();
            }
            this._interfaceMappings.put(key, ser);
        } else {
            if (this._classMappings == null) {
                this._classMappings = new HashMap();
            }
            this._classMappings.put(key, ser);
        }
    }

    protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key) {
        Class[] arr$ = cls.getInterfaces();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Class<?> iface = arr$[i$];
            key.reset(iface);
            JsonSerializer<?> ser = (JsonSerializer) this._interfaceMappings.get(key);
            if (ser != null) {
                return ser;
            }
            ser = _findInterfaceMapping(iface, key);
            if (ser != null) {
                return ser;
            }
            i$++;
        }
        return null;
    }

    public <T> void addSerializer(Class<? extends T> type, JsonSerializer<T> ser) {
        _addSerializer(type, ser);
    }

    public void addSerializer(JsonSerializer<?> ser) {
        Class<?> cls = ser.handledType();
        if (cls == null || cls == Object.class) {
            throw new IllegalArgumentException("JsonSerializer of type " + ser.getClass().getName() + " does not define valid handledType() (use alternative registration method?)");
        }
        _addSerializer(cls, ser);
    }

    public JsonSerializer<?> findArraySerializer(SerializationConfig config, ArrayType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    public JsonSerializer<?> findCollectionLikeSerializer(SerializationConfig config, CollectionLikeType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    public JsonSerializer<?> findCollectionSerializer(SerializationConfig config, CollectionType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    public JsonSerializer<?> findMapLikeSerializer(SerializationConfig config, MapLikeType type, BeanDescription beanDesc, BeanProperty property, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    public JsonSerializer<?> findMapSerializer(SerializationConfig config, MapType type, BeanDescription beanDesc, BeanProperty property, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.codehaus.jackson.map.JsonSerializer<?> findSerializer(org.codehaus.jackson.map.SerializationConfig r6_config, org.codehaus.jackson.type.JavaType r7_type, org.codehaus.jackson.map.BeanDescription r8_beanDesc, org.codehaus.jackson.map.BeanProperty r9_property) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.module.SimpleSerializers.findSerializer(org.codehaus.jackson.map.SerializationConfig, org.codehaus.jackson.type.JavaType, org.codehaus.jackson.map.BeanDescription, org.codehaus.jackson.map.BeanProperty):org.codehaus.jackson.map.JsonSerializer<?>");
        /*
        r5 = this;
        r0 = r7.getRawClass();
        r2 = new org.codehaus.jackson.map.type.ClassKey;
        r2.<init>(r0);
        r3 = 0;
        r4 = r0.isInterface();
        if (r4 == 0) goto L_0x0020;
    L_0x0010:
        r4 = r5._interfaceMappings;
        if (r4 == 0) goto L_0x0047;
    L_0x0014:
        r4 = r5._interfaceMappings;
        r3 = r4.get(r2);
        r3 = (org.codehaus.jackson.map.JsonSerializer) r3;
        if (r3 == 0) goto L_0x0047;
    L_0x001e:
        r4 = r3;
    L_0x001f:
        return r4;
    L_0x0020:
        r4 = r5._classMappings;
        if (r4 == 0) goto L_0x0047;
    L_0x0024:
        r4 = r5._classMappings;
        r3 = r4.get(r2);
        r3 = (org.codehaus.jackson.map.JsonSerializer) r3;
        if (r3 == 0) goto L_0x0030;
    L_0x002e:
        r4 = r3;
        goto L_0x001f;
    L_0x0030:
        r1 = r0;
    L_0x0031:
        if (r1 == 0) goto L_0x0047;
    L_0x0033:
        r2.reset(r1);
        r4 = r5._classMappings;
        r3 = r4.get(r2);
        r3 = (org.codehaus.jackson.map.JsonSerializer) r3;
        if (r3 == 0) goto L_0x0042;
    L_0x0040:
        r4 = r3;
        goto L_0x001f;
    L_0x0042:
        r1 = r1.getSuperclass();
        goto L_0x0031;
    L_0x0047:
        r4 = r5._interfaceMappings;
        if (r4 == 0) goto L_0x0067;
    L_0x004b:
        r3 = r5._findInterfaceMapping(r0, r2);
        if (r3 == 0) goto L_0x0053;
    L_0x0051:
        r4 = r3;
        goto L_0x001f;
    L_0x0053:
        r4 = r0.isInterface();
        if (r4 != 0) goto L_0x0067;
    L_0x0059:
        r0 = r0.getSuperclass();
        if (r0 == 0) goto L_0x0067;
    L_0x005f:
        r3 = r5._findInterfaceMapping(r0, r2);
        if (r3 == 0) goto L_0x0059;
    L_0x0065:
        r4 = r3;
        goto L_0x001f;
    L_0x0067:
        r4 = 0;
        goto L_0x001f;
        */
    }
}