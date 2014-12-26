package org.dom4j.bean;

import java.util.AbstractList;
import org.dom4j.Attribute;
import org.dom4j.QName;

public class BeanAttributeList extends AbstractList {
    private BeanAttribute[] attributes;
    private BeanMetaData beanMetaData;
    private BeanElement parent;

    public BeanAttributeList(BeanElement parent) {
        this.parent = parent;
        Object data = parent.getData();
        this.beanMetaData = BeanMetaData.get(data != null ? data.getClass() : null);
        this.attributes = new BeanAttribute[this.beanMetaData.attributeCount()];
    }

    public BeanAttributeList(BeanElement parent, BeanMetaData beanMetaData) {
        this.parent = parent;
        this.beanMetaData = beanMetaData;
        this.attributes = new BeanAttribute[beanMetaData.attributeCount()];
    }

    public void add(int index, Object object) {
        throw new UnsupportedOperationException("add(int,Object) unsupported");
    }

    public boolean add(Object object) {
        throw new UnsupportedOperationException("add(Object) unsupported");
    }

    public Attribute attribute(String name) {
        return attribute(this.beanMetaData.getIndex(name));
    }

    public Attribute attribute(QName qname) {
        return attribute(this.beanMetaData.getIndex(qname));
    }

    public BeanAttribute attribute(int index) {
        if (index < 0 || index > this.attributes.length) {
            return null;
        }
        BeanAttribute attribute = this.attributes[index];
        if (attribute != null) {
            return attribute;
        }
        attribute = createAttribute(this.parent, index);
        this.attributes[index] = attribute;
        return attribute;
    }

    public void clear() {
        int i = 0;
        int size = this.attributes.length;
        while (i < size) {
            BeanAttribute attribute = this.attributes[i];
            if (attribute != null) {
                attribute.setValue(null);
            }
            i++;
        }
    }

    protected BeanAttribute createAttribute(BeanElement element, int index) {
        return new BeanAttribute(this, index);
    }

    public Object get(int index) {
        BeanAttribute attribute = this.attributes[index];
        if (attribute != null) {
            return attribute;
        }
        attribute = createAttribute(this.parent, index);
        this.attributes[index] = attribute;
        return attribute;
    }

    public Object getData(int index) {
        return this.beanMetaData.getData(index, this.parent.getData());
    }

    public BeanElement getParent() {
        return this.parent;
    }

    public QName getQName(int index) {
        return this.beanMetaData.getQName(index);
    }

    public Object remove(int index) {
        BeanAttribute attribute = (BeanAttribute) get(index);
        String oldValue = attribute.getValue();
        attribute.setValue(null);
        return oldValue;
    }

    public boolean remove(Object object) {
        return false;
    }

    public Object set(int index, Object object) {
        throw new UnsupportedOperationException("set(int,Object) unsupported");
    }

    public void setData(int index, Object data) {
        this.beanMetaData.setData(index, this.parent.getData(), data);
    }

    public int size() {
        return this.attributes.length;
    }
}