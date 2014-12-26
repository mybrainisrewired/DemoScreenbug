package com.millennialmedia.google.gson.internal;

import com.millennialmedia.google.gson.InstanceCreator;
import com.millennialmedia.google.gson.JsonIOException;
import com.millennialmedia.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;

    class AnonymousClass_12 implements ObjectConstructor<T> {
        private final UnsafeAllocator unsafeAllocator;
        final /* synthetic */ Class val$rawType;
        final /* synthetic */ Type val$type;

        AnonymousClass_12(Class cls, Type type) {
            this.val$rawType = cls;
            this.val$type = type;
            this.unsafeAllocator = UnsafeAllocator.create();
        }

        public T construct() {
            try {
                return this.unsafeAllocator.newInstance(this.val$rawType);
            } catch (Exception e) {
                throw new RuntimeException("Unable to invoke no-args constructor for " + this.val$type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", e);
            }
        }
    }

    class AnonymousClass_1 implements ObjectConstructor<T> {
        final /* synthetic */ Type val$type;
        final /* synthetic */ InstanceCreator val$typeCreator;

        AnonymousClass_1(InstanceCreator instanceCreator, Type type) {
            this.val$typeCreator = instanceCreator;
            this.val$type = type;
        }

        public T construct() {
            return this.val$typeCreator.createInstance(this.val$type);
        }
    }

    class AnonymousClass_2 implements ObjectConstructor<T> {
        final /* synthetic */ InstanceCreator val$rawTypeCreator;
        final /* synthetic */ Type val$type;

        AnonymousClass_2(InstanceCreator instanceCreator, Type type) {
            this.val$rawTypeCreator = instanceCreator;
            this.val$type = type;
        }

        public T construct() {
            return this.val$rawTypeCreator.createInstance(this.val$type);
        }
    }

    class AnonymousClass_3 implements ObjectConstructor<T> {
        final /* synthetic */ Constructor val$constructor;

        AnonymousClass_3(Constructor constructor) {
            this.val$constructor = constructor;
        }

        public T construct() {
            try {
                return this.val$constructor.newInstance(null);
            } catch (InstantiationException e) {
                throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", e);
            } catch (InvocationTargetException e2) {
                throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", e2.getTargetException());
            } catch (IllegalAccessException e3) {
                throw new AssertionError(e3);
            }
        }
    }

    class AnonymousClass_5 implements ObjectConstructor<T> {
        final /* synthetic */ Type val$type;

        AnonymousClass_5(Type type) {
            this.val$type = type;
        }

        public T construct() {
            if (this.val$type instanceof ParameterizedType) {
                Type elementType = ((ParameterizedType) this.val$type).getActualTypeArguments()[0];
                if (elementType instanceof Class) {
                    return EnumSet.noneOf((Class) elementType);
                }
                throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
            } else {
                throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
            }
        }
    }

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> rawType) {
        try {
            Constructor<? super T> constructor = rawType.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return new AnonymousClass_3(constructor);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(Type type, Class<? super T> rawType) {
        if (Collection.class.isAssignableFrom(rawType)) {
            if (SortedSet.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(rawType)) {
                return new AnonymousClass_5(type);
            }
            if (Set.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return new LinkedHashSet();
                    }
                };
            }
            return Queue.class.isAssignableFrom(rawType) ? new ObjectConstructor<T>() {
                public T construct() {
                    return new LinkedList();
                }
            } : new ObjectConstructor<T>() {
                public T construct() {
                    return new ArrayList();
                }
            };
        } else if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        } else {
            if (SortedMap.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return new TreeMap();
                    }
                };
            }
            return (!type instanceof ParameterizedType || String.class.isAssignableFrom(TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType())) ? new ObjectConstructor<T>() {
                public T construct() {
                    return new LinkedHashTreeMap();
                }
            } : new ObjectConstructor<T>() {
                public T construct() {
                    return new LinkedHashMap();
                }
            };
        }
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(Type type, Class<? super T> rawType) {
        return new AnonymousClass_12(rawType, type);
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Class<? super T> rawType = typeToken.getRawType();
        InstanceCreator<T> typeCreator = (InstanceCreator) this.instanceCreators.get(type);
        if (typeCreator != null) {
            return new AnonymousClass_1(typeCreator, type);
        }
        InstanceCreator<T> rawTypeCreator = (InstanceCreator) this.instanceCreators.get(rawType);
        if (rawTypeCreator != null) {
            return new AnonymousClass_2(rawTypeCreator, type);
        }
        ObjectConstructor<T> defaultConstructor = newDefaultConstructor(rawType);
        if (defaultConstructor != null) {
            return defaultConstructor;
        }
        ObjectConstructor<T> defaultImplementation = newDefaultImplementationConstructor(type, rawType);
        return defaultImplementation != null ? defaultImplementation : newUnsafeAllocator(type, rawType);
    }

    public String toString() {
        return this.instanceCreators.toString();
    }
}