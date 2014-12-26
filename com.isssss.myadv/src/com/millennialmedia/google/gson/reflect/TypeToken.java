package com.millennialmedia.google.gson.reflect;

import com.millennialmedia.google.gson.internal._$Gson.Preconditions;
import com.millennialmedia.google.gson.internal._$Gson.Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;

    protected TypeToken() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    TypeToken(Type type) {
        this.type = Types.canonicalize((Type) Preconditions.checkNotNull(type));
        this.rawType = Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    private static AssertionError buildUnexpectedTypeError(Type token, Class<?>... expected) {
        StringBuilder exceptionMessage = new StringBuilder("Unexpected type. Expected one of: ");
        Class<?>[] arr$ = expected;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            exceptionMessage.append(arr$[i$].getName()).append(", ");
            i$++;
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName()).append(", for type token: ").append(token.toString()).append('.');
        return new AssertionError(exceptionMessage.toString());
    }

    public static <T> TypeToken<T> get(Class<T> type) {
        return new TypeToken(type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken(type);
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof Class)) {
            return Types.canonicalize(((ParameterizedType) superclass).getActualTypeArguments()[0]);
        }
        throw new RuntimeException("Missing type parameter.");
    }

    private static boolean isAssignableFrom(Type from, GenericArrayType to) {
        Type toGenericComponentType = to.getGenericComponentType();
        if (!(toGenericComponentType instanceof ParameterizedType)) {
            return true;
        }
        Type t = from;
        if (from instanceof GenericArrayType) {
            t = ((GenericArrayType) from).getGenericComponentType();
        } else if (from instanceof Class) {
            Class<?> classType = (Class) from;
            while (classType.isArray()) {
                classType = classType.getComponentType();
            }
            Class<?> t2 = classType;
        }
        return isAssignableFrom(t, (ParameterizedType) toGenericComponentType, new HashMap());
    }

    private static boolean isAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from == null) {
            return false;
        }
        if (to.equals(from)) {
            return true;
        }
        Class<?> clazz = Types.getRawType(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType) from;
        }
        if (ptype != null) {
            Type[] tArgs = ptype.getActualTypeArguments();
            TypeVariable<?>[] tParams = clazz.getTypeParameters();
            int i = 0;
            while (i < tArgs.length) {
                Type arg = tArgs[i];
                TypeVariable<?> var = tParams[i];
                while (arg instanceof TypeVariable) {
                    arg = typeVarMap.get(((TypeVariable) arg).getName());
                }
                typeVarMap.put(var.getName(), arg);
                i++;
            }
            if (typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }
        Type[] arr$ = clazz.getGenericInterfaces();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (isAssignableFrom(arr$[i$], to, new HashMap(typeVarMap))) {
                return true;
            }
            i$++;
        }
        return isAssignableFrom(clazz.getGenericSuperclass(), to, new HashMap(typeVarMap));
    }

    private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
        return to.equals(from) || (from instanceof TypeVariable && to.equals(typeMap.get(((TypeVariable) from).getName())));
    }

    private static boolean typeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (!from.getRawType().equals(to.getRawType())) {
            return false;
        }
        Type[] fromArgs = from.getActualTypeArguments();
        Type[] toArgs = to.getActualTypeArguments();
        int i = 0;
        while (i < fromArgs.length) {
            if (!matches(fromArgs[i], toArgs[i], typeVarMap)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public final boolean equals(Object o) {
        return o instanceof TypeToken && Types.equals(this.type, ((TypeToken) o).type);
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    public final int hashCode() {
        return this.hashCode;
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> token) {
        return isAssignableFrom(token.getType());
    }

    @Deprecated
    public boolean isAssignableFrom(Type cls) {
        return isAssignableFrom(cls);
    }

    @Deprecated
    public boolean isAssignableFrom(Type from) {
        if (from == null) {
            return false;
        }
        if (this.type.equals(from)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom(Types.getRawType(from));
        }
        if (this.type instanceof ParameterizedType) {
            return isAssignableFrom(from, (ParameterizedType) this.type, new HashMap());
        }
        if (this.type instanceof GenericArrayType) {
            boolean z = this.rawType.isAssignableFrom(Types.getRawType(from)) && isAssignableFrom(from, (GenericArrayType) this.type);
            return z;
        } else {
            throw buildUnexpectedTypeError(this.type, new Class[]{Class.class, ParameterizedType.class, GenericArrayType.class});
        }
    }

    public final String toString() {
        return Types.typeToString(this.type);
    }
}