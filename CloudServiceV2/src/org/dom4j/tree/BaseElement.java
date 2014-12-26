package org.dom4j.tree;

import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;

public class BaseElement extends AbstractElement {
    protected List<Attribute> attributes;
    protected List<Node> content;
    private Branch parentBranch;
    private QName qname;

    public BaseElement(String name) {
        this.qname = getDocumentFactory().createQName(name);
    }

    public BaseElement(String name, Namespace namespace) {
        this.qname = getDocumentFactory().createQName(name, namespace);
    }

    public BaseElement(QName qname) {
        this.qname = qname;
    }

    protected List<Attribute> attributeList() {
        if (this.attributes == null) {
            this.attributes = createAttributeList();
        }
        return this.attributes;
    }

    protected List<Attribute> attributeList(int size) {
        if (this.attributes == null) {
            this.attributes = createAttributeList(size);
        }
        return this.attributes;
    }

    public void clearContent() {
        contentList().clear();
    }

    protected List<Node> contentList() {
        if (this.content == null) {
            this.content = createContentList();
        }
        return this.content;
    }

    public Document getDocument() {
        if (this.parentBranch instanceof Document) {
            return (Document) this.parentBranch;
        }
        return this.parentBranch instanceof Element ? ((Element) this.parentBranch).getDocument() : null;
    }

    public Element getParent() {
        return this.parentBranch instanceof Element ? this.parentBranch : null;
    }

    public QName getQName() {
        return this.qname;
    }

    protected void setAttributeList(List<Attribute> attributeList) {
        this.attributes = attributeList;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
        if (attributes instanceof ContentListFacade) {
            this.attributes = ((ContentListFacade) attributes).getBackingList();
        }
    }

    public void setContent(List<Node> content) {
        this.content = content;
        if (content instanceof ContentListFacade) {
            this.content = ((ContentListFacade) content).getBackingList();
        }
    }

    public void setDocument(Document document) {
        if (this.parentBranch instanceof Document || document != null) {
            this.parentBranch = document;
        }
    }

    public void setParent(Element parent) {
        if (this.parentBranch instanceof Element || parent != null) {
            this.parentBranch = parent;
        }
    }

    public void setQName(QName name) {
        this.qname = name;
    }

    public boolean supportsParent() {
        return true;
    }
}