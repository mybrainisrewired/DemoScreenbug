package org.dom4j.util;

import java.lang.ref.WeakReference;

public class PerThreadSingleton implements SingletonStrategy {
    private ThreadLocal perThreadCache;
    private String singletonClassName;

    public PerThreadSingleton() {
        this.singletonClassName = null;
        this.perThreadCache = new ThreadLocal();
    }

    public Object instance() {
        Object singletonInstancePerThread = null;
        WeakReference ref = (WeakReference) this.perThreadCache.get();
        if (ref != null && ref.get() != null) {
            return ref.get();
        }
        try {
            singletonInstancePerThread = Thread.currentThread().getContextClassLoader().loadClass(this.singletonClassName).newInstance();
        } catch (Exception e) {
            try {
                singletonInstancePerThread = Class.forName(this.singletonClassName).newInstance();
            } catch (Exception e2) {
            }
        }
        this.perThreadCache.set(new WeakReference(singletonInstancePerThread));
        return singletonInstancePerThread;
    }

    public void reset() {
        this.perThreadCache = new ThreadLocal();
    }

    public void setSingletonClassName(String singletonClassName) {
        this.singletonClassName = singletonClassName;
    }
}