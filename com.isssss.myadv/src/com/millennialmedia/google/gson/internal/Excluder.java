package com.millennialmedia.google.gson.internal;

import com.millennialmedia.google.gson.ExclusionStrategy;
import com.millennialmedia.google.gson.FieldAttributes;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.annotations.Expose;
import com.millennialmedia.google.gson.annotations.Since;
import com.millennialmedia.google.gson.annotations.Until;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Excluder implements TypeAdapterFactory, Cloneable {
    public static final Excluder DEFAULT;
    private static final double IGNORE_VERSIONS = -1.0d;
    private List<ExclusionStrategy> deserializationStrategies;
    private int modifiers;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies;
    private boolean serializeInnerClasses;
    private double version;

    class AnonymousClass_1 extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;
        final /* synthetic */ Gson val$gson;
        final /* synthetic */ boolean val$skipDeserialize;
        final /* synthetic */ boolean val$skipSerialize;
        final /* synthetic */ TypeToken val$type;

        AnonymousClass_1(boolean z, boolean z2, Gson gson, TypeToken typeToken) {
            this.val$skipDeserialize = z;
            this.val$skipSerialize = z2;
            this.val$gson = gson;
            this.val$type = typeToken;
        }

        private TypeAdapter<T> delegate() {
            TypeAdapter<T> d = this.delegate;
            if (d != null) {
                return d;
            }
            d = this.val$gson.getDelegateAdapter(Excluder.this, this.val$type);
            this.delegate = d;
            return d;
        }

        public T read(JsonReader in) throws IOException {
            if (!this.val$skipDeserialize) {
                return delegate().read(in);
            }
            in.skipValue();
            return null;
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (this.val$skipSerialize) {
                out.nullValue();
            } else {
                delegate().write(out, value);
            }
        }
    }

    static {
        DEFAULT = new Excluder();
    }

    public Excluder() {
        this.version = -1.0d;
        this.modifiers = 136;
        this.serializeInnerClasses = true;
        this.serializationStrategies = Collections.emptyList();
        this.deserializationStrategies = Collections.emptyList();
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & 8) != 0;
    }

    private boolean isValidSince(Since annotation) {
        return annotation == null || annotation.value() <= this.version;
    }

    private boolean isValidUntil(Until annotation) {
        return annotation == null || annotation.value() > this.version;
    }

    private boolean isValidVersion(Since since, Until until) {
        return isValidSince(since) && isValidUntil(until);
    }

    protected Excluder clone() {
        try {
            return (Excluder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();
        boolean skipSerialize = excludeClass(rawType, true);
        boolean skipDeserialize = excludeClass(rawType, false);
        return (skipSerialize || skipDeserialize) ? new AnonymousClass_1(skipDeserialize, skipSerialize, gson, type) : null;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder result = clone();
        result.serializeInnerClasses = false;
        return result;
    }

    public boolean excludeClass(Class<?> clazz, boolean serialize) {
        if (this.version != -1.0d && !isValidVersion((Since) clazz.getAnnotation(Since.class), (Until) clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && isInnerClass(clazz)) {
            return true;
        }
        if (isAnonymousOrLocal(clazz)) {
            return true;
        }
        Iterator i$ = (serialize ? this.serializationStrategies : this.deserializationStrategies).iterator();
        while (i$.hasNext()) {
            if (((ExclusionStrategy) i$.next()).shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean excludeField(Field field, boolean serialize) {
        if ((this.modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if (this.version != -1.0d && !isValidVersion((Since) field.getAnnotation(Since.class), (Until) field.getAnnotation(Until.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        if (this.requireExpose) {
            Expose annotation = (Expose) field.getAnnotation(Expose.class);
            if (annotation != null) {
                if (serialize) {
                }
            }
            return true;
        }
        if (!this.serializeInnerClasses && isInnerClass(field.getType())) {
            return true;
        }
        if (isAnonymousOrLocal(field.getType())) {
            return true;
        }
        List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
        if (!list.isEmpty()) {
            FieldAttributes fieldAttributes = new FieldAttributes(field);
            Iterator i$ = list.iterator();
            while (i$.hasNext()) {
                if (((ExclusionStrategy) i$.next()).shouldSkipField(fieldAttributes)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder result = clone();
        result.requireExpose = true;
        return result;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean serialization, boolean deserialization) {
        Excluder result = clone();
        if (serialization) {
            result.serializationStrategies = new ArrayList(this.serializationStrategies);
            result.serializationStrategies.add(exclusionStrategy);
        }
        if (deserialization) {
            result.deserializationStrategies = new ArrayList(this.deserializationStrategies);
            result.deserializationStrategies.add(exclusionStrategy);
        }
        return result;
    }

    public Excluder withModifiers(int... modifiers) {
        Excluder result = clone();
        result.modifiers = 0;
        int[] arr$ = modifiers;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            result.modifiers |= arr$[i$];
            i$++;
        }
        return result;
    }

    public Excluder withVersion(double ignoreVersionsAfter) {
        Excluder result = clone();
        result.version = ignoreVersionsAfter;
        return result;
    }
}