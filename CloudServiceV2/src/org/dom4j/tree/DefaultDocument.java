package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.NodeHelper;
import org.dom4j.ProcessingInstruction;
import org.xml.sax.EntityResolver;

public class DefaultDocument extends AbstractDocument {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final List<Node> content;
    private DocumentType docType;
    private DocumentFactory documentFactory;
    private transient EntityResolver entityResolver;
    private String name;
    private Element rootElement;

    static {
        $assertionsDisabled = !DefaultDocument.class.desiredAssertionStatus();
    }

    public DefaultDocument() {
        this(null, null, null);
    }

    public DefaultDocument(String name) {
        this(name, null, null);
    }

    public DefaultDocument(String name, Element rootElement, DocumentType docType) {
        this.content = new LazyList();
        this.documentFactory = DocumentFactory.getInstance();
        this.name = name;
        setRootElement(rootElement);
        this.docType = docType;
    }

    public DefaultDocument(DocumentType docType) {
        this(null, null, docType);
    }

    public DefaultDocument(Element rootElement) {
        this(null, rootElement, null);
    }

    public DefaultDocument(Element rootElement, DocumentType docType) {
        this(null, rootElement, docType);
    }

    public Document addDocType(String docTypeName, String publicId, String systemId) {
        setDocType(getDocumentFactory().createDocType(docTypeName, publicId, systemId));
        return this;
    }

    protected void addNode(int index, Node node) {
        if (node != null) {
            Document document = node.getDocument();
            if (document == null || document == this) {
                contentList().add(index, node);
                childAdded(node);
            } else {
                throw new IllegalAddException(this, node, "The Node already has an existing document: " + document);
            }
        }
    }

    protected void addNode(Node node) {
        if (node != null) {
            Document document = node.getDocument();
            if (document == null || document == this) {
                contentList().add(node);
                childAdded(node);
            } else {
                throw new IllegalAddException(this, node, "The Node already has an existing document: " + document);
            }
        }
    }

    public void clearContent() {
        contentRemoved();
        contentList().clear();
        this.rootElement = null;
    }

    public DefaultDocument clone() {
        DefaultDocument document = (DefaultDocument) super.clone();
        document.rootElement = null;
        CloneHelper.setFinalContent(DefaultDocument.class, document);
        document.appendContent(this);
        return document;
    }

    protected List<Node> contentList() {
        if ($assertionsDisabled || this.content != null) {
            return this.content;
        }
        throw new AssertionError();
    }

    public DocumentType getDocType() {
        return this.docType;
    }

    protected DocumentFactory getDocumentFactory() {
        return this.documentFactory;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public String getName() {
        return this.name;
    }

    public Element getRootElement() {
        return this.rootElement;
    }

    public String getXMLEncoding() {
        return this.encoding;
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
        List<ProcessingInstruction> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) i$.next());
            if (pi != null) {
                answer.add(pi);
            }
        }
        return answer;
    }

    public List<ProcessingInstruction> processingInstructions(String target) {
        List<ProcessingInstruction> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) i$.next());
            if (pi != null && target.equals(pi.getName())) {
                answer.add(pi);
            }
        }
        return answer;
    }

    protected boolean removeNode(Node node) {
        if (node == this.rootElement) {
            this.rootElement = null;
        }
        if (!contentList().remove(node)) {
            return false;
        }
        childRemoved(node);
        return true;
    }

    public boolean removeProcessingInstruction(String target) {
        Iterator<Node> iter = contentList().iterator();
        while (iter.hasNext()) {
            ProcessingInstruction pi = NodeHelper.nodeAsProcessingInstruction((Node) iter.next());
            if (pi != null && target.equals(pi.getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    protected void rootElementAdded(Element element) {
        this.rootElement = element;
        element.setDocument(this);
    }

    public void setContent(List<Node> content) {
        this.rootElement = null;
        contentRemoved();
        if (content instanceof ContentListFacade) {
            content = ((ContentListFacade) content).getBackingList();
        }
        contentList().clear();
        if (content != null) {
            List<Node> newContent = createContentList();
            Iterator i$ = content.iterator();
            while (i$.hasNext()) {
                Node node = (Node) i$.next();
                Document doc = node.getDocument();
                if (!(doc == null || doc == this)) {
                    node = node.clone();
                }
                Element element = NodeHelper.nodeAsElement(node);
                if (element == null || this.rootElement != null) {
                    throw new IllegalAddException("A document may only contain one root element: " + content);
                }
                this.rootElement = element;
                newContent.add(node);
                childAdded(node);
            }
            contentList().addAll(newContent);
        }
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setName(String name) {
        this.name = name;
    }
}