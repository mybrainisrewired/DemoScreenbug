package com.millennialmedia.google.gson.internal;

import com.millennialmedia.google.gson.internal._$Gson.Preconditions;
import com.millennialmedia.google.gson.internal._$Gson.Types;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

// compiled from: $Gson$Types.java
public final class _$Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = null;

    // compiled from: $Gson$Types.java
    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = Types.canonicalize(componentType);
        }

        public boolean equals(Object o) {
            return o instanceof GenericArrayType && Types.equals(this, (GenericArrayType) o);
        }

        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return Types.typeToString(this.componentType) + "[]";
        }
    }

    // compiled from: $Gson$Types.java
    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            boolean z = false;
            if (rawType instanceof Class) {
                boolean z2;
                Class<?> rawTypeAsClass = (Class) rawType;
                if (ownerType != null || rawTypeAsClass.getEnclosingClass() == null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                Preconditions.checkArgument(z2);
                if (ownerType == null || rawTypeAsClass.getEnclosingClass() != null) {
                    z = true;
                }
                Preconditions.checkArgument(z);
            }
            this.ownerType = ownerType == null ? null : Types.canonicalize(ownerType);
            this.rawType = Types.canonicalize(rawType);
            this.typeArguments = (Type[]) typeArguments.clone();
            int t = 0;
            while (t < this.typeArguments.length) {
                Preconditions.checkNotNull(this.typeArguments[t]);
                Types.access$000(this.typeArguments[t]);
                this.typeArguments[t] = Types.canonicalize(this.typeArguments[t]);
                t++;
            }
        }

        public boolean equals(Object other) {
            return other instanceof ParameterizedType && Types.equals(this, (ParameterizedType) other);
        }

        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        public Type getOwnerType() {
            return this.ownerType;
        }

        public Type getRawType() {
            return this.rawType;
        }

        public int hashCode() {
            return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ Types.access$100(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
            stringBuilder.append(Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append(Types.typeToString(this.typeArguments[0]));
            int i = 1;
            while (i < this.typeArguments.length) {
                stringBuilder.append(", ").append(Types.typeToString(this.typeArguments[i]));
                i++;
            }
            return stringBuilder.append(">").toString();
        }
    }

    // compiled from: $Gson$Types.java
    private static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            boolean z;
            boolean z2 = true;
            Preconditions.checkArgument(lowerBounds.length <= 1);
            if (upperBounds.length == 1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            if (lowerBounds.length == 1) {
                Preconditions.checkNotNull(lowerBounds[0]);
                Types.access$000(lowerBounds[0]);
                if (upperBounds[0] != Object.class) {
                    z2 = false;
                }
                Preconditions.checkArgument(z2);
                this.lowerBound = Types.canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
            } else {
                Preconditions.checkNotNull(upperBounds[0]);
                Types.access$000(upperBounds[0]);
                this.lowerBound = null;
                this.upperBound = Types.canonicalize(upperBounds[0]);
            }
        }

        public boolean equals(Object other) {
            return other instanceof WildcardType && Types.equals(this, (WildcardType) other);
        }

        public Type[] getLowerBounds() {
            if (this.lowerBound == null) {
                return EMPTY_TYPE_ARRAY;
            }
            return new Type[]{this.lowerBound};
        }

        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        public int hashCode() {
            return (this.lowerBound != null ? this.lowerBound.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + Types.typeToString(this.lowerBound);
            }
            return this.upperBound == Object.class ? "?" : "? extends " + Types.typeToString(this.upperBound);
        }
    }

    static {
        Types.EMPTY_TYPE_ARRAY = new Type[0];
    }

    private _$Gson$Types() {
    }

    public static GenericArrayType arrayOf(Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            GenericArrayTypeImpl genericArrayTypeImpl;
            Class<?> c = (Class) type;
            if (c.isArray()) {
                genericArrayTypeImpl = new GenericArrayTypeImpl(Types.canonicalize(c.getComponentType()));
            } else {
                Class<?> cls = c;
            }
            return genericArrayTypeImpl;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
        } else if (type instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(((GenericArrayType) type).getGenericComponentType());
        } else {
            if (!(type instanceof WildcardType)) {
                return type;
            }
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
        }
    }

    private static void checkNotPrimitive(Type type) {
        boolean z = (type instanceof Class && ((Class) type).isPrimitive()) ? false : true;
        Preconditions.checkArgument(z);
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        return genericDeclaration instanceof Class ? (Class) genericDeclaration : null;
    }

    static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean equals(java.lang.reflect.Type r12_a, java.lang.reflect.Type r13_b) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.google.gson.internal._$Gson$Types.equals(java.lang.reflect.Type, java.lang.reflect.Type):boolean");
        /*
        r8 = 1;
        r9 = 0;
        if (r12 != r13) goto L_0x0006;
    L_0x0004:
        r9 = r8;
    L_0x0005:
        return r9;
    L_0x0006:
        r10 = r12 instanceof java.lang.Class;
        if (r10 == 0) goto L_0x000f;
    L_0x000a:
        r9 = r12.equals(r13);
        goto L_0x0005;
    L_0x000f:
        r10 = r12 instanceof java.lang.reflect.ParameterizedType;
        if (r10 == 0) goto L_0x004b;
    L_0x0013:
        r10 = r13 instanceof java.lang.reflect.ParameterizedType;
        if (r10 == 0) goto L_0x0005;
    L_0x0017:
        r2 = r12;
        r2 = (java.lang.reflect.ParameterizedType) r2;
        r3 = r13;
        r3 = (java.lang.reflect.ParameterizedType) r3;
        r10 = r2.getOwnerType();
        r11 = r3.getOwnerType();
        r10 = com.millennialmedia.google.gson.internal._$Gson.Types.equal(r10, r11);
        if (r10 == 0) goto L_0x0049;
    L_0x002b:
        r10 = r2.getRawType();
        r11 = r3.getRawType();
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0049;
    L_0x0039:
        r10 = r2.getActualTypeArguments();
        r11 = r3.getActualTypeArguments();
        r10 = java.util.Arrays.equals(r10, r11);
        if (r10 == 0) goto L_0x0049;
    L_0x0047:
        r9 = r8;
        goto L_0x0005;
    L_0x0049:
        r8 = r9;
        goto L_0x0047;
    L_0x004b:
        r10 = r12 instanceof java.lang.reflect.GenericArrayType;
        if (r10 == 0) goto L_0x0066;
    L_0x004f:
        r8 = r13 instanceof java.lang.reflect.GenericArrayType;
        if (r8 == 0) goto L_0x0005;
    L_0x0053:
        r0 = r12;
        r0 = (java.lang.reflect.GenericArrayType) r0;
        r1 = r13;
        r1 = (java.lang.reflect.GenericArrayType) r1;
        r8 = r0.getGenericComponentType();
        r9 = r1.getGenericComponentType();
        r9 = com.millennialmedia.google.gson.internal._$Gson.Types.equals(r8, r9);
        goto L_0x0005;
    L_0x0066:
        r10 = r12 instanceof java.lang.reflect.WildcardType;
        if (r10 == 0) goto L_0x0095;
    L_0x006a:
        r10 = r13 instanceof java.lang.reflect.WildcardType;
        if (r10 == 0) goto L_0x0005;
    L_0x006e:
        r6 = r12;
        r6 = (java.lang.reflect.WildcardType) r6;
        r7 = r13;
        r7 = (java.lang.reflect.WildcardType) r7;
        r10 = r6.getUpperBounds();
        r11 = r7.getUpperBounds();
        r10 = java.util.Arrays.equals(r10, r11);
        if (r10 == 0) goto L_0x0093;
    L_0x0082:
        r10 = r6.getLowerBounds();
        r11 = r7.getLowerBounds();
        r10 = java.util.Arrays.equals(r10, r11);
        if (r10 == 0) goto L_0x0093;
    L_0x0090:
        r9 = r8;
        goto L_0x0005;
    L_0x0093:
        r8 = r9;
        goto L_0x0090;
    L_0x0095:
        r10 = r12 instanceof java.lang.reflect.TypeVariable;
        if (r10 == 0) goto L_0x0005;
    L_0x0099:
        r10 = r13 instanceof java.lang.reflect.TypeVariable;
        if (r10 == 0) goto L_0x0005;
    L_0x009d:
        r4 = r12;
        r4 = (java.lang.reflect.TypeVariable) r4;
        r5 = r13;
        r5 = (java.lang.reflect.TypeVariable) r5;
        r10 = r4.getGenericDeclaration();
        r11 = r5.getGenericDeclaration();
        if (r10 != r11) goto L_0x00be;
    L_0x00ad:
        r10 = r4.getName();
        r11 = r5.getName();
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x00be;
    L_0x00bb:
        r9 = r8;
        goto L_0x0005;
    L_0x00be:
        r8 = r9;
        goto L_0x00bb;
        */
    }

    public static Type getArrayComponentType(Type array) {
        return array instanceof GenericArrayType ? ((GenericArrayType) array).getGenericComponentType() : ((Class) array).getComponentType();
    }

    public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
        Type collectionType = Types.getSupertype(context, contextRawType, Collection.class);
        if (collectionType instanceof WildcardType) {
            collectionType = ((WildcardType) collectionType).getUpperBounds()[0];
        }
        return collectionType instanceof ParameterizedType ? ((ParameterizedType) collectionType).getActualTypeArguments()[0] : Object.class;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.reflect.Type getGenericSupertype(java.lang.reflect.Type r6_context, java.lang.Class<?> r7_rawType, java.lang.Class<?> r8_toResolve) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.google.gson.internal._$Gson$Types.getGenericSupertype(java.lang.reflect.Type, java.lang.Class, java.lang.Class):java.lang.reflect.Type");
        /*
        if (r8 != r7) goto L_0x0003;
    L_0x0002:
        return r6;
    L_0x0003:
        r4 = r8.isInterface();
        if (r4 == 0) goto L_0x0034;
    L_0x0009:
        r1 = r7.getInterfaces();
        r0 = 0;
        r2 = r1.length;
    L_0x000f:
        if (r0 >= r2) goto L_0x0034;
    L_0x0011:
        r4 = r1[r0];
        if (r4 != r8) goto L_0x001c;
    L_0x0015:
        r4 = r7.getGenericInterfaces();
        r6 = r4[r0];
        goto L_0x0002;
    L_0x001c:
        r4 = r1[r0];
        r4 = r8.isAssignableFrom(r4);
        if (r4 == 0) goto L_0x0031;
    L_0x0024:
        r4 = r7.getGenericInterfaces();
        r4 = r4[r0];
        r5 = r1[r0];
        r6 = com.millennialmedia.google.gson.internal._$Gson.Types.getGenericSupertype(r4, r5, r8);
        goto L_0x0002;
    L_0x0031:
        r0 = r0 + 1;
        goto L_0x000f;
    L_0x0034:
        r4 = r7.isInterface();
        if (r4 != 0) goto L_0x005a;
    L_0x003a:
        r4 = java.lang.Object.class;
        if (r7 == r4) goto L_0x005a;
    L_0x003e:
        r3 = r7.getSuperclass();
        if (r3 != r8) goto L_0x0049;
    L_0x0044:
        r6 = r7.getGenericSuperclass();
        goto L_0x0002;
    L_0x0049:
        r4 = r8.isAssignableFrom(r3);
        if (r4 == 0) goto L_0x0058;
    L_0x004f:
        r4 = r7.getGenericSuperclass();
        r6 = com.millennialmedia.google.gson.internal._$Gson.Types.getGenericSupertype(r4, r3, r8);
        goto L_0x0002;
    L_0x0058:
        r7 = r3;
        goto L_0x003a;
    L_0x005a:
        r6 = r8;
        goto L_0x0002;
        */
    }

    public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
        if (context == Properties.class) {
            return new Type[]{String.class, String.class};
        } else {
            Type mapType = Types.getSupertype(context, contextRawType, Map.class);
            if (mapType instanceof ParameterizedType) {
                return ((ParameterizedType) mapType).getActualTypeArguments();
            }
            return new Type[]{Object.class, Object.class};
        }
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Preconditions.checkArgument(rawType instanceof Class);
            return (Class) rawType;
        } else if (type instanceof GenericArrayType) {
            return Array.newInstance(Types.getRawType(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        } else {
            if (type instanceof TypeVariable) {
                return Object.class;
            }
            if (type instanceof WildcardType) {
                return Types.getRawType(((WildcardType) type).getUpperBounds()[0]);
            }
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + (type == null ? "null" : type.getClass().getName()));
        }
    }

    static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
        return Types.resolve(context, contextRawType, Types.getGenericSupertype(context, contextRawType, supertype));
    }

    private static int hashCodeOrZero(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    private static int indexOf(Object[] array, Object toFind) {
        int i = 0;
        while (i < array.length) {
            if (toFind.equals(array[i])) {
                return i;
            }
            i++;
        }
        throw new NoSuchElementException();
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
        while (toResolve instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable) toResolve;
            toResolve = Types.resolveTypeVariable(context, contextRawType, typeVariable);
            if (toResolve == typeVariable) {
                return toResolve;
            }
        }
        Type componentType;
        Type newComponentType;
        if (toResolve instanceof Class && ((Class) toResolve).isArray()) {
            Class<?> original = (Class) toResolve;
            componentType = original.getComponentType();
            newComponentType = Types.resolve(context, contextRawType, componentType);
            if (componentType != newComponentType) {
                original = Types.arrayOf(newComponentType);
            }
            return original;
        } else if (toResolve instanceof GenericArrayType) {
            GenericArrayType original2 = (GenericArrayType) toResolve;
            componentType = original2.getGenericComponentType();
            newComponentType = Types.resolve(context, contextRawType, componentType);
            return componentType != newComponentType ? Types.arrayOf(newComponentType) : original2;
        } else if (toResolve instanceof ParameterizedType) {
            ParameterizedType original3 = (ParameterizedType) toResolve;
            Type ownerType = original3.getOwnerType();
            Type newOwnerType = Types.resolve(context, contextRawType, ownerType);
            boolean changed = newOwnerType != ownerType;
            Type[] args = original3.getActualTypeArguments();
            int t = 0;
            int length = args.length;
            while (t < length) {
                Type resolvedTypeArgument = Types.resolve(context, contextRawType, args[t]);
                if (resolvedTypeArgument != args[t]) {
                    if (!changed) {
                        args = args.clone();
                        changed = true;
                    }
                    args[t] = resolvedTypeArgument;
                }
                t++;
            }
            return changed ? Types.newParameterizedTypeWithOwner(newOwnerType, original3.getRawType(), args) : original3;
        } else if (!(toResolve instanceof WildcardType)) {
            return toResolve;
        } else {
            WildcardType original4 = (WildcardType) toResolve;
            Type[] originalLowerBound = original4.getLowerBounds();
            Type[] originalUpperBound = original4.getUpperBounds();
            if (originalLowerBound.length == 1) {
                Type lowerBound = Types.resolve(context, contextRawType, originalLowerBound[0]);
                return lowerBound != originalLowerBound[0] ? Types.supertypeOf(lowerBound) : original4;
            } else if (originalUpperBound.length != 1) {
                return original4;
            } else {
                Type upperBound = Types.resolve(context, contextRawType, originalUpperBound[0]);
                return upperBound != originalUpperBound[0] ? Types.subtypeOf(upperBound) : original4;
            }
        }
    }

    static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = Types.declaringClassOf(unknown);
        if (declaredByRaw == null) {
            return unknown;
        }
        Type declaredBy = Types.getGenericSupertype(context, contextRawType, declaredByRaw);
        if (!(declaredBy instanceof ParameterizedType)) {
            return unknown;
        }
        return ((ParameterizedType) declaredBy).getActualTypeArguments()[Types.indexOf(declaredByRaw.getTypeParameters(), unknown)];
    }

    public static WildcardType subtypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{bound}, Types.EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{bound});
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }
}