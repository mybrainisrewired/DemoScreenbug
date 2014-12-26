package org.dom4j.dom;

import java.util.List;
import org.dom4j.Branch;
import org.dom4j.CharacterData;
import org.dom4j.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMNodeHelper {
    public static final NodeList EMPTY_NODE_LIST;

    static class AnonymousClass_1 implements NodeList {
        final /* synthetic */ List val$list;

        AnonymousClass_1(List list) {
            this.val$list = list;
        }

        public int getLength() {
            return this.val$list.size();
        }

        public Node item(int index) {
            return index >= getLength() ? null : DOMNodeHelper.asDOMNode((org.dom4j.Node) this.val$list.get(index));
        }
    }

    public static class EmptyNodeList implements NodeList {
        public int getLength() {
            return 0;
        }

        public Node item(int index) {
            return null;
        }
    }

    static {
        EMPTY_NODE_LIST = new EmptyNodeList();
    }

    protected DOMNodeHelper() {
    }

    public static Node appendChild(org.dom4j.Node node, Node newChild) throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            Node previousParent = newChild.getParentNode();
            if (previousParent != null) {
                previousParent.removeChild(newChild);
            }
            branch.add((org.dom4j.Node) newChild);
            return newChild;
        } else {
            throw new DOMException((short) 3, "Children not allowed for this node: " + node);
        }
    }

    public static void appendData(CharacterData charData, String arg) throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException((short) 7, "CharacterData node is read only: " + charData);
        }
        String text = charData.getText();
        if (text == null) {
            charData.setText(text);
        } else {
            charData.setText(text + arg);
        }
    }

    public static void appendElementsByTagName(List list, Branch parent, String name) {
        boolean isStar = "*".equals(name);
        int i = 0;
        int size = parent.nodeCount();
        while (i < size) {
            org.dom4j.Node node = parent.node(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (isStar || name.equals(element.getName())) {
                    list.add(element);
                }
                appendElementsByTagName(list, element, name);
            }
            i++;
        }
    }

    public static void appendElementsByTagNameNS(List list, Branch parent, String namespace, String localName) {
        boolean isStarNS = "*".equals(namespace);
        boolean isStar = "*".equals(localName);
        int i = 0;
        int size = parent.nodeCount();
        while (i < size) {
            org.dom4j.Node node = parent.node(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (!isStarNS) {
                    if (namespace != null) {
                    }
                }
                if (isStar || localName.equals(element.getName())) {
                    list.add(element);
                }
                appendElementsByTagNameNS(list, element, namespace, localName);
            }
            i++;
        }
    }

    public static Attr asDOMAttr(org.dom4j.Node attribute) {
        if (attribute == null) {
            return null;
        }
        if (attribute instanceof Attr) {
            return (Attr) attribute;
        }
        notSupported();
        return null;
    }

    public static Document asDOMDocument(org.dom4j.Document document) {
        if (document == null) {
            return null;
        }
        if (document instanceof Document) {
            return (Document) document;
        }
        notSupported();
        return null;
    }

    public static DocumentType asDOMDocumentType(org.dom4j.DocumentType dt) {
        if (dt == null) {
            return null;
        }
        if (dt instanceof DocumentType) {
            return (DocumentType) dt;
        }
        notSupported();
        return null;
    }

    public static org.w3c.dom.Element asDOMElement(org.dom4j.Node element) {
        if (element == null) {
            return null;
        }
        if (element instanceof org.w3c.dom.Element) {
            return (org.w3c.dom.Element) element;
        }
        notSupported();
        return null;
    }

    public static Node asDOMNode(org.dom4j.Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof Node) {
            return (Node) node;
        }
        System.out.println("Cannot convert: " + node + " into a W3C DOM Node");
        notSupported();
        return null;
    }

    public static Text asDOMText(CharacterData text) {
        if (text == null) {
            return null;
        }
        if (text instanceof Text) {
            return (Text) text;
        }
        notSupported();
        return null;
    }

    public static Node cloneNode(org.dom4j.Node node, boolean deep) {
        return asDOMNode((org.dom4j.Node) node.clone());
    }

    public static NodeList createNodeList(List list) {
        return new AnonymousClass_1(list);
    }

    public static void deleteData(CharacterData charData, int offset, int count) throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException((short) 7, "CharacterData node is read only: " + charData);
        } else if (count < 0) {
            throw new DOMException((short) 1, "Illegal value for count: " + count);
        } else {
            String text = charData.getText();
            if (text != null) {
                int length = text.length();
                if (offset < 0 || offset >= length) {
                    throw new DOMException((short) 1, "No text at offset: " + offset);
                }
                StringBuffer buffer = new StringBuffer(text);
                buffer.delete(offset, offset + count);
                charData.setText(buffer.toString());
            }
        }
    }

    public static NamedNodeMap getAttributes(org.dom4j.Node node) {
        return null;
    }

    public static NodeList getChildNodes(org.dom4j.Node node) {
        return EMPTY_NODE_LIST;
    }

    public static String getData(CharacterData charData) throws DOMException {
        return charData.getText();
    }

    public static Node getFirstChild(org.dom4j.Node node) {
        return null;
    }

    public static Node getLastChild(org.dom4j.Node node) {
        return null;
    }

    public static int getLength(CharacterData charData) {
        String text = charData.getText();
        return text != null ? text.length() : 0;
    }

    public static String getLocalName(org.dom4j.Node node) {
        return null;
    }

    public static String getNamespaceURI(org.dom4j.Node node) {
        return null;
    }

    public static Node getNextSibling(org.dom4j.Node node) {
        Element parent = node.getParent();
        if (parent != null) {
            int index = parent.indexOf(node);
            if (index >= 0) {
                index++;
                if (index < parent.nodeCount()) {
                    return asDOMNode(parent.node(index));
                }
            }
        }
        return null;
    }

    public static String getNodeValue(org.dom4j.Node node) throws DOMException {
        return node.getText();
    }

    public static Document getOwnerDocument(org.dom4j.Node node) {
        return asDOMDocument(node.getDocument());
    }

    public static Node getParentNode(org.dom4j.Node node) {
        return asDOMNode(node.getParent());
    }

    public static String getPrefix(org.dom4j.Node node) {
        return null;
    }

    public static Node getPreviousSibling(org.dom4j.Node node) {
        Element parent = node.getParent();
        if (parent != null) {
            int index = parent.indexOf(node);
            if (index > 0) {
                return asDOMNode(parent.node(index - 1));
            }
        }
        return null;
    }

    public static boolean hasAttributes(org.dom4j.Node node) {
        return node != null && node instanceof Element && ((Element) node).attributeCount() > 0;
    }

    public static boolean hasChildNodes(org.dom4j.Node node) {
        return false;
    }

    public static Node insertBefore(org.dom4j.Node node, Node newChild, Node refChild) throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            List list = branch.content();
            int index = list.indexOf(refChild);
            if (index < 0) {
                branch.add((org.dom4j.Node) newChild);
            } else {
                list.add(index, newChild);
            }
            return newChild;
        } else {
            throw new DOMException((short) 3, "Children not allowed for this node: " + node);
        }
    }

    public static void insertData(CharacterData data, int offset, String arg) throws DOMException {
        if (data.isReadOnly()) {
            throw new DOMException((short) 7, "CharacterData node is read only: " + data);
        }
        String text = data.getText();
        if (text == null) {
            data.setText(arg);
        } else {
            int length = text.length();
            if (offset < 0 || offset > length) {
                throw new DOMException((short) 1, "No text at offset: " + offset);
            }
            StringBuffer buffer = new StringBuffer(text);
            buffer.insert(offset, arg);
            data.setText(buffer.toString());
        }
    }

    public static boolean isSupported(org.dom4j.Node n, String feature, String version) {
        return false;
    }

    public static void normalize(org.dom4j.Node node) {
        notSupported();
    }

    public static void notSupported() {
        throw new DOMException((short) 9, "Not supported yet");
    }

    public static Node removeChild(org.dom4j.Node node, Node oldChild) throws DOMException {
        if (node instanceof Branch) {
            ((Branch) node).remove((org.dom4j.Node) oldChild);
            return oldChild;
        } else {
            throw new DOMException((short) 3, "Children not allowed for this node: " + node);
        }
    }

    public static Node replaceChild(org.dom4j.Node node, Node newChild, Node oldChild) throws DOMException {
        if (node instanceof Branch) {
            List list = ((Branch) node).content();
            int index = list.indexOf(oldChild);
            if (index < 0) {
                throw new DOMException((short) 8, "Tried to replace a non existing child for node: " + node);
            }
            list.set(index, newChild);
            return oldChild;
        } else {
            throw new DOMException((short) 3, "Children not allowed for this node: " + node);
        }
    }

    public static void replaceData(CharacterData charData, int offset, int count, String arg) throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException((short) 7, "CharacterData node is read only: " + charData);
        } else if (count < 0) {
            throw new DOMException((short) 1, "Illegal value for count: " + count);
        } else {
            String text = charData.getText();
            if (text != null) {
                int length = text.length();
                if (offset < 0 || offset >= length) {
                    throw new DOMException((short) 1, "No text at offset: " + offset);
                }
                StringBuffer buffer = new StringBuffer(text);
                buffer.replace(offset, offset + count, arg);
                charData.setText(buffer.toString());
            }
        }
    }

    public static void setData(CharacterData charData, String data) throws DOMException {
        charData.setText(data);
    }

    public static void setNodeValue(org.dom4j.Node node, String nodeValue) throws DOMException {
        node.setText(nodeValue);
    }

    public static void setPrefix(org.dom4j.Node node, String prefix) throws DOMException {
        notSupported();
    }

    public static String substringData(CharacterData charData, int offset, int count) throws DOMException {
        if (count < 0) {
            throw new DOMException((short) 1, "Illegal value for count: " + count);
        }
        String text = charData.getText();
        int length = text != null ? text.length() : 0;
        if (offset >= 0 && offset < length) {
            return offset + count > length ? text.substring(offset) : text.substring(offset, offset + count);
        } else {
            throw new DOMException((short) 1, "No text at offset: " + offset);
        }
    }

    public static boolean supports(org.dom4j.Node node, String feature, String version) {
        return false;
    }
}