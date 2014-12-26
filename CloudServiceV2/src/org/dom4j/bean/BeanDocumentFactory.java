package org.dom4j.bean;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;
import org.xml.sax.Attributes;

public class BeanDocumentFactory extends DocumentFactory {
    private static BeanDocumentFactory singleton;

    static {
        singleton = new BeanDocumentFactory();
    }

    public static DocumentFactory getInstance() {
        return singleton;
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DefaultAttribute(qname, value);
    }

    protected Object createBean(QName qname) {
        return null;
    }

    protected Object createBean(QName qname, Attributes attributes) {
        String value = attributes.getValue("class");
        if (value != null) {
            try {
                return Class.forName(value, true, BeanDocumentFactory.class.getClassLoader()).newInstance();
            } catch (Exception e) {
                handleException(e);
            }
        }
        return null;
    }

    public Element createElement(QName qname) {
        Object bean = createBean(qname);
        return bean == null ? new BeanElement(qname) : new BeanElement(qname, bean);
    }

    public Element createElement(QName qname, Attributes attributes) {
        Object bean = createBean(qname, attributes);
        return bean == null ? new BeanElement(qname) : new BeanElement(qname, bean);
    }

    protected void handleException(Exception e) {
        System.out.println("#### Warning: couldn't create bean: " + e);
    }
}