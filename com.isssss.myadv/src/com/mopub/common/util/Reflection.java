package com.mopub.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

    public static class MethodBuilder {
        private Class<?> mClass;
        private final Object mInstance;
        private boolean mIsAccessible;
        private boolean mIsStatic;
        private final String mMethodName;
        private List<Class<?>> mParameterClasses;
        private List<Object> mParameters;

        public MethodBuilder(Object instance, String methodName) {
            this.mInstance = instance;
            this.mMethodName = methodName;
            this.mParameterClasses = new ArrayList();
            this.mParameters = new ArrayList();
            this.mClass = instance != null ? instance.getClass() : null;
        }

        public <T> com.mopub.common.util.Reflection.MethodBuilder addParam(Class<T> clazz, T parameter) {
            this.mParameterClasses.add(clazz);
            this.mParameters.add(parameter);
            return this;
        }

        public Object execute() throws Exception {
            Method method = Reflection.getDeclaredMethodWithTraversal(this.mClass, this.mMethodName, (Class[]) this.mParameterClasses.toArray(new Class[this.mParameterClasses.size()]));
            if (this.mIsAccessible) {
                method.setAccessible(true);
            }
            Object[] parameters = this.mParameters.toArray();
            return this.mIsStatic ? method.invoke(null, parameters) : method.invoke(this.mInstance, parameters);
        }

        public com.mopub.common.util.Reflection.MethodBuilder setAccessible() {
            this.mIsAccessible = true;
            return this;
        }

        public com.mopub.common.util.Reflection.MethodBuilder setStatic(Class<?> clazz) {
            this.mIsStatic = true;
            this.mClass = clazz;
            return this;
        }
    }

    public static boolean classFound(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Method getDeclaredMethodWithTraversal(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
}