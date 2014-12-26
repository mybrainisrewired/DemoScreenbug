package org.dom4j.util;

import java.util.HashMap;
import java.util.Map;
import org.dom4j.QName;

public class DoubleNameMap<T> {
    private Map<String, T> namedMap;
    private Map<QName, T> qNamedMap;

    public DoubleNameMap() {
        this.namedMap = new HashMap();
        this.qNamedMap = new HashMap();
    }

    public T get(String name) {
        return this.namedMap.get(name);
    }

    public T get(QName qName) {
        return this.qNamedMap.get(qName);
    }

    public void put(QName qName, T value) {
        this.qNamedMap.put(qName, value);
        this.namedMap.put(qName.getName(), value);
    }

    public void remove(QName qName) {
        this.qNamedMap.remove(qName);
        this.namedMap.remove(qName.getName());
    }
}