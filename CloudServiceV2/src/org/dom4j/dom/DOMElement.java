package org.dom4j.dom;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DOMElement extends DefaultElement implements Element {
    private static final DocumentFactory DOCUMENT_FACTORY;

    static {
        DOCUMENT_FACTORY = DOMDocumentFactory.getInstance();
    }

    public DOMElement(String name) {
        super(name);
    }

    public DOMElement(String name, Namespace namespace) {
        super(name, namespace);
    }

    public DOMElement(QName qname) {
        super(qname);
    }

    public DOMElement(QName qname, int attributeCount) {
        super(qname, attributeCount);
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        int nodeType = newChild.getNodeType();
        if (nodeType != 1 && nodeType != 3 && nodeType != 8 && nodeType != 7 && nodeType != 4 && nodeType != 5) {
            throw new DOMException((short) 3, "Given node cannot be a child of element");
        }
    }

    public Node appendChild(Node newChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.appendChild(this, newChild);
    }

    protected Attribute attribute(String namespaceURI, String localName) {
        List attributes = attributeList();
        int size = attributes.size();
        int i = 0;
        while (i < size) {
            Attribute attribute = (Attribute) attributes.get(i);
            if (localName.equals(attribute.getName())) {
                if ((namespaceURI == null || namespaceURI.length() == 0) && (attribute.getNamespaceURI() == null || attribute.getNamespaceURI().length() == 0)) {
                    return attribute;
                }
                if (namespaceURI != null && namespaceURI.equals(attribute.getNamespaceURI())) {
                    return attribute;
                }
            }
            i++;
        }
        return null;
    }

    protected Attribute attribute(Attr attr) {
        return attribute(DOCUMENT_FACTORY.createQName(attr.getLocalName(), attr.getPrefix(), attr.getNamespaceURI()));
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Attribute createAttribute(Attr newAttr) {
        QName qname;
        String name = newAttr.getLocalName();
        if (name != null) {
            qname = getDocumentFactory().createQName(name, newAttr.getPrefix(), newAttr.getNamespaceURI());
        } else {
            qname = getDocumentFactory().createQName(newAttr.getName());
        }
        return new DOMAttribute(qname, newAttr.getValue());
    }

    public String getAttribute(String name) {
        String answer = attributeValue(name);
        return answer != null ? answer : "";
    }

    public String getAttributeNS(String namespaceURI, String localName) {
        Attribute attribute = attribute(namespaceURI, localName);
        if (attribute != null) {
            String answer = attribute.getValue();
            if (answer != null) {
                return answer;
            }
        }
        return "";
    }

    public Attr getAttributeNode(String name) {
        return DOMNodeHelper.asDOMAttr(attribute(name));
    }

    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        Attribute attribute = attribute(namespaceURI, localName);
        if (attribute != null) {
            DOMNodeHelper.asDOMAttr(attribute);
        }
        return null;
    }

    public NamedNodeMap getAttributes() {
        return new DOMAttributeNodeMap(this);
    }

    public String getBaseURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getChildNodes() {
        return DOMNodeHelper.createNodeList(content());
    }

    protected DocumentFactory getDocumentFactory() {
        DocumentFactory factory = getQName().getDocumentFactory();
        return factory != null ? factory : DOCUMENT_FACTORY;
    }

    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagName(list, this, name);
        return DOMNodeHelper.createNodeList(list);
    }

    public NodeList getElementsByTagNameNS(String namespace, String lName) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagNameNS(list, this, namespace, lName);
        return DOMNodeHelper.createNodeList(list);
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getFirstChild() {
        return DOMNodeHelper.asDOMNode(node(0));
    }

    public Node getLastChild() {
        return DOMNodeHelper.asDOMNode(node(nodeCount() - 1));
    }

    public String getLocalName() {
        return getQName().getName();
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public String getNodeName() {
        return getName();
    }

    public String getNodeValue() throws DOMException {
        return null;
    }

    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }

    public String getPrefix() {
        return getQName().getNamespacePrefix();
    }

    public Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    protected QName getQName(String namespace, String qualifiedName) {
        int index = qualifiedName.indexOf(Opcodes.ASTORE);
        String str = "";
        String str2 = qualifiedName;
        if (index >= 0) {
            str = qualifiedName.substring(0, index);
            str2 = qualifiedName.substring(index + 1);
        }
        return getDocumentFactory().createQName(str2, str, namespace);
    }

    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTagName() {
        return getName();
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttribute(String name) {
        return attribute(name) != null;
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return attribute(namespaceURI, localName) != null;
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    public boolean hasChildNodes() {
        return nodeCount() > 0;
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEqualNode(Node arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSameNode(Node other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public String lookupNamespaceURI(String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String name) throws DOMException {
        Attribute attribute = attribute(name);
        if (attribute != null) {
            remove(attribute);
        }
    }

    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        Attribute attribute = attribute(namespaceURI, localName);
        if (attribute != null) {
            remove(attribute);
        }
    }

    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        Attribute attribute = attribute(oldAttr);
        if (attribute != null) {
            attribute.detach();
            return DOMNodeHelper.asDOMAttr(attribute);
        } else {
            throw new DOMException((short) 8, "No such attribute");
        }
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public void setAttribute(String name, String value) throws DOMException {
        addAttribute(name, value);
    }

    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        Attribute attribute = attribute(namespaceURI, qualifiedName);
        if (attribute != null) {
            attribute.setValue(value);
        } else {
            addAttribute(getQName(namespaceURI, qualifiedName), value);
        }
    }

    public Attr setAttributeNode(Attribute newAttr) throws DOMException {
        if (isReadOnly()) {
            throw new DOMException((short) 7, "No modification allowed");
        }
        Attribute attribute = attribute(newAttr);
        if (attribute == newAttr || newAttr.getOwnerElement() == null) {
            Attribute newAttribute = createAttribute(newAttr);
            if (attribute != null) {
                attribute.detach();
            }
            add(newAttribute);
        } else {
            throw new DOMException((short) 10, "Attribute is already in use");
        }
        return DOMNodeHelper.asDOMAttr(attribute);
    }

    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        Attribute attribute = attribute(newAttr.getNamespaceURI(), newAttr.getLocalName());
        if (attribute != null) {
            attribute.setValue(newAttr.getValue());
        } else {
            attribute = createAttribute(newAttr);
            add(attribute);
        }
        return DOMNodeHelper.asDOMAttr(attribute);
    }

    public void setIdAttribute(String name, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNodeValue(String nodeValue) throws DOMException {
    }

    public void setPrefix(String prefix) throws DOMException {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public void setTextContent(String textContent) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean supports(String feature, String version) {
        return DOMNodeHelper.supports(this, feature, version);
    }
}