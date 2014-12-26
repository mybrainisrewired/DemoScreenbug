package com.millennialmedia.google.gson.internal.bind;

import com.millennialmedia.google.gson.FieldNamingStrategy;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.annotations.SerializedName;
import com.millennialmedia.google.gson.internal.ConstructorConstructor;
import com.millennialmedia.google.gson.internal.Excluder;
import com.millennialmedia.google.gson.internal.ObjectConstructor;
import com.millennialmedia.google.gson.internal.Primitives;
import com.millennialmedia.google.gson.internal._$Gson.Types;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        protected BoundField(String name, boolean serialized, boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }

        abstract void read(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;
    }

    class AnonymousClass_1 extends BoundField {
        final TypeAdapter<?> typeAdapter;
        final /* synthetic */ Gson val$context;
        final /* synthetic */ Field val$field;
        final /* synthetic */ TypeToken val$fieldType;
        final /* synthetic */ boolean val$isPrimitive;

        AnonymousClass_1(String x0, boolean x1, boolean x2, Gson gson, TypeToken typeToken, Field field, boolean z) {
            this.val$context = gson;
            this.val$fieldType = typeToken;
            this.val$field = field;
            this.val$isPrimitive = z;
            super(x0, x1, x2);
            this.typeAdapter = this.val$context.getAdapter(this.val$fieldType);
        }

        void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
            Object fieldValue = this.typeAdapter.read(reader);
            if (fieldValue != null || !this.val$isPrimitive) {
                this.val$field.set(value, fieldValue);
            }
        }

        void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            new TypeAdapterRuntimeTypeWrapper(this.val$context, this.typeAdapter, this.val$fieldType.getType()).write(writer, this.val$field.get(value));
        }
    }

    public static final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        private Adapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }

        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                T instance = this.constructor.construct();
                try {
                    in.beginObject();
                    while (in.hasNext()) {
                        BoundField field = (BoundField) this.boundFields.get(in.nextName());
                        if (field == null || !field.deserialized) {
                            in.skipValue();
                        } else {
                            field.read(in, instance);
                        }
                    }
                    in.endObject();
                    return instance;
                } catch (IllegalStateException e) {
                    throw new JsonSyntaxException(e);
                } catch (IllegalAccessException e2) {
                    throw new AssertionError(e2);
                }
            }
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();
                try {
                    Iterator i$ = this.boundFields.values().iterator();
                    while (i$.hasNext()) {
                        BoundField boundField = (BoundField) i$.next();
                        if (boundField.serialized) {
                            out.name(boundField.name);
                            boundField.write(out, value);
                        }
                    }
                    out.endObject();
                } catch (IllegalAccessException e) {
                    throw new AssertionError();
                }
            }
        }
    }

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
    }

    private BoundField createBoundField(Gson context, Field field, String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        return new AnonymousClass_1(name, serialize, deserialize, context, fieldType, field, Primitives.isPrimitive(fieldType.getRawType()));
    }

    private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
        Map<String, BoundField> result = new LinkedHashMap();
        if (!raw.isInterface()) {
            Type declaredType = type.getType();
            while (raw != Object.class) {
                Field[] arr$ = raw.getDeclaredFields();
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Field field = arr$[i$];
                    boolean serialize = excludeField(field, true);
                    boolean deserialize = excludeField(field, false);
                    if (serialize || deserialize) {
                        field.setAccessible(true);
                        Gson gson = context;
                        BoundField boundField = createBoundField(gson, field, getFieldName(field), TypeToken.get(Types.resolve(type.getType(), raw, field.getGenericType())), serialize, deserialize);
                        BoundField previous = (BoundField) result.put(boundField.name, boundField);
                        if (previous != null) {
                            throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
                        }
                    }
                    i$++;
                }
                type = TypeToken.get(Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
                raw = type.getRawType();
            }
        }
        return result;
    }

    private String getFieldName(Field f) {
        SerializedName serializedName = (SerializedName) f.getAnnotation(SerializedName.class);
        return serializedName == null ? this.fieldNamingPolicy.translateName(f) : serializedName.value();
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        return !Object.class.isAssignableFrom(raw) ? null : new Adapter(getBoundFields(gson, type, raw), null);
    }

    public boolean excludeField(Field f, boolean serialize) {
        return (this.excluder.excludeClass(f.getType(), serialize) || this.excluder.excludeField(f, serialize)) ? false : true;
    }
}