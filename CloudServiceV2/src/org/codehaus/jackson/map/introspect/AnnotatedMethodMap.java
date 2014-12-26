package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

public final class AnnotatedMethodMap implements Iterable<AnnotatedMethod> {
    LinkedHashMap<MemberKey, AnnotatedMethod> _methods;

    public void add(AnnotatedMethod am) {
        if (this._methods == null) {
            this._methods = new LinkedHashMap();
        }
        this._methods.put(new MemberKey(am.getAnnotated()), am);
    }

    public AnnotatedMethod find(String name, Class<?>[] paramTypes) {
        return this._methods == null ? null : (AnnotatedMethod) this._methods.get(new MemberKey(name, paramTypes));
    }

    public AnnotatedMethod find(Method m) {
        return this._methods == null ? null : (AnnotatedMethod) this._methods.get(new MemberKey(m));
    }

    public boolean isEmpty() {
        return this._methods == null || this._methods.size() == 0;
    }

    public Iterator<AnnotatedMethod> iterator() {
        return this._methods != null ? this._methods.values().iterator() : Collections.emptyList().iterator();
    }

    public AnnotatedMethod remove(Method m) {
        return this._methods != null ? (AnnotatedMethod) this._methods.remove(new MemberKey(m)) : null;
    }

    public AnnotatedMethod remove(AnnotatedMethod am) {
        return remove(am.getAnnotated());
    }

    public int size() {
        return this._methods == null ? 0 : this._methods.size();
    }
}