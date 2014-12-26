package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeHelper;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;

public class DefaultElement extends AbstractElement {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final transient DocumentFactory DOCUMENT_FACTORY;
    private final List<Attribute> attributes;
    private List<Node> content;
    private Branch parentBranch;
    private QName qname;

    static {
        $assertionsDisabled = !DefaultElement.class.desiredAssertionStatus();
        DOCUMENT_FACTORY = DocumentFactory.getInstance();
    }

    public DefaultElement(String name) {
        this(DOCUMENT_FACTORY.createQName(name));
    }

    public DefaultElement(String name, Namespace namespace) {
        this(DOCUMENT_FACTORY.createQName(name, namespace));
    }

    public DefaultElement(QName qname) {
        this(qname, 0);
    }

    public DefaultElement(QName qname, int attributeCount) {
        this.content = new LazyList();
        this.qname = qname;
        this.attributes = new LazyList();
    }

    public void add(Node attribute) {
        if (attribute.getParent() != null) {
            throw new IllegalAddException(this, attribute, "The Attribute already has an existing parent \"" + attribute.getParent().getQualifiedName() + "\"");
        } else if (attribute.getValue() == null) {
            Attribute oldAttribute = attribute(attribute.getQName());
            if (oldAttribute != null) {
                remove(oldAttribute);
            }
        } else {
            attributeList().add(attribute);
            childAdded(attribute);
        }
    }

    protected void addNewNode(Node node) {
        contentList().add(node);
        childAdded(node);
    }

    public List<Namespace> additionalNamespaces() {
        BackedList<Namespace> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Namespace namespace = NodeHelper.nodeAsNamespace((Node) i$.next());
            if (!(namespace == null || namespace.equals(getNamespace()))) {
                answer.addLocal(namespace);
            }
        }
        return answer;
    }

    public List<Namespace> additionalNamespaces(String defaultNamespaceURI) {
        BackedList<Namespace> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Namespace namespace = NodeHelper.nodeAsNamespace((Node) i$.next());
            if (!(namespace == null || defaultNamespaceURI.equals(namespace.getURI()))) {
                answer.addLocal(namespace);
            }
        }
        return answer;
    }

    public Attribute attribute(int index) {
        return (index < 0 || index >= attributeList().size()) ? null : (Attribute) attributeList().get(index);
    }

    public Attribute attribute(String name) {
        Iterator i$ = attributeList().iterator();
        while (i$.hasNext()) {
            Attribute attribute = (Attribute) i$.next();
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }

    public Attribute attribute(String name, Namespace namespace) {
        return attribute(getDocumentFactory().createQName(name, namespace));
    }

    public Attribute attribute(QName qName) {
        Iterator i$ = attributeList().iterator();
        while (i$.hasNext()) {
            Attribute attribute = (Attribute) i$.next();
            if (qName.equals(attribute.getQName())) {
                return attribute;
            }
        }
        return null;
    }

    public int attributeCount() {
        return attributeList().size();
    }

    public Iterator<Attribute> attributeIterator() {
        return attributeList().iterator();
    }

    protected List<Attribute> attributeList() {
        if ($assertionsDisabled || this.attributes != null) {
            return this.attributes;
        }
        throw new AssertionError();
    }

    @Deprecated
    protected List<Attribute> attributeList(int attributeCount) {
        if ($assertionsDisabled || this.attributes != null) {
            return this.attributes;
        }
        throw new AssertionError();
    }

    public List<Attribute> attributes() {
        return new ContentListFacade(this, attributeList());
    }

    public void clearContent() {
        if ($assertionsDisabled || this.content != null) {
            contentRemoved();
            this.content.clear();
        } else {
            throw new AssertionError();
        }
    }

    public DefaultElement clone() {
        DefaultElement answer = (DefaultElement) super.clone();
        if (answer != this) {
            CloneHelper.setFinalLazyList(DefaultElement.class, answer, "attributes");
            CloneHelper.setFinalContent(DefaultElement.class, answer);
            answer.appendAttributes(this);
            answer.appendContent(this);
        }
        return answer;
    }

    protected List<Node> contentList() {
        if ($assertionsDisabled || this.content != null) {
            return this.content;
        }
        throw new AssertionError();
    }

    public List<Namespace> declaredNamespaces() {
        BackedList<Namespace> answer = createResultList();
        if (this.content != null) {
            Iterator i$ = this.content.iterator();
            while (i$.hasNext()) {
                Namespace namespace = NodeHelper.nodeAsNamespace((Node) i$.next());
                if (namespace != null) {
                    answer.addLocal(namespace);
                }
            }
        }
        return answer;
    }

    public Element element(String name) {
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Element element = NodeHelper.nodeAsElement((Node) i$.next());
            if (element != null && name.equals(element.getName())) {
                return element;
            }
        }
        return null;
    }

    public Element element(String name, Namespace namespace) {
        return element(getDocumentFactory().createQName(name, namespace));
    }

    public Element element(QName qName) {
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Element element = NodeHelper.nodeAsElement((Node) i$.next());
            if (element != null && qName.equals(element.getQName())) {
                return element;
            }
        }
        return null;
    }

    public Document getDocument() {
        if (this.parentBranch instanceof Document) {
            return (Document) this.parentBranch;
        }
        return this.parentBranch instanceof Element ? ((Element) this.parentBranch).getDocument() : null;
    }

    protected DocumentFactory getDocumentFactory() {
        DocumentFactory factory = this.qname.getDocumentFactory();
        return factory != null ? factory : DOCUMENT_FACTORY;
    }

    public Namespace getNamespaceForPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        if (prefix.equals(getNamespacePrefix())) {
            return getNamespace();
        }
        if (prefix.equals("xml")) {
            return Namespace.XML_NAMESPACE;
        }
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Namespace namespace = NodeHelper.nodeAsNamespace((Node) i$.next());
            if (namespace != null && prefix.equals(namespace.getPrefix())) {
                return namespace;
            }
        }
        Element parent = getParent();
        if (parent != null) {
            Namespace answer = parent.getNamespaceForPrefix(prefix);
            if (answer != null) {
                return answer;
            }
        }
        return (prefix == null || prefix.length() <= 0) ? Namespace.NO_NAMESPACE : null;
    }

    public Namespace getNamespaceForURI(String uri) {
        if (uri == null || uri.length() <= 0) {
            return Namespace.NO_NAMESPACE;
        }
        if (uri.equals(getNamespaceURI())) {
            return getNamespace();
        }
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            Namespace namespace = NodeHelper.nodeAsNamespace((Node) i$.next());
            if (namespace != null && uri.equals(namespace.getURI())) {
                return namespace;
            }
        }
        Element parent = getParent();
        return parent != null ? parent.getNamespaceForURI(uri) : null;
    }

    public Element getParent() {
        return this.parentBranch instanceof Element ? this.parentBranch : null;
    }

    public QName getQName() {
        return this.qname;
    }

    public String getStringValue() {
        if (contentList().size() == 0) {
            return "";
        }
        if (contentList().size() == 1) {
            return getContentAsStringValue((Node) contentList().get(0));
        }
        StringBuilder builder = new StringBuilder();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            String string = getContentAsStringValue((Node) i$.next());
            if (string.length() > 0) {
                builder.append(string);
            }
        }
        return builder.toString();
    }

    public int indexOf(Node node) {
        return contentList().indexOf(node);
    }

    public Node node(int index) {
        return (index < 0 || index >= contentList().size()) ? null : (Node) contentList().get(index);
    }

    public int nodeCount() {
        return contentList().size();
    }

    public Iterator<Node> nodeIterator() {
        return contentList().iterator();
    }

    public ProcessingInstruction processingInstruction(String target) {
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) i$.next());
            if (pi != null && target.equals(pi.getName())) {
                return pi;
            }
        }
        return null;
    }

    public List<ProcessingInstruction> processingInstructions() {
        BackedList<ProcessingInstruction> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) i$.next());
            if (pi != null) {
                answer.addLocal(pi);
            }
        }
        return answer;
    }

    public List<ProcessingInstruction> processingInstructions(String target) {
        BackedList<ProcessingInstruction> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) i$.next());
            if (pi != null && target.equals(pi.getName())) {
                answer.addLocal(pi);
            }
        }
        return answer;
    }

    public boolean remove(Attribute attribute) {
        boolean answer = attributeList().remove(attribute);
        if (!answer) {
            Attribute copy = attribute(attribute.getQName());
            if (copy != null) {
                answer = attributeList().remove(copy);
            }
        }
        if (answer) {
            childRemoved(attribute);
        }
        return answer;
    }

    protected boolean removeNode(Node node) {
        boolean answer = contentList().remove(node);
        if (answer) {
            childRemoved(node);
        }
        return answer;
    }

    public boolean removeProcessingInstruction(String target) {
        Iterator<? extends Node> iterator = this.content.iterator();
        while (iterator.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) iterator.next());
            if (pi != null && target.equals(pi.getName())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    protected void setAttributeList(List<Attribute> attributeList) {
        attributeList().clear();
        attributeList().addAll(attributeList);
    }

    public void setAttributes(List<Attribute> attributes) {
        if (attributes instanceof ContentListFacade) {
            attributes = ((ContentListFacade) attributes).getBackingList();
        }
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.addAll(attributes);
        }
    }

    public void setContent(List<Node> content) {
        contentRemoved();
        if (content instanceof ContentListFacade) {
            content = ((ContentListFacade) content).getBackingList();
        }
        List<Node> newContent = createContentList();
        if (content != null) {
            Iterator i$ = content.iterator();
            while (i$.hasNext()) {
                Node node = (Node) i$.next();
                Element parent = node.getParent();
                if (!(parent == null || parent == this)) {
                    node = node.clone();
                }
                newContent.add(node);
                childAdded(node);
            }
        }
        this.content = newContent;
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