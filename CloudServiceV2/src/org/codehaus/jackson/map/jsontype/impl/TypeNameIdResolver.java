package org.codehaus.jackson.map.jsontype.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.type.JavaType;

public class TypeNameIdResolver extends TypeIdResolverBase {
    protected final MapperConfig<?> _config;
    protected final HashMap<String, JavaType> _idToType;
    protected final HashMap<String, String> _typeToId;

    protected TypeNameIdResolver(MapperConfig<?> config, JavaType baseType, HashMap<String, String> typeToId, HashMap<String, JavaType> idToType) {
        super(baseType, config.getTypeFactory());
        this._config = config;
        this._typeToId = typeToId;
        this._idToType = idToType;
    }

    protected static String _defaultTypeId(Class<?> cls) {
        String n = cls.getName();
        int ix = n.lastIndexOf(Opcodes.V1_2);
        return ix < 0 ? n : n.substring(ix + 1);
    }

    public static TypeNameIdResolver construct(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
        if (forSer == forDeser) {
            throw new IllegalArgumentException();
        }
        HashMap<String, String> typeToId = null;
        HashMap<String, JavaType> idToType = null;
        if (forSer) {
            typeToId = new HashMap();
        }
        if (forDeser) {
            idToType = new HashMap();
        }
        if (subtypes != null) {
            Iterator i$ = subtypes.iterator();
            while (i$.hasNext()) {
                NamedType t = (NamedType) i$.next();
                Class<?> cls = t.getType();
                String id = t.hasName() ? t.getName() : _defaultTypeId(cls);
                if (forSer) {
                    typeToId.put(cls.getName(), id);
                }
                if (forDeser) {
                    JavaType prev = (JavaType) idToType.get(id);
                    if (prev == null || !cls.isAssignableFrom(prev.getRawClass())) {
                        idToType.put(id, config.constructType(cls));
                    }
                }
            }
        }
        return new TypeNameIdResolver(config, baseType, typeToId, idToType);
    }

    public Id getMechanism() {
        return Id.NAME;
    }

    public String idFromValue(Object value) {
        String name;
        Class<?> cls = value.getClass();
        String key = cls.getName();
        synchronized (this._typeToId) {
            name = (String) this._typeToId.get(key);
            if (name == null) {
                if (this._config.isAnnotationProcessingEnabled()) {
                    name = this._config.getAnnotationIntrospector().findTypeName(((BasicBeanDescription) this._config.introspectClassAnnotations(cls)).getClassInfo());
                }
                if (name == null) {
                    name = _defaultTypeId(cls);
                }
                this._typeToId.put(key, name);
            }
        }
        return name;
    }

    public String idFromValueAndType(Object value, Class<?> type) {
        return idFromValue(value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(getClass().getName());
        sb.append("; id-to-type=").append(this._idToType);
        sb.append(']');
        return sb.toString();
    }

    public JavaType typeFromId(String id) throws IllegalArgumentException {
        return (JavaType) this._idToType.get(id);
    }
}