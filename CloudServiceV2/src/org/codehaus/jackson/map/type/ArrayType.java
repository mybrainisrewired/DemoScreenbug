package org.codehaus.jackson.map.type;

import java.lang.reflect.Array;
import org.codehaus.jackson.type.JavaType;

public final class ArrayType extends TypeBase {
    final JavaType _componentType;
    final Object _emptyArray;

    private ArrayType(JavaType componentType, Object emptyInstance) {
        super(emptyInstance.getClass(), componentType.hashCode());
        this._componentType = componentType;
        this._emptyArray = emptyInstance;
    }

    public static ArrayType construct(JavaType componentType) {
        return new ArrayType(componentType, Array.newInstance(componentType.getRawClass(), 0));
    }

    protected JavaType _narrow(Class<?> subclass) {
        if (subclass.isArray()) {
            return construct(TypeFactory.defaultInstance().constructType(subclass.getComponentType()));
        } else {
            throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
        }
    }

    protected String buildCanonicalName() {
        return this._class.getName();
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._componentType : null;
    }

    public int containedTypeCount() {
        return 1;
    }

    public String containedTypeName(int index) {
        return index == 0 ? "E" : null;
    }

    public boolean equals(ArrayType o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return this._componentType.equals(o._componentType);
    }

    public JavaType getContentType() {
        return this._componentType;
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getErasedSignature(sb);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getGenericSignature(sb);
    }

    public boolean hasGenericTypes() {
        return this._componentType.hasGenericTypes();
    }

    public boolean isAbstract() {
        return false;
    }

    public boolean isArrayType() {
        return true;
    }

    public boolean isConcrete() {
        return true;
    }

    public boolean isContainerType() {
        return true;
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.narrowBy(contentClass)).copyHandlers(this);
    }

    public String toString() {
        return "[array type, component type: " + this._componentType + "]";
    }

    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.widenBy(contentClass)).copyHandlers(this);
    }

    public ArrayType withContentTypeHandler(Object h) {
        return new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray);
    }

    public ArrayType withTypeHandler(Object h) {
        ArrayType newInstance = new ArrayType(this._componentType, this._emptyArray);
        newInstance._typeHandler = h;
        return newInstance;
    }
}