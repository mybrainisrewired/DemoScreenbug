package org.dom4j.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeNodeMap implements NamedNodeMap {
    private DOMElement element;

    public DOMAttributeNodeMap(DOMElement element) {
        this.element = element;
    }

    public void foo() throws DOMException {
        DOMNodeHelper.notSupported();
    }

    public int getLength() {
        return this.element.attributeCount();
    }

    public Node getNamedItem(String name) {
        return this.element.getAttributeNode(name);
    }

    public Node getNamedItemNS(String namespaceURI, String localName) {
        return this.element.getAttributeNodeNS(namespaceURI, localName);
    }

    public Node item(int index) {
        return DOMNodeHelper.asDOMAttr(this.element.attribute(index));
    }

    public Node removeNamedItem(String name) throws DOMException {
        Attr attr = this.element.getAttributeNode(name);
        if (attr != null) {
            return this.element.removeAttributeNode(attr);
        }
        throw new DOMException((short) 8, "No attribute named " + name);
    }

    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        Attr attr = this.element.getAttributeNodeNS(namespaceURI, localName);
        return attr != null ? this.element.removeAttributeNode(attr) : attr;
    }

    public Node setNamedItem(Node arg) throws DOMException {
        if (arg instanceof Attr) {
            return this.element.setAttributeNode((Attr) arg);
        }
        throw new DOMException((short) 9, "Node is not an Attr: " + arg);
    }

    public Node setNamedItemNS(Node arg) throws DOMException {
        if (arg instanceof Attr) {
            return this.element.setAttributeNodeNS((Attr) arg);
        }
        throw new DOMException((short) 9, "Node is not an Attr: " + arg);
    }
}