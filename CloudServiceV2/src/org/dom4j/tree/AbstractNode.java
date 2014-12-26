package org.dom4j.tree;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.NodeType;
import org.dom4j.XPath;
import org.dom4j.rule.Pattern;

public abstract class AbstractNode implements Node, Cloneable, Serializable {
    private static final DocumentFactory DOCUMENT_FACTORY;

    static {
        DOCUMENT_FACTORY = DocumentFactory.getInstance();
    }

    public Node asXPathResult(Element parent) {
        return supportsParent() ? this : createXPathResult(parent);
    }

    public AbstractNode clone() {
        if (isReadOnly()) {
            return this;
        }
        try {
            AbstractNode answer = (AbstractNode) super.clone();
            answer.setParent(null);
            answer.setDocument(null);
            return answer;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This should never happen. Caught: ", e);
        }
    }

    public Pattern createPattern(String patternText) {
        return getDocumentFactory().createPattern(patternText);
    }

    public XPath createXPath(String xpathExpression) {
        return getDocumentFactory().createXPath(xpathExpression);
    }

    public NodeFilter createXPathFilter(String patternText) {
        return getDocumentFactory().createXPathFilter(patternText);
    }

    protected Node createXPathResult(Element parent) {
        throw new RuntimeException("asXPathResult() not yet implemented fully for: " + this);
    }

    public Node detach() {
        Element parent = getParent();
        if (parent != null) {
            parent.remove(this);
        } else {
            Document document = getDocument();
            if (document != null) {
                document.remove(this);
            }
        }
        setParent(null);
        setDocument(null);
        return this;
    }

    public Document getDocument() {
        Element element = getParent();
        return element != null ? element.getDocument() : null;
    }

    protected DocumentFactory getDocumentFactory() {
        return DOCUMENT_FACTORY;
    }

    public String getName() {
        return null;
    }

    public short getNodeType() {
        return getNodeTypeEnum().getCode();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.UNKNOWN_NODE;
    }

    public String getNodeTypeName() {
        return getNodeTypeEnum().getName();
    }

    public Element getParent() {
        return null;
    }

    public String getPath() {
        return getPath(null);
    }

    public String getStringValue() {
        return getText();
    }

    public String getText() {
        return null;
    }

    public String getUniquePath() {
        return getUniquePath(null);
    }

    public boolean hasContent() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean matches(String patternText) {
        return createXPathFilter(patternText).matches(this);
    }

    public Number numberValueOf(String xpathExpression) {
        return createXPath(xpathExpression).numberValueOf(this);
    }

    public List<? extends Node> selectNodes(String xpathExpression) {
        return createXPath(xpathExpression).selectNodes(this);
    }

    public List<? extends Node> selectNodes(String xpathExpression, String comparisonXPathExpression) {
        return selectNodes(xpathExpression, comparisonXPathExpression, false);
    }

    public List<? extends Node> selectNodes(String xpathExpression, String comparisonXPathExpression, boolean removeDuplicates) {
        return createXPath(xpathExpression).selectNodes(this, createXPath(comparisonXPathExpression), removeDuplicates);
    }

    public Object selectObject(String xpathExpression) {
        return createXPath(xpathExpression).evaluate(this);
    }

    public Node selectSingleNode(String xpathExpression) {
        return createXPath(xpathExpression).selectSingleNode(this);
    }

    public void setDocument(Document document) {
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("This node cannot be modified");
    }

    public void setParent(Element parent) {
    }

    public void setText(String text) {
        throw new UnsupportedOperationException("This node cannot be modified");
    }

    public boolean supportsParent() {
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    protected void toString(StringBuilder builder) {
        builder.append(super.toString());
    }

    public String valueOf(String xpathExpression) {
        return createXPath(xpathExpression).valueOf(this);
    }

    public void write(Writer writer) throws IOException {
        writer.write(asXML());
    }
}