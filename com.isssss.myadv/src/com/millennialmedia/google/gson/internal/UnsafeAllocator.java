package com.millennialmedia.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {

    static class AnonymousClass_1 extends UnsafeAllocator {
        final /* synthetic */ Method val$allocateInstance;
        final /* synthetic */ Object val$unsafe;

        AnonymousClass_1(Method method, Object obj) {
            this.val$allocateInstance = method;
            this.val$unsafe = obj;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$allocateInstance.invoke(this.val$unsafe, new Object[]{c});
        }
    }

    static class AnonymousClass_2 extends UnsafeAllocator {
        final /* synthetic */ Method val$newInstance;

        AnonymousClass_2(Method method) {
            this.val$newInstance = method;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$newInstance.invoke(null, new Object[]{c, Object.class});
        }
    }

    static class AnonymousClass_3 extends UnsafeAllocator {
        final /* synthetic */ int val$constructorId;
        final /* synthetic */ Method val$newInstance;

        AnonymousClass_3(Method method, int i) {
            this.val$newInstance = method;
            this.val$constructorId = i;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$newInstance.invoke(null, new Object[]{c, Integer.valueOf(this.val$constructorId)});
        }
    }

    public static UnsafeAllocator create() {
        Method newInstance;
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return new AnonymousClass_1(unsafeClass.getMethod("allocateInstance", new Class[]{Class.class}), f.get(null));
        } catch (Exception e) {
            try {
                newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Class.class});
                newInstance.setAccessible(true);
                return new AnonymousClass_2(newInstance);
            } catch (Exception e2) {
                try {
                    Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[]{Class.class});
                    getConstructorId.setAccessible(true);
                    int constructorId = ((Integer) getConstructorId.invoke(null, new Object[]{Object.class})).intValue();
                    newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Integer.TYPE});
                    newInstance.setAccessible(true);
                    return new AnonymousClass_3(newInstance, constructorId);
                } catch (Exception e3) {
                    return new UnsafeAllocator() {
                        public <T> T newInstance(Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + c);
                        }
                    };
                }
            }
        }
    }

    public abstract <T> T newInstance(Class<T> cls) throws Exception;
}