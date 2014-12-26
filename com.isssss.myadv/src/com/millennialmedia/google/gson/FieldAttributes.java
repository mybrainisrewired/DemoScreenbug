package com.millennialmedia.google.gson;

import com.millennialmedia.google.gson.internal._$Gson.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public final class FieldAttributes {
    private final Field field;

    public FieldAttributes(Field f) {
        Preconditions.checkNotNull(f);
        this.field = f;
    }

    Object get(Object instance) throws IllegalAccessException {
        return this.field.get(instance);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return this.field.getAnnotation(annotation);
    }

    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(this.field.getAnnotations());
    }

    public Class<?> getDeclaredClass() {
        return this.field.getType();
    }

    public Type getDeclaredType() {
        return this.field.getGenericType();
    }

    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }

    public String getName() {
        return this.field.getName();
    }

    public boolean hasModifier(int modifier) {
        return (this.field.getModifiers() & modifier) != 0;
    }

    boolean isSynthetic() {
        return this.field.isSynthetic();
    }
}