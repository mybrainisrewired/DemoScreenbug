package org.dom4j.util;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

public class IndexedDocumentFactory extends DocumentFactory {
    protected static transient IndexedDocumentFactory singleton;

    static {
        singleton = new IndexedDocumentFactory();
    }

    public static DocumentFactory getInstance() {
        return singleton;
    }

    public Element createElement(QName qname) {
        return new IndexedElement(qname);
    }

    public Element createElement(QName qname, int attributeCount) {
        return new IndexedElement(qname, attributeCount);
    }
}