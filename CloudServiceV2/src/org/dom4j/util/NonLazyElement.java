package org.dom4j.util;

import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.BaseElement;

public class NonLazyElement extends BaseElement {
    public NonLazyElement(String name) {
        super(name);
        this.attributes = createAttributeList();
        this.content = createContentList();
    }

    public NonLazyElement(String name, Namespace namespace) {
        super(name, namespace);
        this.attributes = createAttributeList();
        this.content = createContentList();
    }

    public NonLazyElement(QName qname) {
        super(qname);
        this.attributes = createAttributeList();
        this.content = createContentList();
    }

    public NonLazyElement(QName qname, int attributeCount) {
        super(qname);
        this.attributes = createAttributeList(attributeCount);
        this.content = createContentList();
    }
}