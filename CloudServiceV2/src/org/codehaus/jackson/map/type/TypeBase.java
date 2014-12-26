package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

public abstract class TypeBase extends JavaType {
    volatile String _canonicalName;

    protected TypeBase(Class<?> raw, int hash) {
        super(raw, hash);
    }

    protected static StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon) {
        if (!cls.isPrimitive()) {
            sb.append('L');
            String name = cls.getName();
            int i = 0;
            int len = name.length();
            while (i < len) {
                char c = name.charAt(i);
                if (c == '.') {
                    c = '/';
                }
                sb.append(c);
                i++;
            }
            if (trailingSemicolon) {
                sb.append(';');
            }
        } else if (cls == Boolean.TYPE) {
            sb.append('Z');
        } else if (cls == Byte.TYPE) {
            sb.append('B');
        } else if (cls == Short.TYPE) {
            sb.append('S');
        } else if (cls == Character.TYPE) {
            sb.append('C');
        } else if (cls == Integer.TYPE) {
            sb.append('I');
        } else if (cls == Long.TYPE) {
            sb.append('J');
        } else if (cls == Float.TYPE) {
            sb.append('F');
        } else if (cls == Double.TYPE) {
            sb.append('D');
        } else if (cls == Void.TYPE) {
            sb.append('V');
        } else {
            throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
        }
        return sb;
    }

    protected abstract String buildCanonicalName();

    protected final JavaType copyHandlers(JavaType fromType) {
        this._valueHandler = fromType.getValueHandler();
        this._typeHandler = fromType.getTypeHandler();
        return this;
    }

    public abstract StringBuilder getErasedSignature(StringBuilder stringBuilder);

    public abstract StringBuilder getGenericSignature(StringBuilder stringBuilder);

    public String toCanonical() {
        String str = this._canonicalName;
        return str == null ? buildCanonicalName() : str;
    }
}