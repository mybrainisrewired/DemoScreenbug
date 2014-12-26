package org.dom4j.bean;

import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.Attributes;

public class BeanElement extends DefaultElement {
    private static final DocumentFactory DOCUMENT_FACTORY;
    private Object bean;

    static {
        DOCUMENT_FACTORY = BeanDocumentFactory.getInstance();
    }

    public BeanElement(String name, Object bean) {
        this(DOCUMENT_FACTORY.createQName(name), bean);
    }

    public BeanElement(String name, Namespace namespace, Object bean) {
        this(DOCUMENT_FACTORY.createQName(name, namespace), bean);
    }

    public BeanElement(QName qname) {
        super(qname);
    }

    public BeanElement(QName qname, Object bean) {
        super(qname);
        this.bean = bean;
    }

    public Element addAttribute(String name, String value) {
        Attribute attribute = attribute(name);
        if (attribute != null) {
            attribute.setValue(value);
        }
        return this;
    }

    public Element addAttribute(QName qName, String value) {
        Attribute attribute = attribute(qName);
        if (attribute != null) {
            attribute.setValue(value);
        }
        return this;
    }

    public Attribute attribute(String name) {
        return getBeanAttributeList().attribute(name);
    }

    public Attribute attribute(QName qname) {
        return getBeanAttributeList().attribute(qname);
    }

    protected List createAttributeList() {
        return new BeanAttributeList(this);
    }

    protected List createAttributeList(int size) {
        return new BeanAttributeList(this);
    }

    protected BeanAttributeList getBeanAttributeList() {
        return (BeanAttributeList) attributeList();
    }

    public Object getData() {
        return this.bean;
    }

    protected DocumentFactory getDocumentFactory() {
        return DOCUMENT_FACTORY;
    }

    public void setAttributes(List attributes) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void setAttributes(Attributes attributes, NamespaceStack namespaceStack, boolean noNamespaceAttributes) {
        String className = attributes.getValue("class");
        if (className != null) {
            try {
                setData(Class.forName(className, true, BeanElement.class.getClassLoader()).newInstance());
                int i = 0;
                while (i < attributes.getLength()) {
                    String attributeName = attributes.getLocalName(i);
                    if (!"class".equalsIgnoreCase(attributeName)) {
                        addAttribute(attributeName, attributes.getValue(i));
                    }
                    i++;
                }
            } catch (Exception e) {
                ((BeanDocumentFactory) getDocumentFactory()).handleException(e);
            }
        } else {
            super.setAttributes(attributes, namespaceStack, noNamespaceAttributes);
        }
    }

    public void setData(Object data) {
        this.bean = data;
        setAttributeList(null);
    }
}