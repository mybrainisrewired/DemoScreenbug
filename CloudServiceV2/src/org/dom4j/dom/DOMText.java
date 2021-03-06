package org.dom4j.dom;

import org.dom4j.Element;
import org.dom4j.tree.DefaultText;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public class DOMText extends DefaultText implements Text {
    public DOMText(String text) {
        super(text);
    }

    public DOMText(Element parent, String text) {
        super(parent, text);
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        throw new DOMException((short) 3, "Text nodes cannot have children");
    }

    public Node appendChild(Node newChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.appendChild(this, newChild);
    }

    public void appendData(String arg) throws DOMException {
        DOMNodeHelper.appendData(this, arg);
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected org.dom4j.Text createText(String text) {
        return new DOMText(text);
    }

    public void deleteData(int offset, int count) throws DOMException {
        DOMNodeHelper.deleteData(this, offset, count);
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

    public String getData() throws DOMException {
        return DOMNodeHelper.getData(this);
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

    public int getLength() {
        return DOMNodeHelper.getLength(this);
    }

    public String getLocalName() {
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNamespaceURI() {
        return DOMNodeHelper.getNamespaceURI(this);
    }

    public Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public String getNodeName() {
        return "#text";
    }

    public String getNodeValue() throws DOMException {
        return DOMNodeHelper.getNodeValue(this);
    }

    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }

    public String getPrefix() {
        return DOMNodeHelper.getPrefix(this);
    }

    public Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getWholeText() {
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

    public void insertData(int offset, String arg) throws DOMException {
        DOMNodeHelper.insertData(this, offset, arg);
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isElementContentWhitespace() {
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

    public void replaceData(int offset, int count, String arg) throws DOMException {
        DOMNodeHelper.replaceData(this, offset, count, arg);
    }

    public Text replaceWholeText(String content) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setData(String data) throws DOMException {
        DOMNodeHelper.setData(this, data);
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

    public Text splitText(int offset) throws DOMException {
        if (isReadOnly()) {
            throw new DOMException((short) 7, "CharacterData node is read only: " + this);
        }
        String text = getText();
        int length;
        if (text != null) {
            length = text.length();
        } else {
            length = 0;
        }
        if (offset < 0 || offset >= length) {
            throw new DOMException((short) 1, "No text at offset: " + offset);
        }
        String start = text.substring(0, offset);
        String rest = text.substring(offset);
        setText(start);
        Element parent = getParent();
        org.dom4j.Text newText = createText(rest);
        if (parent != null) {
            parent.add(newText);
        }
        return DOMNodeHelper.asDOMText(newText);
    }

    public String substringData(int offset, int count) throws DOMException {
        return DOMNodeHelper.substringData(this, offset, count);
    }

    public boolean supports(String feature, String version) {
        return DOMNodeHelper.supports(this, feature, version);
    }
}