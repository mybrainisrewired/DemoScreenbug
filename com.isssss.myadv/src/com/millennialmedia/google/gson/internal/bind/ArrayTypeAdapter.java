package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.internal._$Gson.Types;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY;
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    static {
        FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Type type = typeToken.getType();
                if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class) type).isArray())) {
                    return null;
                }
                Type componentType = Types.getArrayComponentType(type);
                return new ArrayTypeAdapter(gson, gson.getAdapter(TypeToken.get(componentType)), Types.getRawType(componentType));
            }
        };
    }

    public ArrayTypeAdapter(Gson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }

    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            List<E> list = new ArrayList();
            in.beginArray();
            while (in.hasNext()) {
                list.add(this.componentTypeAdapter.read(in));
            }
            in.endArray();
            Object array = Array.newInstance(this.componentType, list.size());
            int i = 0;
            while (i < list.size()) {
                Array.set(array, i, list.get(i));
                i++;
            }
            return array;
        }
    }

    public void write(JsonWriter out, Object array) throws IOException {
        if (array == null) {
            out.nullValue();
        } else {
            out.beginArray();
            int i = 0;
            int length = Array.getLength(array);
            while (i < length) {
                this.componentTypeAdapter.write(out, Array.get(array, i));
                i++;
            }
            out.endArray();
        }
    }
}