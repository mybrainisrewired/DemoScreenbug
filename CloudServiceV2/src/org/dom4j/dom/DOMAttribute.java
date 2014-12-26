package org.dom4j.dom;

import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DOMAttribute extends DefaultAttribute implements Attr {
    public DOMAttribute(Element parent, QName qname, String value) {
        super(parent, qname, value);
    }

    public DOMAttribute(QName qname) {
        super(qname);
    }

    public DOMAttribute(QName qname, String value) {
        super(qname, value);
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        int nodeType = newChild.getNodeType();
        if (nodeType != 3 && nodeType != 5) {
            throw new DOMException((short) 3, "The node cannot be a child of attribute");
        }
    }

    public Node appendChild(Node newChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.appendChild(this, newChild);
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public String getBaseURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getChildNodes() {
        return DOMNodeHelper.getChildNodes(this);
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getFirstChild() {
        return DOMNodeHelper.getFirstChild(this);
    }

    public Node getLastChild() {
        return DOMNodeHelper.getLastChild(this);
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
        return DOMNodeHelper.getNodeValue(this);
    }

    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public org.w3c.dom.Element getOwnerElement() {
        return DOMNodeHelper.asDOMElement(getParent());
    }

    public Node getParentNode() {
        return null;
    }

    public String getPrefix() {
        return getQName().getNamespacePrefix();
    }

    public Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getSpecified() {
        return true;
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    public boolean hasChildNodes() {
        return DOMNodeHelper.hasChildNodes(this);
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

    public boolean isId() {
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

    public void normalize() {
        DOMNodeHelper.normalize(this);
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        DOMNodeHelper.setNodeValue(this, nodeValue);
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