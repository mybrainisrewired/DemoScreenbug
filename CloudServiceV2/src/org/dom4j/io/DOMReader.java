package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.tree.NamespaceStack;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMReader {
    private DocumentFactory factory;
    private NamespaceStack namespaceStack;

    public DOMReader() {
        this.factory = DocumentFactory.getInstance();
        this.namespaceStack = new NamespaceStack(this.factory);
    }

    public DOMReader(DocumentFactory factory) {
        this.factory = factory;
        this.namespaceStack = new NamespaceStack(factory);
    }

    private String getPrefix(String xmlnsDecl) {
        int index = xmlnsDecl.indexOf(Opcodes.ASTORE, JsonWriteContext.STATUS_EXPECT_NAME);
        return index != -1 ? xmlnsDecl.substring(index + 1) : "";
    }

    protected void clearNamespaceStack() {
        this.namespaceStack.clear();
        if (!this.namespaceStack.contains(Namespace.XML_NAMESPACE)) {
            this.namespaceStack.push(Namespace.XML_NAMESPACE);
        }
    }

    protected Document createDocument() {
        return getDocumentFactory().createDocument();
    }

    public DocumentFactory getDocumentFactory() {
        return this.factory;
    }

    protected Namespace getNamespace(String prefix, String uri) {
        return getDocumentFactory().createNamespace(prefix, uri);
    }

    public Document read(org.w3c.dom.Document domDocument) {
        if (domDocument instanceof Document) {
            return (Document) domDocument;
        }
        Document document = createDocument();
        clearNamespaceStack();
        NodeList nodeList = domDocument.getChildNodes();
        int i = 0;
        int size = nodeList.getLength();
        while (i < size) {
            readTree(nodeList.item(i), document);
            i++;
        }
        return document;
    }

    protected void readElement(Node node, Branch current) {
        Node attribute;
        int size;
        int i;
        int previouslyDeclaredNamespaces = this.namespaceStack.size();
        String namespaceUri = node.getNamespaceURI();
        if (node.getPrefix() == null) {
            String elementPrefix = "";
        }
        NamedNodeMap attributeList = node.getAttributes();
        if (attributeList != null && namespaceUri == null) {
            attribute = attributeList.getNamedItem("xmlns");
            if (attribute != null) {
                namespaceUri = attribute.getNodeValue();
                elementPrefix = "";
            }
        }
        Element element = current.addElement(this.namespaceStack.getQName(namespaceUri, node.getLocalName(), node.getNodeName()));
        if (attributeList != null) {
            size = attributeList.getLength();
            List attributes = new ArrayList(size);
            i = 0;
            while (i < size) {
                attribute = attributeList.item(i);
                String name = attribute.getNodeName();
                if (name.startsWith("xmlns")) {
                    element.add(this.namespaceStack.addNamespace(getPrefix(name), attribute.getNodeValue()));
                } else {
                    attributes.add(attribute);
                }
                i++;
            }
            size = attributes.size();
            i = 0;
            while (i < size) {
                attribute = attributes.get(i);
                element.addAttribute(this.namespaceStack.getQName(attribute.getNamespaceURI(), attribute.getLocalName(), attribute.getNodeName()), attribute.getNodeValue());
                i++;
            }
        }
        NodeList children = node.getChildNodes();
        i = 0;
        size = children.getLength();
        while (i < size) {
            readTree(children.item(i), element);
            i++;
        }
        while (this.namespaceStack.size() > previouslyDeclaredNamespaces) {
            this.namespaceStack.pop();
        }
    }

    protected void readTree(Node node, Branch current) {
        Element element = null;
        Document document = null;
        if (current instanceof Element) {
            element = (Element) current;
        } else {
            document = (Document) current;
        }
        switch (node.getNodeType()) {
            case LocalAudioAll.SORT_BY_DATE:
                readElement(node, current);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                element.addText(node.getNodeValue());
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                element.addCDATA(node.getNodeValue());
            case JsonWriteContext.STATUS_EXPECT_NAME:
                Node firstChild = node.getFirstChild();
                if (firstChild != null) {
                    element.addEntity(node.getNodeName(), firstChild.getNodeValue());
                } else {
                    element.addEntity(node.getNodeName(), "");
                }
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                element.addEntity(node.getNodeName(), node.getNodeValue());
            case Type.LONG:
                if (current instanceof Element) {
                    ((Element) current).addProcessingInstruction(node.getNodeName(), node.getNodeValue());
                } else {
                    ((Document) current).addProcessingInstruction(node.getNodeName(), node.getNodeValue());
                }
            case Type.DOUBLE:
                if (current instanceof Element) {
                    ((Element) current).addComment(node.getNodeValue());
                } else {
                    ((Document) current).addComment(node.getNodeValue());
                }
            case Type.OBJECT:
                DocumentType domDocType = (DocumentType) node;
                document.addDocType(domDocType.getName(), domDocType.getPublicId(), domDocType.getSystemId());
            default:
                System.out.println("WARNING: Unknown DOM node type: " + node.getNodeType());
        }
    }

    public void setDocumentFactory(DocumentFactory docFactory) {
        this.factory = docFactory;
        this.namespaceStack.setDocumentFactory(this.factory);
    }
}