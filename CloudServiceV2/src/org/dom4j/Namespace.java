package org.dom4j;

import org.dom4j.tree.AbstractNode;
import org.dom4j.tree.DefaultNamespace;
import org.dom4j.tree.NamespaceCache;

public class Namespace extends AbstractNode {
    protected static final NamespaceCache CACHE;
    public static final Namespace NO_NAMESPACE;
    public static final Namespace XML_NAMESPACE;
    private int hashCode;
    private String prefix;
    private String uri;

    static {
        CACHE = new NamespaceCache();
        XML_NAMESPACE = CACHE.get("xml", "http://www.w3.org/XML/1998/namespace");
        NO_NAMESPACE = CACHE.get("", "");
    }

    public Namespace(String prefix, String uri) {
        if (prefix == null) {
            prefix = "";
        }
        this.prefix = prefix;
        if (uri == null) {
            uri = "";
        }
        this.uri = uri;
    }

    public static Namespace get(String uri) {
        return CACHE.get(uri);
    }

    public static Namespace get(String prefix, String uri) {
        return CACHE.get(prefix, uri);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        StringBuffer asxml = new StringBuffer(10);
        String pref = getPrefix();
        if (pref == null || pref.length() <= 0) {
            asxml.append("xmlns=\"");
        } else {
            asxml.append("xmlns:");
            asxml.append(pref);
            asxml.append("=\"");
        }
        asxml.append(getURI());
        asxml.append("\"");
        return asxml.toString();
    }

    protected int createHashCode() {
        int result = this.uri.hashCode() ^ this.prefix.hashCode();
        return result == 0 ? 47806 : result;
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultNamespace(parent, getPrefix(), getURI());
    }

    public boolean equals(Namespace object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Namespace) {
            Namespace that = object;
            if (hashCode() == that.hashCode()) {
                return this.uri.equals(that.getURI()) && this.prefix.equals(that.getPrefix());
            }
        }
        return false;
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.NAMESPACE_NODE;
    }

    public String getPath(Element context) {
        StringBuffer path = new StringBuffer(10);
        Element parent = getParent();
        if (!(parent == null || parent == context)) {
            path.append(parent.getPath(context));
            path.append('/');
        }
        path.append(getXPathNameStep());
        return path.toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getStringValue() {
        return this.uri;
    }

    public String getText() {
        return this.uri;
    }

    public String getURI() {
        return this.uri;
    }

    public String getUniquePath(Element context) {
        StringBuffer path = new StringBuffer(10);
        Element parent = getParent();
        if (!(parent == null || parent == context)) {
            path.append(parent.getUniquePath(context));
            path.append('/');
        }
        path.append(getXPathNameStep());
        return path.toString();
    }

    public String getXPathNameStep() {
        return (this.prefix == null || "".equals(this.prefix)) ? "namespace::*[name()='']" : "namespace::" + this.prefix;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = createHashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString() + " [Namespace: prefix " + getPrefix() + " mapped to URI \"" + getURI() + "\"]";
    }
}