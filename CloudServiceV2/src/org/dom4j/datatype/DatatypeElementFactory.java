package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.XSDatatype;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

public class DatatypeElementFactory extends DocumentFactory {
    private Map attributeXSDatatypes;
    private Map childrenXSDatatypes;
    private QName elementQName;

    public DatatypeElementFactory(QName elementQName) {
        this.attributeXSDatatypes = new HashMap();
        this.childrenXSDatatypes = new HashMap();
        this.elementQName = elementQName;
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        XSDatatype dataType = getAttributeXSDatatype(qname);
        return dataType == null ? super.createAttribute(owner, qname, value) : new DatatypeAttribute(qname, dataType, value);
    }

    public Element createElement(QName qname) {
        XSDatatype dataType = getChildElementXSDatatype(qname);
        if (dataType != null) {
            return new DatatypeElement(qname, dataType);
        }
        DocumentFactory factory = qname.getDocumentFactory();
        if (factory instanceof DatatypeElementFactory) {
            dataType = ((DatatypeElementFactory) factory).getChildElementXSDatatype(qname);
            if (dataType != null) {
                return new DatatypeElement(qname, dataType);
            }
        }
        return super.createElement(qname);
    }

    public XSDatatype getAttributeXSDatatype(QName attributeQName) {
        return (XSDatatype) this.attributeXSDatatypes.get(attributeQName);
    }

    public XSDatatype getChildElementXSDatatype(QName qname) {
        return (XSDatatype) this.childrenXSDatatypes.get(qname);
    }

    public QName getQName() {
        return this.elementQName;
    }

    public void setAttributeXSDatatype(QName attributeQName, XSDatatype type) {
        this.attributeXSDatatypes.put(attributeQName, type);
    }

    public void setChildElementXSDatatype(QName qname, XSDatatype dataType) {
        this.childrenXSDatatypes.put(qname, dataType);
    }
}