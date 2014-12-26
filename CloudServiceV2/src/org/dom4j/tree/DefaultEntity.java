package org.dom4j.tree;

import org.dom4j.Element;

public class DefaultEntity extends FlyweightEntity {
    private Element parent;

    public DefaultEntity(String name) {
        super(name);
    }

    public DefaultEntity(String name, String text) {
        super(name, text);
    }

    public DefaultEntity(Element parent, String name, String text) {
        super(name, text);
        this.parent = parent;
    }

    public Element getParent() {
        return this.parent;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean supportsParent() {
        return true;
    }
}