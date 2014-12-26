package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.JsonElement;
import com.millennialmedia.google.gson.JsonPrimitive;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.internal.ConstructorConstructor;
import com.millennialmedia.google.gson.internal.JsonReaderInternalAccess;
import com.millennialmedia.google.gson.internal.ObjectConstructor;
import com.millennialmedia.google.gson.internal.Streams;
import com.millennialmedia.google.gson.internal._$Gson.Types;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class MapTypeAdapterFactory implements TypeAdapterFactory {
    private final boolean complexMapKeySerialization;
    private final ConstructorConstructor constructorConstructor;

    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>> {
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;

        public Adapter(Gson context, Type keyType, TypeAdapter<K> keyTypeAdapter, Type valueType, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<? extends Map<K, V>> constructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, keyTypeAdapter, keyType);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, valueTypeAdapter, valueType);
            this.constructor = constructor;
        }

        private String keyToString(JsonElement keyElement) {
            if (keyElement.isJsonPrimitive()) {
                JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return String.valueOf(primitive.getAsNumber());
                }
                if (primitive.isBoolean()) {
                    return Boolean.toString(primitive.getAsBoolean());
                }
                if (primitive.isString()) {
                    return primitive.getAsString();
                }
                throw new AssertionError();
            } else if (keyElement.isJsonNull()) {
                return "null";
            } else {
                throw new AssertionError();
            }
        }

        public Map<K, V> read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                Map<K, V> map = (Map) this.constructor.construct();
                K key;
                if (peek == JsonToken.BEGIN_ARRAY) {
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginArray();
                        key = this.keyTypeAdapter.read(in);
                        if (map.put(key, this.valueTypeAdapter.read(in)) != null) {
                            throw new JsonSyntaxException("duplicate key: " + key);
                        }
                        in.endArray();
                    }
                    in.endArray();
                    return map;
                } else {
                    in.beginObject();
                    while (in.hasNext()) {
                        JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
                        key = this.keyTypeAdapter.read(in);
                        if (map.put(key, this.valueTypeAdapter.read(in)) != null) {
                            throw new JsonSyntaxException("duplicate key: " + key);
                        }
                    }
                    in.endObject();
                    return map;
                }
            }
        }

        public void write(JsonWriter out, Map<K, V> map) throws IOException {
            if (map == null) {
                out.nullValue();
            } else if (MapTypeAdapterFactory.this.complexMapKeySerialization) {
                boolean hasComplexKeys = false;
                List<JsonElement> keys = new ArrayList(map.size());
                List<V> values = new ArrayList(map.size());
                i$ = map.entrySet().iterator();
                while (i$.hasNext()) {
                    entry = i$.next();
                    JsonElement keyElement = this.keyTypeAdapter.toJsonTree(entry.getKey());
                    keys.add(keyElement);
                    values.add(entry.getValue());
                    int i = (keyElement.isJsonArray() || keyElement.isJsonObject()) ? 1 : 0;
                    hasComplexKeys |= i;
                }
                int i2;
                if (hasComplexKeys) {
                    out.beginArray();
                    i2 = 0;
                    while (i2 < keys.size()) {
                        out.beginArray();
                        Streams.write((JsonElement) keys.get(i2), out);
                        this.valueTypeAdapter.write(out, values.get(i2));
                        out.endArray();
                        i2++;
                    }
                    out.endArray();
                } else {
                    out.beginObject();
                    i2 = 0;
                    while (i2 < keys.size()) {
                        out.name(keyToString(keys.get(i2)));
                        this.valueTypeAdapter.write(out, values.get(i2));
                        i2++;
                    }
                    out.endObject();
                }
            } else {
                out.beginObject();
                i$ = map.entrySet().iterator();
                while (i$.hasNext()) {
                    entry = (Entry) i$.next();
                    out.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(out, entry.getValue());
                }
                out.endObject();
            }
        }
    }

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }

    private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
        return (keyType == Boolean.TYPE || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (!Map.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        Type[] keyAndValueTypes = Types.getMapKeyAndValueTypes(type, Types.getRawType(type));
        return new Adapter(gson, keyAndValueTypes[0], getKeyAdapter(gson, keyAndValueTypes[0]), keyAndValueTypes[1], gson.getAdapter(TypeToken.get(keyAndValueTypes[1])), this.constructorConstructor.get(typeToken));
    }
}