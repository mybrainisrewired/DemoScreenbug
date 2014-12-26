package org.codehaus.jackson.map.util;

import com.wmt.data.MediaItem;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class ClassUtil {

    private static class EnumTypeLocator {
        static final EnumTypeLocator instance;
        private final Field enumMapTypeField;
        private final Field enumSetTypeField;

        static {
            instance = new EnumTypeLocator();
        }

        private EnumTypeLocator() {
            this.enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
            this.enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
        }

        private Object get(Object bean, Field field) {
            try {
                return field.get(bean);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type) {
            Field found = null;
            Field[] fields = fromClass.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Field f = arr$[i$];
                if (expectedName.equals(f.getName()) && f.getType() == type) {
                    found = f;
                    break;
                } else {
                    i$++;
                }
            }
            if (found == null) {
                arr$ = fields;
                len$ = arr$.length;
                i$ = 0;
                while (i$ < len$) {
                    f = arr$[i$];
                    if (f.getType() == type && found != null) {
                        return null;
                    }
                    found = f;
                    i$++;
                }
            }
            if (found != null) {
                try {
                    found.setAccessible(true);
                } catch (Throwable th) {
                }
            }
            return found;
        }

        public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> set) {
            if (this.enumMapTypeField != null) {
                return (Class) get(set, this.enumMapTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
        }

        public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> set) {
            if (this.enumSetTypeField != null) {
                return (Class) get(set, this.enumSetTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
        }
    }

    private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
        if (cls != endBefore && cls != null && cls != Object.class) {
            if (addClassItself && !result.contains(cls)) {
                result.add(cls);
                Class[] arr$ = cls.getInterfaces();
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    _addSuperTypes(arr$[i$], endBefore, result, true);
                    i$++;
                }
                _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
            }
        }
    }

    public static String canBeABeanType(Class<?> type) {
        if (type.isAnnotation()) {
            return "annotation";
        }
        if (type.isArray()) {
            return "array";
        }
        if (type.isEnum()) {
            return "enum";
        }
        return type.isPrimitive() ? "primitive" : null;
    }

    public static void checkAndFixAccess(Member member) {
        AccessibleObject ao = (AccessibleObject) member;
        try {
            ao.setAccessible(true);
        } catch (SecurityException e) {
            se = e;
            if (!ao.isAccessible()) {
                SecurityException se2;
                throw new IllegalArgumentException("Can not access " + member + " (from class " + member.getDeclaringClass().getName() + "; failed to set access: " + se2.getMessage());
            }
        }
    }

    public static <T> T createInstance(Class<T> cls, boolean canFixAccess) throws IllegalArgumentException {
        Constructor<T> ctor = findConstructor(cls, canFixAccess);
        if (ctor == null) {
            throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
        }
        try {
            return ctor.newInstance(new Object[0]);
        } catch (Exception e) {
            Exception e2 = e;
            unwrapAndThrowAsIAE(e2, "Failed to instantiate class " + cls.getName() + ", problem: " + e2.getMessage());
            return null;
        }
    }

    public static Object defaultValue(Class<?> cls) {
        if (cls == Integer.TYPE) {
            return Integer.valueOf(0);
        }
        if (cls == Long.TYPE) {
            return Long.valueOf(0);
        }
        if (cls == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (cls == Double.TYPE) {
            return Double.valueOf(MediaItem.INVALID_LATLNG);
        }
        if (cls == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (cls == Byte.TYPE) {
            return Byte.valueOf((byte) 0);
        }
        if (cls == Short.TYPE) {
            return Short.valueOf((short) 0);
        }
        if (cls == Character.TYPE) {
            return Character.valueOf('\u0000');
        }
        throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
    }

    public static <T> Constructor<T> findConstructor(Class<T> cls, boolean canFixAccess) throws IllegalArgumentException {
        try {
            Constructor<T> ctor = cls.getDeclaredConstructor(new Class[0]);
            if (canFixAccess) {
                checkAndFixAccess(ctor);
                return ctor;
            } else if (Modifier.isPublic(ctor.getModifiers())) {
                return ctor;
            } else {
                throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
            }
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Exception e2) {
            Exception e3 = e2;
            unwrapAndThrowAsIAE(e3, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e3.getMessage());
            return null;
        }
    }

    public static Class<? extends Enum<?>> findEnumType(Class<?> cls) {
        return cls.getSuperclass() != Enum.class ? cls.getSuperclass() : cls;
    }

    public static Class<? extends Enum<?>> findEnumType(Enum<?> en) {
        Class<?> ec = en.getClass();
        return ec.getSuperclass() != Enum.class ? ec.getSuperclass() : ec;
    }

    public static Class<? extends Enum<?>> findEnumType(EnumMap m) {
        return !m.isEmpty() ? findEnumType((Enum) m.keySet().iterator().next()) : EnumTypeLocator.instance.enumTypeFor(m);
    }

    public static Class<? extends Enum<?>> findEnumType(EnumSet s) {
        return !s.isEmpty() ? findEnumType((Enum) s.iterator().next()) : EnumTypeLocator.instance.enumTypeFor(s);
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
        return findSuperTypes(cls, endBefore, new ArrayList());
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
        _addSuperTypes(cls, endBefore, result, false);
        return result;
    }

    public static String getClassDescription(Object classOrInstance) {
        if (classOrInstance == null) {
            return "unknown";
        }
        return (classOrInstance instanceof Class ? (Class) classOrInstance : classOrInstance.getClass()).getName();
    }

    public static Throwable getRootCause(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    public static boolean hasGetterSignature(Method m) {
        if (Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        Class<?>[] pts = m.getParameterTypes();
        return (pts == null || pts.length == 0) && Void.TYPE != m.getReturnType();
    }

    public static boolean isCollectionMapOrArray(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
    }

    public static boolean isConcrete(Class<?> type) {
        return (type.getModifiers() & 1536) == 0;
    }

    public static boolean isConcrete(Member member) {
        return (member.getModifiers() & 1536) == 0;
    }

    public static String isLocalType(Class<?> type) {
        try {
            if (type.getEnclosingMethod() != null) {
                return "local/anonymous";
            }
            if (!(type.getEnclosingClass() == null || Modifier.isStatic(type.getModifiers()))) {
                return "non-static member class";
            }
            return null;
        } catch (SecurityException e) {
        } catch (NullPointerException e2) {
        }
    }

    public static boolean isProxyType(Class<?> type) {
        if (Proxy.isProxyClass(type)) {
            return true;
        }
        String name = type.getName();
        return name.startsWith("net.sf.cglib.proxy.") || name.startsWith("org.hibernate.proxy.");
    }

    public static void throwAsIAE(Throwable t) {
        throwAsIAE(t, t.getMessage());
    }

    public static void throwAsIAE(Throwable t, String msg) {
        if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        } else if (t instanceof Error) {
            throw ((Error) t);
        } else {
            throw new IllegalArgumentException(msg, t);
        }
    }

    public static void throwRootCause(Throwable t) throws Exception {
        t = getRootCause(t);
        if (t instanceof Exception) {
            throw ((Exception) t);
        }
        throw ((Error) t);
    }

    public static void unwrapAndThrowAsIAE(Throwable t) {
        throwAsIAE(getRootCause(t));
    }

    public static void unwrapAndThrowAsIAE(Throwable t, String msg) {
        throwAsIAE(getRootCause(t), msg);
    }

    public static Class<?> wrapperType(Class<?> primitiveType) {
        if (primitiveType == Integer.TYPE) {
            return Integer.class;
        }
        if (primitiveType == Long.TYPE) {
            return Long.class;
        }
        if (primitiveType == Boolean.TYPE) {
            return Boolean.class;
        }
        if (primitiveType == Double.TYPE) {
            return Double.class;
        }
        if (primitiveType == Float.TYPE) {
            return Float.class;
        }
        if (primitiveType == Byte.TYPE) {
            return Byte.class;
        }
        if (primitiveType == Short.TYPE) {
            return Short.class;
        }
        if (primitiveType == Character.TYPE) {
            return Character.class;
        }
        throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
    }
}