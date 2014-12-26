package org.dom4j.tree;

import org.dom4j.Element;

public class DefaultCDATA extends FlyweightCDATA {
    private Element parent;

    public DefaultCDATA(String text) {
        super(text);
    }

    public DefaultCDATA(Element parent, String text) {
        super(text);
        this.parent = parent;
    }

    public Element getParent() {
        return this.parent;
    }

    public boolean isReadOnly() {
        return false;
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