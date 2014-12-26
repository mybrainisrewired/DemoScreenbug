package org.dom4j.util;

public class SimpleSingleton implements SingletonStrategy {
    private String singletonClassName;
    private Object singletonInstance;

    public SimpleSingleton() {
        this.singletonClassName = null;
        this.singletonInstance = null;
    }

    public Object instance() {
        return this.singletonInstance;
    }

    public void reset() {
        if (this.singletonClassName != null) {
            try {
                this.singletonInstance = Thread.currentThread().getContextClassLoader().loadClass(this.singletonClassName).newInstance();
            } catch (Exception e) {
                try {
                    this.singletonInstance = Class.forName(this.singletonClassName).newInstance();
                } catch (Exception e2) {
                }
            }
        }
    }

    public void setSingletonClassName(String singletonClassName) {
        this.singletonClassName = singletonClassName;
        reset();
    }
}