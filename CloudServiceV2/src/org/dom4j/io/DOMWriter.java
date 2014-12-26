package org.dom4j.io;

import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.tree.NamespaceStack;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DOMWriter {
    private static final String[] DEFAULT_DOM_DOCUMENT_CLASSES;
    private static boolean loggedWarning;
    private Class domDocumentClass;
    private NamespaceStack namespaceStack;

    static {
        loggedWarning = false;
        DEFAULT_DOM_DOCUMENT_CLASSES = new String[]{"org.apache.xerces.dom.DocumentImpl", "gnu.xml.dom.DomDocument", "org.apache.crimson.tree.XmlDocument", "com.sun.xml.tree.XmlDocument", "oracle.xml.parser.v2.XMLDocument", "oracle.xml.parser.XMLDocument", "org.dom4j.dom.DOMDocument"};
    }

    public DOMWriter() {
        this.namespaceStack = new NamespaceStack();
    }

    public DOMWriter(Class domDocumentClass) {
        this.namespaceStack = new NamespaceStack();
        this.domDocumentClass = domDocumentClass;
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, String text) {
        domCurrent.appendChild(domDocument.createTextNode(text));
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, List content) {
        int size = content.size();
        int i = 0;
        while (i < size) {
            Object object = content.get(i);
            if (object instanceof Element) {
                appendDOMTree(domDocument, domCurrent, (Element) object);
            } else if (object instanceof String) {
                appendDOMTree(domDocument, domCurrent, (String) object);
            } else if (object instanceof Text) {
                appendDOMTree(domDocument, domCurrent, ((Text) object).getText());
            } else if (object instanceof CDATA) {
                appendDOMTree(domDocument, domCurrent, (CDATA) object);
            } else if (object instanceof Comment) {
                appendDOMTree(domDocument, domCurrent, (Comment) object);
            } else if (object instanceof Entity) {
                appendDOMTree(domDocument, domCurrent, (Entity) object);
            } else if (object instanceof ProcessingInstruction) {
                appendDOMTree(domDocument, domCurrent, (ProcessingInstruction) object);
            }
            i++;
        }
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, CDATA cdata) {
        domCurrent.appendChild(domDocument.createCDATASection(cdata.getText()));
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, Comment comment) {
        domCurrent.appendChild(domDocument.createComment(comment.getText()));
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, Element element) {
        Node domElement = domDocument.createElementNS(element.getNamespaceURI(), element.getQualifiedName());
        int stackSize = this.namespaceStack.size();
        Namespace elementNamespace = element.getNamespace();
        if (isNamespaceDeclaration(elementNamespace)) {
            this.namespaceStack.push(elementNamespace);
            writeNamespace(domElement, elementNamespace);
        }
        List declaredNamespaces = element.declaredNamespaces();
        int i = 0;
        int size = declaredNamespaces.size();
        while (i < size) {
            Namespace namespace = (Namespace) declaredNamespaces.get(i);
            if (isNamespaceDeclaration(namespace)) {
                this.namespaceStack.push(namespace);
                writeNamespace(domElement, namespace);
            }
            i++;
        }
        i = 0;
        size = element.attributeCount();
        while (i < size) {
            Attribute attribute = element.attribute(i);
            domElement.setAttributeNS(attribute.getNamespaceURI(), attribute.getQualifiedName(), attribute.getValue());
            i++;
        }
        appendDOMTree(domDocument, domElement, element.content());
        domCurrent.appendChild(domElement);
        while (this.namespaceStack.size() > stackSize) {
            this.namespaceStack.pop();
        }
    }

    protected void appendDOMTree(Document domDocument, Node domCurrent, Entity entity) {
        domCurrent.appendChild(domDocument.createEntityReference(entity.getName()));
    }

    protected void appendDOMTree(Document domDoc, Node domCurrent, ProcessingInstruction pi) {
        domCurrent.appendChild(domDoc.createProcessingInstruction(pi.getTarget(), pi.getText()));
    }

    protected String attributeNameForNamespace(Namespace namespace) {
        String xmlns = "xmlns";
        String prefix = namespace.getPrefix();
        return prefix.length() > 0 ? xmlns + ":" + prefix : xmlns;
    }

    protected Document createDomDocument(org.dom4j.Document document) throws DocumentException {
        if (this.domDocumentClass != null) {
            try {
                return this.domDocumentClass.newInstance();
            } catch (Exception e) {
                throw new DocumentException("Could not instantiate an instance of DOM Document with class: " + this.domDocumentClass.getName(), e);
            }
        }
        Document result = createDomDocumentViaJAXP();
        if (result != null) {
            return result;
        }
        Class theClass = getDomDocumentClass();
        try {
            return theClass.newInstance();
        } catch (Exception e2) {
            throw new DocumentException("Could not instantiate an instance of DOM Document with class: " + theClass.getName(), e2);
        }
    }

    protected Document createDomDocument(org.dom4j.Document document, DOMImplementation domImpl) throws DocumentException {
        return domImpl.createDocument(null, null, null);
    }

    protected Document createDomDocumentViaJAXP() throws DocumentException {
        boolean z = true;
        try {
            return JAXPHelper.createDocument(false, true);
        } catch (Throwable th) {
            Throwable e = th;
            if (!loggedWarning) {
                loggedWarning = z;
                if (SAXHelper.isVerboseErrorReporting()) {
                    System.out.println("Warning: Caught exception attempting to use JAXP to create a W3C DOM document");
                    System.out.println("Warning: Exception was: " + e);
                    e.printStackTrace();
                } else {
                    System.out.println("Warning: Error occurred using JAXP to create a DOM document.");
                }
            }
            return null;
        }
    }

    public Class getDomDocumentClass() throws DocumentException {
        Class result = this.domDocumentClass;
        if (result == null) {
            int size = DEFAULT_DOM_DOCUMENT_CLASSES.length;
            int i = 0;
            while (i < size) {
                try {
                    result = Class.forName(DEFAULT_DOM_DOCUMENT_CLASSES[i], true, DOMWriter.class.getClassLoader());
                    if (result != null) {
                    }
                } catch (Exception e) {
                }
                i++;
            }
        }
        return result;
    }

    protected boolean isNamespaceDeclaration(Namespace ns) {
        if (!(ns == null || ns == Namespace.NO_NAMESPACE || ns == Namespace.XML_NAMESPACE)) {
            String uri = ns.getURI();
            if (!(uri == null || uri.length() <= 0 || this.namespaceStack.contains(ns))) {
                return true;
            }
        }
        return false;
    }

    protected void resetNamespaceStack() {
        this.namespaceStack.clear();
        this.namespaceStack.push(Namespace.XML_NAMESPACE);
    }

    public void setDomDocumentClass(Class domDocumentClass) {
        this.domDocumentClass = domDocumentClass;
    }

    public void setDomDocumentClassName(String name) throws DocumentException {
        try {
            this.domDocumentClass = Class.forName(name, true, DOMWriter.class.getClassLoader());
        } catch (Exception e) {
            throw new DocumentException("Could not load the DOM Document class: " + name, e);
        }
    }

    public Document write(org.dom4j.Document document) throws DocumentException {
        if (document instanceof Document) {
            return (Document) document;
        }
        resetNamespaceStack();
        Document domDocument = createDomDocument(document);
        appendDOMTree(domDocument, (Node)domDocument, document.content());
        this.namespaceStack.clear();
        return domDocument;
    }

    public Document write(org.dom4j.Document document, DOMImplementation domImpl) throws DocumentException {
        if (document instanceof Document) {
            return (Document) document;
        }
        resetNamespaceStack();
        Document domDocument = createDomDocument(document, domImpl);
        appendDOMTree(domDocument, (Node)domDocument, document.content());
        this.namespaceStack.clear();
        return domDocument;
    }

    protected void writeNamespace(org.w3c.dom.Element domElement, Namespace namespace) {
        domElement.setAttribute(attributeNameForNamespace(namespace), namespace.getURI());
    }
}