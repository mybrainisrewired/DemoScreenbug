package org.codehaus.jackson.map.ser;

import java.util.HashMap;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap;
import org.codehaus.jackson.type.JavaType;

public final class SerializerCache {
    private ReadOnlyClassToSerializerMap _readOnlyMap;
    private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap;

    public static final class TypeKey {
        protected Class<?> _class;
        protected int _hashCode;
        protected boolean _isTyped;
        protected JavaType _type;

        public TypeKey(Class key, boolean typed) {
            this._class = key;
            this._type = null;
            this._isTyped = typed;
            this._hashCode = hash(key, typed);
        }

        public TypeKey(JavaType key, boolean typed) {
            this._type = key;
            this._class = null;
            this._isTyped = typed;
            this._hashCode = hash(key, typed);
        }

        private static final int hash(Class<?> cls, boolean typed) {
            int hash = cls.getName().hashCode();
            return typed ? hash + 1 : hash;
        }

        private static final int hash(JavaType type, boolean typed) {
            int hash = type.hashCode() - 1;
            return typed ? hash - 1 : hash;
        }

        public final boolean equals(org.codehaus.jackson.map.ser.SerializerCache.TypeKey o) {
            if (o == this) {
                return true;
            }
            org.codehaus.jackson.map.ser.SerializerCache.TypeKey other = o;
            if (other._isTyped != this._isTyped) {
                return false;
            }
            if (this._class != null) {
                return other._class == this._class;
            } else {
                return this._type.equals(other._type);
            }
        }

        public final int hashCode() {
            return this._hashCode;
        }

        public void resetTyped(Class cls) {
            this._type = null;
            this._class = cls;
            this._isTyped = true;
            this._hashCode = hash(cls, true);
        }

        public void resetTyped(JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = true;
            this._hashCode = hash(type, true);
        }

        public void resetUntyped(Class cls) {
            this._type = null;
            this._class = cls;
            this._isTyped = false;
            this._hashCode = hash(cls, false);
        }

        public void resetUntyped(JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = false;
            this._hashCode = hash(type, false);
        }

        public final String toString() {
            return this._class != null ? "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}" : "{type: " + this._type + ", typed? " + this._isTyped + "}";
        }
    }

    public SerializerCache() {
        this._sharedMap = new HashMap(64);
        this._readOnlyMap = null;
    }

    public void addAndResolveNonTypedSerializer(Class type, JsonSerializer<Object> ser, SerializerProvider provider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(type, false), ser) == null) {
                this._readOnlyMap = null;
            }
            if (ser instanceof ResolvableSerializer) {
                ((ResolvableSerializer) ser).resolve(provider);
            }
        }
    }

    public void addAndResolveNonTypedSerializer(JavaType type, JsonSerializer<Object> ser, SerializerProvider provider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(type, false), ser) == null) {
                this._readOnlyMap = null;
            }
            if (ser instanceof ResolvableSerializer) {
                ((ResolvableSerializer) ser).resolve(provider);
            }
        }
    }

    public void addTypedSerializer(Class cls, JsonSerializer<Object> ser) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(cls, true), ser) == null) {
                this._readOnlyMap = null;
            }
        }
    }

    public void addTypedSerializer(JavaType type, JsonSerializer<Object> ser) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(type, true), ser) == null) {
                this._readOnlyMap = null;
            }
        }
    }

    public synchronized void flush() {
        this._sharedMap.clear();
    }

    public ReadOnlyClassToSerializerMap getReadOnlyLookupMap() {
        ReadOnlyClassToSerializerMap m;
        synchronized (this) {
            m = this._readOnlyMap;
            if (m == null) {
                m = ReadOnlyClassToSerializerMap.from(this._sharedMap);
                this._readOnlyMap = m;
            }
        }
        return m.instance();
    }

    public synchronized int size() {
        return this._sharedMap.size();
    }

    public JsonSerializer<Object> typedValueSerializer(Class cls) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(cls, true));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> typedValueSerializer(JavaType type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(type, true));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> untypedValueSerializer(Class type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(type, false));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> untypedValueSerializer(JavaType type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(type, false));
        }
        return jsonSerializer;
    }
}