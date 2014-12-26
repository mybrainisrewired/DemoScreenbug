package org.dom4j.util;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

public class NonLazyDocumentFactory extends DocumentFactory {
    protected static transient NonLazyDocumentFactory singleton;

    static {
        singleton = new NonLazyDocumentFactory();
    }

    public static DocumentFactory getInstance() {
        return singleton;
    }

    public Element createElement(QName qname) {
        return new NonLazyElement(qname);
    }
}