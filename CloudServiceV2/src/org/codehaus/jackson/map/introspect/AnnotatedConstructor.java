package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedConstructor extends AnnotatedWithParams {
    protected final Constructor<?> _constructor;

    public AnnotatedConstructor(Constructor<?> constructor, AnnotationMap classAnn, AnnotationMap[] paramAnn) {
        super(classAnn, paramAnn);
        if (constructor == null) {
            throw new IllegalArgumentException("Null constructor not allowed");
        }
        this._constructor = constructor;
    }

    public Constructor<?> getAnnotated() {
        return this._constructor;
    }

    public Class<?> getDeclaringClass() {
        return this._constructor.getDeclaringClass();
    }

    public Type getGenericType() {
        return getRawType();
    }

    public Member getMember() {
        return this._constructor;
    }

    public int getModifiers() {
        return this._constructor.getModifiers();
    }

    public String getName() {
        return this._constructor.getName();
    }

    public AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
    }

    public Class<?> getParameterClass(int index) {
        Class<?>[] types = this._constructor.getParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public int getParameterCount() {
        return this._constructor.getParameterTypes().length;
    }

    public Type getParameterType(int index) {
        Type[] types = this._constructor.getGenericParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Class<?> getRawType() {
        return this._constructor.getDeclaringClass();
    }

    public JavaType getType(TypeBindings bindings) {
        return getType(bindings, this._constructor.getTypeParameters());
    }

    public String toString() {
        return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
    }
}