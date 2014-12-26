package org.dom4j.tree;

import org.dom4j.Namespace;
import org.dom4j.QName;

public class FlyweightAttribute extends AbstractAttribute {
    private QName qname;
    protected String value;

    public FlyweightAttribute(String name, String value) {
        this.qname = getDocumentFactory().createQName(name);
        this.value = value;
    }

    public FlyweightAttribute(String name, String value, Namespace namespace) {
        this.qname = getDocumentFactory().createQName(name, namespace);
        this.value = value;
    }

    public FlyweightAttribute(QName qname) {
        this.qname = qname;
    }

    public FlyweightAttribute(QName qname, String value) {
        this.qname = qname;
        this.value = value;
    }

    public QName getQName() {
        return this.qname;
    }

    public String getValue() {
        return this.value;
    }
}