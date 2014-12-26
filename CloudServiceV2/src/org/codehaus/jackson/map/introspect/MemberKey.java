package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class MemberKey {
    static final Class<?>[] NO_CLASSES;
    final Class<?>[] _argTypes;
    final String _name;

    static {
        NO_CLASSES = new Class[0];
    }

    public MemberKey(String name, Class<?>[] argTypes) {
        this._name = name;
        if (argTypes == null) {
            argTypes = NO_CLASSES;
        }
        this._argTypes = argTypes;
    }

    public MemberKey(Constructor<?> ctor) {
        this("", ctor.getParameterTypes());
    }

    public MemberKey(Method m) {
        this(m.getName(), m.getParameterTypes());
    }

    public boolean equals(MemberKey o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        MemberKey other = o;
        if (!this._name.equals(other._name)) {
            return false;
        }
        Class<?>[] otherArgs = other._argTypes;
        int len = this._argTypes.length;
        if (otherArgs.length != len) {
            return false;
        }
        int i = 0;
        while (i < len) {
            Class<?> type1 = otherArgs[i];
            Class<?> type2 = this._argTypes[i];
            if (type1 != type2 && !type1.isAssignableFrom(type2) && !type2.isAssignableFrom(type1)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public int hashCode() {
        return this._name.hashCode() + this._argTypes.length;
    }

    public String toString() {
        return this._name + "(" + this._argTypes.length + "-args)";
    }
}