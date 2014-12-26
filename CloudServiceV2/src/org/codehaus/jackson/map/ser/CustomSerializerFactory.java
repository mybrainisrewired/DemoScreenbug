package org.codehaus.jackson.map.ser;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class CustomSerializerFactory extends BeanSerializerFactory {
    protected HashMap<ClassKey, JsonSerializer<?>> _directClassMappings;
    protected JsonSerializer<?> _enumSerializerOverride;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _transitiveClassMappings;

    public CustomSerializerFactory() {
        this(null);
    }

    public CustomSerializerFactory(Config config) {
        super(config);
        this._directClassMappings = null;
        this._transitiveClassMappings = null;
        this._interfaceMappings = null;
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

    public <T> void addGenericMapping(Class<? extends T> type, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(type);
        if (type.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap();
            }
            this._interfaceMappings.put(key, ser);
        } else {
            if (this._transitiveClassMappings == null) {
                this._transitiveClassMappings = new HashMap();
            }
            this._transitiveClassMappings.put(key, ser);
        }
    }

    public <T> void addSpecificMapping(Class<? extends T> forClass, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(forClass);
        if (forClass.isInterface()) {
            throw new IllegalArgumentException("Can not add specific mapping for an interface (" + forClass.getName() + ")");
        } else if (Modifier.isAbstract(forClass.getModifiers())) {
            throw new IllegalArgumentException("Can not add specific mapping for an abstract class (" + forClass.getName() + ")");
        } else {
            if (this._directClassMappings == null) {
                this._directClassMappings = new HashMap();
            }
            this._directClassMappings.put(key, ser);
        }
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = findCustomSerializer(type.getRawClass(), config);
        return ser != null ? ser : super.createSerializer(config, type, property);
    }

    protected JsonSerializer<?> findCustomSerializer(Class<?> type, SerializationConfig config) {
        JsonSerializer<?> ser;
        ClassKey key = new ClassKey(type);
        if (this._directClassMappings != null) {
            ser = this._directClassMappings.get(key);
            if (ser != null) {
                return ser;
            }
        }
        if (type.isEnum() && this._enumSerializerOverride != null) {
            return this._enumSerializerOverride;
        }
        Class<?> curr;
        if (this._transitiveClassMappings != null) {
            curr = type;
            while (curr != null) {
                key.reset(curr);
                ser = this._transitiveClassMappings.get(key);
                if (ser != null) {
                    return ser;
                }
                curr = curr.getSuperclass();
            }
        }
        if (this._interfaceMappings != null) {
            key.reset(type);
            ser = this._interfaceMappings.get(key);
            if (ser != null) {
                return ser;
            }
            curr = type;
            while (curr != null) {
                ser = _findInterfaceMapping(curr, key);
                if (ser != null) {
                    return ser;
                }
                curr = curr.getSuperclass();
            }
        }
        return null;
    }

    public void setEnumSerializer(JsonSerializer<?> enumSer) {
        this._enumSerializerOverride = enumSer;
    }

    public SerializerFactory withConfig(Config config) {
        if (getClass() == CustomSerializerFactory.class) {
            return new CustomSerializerFactory(config);
        }
        throw new IllegalStateException("Subtype of CustomSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
    }
}