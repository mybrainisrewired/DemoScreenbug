package org.codehaus.jackson.type;

import java.lang.reflect.Modifier;

public abstract class JavaType {
    protected final Class<?> _class;
    protected final int _hashCode;
    protected Object _typeHandler;
    protected Object _valueHandler;

    protected JavaType(Class<?> clz, int hash) {
        this._class = clz;
        this._hashCode = clz.getName().hashCode() + hash;
    }

    protected void _assertSubclass(Class<?> subclass, Class<?> superClass) {
        if (!this._class.isAssignableFrom(subclass)) {
            throw new IllegalArgumentException("Class " + subclass.getName() + " is not assignable to " + this._class.getName());
        }
    }

    protected abstract JavaType _narrow(Class<?> cls);

    protected JavaType _widen(Class<?> superclass) {
        return _narrow(superclass);
    }

    public JavaType containedType(int index) {
        return null;
    }

    public int containedTypeCount() {
        return 0;
    }

    public String containedTypeName(int index) {
        return null;
    }

    public abstract boolean equals(Object obj);

    public final JavaType forcedNarrowBy(Class<?> subclass) {
        if (subclass == this._class) {
            return this;
        }
        JavaType result = _narrow(subclass);
        if (this._valueHandler != null) {
            result.setValueHandler(this._valueHandler);
        }
        if (this._typeHandler != null) {
            result = result.withTypeHandler(this._typeHandler);
        }
        return result;
    }

    public JavaType getContentType() {
        return null;
    }

    public String getErasedSignature() {
        StringBuilder sb = new StringBuilder(40);
        getErasedSignature(sb);
        return sb.toString();
    }

    public abstract StringBuilder getErasedSignature(StringBuilder stringBuilder);

    public String getGenericSignature() {
        StringBuilder sb = new StringBuilder(40);
        getGenericSignature(sb);
        return sb.toString();
    }

    public abstract StringBuilder getGenericSignature(StringBuilder stringBuilder);

    public JavaType getKeyType() {
        return null;
    }

    public final Class<?> getRawClass() {
        return this._class;
    }

    public <T> T getTypeHandler() {
        return this._typeHandler;
    }

    public <T> T getValueHandler() {
        return this._valueHandler;
    }

    public boolean hasGenericTypes() {
        return containedTypeCount() > 0;
    }

    public final boolean hasRawClass(Class<?> clz) {
        return this._class == clz;
    }

    public final int hashCode() {
        return this._hashCode;
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(this._class.getModifiers());
    }

    public boolean isArrayType() {
        return false;
    }

    public boolean isCollectionLikeType() {
        return false;
    }

    public boolean isConcrete() {
        return (this._class.getModifiers() & 1536) == 0 || this._class.isPrimitive();
    }

    public abstract boolean isContainerType();

    public final boolean isEnumType() {
        return this._class.isEnum();
    }

    public final boolean isFinal() {
        return Modifier.isFinal(this._class.getModifiers());
    }

    public final boolean isInterface() {
        return this._class.isInterface();
    }

    public boolean isMapLikeType() {
        return false;
    }

    public final boolean isPrimitive() {
        return this._class.isPrimitive();
    }

    public boolean isThrowable() {
        return Throwable.class.isAssignableFrom(this._class);
    }

    public final JavaType narrowBy(Class<?> subclass) {
        if (subclass == this._class) {
            return this;
        }
        _assertSubclass(subclass, this._class);
        JavaType result = _narrow(subclass);
        if (this._valueHandler != null) {
            result.setValueHandler(this._valueHandler);
        }
        if (this._typeHandler != null) {
            result = result.withTypeHandler(this._typeHandler);
        }
        return result;
    }

    public abstract JavaType narrowContentsBy(Class<?> cls);

    @Deprecated
    public void setTypeHandler(Object h) {
        if (h == null || this._typeHandler == null) {
            this._typeHandler = h;
        } else {
            throw new IllegalStateException("Trying to reset type handler for type [" + toString() + "]; old handler of type " + this._typeHandler.getClass().getName() + ", new handler of type " + h.getClass().getName());
        }
    }

    public void setValueHandler(Object h) {
        if (h == null || this._valueHandler == null) {
            this._valueHandler = h;
        } else {
            throw new IllegalStateException("Trying to reset value handler for type [" + toString() + "]; old handler of type " + this._valueHandler.getClass().getName() + ", new handler of type " + h.getClass().getName());
        }
    }

    public abstract String toCanonical();

    public abstract String toString();

    public final JavaType widenBy(Class<?> superclass) {
        if (superclass == this._class) {
            return this;
        }
        _assertSubclass(this._class, superclass);
        return _widen(superclass);
    }

    public abstract JavaType widenContentsBy(Class<?> cls);

    public abstract JavaType withContentTypeHandler(Object obj);

    public abstract JavaType withTypeHandler(Object obj);
}