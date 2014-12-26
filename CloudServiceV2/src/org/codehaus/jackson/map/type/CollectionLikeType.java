package org.codehaus.jackson.map.type;

import java.util.Collection;
import org.codehaus.jackson.type.JavaType;

public class CollectionLikeType extends TypeBase {
    protected final JavaType _elementType;

    protected CollectionLikeType(Class<?> collT, JavaType elemT) {
        super(collT, elemT.hashCode());
        this._elementType = elemT;
    }

    public static CollectionLikeType construct(Class<?> rawType, JavaType elemT) {
        return new CollectionLikeType(rawType, elemT);
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new CollectionLikeType(subclass, this._elementType);
    }

    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._elementType != null) {
            sb.append('<');
            sb.append(this._elementType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._elementType : null;
    }

    public int containedTypeCount() {
        return 1;
    }

    public String containedTypeName(int index) {
        return index == 0 ? "E" : null;
    }

    public boolean equals(CollectionLikeType o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        CollectionLikeType other = o;
        return this._class == other._class && this._elementType.equals(other._elementType);
    }

    public JavaType getContentType() {
        return this._elementType;
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(this._class, sb, true);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        _classSignature(this._class, sb, false);
        sb.append('<');
        this._elementType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    public boolean isCollectionLikeType() {
        return true;
    }

    public boolean isContainerType() {
        return true;
    }

    public boolean isTrueCollectionType() {
        return Collection.class.isAssignableFrom(this._class);
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionLikeType(this._class, this._elementType.narrowBy(contentClass)).copyHandlers(this);
    }

    public String toString() {
        return "[collection-like type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }

    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionLikeType(this._class, this._elementType.widenBy(contentClass)).copyHandlers(this);
    }

    public CollectionLikeType withContentTypeHandler(Object h) {
        return new CollectionLikeType(this._class, this._elementType.withTypeHandler(h));
    }

    public CollectionLikeType withTypeHandler(Object h) {
        CollectionLikeType newInstance = new CollectionLikeType(this._class, this._elementType);
        newInstance._typeHandler = h;
        return newInstance;
    }
}