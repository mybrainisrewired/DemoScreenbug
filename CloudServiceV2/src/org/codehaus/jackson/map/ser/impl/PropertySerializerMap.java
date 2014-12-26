package org.codehaus.jackson.map.ser.impl;

import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.type.JavaType;

public abstract class PropertySerializerMap {

    public static final class SerializerAndMapResult {
        public final PropertySerializerMap map;
        public final JsonSerializer<Object> serializer;

        public SerializerAndMapResult(JsonSerializer<Object> serializer, PropertySerializerMap map) {
            this.serializer = serializer;
            this.map = map;
        }
    }

    private static final class TypeAndSerializer {
        public final JsonSerializer<Object> serializer;
        public final Class<?> type;

        public TypeAndSerializer(Class<?> type, JsonSerializer<Object> serializer) {
            this.type = type;
            this.serializer = serializer;
        }
    }

    private static final class Double extends PropertySerializerMap {
        private final JsonSerializer<Object> _serializer1;
        private final JsonSerializer<Object> _serializer2;
        private final Class<?> _type1;
        private final Class<?> _type2;

        public Double(Class<?> type1, JsonSerializer<Object> serializer1, Class<?> type2, JsonSerializer<Object> serializer2) {
            this._type1 = type1;
            this._serializer1 = serializer1;
            this._type2 = type2;
            this._serializer2 = serializer2;
        }

        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            return new Multi(new TypeAndSerializer[]{new TypeAndSerializer(this._type1, this._serializer1), new TypeAndSerializer(this._type2, this._serializer2)});
        }

        public JsonSerializer<Object> serializerFor(Class<?> type) {
            if (type == this._type1) {
                return this._serializer1;
            }
            return type == this._type2 ? this._serializer2 : null;
        }
    }

    private static final class Empty extends PropertySerializerMap {
        protected static final Empty instance;

        static {
            instance = new Empty();
        }

        private Empty() {
        }

        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            return new Single(type, serializer);
        }

        public JsonSerializer<Object> serializerFor(Class<?> type) {
            return null;
        }
    }

    private static final class Multi extends PropertySerializerMap {
        private static final int MAX_ENTRIES = 8;
        private final TypeAndSerializer[] _entries;

        public Multi(TypeAndSerializer[] entries) {
            this._entries = entries;
        }

        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            int len = this._entries.length;
            if (len == 8) {
                return this;
            }
            TypeAndSerializer[] entries = new TypeAndSerializer[(len + 1)];
            System.arraycopy(this._entries, 0, entries, 0, len);
            entries[len] = new TypeAndSerializer(type, serializer);
            this(entries);
            return this;
        }

        public JsonSerializer<Object> serializerFor(Class<?> type) {
            int i = 0;
            int len = this._entries.length;
            while (i < len) {
                TypeAndSerializer entry = this._entries[i];
                if (entry.type == type) {
                    return entry.serializer;
                }
                i++;
            }
            return null;
        }
    }

    private static final class Single extends PropertySerializerMap {
        private final JsonSerializer<Object> _serializer;
        private final Class<?> _type;

        public Single(Class<?> type, JsonSerializer<Object> serializer) {
            this._type = type;
            this._serializer = serializer;
        }

        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            return new Double(this._type, this._serializer, type, serializer);
        }

        public JsonSerializer<Object> serializerFor(Class<?> type) {
            return type == this._type ? this._serializer : null;
        }
    }

    public static PropertySerializerMap emptyMap() {
        return Empty.instance;
    }

    public final SerializerAndMapResult findAndAddSerializer(Class type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
        return new SerializerAndMapResult(serializer, newWith(type, serializer));
    }

    public final SerializerAndMapResult findAndAddSerializer(JavaType type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
        return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
    }

    protected abstract PropertySerializerMap newWith(Class<?> cls, JsonSerializer<Object> jsonSerializer);

    public abstract JsonSerializer<Object> serializerFor(Class<?> cls);
}