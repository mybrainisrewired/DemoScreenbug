package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.QName;
import org.dom4j.Visitor;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public abstract class AbstractDocument extends AbstractBranch implements Document {
    protected String encoding;

    public void accept(Visitor visitor) {
        visitor.visit(this);
        DocumentType docType = getDocType();
        if (docType != null) {
            visitor.visit(docType);
        }
        List content = content();
        if (content != null) {
            Iterator iter = content.iterator();
            while (iter.hasNext()) {
                Object object = iter.next();
                if (object instanceof String) {
                    visitor.visit(getDocumentFactory().createText((String) object));
                } else {
                    ((Node) object).accept(visitor);
                }
            }
        }
    }

    public void add(Element element) {
        checkAddElementAllowed(element);
        super.add(element);
        rootElementAdded(element);
    }

    public Document addComment(String comment) {
        add(getDocumentFactory().createComment(comment));
        return this;
    }

    public Element addElement(String name) {
        Element element = getDocumentFactory().createElement(name);
        add(element);
        return element;
    }

    public Element addElement(String qualifiedName, String namespaceURI) {
        Element element = getDocumentFactory().createElement(qualifiedName, namespaceURI);
        add(element);
        return element;
    }

    public Element addElement(QName qName) {
        Element element = getDocumentFactory().createElement(qName);
        add(element);
        return element;
    }

    public Document addProcessingInstruction(String target, String data) {
        add(getDocumentFactory().createProcessingInstruction(target, data));
        return this;
    }

    public Document addProcessingInstruction(String target, Map data) {
        add(getDocumentFactory().createProcessingInstruction(target, data));
        return this;
    }

    public String asXML() {
        OutputFormat format = new OutputFormat();
        format.setEncoding(this.encoding);
        try {
            Writer out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(this);
            writer.flush();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException while generating textual representation: " + e.getMessage());
        }
    }

    public Node asXPathResult(Element parent) {
        return this;
    }

    protected void checkAddElementAllowed(Node element) {
        Element root = getRootElement();
        if (root != null) {
            throw new IllegalAddException(this, element, "Cannot add another element to this Document as it already has a root element of: " + root.getQualifiedName());
        }
    }

    protected void childAdded(Node node) {
        if (node != null) {
            node.setDocument(this);
        }
    }

    protected void childRemoved(Node node) {
        if (node != null) {
            node.setDocument(null);
        }
    }

    public Document getDocument() {
        return this;
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.DOCUMENT_NODE;
    }

    public String getPath(Element context) {
        return "/";
    }

    public String getStringValue() {
        Element root = getRootElement();
        return root != null ? root.getStringValue() : "";
    }

    public String getUniquePath(Element context) {
        return "/";
    }

    public String getXMLEncoding() {
        return null;
    }

    public void normalize() {
        Element element = getRootElement();
        if (element != null) {
            element.normalize();
        }
    }

    public boolean remove(Element element) {
        boolean answer = super.remove(element);
        if (getRootElement() != null && answer) {
            setRootElement(null);
        }
        element.setDocument(null);
        return answer;
    }

    protected abstract void rootElementAdded(Element element);

    public void setRootElement(Element rootElement) {
        clearContent();
        if (rootElement != null) {
            super.add(rootElement);
            rootElementAdded(rootElement);
        }
    }

    public void setXMLEncoding(String enc) {
        this.encoding = enc;
    }

    protected void toString(StringBuilder builder) {
        super.toString(builder);
        builder.append(" [Document: name ");
        builder.append(getName());
        builder.append(']');
    }

    public void write(Writer out) throws IOException {
        OutputFormat format = new OutputFormat();
        format.setEncoding(this.encoding);
        new XMLWriter(out, format).write(this);
    }
}