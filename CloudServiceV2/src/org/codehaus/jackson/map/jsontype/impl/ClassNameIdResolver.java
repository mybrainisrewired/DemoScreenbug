package org.codehaus.jackson.map.jsontype.impl;

import java.util.EnumMap;
import java.util.EnumSet;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;

public class ClassNameIdResolver extends TypeIdResolverBase {
    public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory) {
        super(baseType, typeFactory);
    }

    protected final String _idFrom(Object value, Class<?> cls) {
        if (Enum.class.isAssignableFrom(cls) && !cls.isEnum()) {
            cls = cls.getSuperclass();
        }
        String str = cls.getName();
        if (!str.startsWith("java.util")) {
            return str;
        }
        if (value instanceof EnumSet) {
            return TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, ClassUtil.findEnumType((EnumSet) value)).toCanonical();
        } else if (value instanceof EnumMap) {
            return TypeFactory.defaultInstance().constructMapType(EnumMap.class, ClassUtil.findEnumType((EnumMap) value), Object.class).toCanonical();
        } else {
            String end = str.substring(Type.ARRAY);
            return ((end.startsWith(".Arrays$") || end.startsWith(".Collections$")) && str.indexOf("List") >= 0) ? "java.util.ArrayList" : str;
        }
    }

    public Id getMechanism() {
        return Id.CLASS;
    }

    public String idFromValue(Object value) {
        return _idFrom(value, value.getClass());
    }

    public String idFromValueAndType(Object value, Class<?> type) {
        return _idFrom(value, type);
    }

    public void registerSubtype(Class<?> type, String name) {
    }

    public JavaType typeFromId(String id) {
        if (id.indexOf(60) > 0) {
            return TypeFactory.fromCanonical(id);
        }
        try {
            return this._typeFactory.constructSpecializedType(this._baseType, Class.forName(id, true, Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found");
        } catch (Exception e2) {
            Exception e3 = e2;
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e3.getMessage(), e3);
        }
    }
}