package org.dom4j.xpp;

import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.AbstractElement;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlStartTag;

public class ProxyXmlStartTag implements XmlStartTag {
    private Element element;
    private DocumentFactory factory;

    public ProxyXmlStartTag() {
        this.factory = DocumentFactory.getInstance();
    }

    public ProxyXmlStartTag(Element element) {
        this.factory = DocumentFactory.getInstance();
        this.element = element;
    }

    public void addAttribute(String namespaceURI, String localName, String rawName, String value) throws XmlPullParserException {
        this.element.addAttribute(QName.get(rawName, namespaceURI), value);
    }

    public void addAttribute(String namespaceURI, String localName, String rawName, String value, boolean isNamespaceDeclaration) throws XmlPullParserException {
        if (isNamespaceDeclaration) {
            String prefix = "";
            int idx = rawName.indexOf(Opcodes.ASTORE);
            if (idx > 0) {
                prefix = rawName.substring(0, idx);
            }
            this.element.addNamespace(prefix, namespaceURI);
        } else {
            this.element.addAttribute(QName.get(rawName, namespaceURI), value);
        }
    }

    public void ensureAttributesCapacity(int minCapacity) throws XmlPullParserException {
        if (this.element instanceof AbstractElement) {
            ((AbstractElement) this.element).ensureAttributesCapacity(minCapacity);
        }
    }

    public int getAttributeCount() {
        return this.element != null ? this.element.attributeCount() : 0;
    }

    public String getAttributeLocalName(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                return attribute.getName();
            }
        }
        return null;
    }

    public String getAttributeNamespaceUri(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                return attribute.getNamespaceURI();
            }
        }
        return null;
    }

    public String getAttributePrefix(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                String prefix = attribute.getNamespacePrefix();
                if (prefix != null && prefix.length() > 0) {
                    return prefix;
                }
            }
        }
        return null;
    }

    public String getAttributeRawName(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                return attribute.getQualifiedName();
            }
        }
        return null;
    }

    public String getAttributeValue(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                return attribute.getValue();
            }
        }
        return null;
    }

    public String getAttributeValueFromName(String namespaceURI, String localName) {
        if (this.element != null) {
            Iterator iter = this.element.attributeIterator();
            while (iter.hasNext()) {
                Attribute attribute = (Attribute) iter.next();
                if (namespaceURI.equals(attribute.getNamespaceURI()) && localName.equals(attribute.getName())) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

    public String getAttributeValueFromRawName(String rawName) {
        if (this.element != null) {
            Iterator iter = this.element.attributeIterator();
            while (iter.hasNext()) {
                Attribute attribute = (Attribute) iter.next();
                if (rawName.equals(attribute.getQualifiedName())) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

    public DocumentFactory getDocumentFactory() {
        return this.factory;
    }

    public Element getElement() {
        return this.element;
    }

    public String getLocalName() {
        return this.element.getName();
    }

    public String getNamespaceUri() {
        return this.element.getNamespaceURI();
    }

    public String getPrefix() {
        return this.element.getNamespacePrefix();
    }

    public String getRawName() {
        return this.element.getQualifiedName();
    }

    public boolean isAttributeNamespaceDeclaration(int index) {
        if (this.element != null) {
            Attribute attribute = this.element.attribute(index);
            if (attribute != null) {
                return "xmlns".equals(attribute.getNamespacePrefix());
            }
        }
        return false;
    }

    public void modifyTag(String namespaceURI, String lName, String rawName) {
        this.element = this.factory.createElement(rawName, namespaceURI);
    }

    public boolean removeAttributeByName(String arg0, String arg1) throws XmlPullParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAttributeByRawName(String arg0) throws XmlPullParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttributes() throws XmlPullParserException {
        if (this.element != null) {
            this.element.setAttributes(new ArrayList());
        }
    }

    public void resetStartTag() {
        this.element = null;
    }

    public void resetTag() {
        this.element = null;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.factory = documentFactory;
    }
}