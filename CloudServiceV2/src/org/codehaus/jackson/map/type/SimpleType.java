package org.codehaus.jackson.map.type;

import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;

public final class SimpleType extends TypeBase {
    protected final String[] _typeNames;
    protected final JavaType[] _typeParameters;

    protected SimpleType(Class<?> cls) {
        this(cls, null, null);
    }

    protected SimpleType(Class<?> cls, String[] typeNames, JavaType[] typeParams) {
        super(cls, 0);
        if (typeNames == null || typeNames.length == 0) {
            this._typeNames = null;
            this._typeParameters = null;
        } else {
            this._typeNames = typeNames;
            this._typeParameters = typeParams;
        }
    }

    public static SimpleType construct(Class<?> cls) {
        if (Map.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: " + cls.getName() + ")");
        } else if (Collection.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: " + cls.getName() + ")");
        } else if (!cls.isArray()) {
            return new SimpleType(cls);
        } else {
            throw new IllegalArgumentException("Can not construct SimpleType for an array (class: " + cls.getName() + ")");
        }
    }

    public static SimpleType constructUnsafe(Class<?> raw) {
        return new SimpleType(raw, null, null);
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new SimpleType(subclass, this._typeNames, this._typeParameters);
    }

    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._typeParameters != null && this._typeParameters.length > 0) {
            sb.append('<');
            boolean first = true;
            JavaType[] arr$ = this._typeParameters;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                JavaType t = arr$[i$];
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(t.toCanonical());
                i$++;
            }
            sb.append('>');
        }
        return sb.toString();
    }

    public JavaType containedType(int index) {
        return (index < 0 || this._typeParameters == null || index >= this._typeParameters.length) ? null : this._typeParameters[index];
    }

    public int containedTypeCount() {
        return this._typeParameters == null ? 0 : this._typeParameters.length;
    }

    public String containedTypeName(int index) {
        return (index < 0 || this._typeNames == null || index >= this._typeNames.length) ? null : this._typeNames[index];
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(org.codehaus.jackson.map.type.SimpleType r10_o) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.type.SimpleType.equals(java.lang.Object):boolean");
        /*
        r9 = this;
        r6 = 1;
        r5 = 0;
        if (r10 != r9) goto L_0x0006;
    L_0x0004:
        r5 = r6;
    L_0x0005:
        return r5;
    L_0x0006:
        if (r10 == 0) goto L_0x0005;
    L_0x0008:
        r7 = r10.getClass();
        r8 = r9.getClass();
        if (r7 != r8) goto L_0x0005;
    L_0x0012:
        r2 = r10;
        r2 = (org.codehaus.jackson.map.type.SimpleType) r2;
        r7 = r2._class;
        r8 = r9._class;
        if (r7 != r8) goto L_0x0005;
    L_0x001b:
        r3 = r9._typeParameters;
        r4 = r2._typeParameters;
        if (r3 != 0) goto L_0x0028;
    L_0x0021:
        if (r4 == 0) goto L_0x0026;
    L_0x0023:
        r7 = r4.length;
        if (r7 != 0) goto L_0x0005;
    L_0x0026:
        r5 = r6;
        goto L_0x0005;
    L_0x0028:
        if (r4 == 0) goto L_0x0005;
    L_0x002a:
        r7 = r3.length;
        r8 = r4.length;
        if (r7 != r8) goto L_0x0005;
    L_0x002e:
        r0 = 0;
        r1 = r3.length;
    L_0x0030:
        if (r0 >= r1) goto L_0x003f;
    L_0x0032:
        r7 = r3[r0];
        r8 = r4[r0];
        r7 = r7.equals(r8);
        if (r7 == 0) goto L_0x0005;
    L_0x003c:
        r0 = r0 + 1;
        goto L_0x0030;
    L_0x003f:
        r5 = r6;
        goto L_0x0005;
        */
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(this._class, sb, true);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        _classSignature(this._class, sb, false);
        if (this._typeParameters != null) {
            sb.append('<');
            JavaType[] arr$ = this._typeParameters;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                sb = arr$[i$].getGenericSignature(sb);
                i$++;
            }
            sb.append('>');
        }
        sb.append(';');
        return sb;
    }

    public boolean isContainerType() {
        return false;
    }

    public JavaType narrowContentsBy(Class<?> subclass) {
        throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
        return sb.toString();
    }

    public JavaType widenContentsBy(Class<?> subclass) {
        throw new IllegalArgumentException("Internal error: SimpleType.widenContentsBy() should never be called");
    }

    public JavaType withContentTypeHandler(Object h) {
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
    }

    public SimpleType withTypeHandler(Object h) {
        SimpleType newInstance = new SimpleType(this._class, this._typeNames, this._typeParameters);
        newInstance._typeHandler = h;
        return newInstance;
    }
}