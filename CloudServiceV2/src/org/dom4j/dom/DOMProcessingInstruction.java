package org.dom4j.dom;

import java.util.Map;
import org.dom4j.Element;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.UserDataHandler;

public class DOMProcessingInstruction extends DefaultProcessingInstruction implements ProcessingInstruction {
    public DOMProcessingInstruction(String target, String values) {
        super(target, values);
    }

    public DOMProcessingInstruction(String target, Map values) {
        super(target, values);
    }

    public DOMProcessingInstruction(Element parent, String target, String val) {
        super(parent, target, val);
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        throw new DOMException((short) 3, "PI nodes cannot have children");
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

    public String getData() {
        return getText();
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
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNamespaceURI() {
        return DOMNodeHelper.getNamespaceURI(this);
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

    public void setData(String data) throws DOMException {
        if (isReadOnly()) {
            throw new DOMException((short) 7, "This ProcessingInstruction is read only");
        }
        setText(data);
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