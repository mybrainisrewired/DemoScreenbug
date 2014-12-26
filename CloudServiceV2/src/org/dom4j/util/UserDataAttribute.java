package org.dom4j.util;

import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;

public class UserDataAttribute extends DefaultAttribute {
    private Object data;

    public UserDataAttribute(QName qname) {
        super(qname);
    }

    public UserDataAttribute(QName qname, String text) {
        super(qname, text);
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}