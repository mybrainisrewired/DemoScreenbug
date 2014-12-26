package org.dom4j.datatype;

import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.SerializationContext;
import com.sun.msv.datatype.xsd.XSDatatype;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.AbstractAttribute;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

public class DatatypeAttribute extends AbstractAttribute implements SerializationContext, ValidationContext {
    private Object data;
    private XSDatatype datatype;
    private Element parent;
    private QName qname;
    private String text;

    public DatatypeAttribute(QName qname, XSDatatype datatype) {
        this.qname = qname;
        this.datatype = datatype;
    }

    public DatatypeAttribute(QName qname, XSDatatype datatype, String text) {
        this.qname = qname;
        this.datatype = datatype;
        this.text = text;
        this.data = convertToValue(text);
    }

    protected Object convertToValue(String txt) {
        return this.datatype instanceof DatabindableDatatype ? this.datatype.createJavaObject(txt, this) : this.datatype.createValue(txt, this);
    }

    public String getBaseUri() {
        return null;
    }

    public Object getData() {
        return this.data;
    }

    public String getNamespacePrefix(String uri) {
        Element parentElement = getParent();
        if (parentElement != null) {
            Namespace namespace = parentElement.getNamespaceForURI(uri);
            if (namespace != null) {
                return namespace.getPrefix();
            }
        }
        return null;
    }

    public Element getParent() {
        return this.parent;
    }

    public QName getQName() {
        return this.qname;
    }

    public String getValue() {
        return this.text;
    }

    public XSDatatype getXSDatatype() {
        return this.datatype;
    }

    public boolean isNotation(String notationName) {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isUnparsedEntity(String entityName) {
        return true;
    }

    public String resolveNamespacePrefix(String prefix) {
        if (prefix.equals(getNamespacePrefix())) {
            return getNamespaceURI();
        }
        Element parentElement = getParent();
        if (parentElement != null) {
            Namespace namespace = parentElement.getNamespaceForPrefix(prefix);
            if (namespace != null) {
                return namespace.getURI();
            }
        }
        return null;
    }

    public void setData(Object data) {
        String s = this.datatype.convertToLexicalValue(data, this);
        validate(s);
        this.text = s;
        this.data = data;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public void setValue(String value) {
        validate(value);
        this.text = value;
        this.data = convertToValue(value);
    }

    public boolean supportsParent() {
        return true;
    }

    public String toString() {
        return getClass().getName() + hashCode() + " [Attribute: name " + getQualifiedName() + " value \"" + getValue() + "\" data: " + getData() + "]";
    }

    protected void validate(String txt) throws IllegalArgumentException {
        try {
            this.datatype.checkValid(txt, this);
        } catch (DatatypeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}