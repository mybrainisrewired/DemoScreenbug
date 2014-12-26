package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Namespace;

public class DefaultNamespace extends Namespace {
    private Element parent;

    public DefaultNamespace(String prefix, String uri) {
        super(prefix, uri);
    }

    public DefaultNamespace(Element parent, String prefix, String uri) {
        super(prefix, uri);
        this.parent = parent;
    }

    protected int createHashCode() {
        int hashCode = super.createHashCode();
        return this.parent != null ? hashCode ^ this.parent.hashCode() : hashCode;
    }

    public boolean equals(Object object) {
        return (object instanceof DefaultNamespace && ((DefaultNamespace) object).parent == this.parent) ? super.equals(object) : false;
    }

    public Element getParent() {
        return this.parent;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean isReadOnly() {
        return false;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public boolean supportsParent() {
        return true;
    }
}