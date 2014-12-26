package org.dom4j.tree;

import com.wmt.data.LocalAudioAll;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;

public abstract class AbstractBranch extends AbstractNode implements Branch {
    protected static final int DEFAULT_CONTENT_LIST_SIZE = 5;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.CDATA_SECTION_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ENTITY_REFERENCE_NODE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.TEXT_NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.COMMENT_NODE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.PROCESSING_INSTRUCTION_NODE.ordinal()] = 6;
        }
    }

    public void add(Comment comment) {
        addNode(comment);
    }

    public void add(Element element) {
        addNode(element);
    }

    public void add(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                add((Element) node);
            case DEFAULT_CONTENT_LIST_SIZE:
                add((Comment) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                add((ProcessingInstruction) node);
            default:
                invalidNodeTypeAddException(node);
        }
    }

    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }

    public Element addElement(String name) {
        Element node = getDocumentFactory().createElement(name);
        add(node);
        return node;
    }

    public Element addElement(String qualifiedName, String namespaceURI) {
        Element node = getDocumentFactory().createElement(qualifiedName, namespaceURI);
        add(node);
        return node;
    }

    public Element addElement(String name, String prefix, String uri) {
        return addElement(getDocumentFactory().createQName(name, Namespace.get(prefix, uri)));
    }

    public Element addElement(QName qname) {
        Element node = getDocumentFactory().createElement(qname);
        add(node);
        return node;
    }

    protected abstract void addNode(int i, Node node);

    protected abstract void addNode(Node node);

    public void appendContent(Branch branch) {
        Iterator i$ = branch.iterator();
        while (i$.hasNext()) {
            add((Node) ((Node) i$.next()).clone());
        }
    }

    protected abstract void childAdded(Node node);

    protected abstract void childRemoved(Node node);

    public List<Node> content() {
        return new ContentListFacade(this, contentList());
    }

    protected abstract List<Node> contentList();

    protected void contentRemoved() {
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            childRemoved((Node) i$.next());
        }
    }

    protected List<Node> createContentList() {
        return new LazyList();
    }

    protected <T extends Node> BackedList<T> createResultList() {
        return new BackedList(this, contentList());
    }

    public Element elementByID(String elementID) {
        int i = 0;
        int size = nodeCount();
        while (i < size) {
            Node node = node(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String id = elementID(element);
                if (id != null && id.equals(elementID)) {
                    return element;
                }
                element = element.elementByID(elementID);
                if (element != null) {
                    return element;
                }
            }
            i++;
        }
        return null;
    }

    protected String elementID(Element element) {
        return element.attributeValue("ID");
    }

    protected String getContentAsStringValue(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return node.getStringValue();
            default:
                return "";
        }
    }

    protected String getContentAsText(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return node.getText();
            default:
                return "";
        }
    }

    public String getText() {
        List<Node> content = contentList();
        if (content != null) {
            int size = content.size();
            if (size >= 1) {
                String firstText = getContentAsText((Node) content.get(0));
                if (size == 1) {
                    return firstText;
                }
                StringBuilder buffer = new StringBuilder(firstText);
                int i = 1;
                while (i < size) {
                    buffer.append(getContentAsText((Node) content.get(i)));
                    i++;
                }
                return buffer.toString();
            }
        }
        return "";
    }

    public String getTextTrim() {
        String text = getText();
        StringBuilder textContent = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            textContent.append(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens()) {
                textContent.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
        }
        return textContent.toString();
    }

    public boolean hasContent() {
        return nodeCount() > 0;
    }

    public int indexOf(Node node) {
        return contentList().indexOf(node);
    }

    protected void invalidNodeTypeAddException(Node node) {
        throw new IllegalAddException("Invalid node type. Cannot add node: " + node + " to this branch: " + this);
    }

    public boolean isReadOnly() {
        return false;
    }

    public Iterator<Node> iterator() {
        return nodeIterator();
    }

    public Node node(int index) {
        return (Node) contentList().get(index);
    }

    public int nodeCount() {
        return contentList().size();
    }

    public Iterator<Node> nodeIterator() {
        return contentList().iterator();
    }

    public boolean remove(Comment comment) {
        return removeNode(comment);
    }

    public boolean remove(Element element) {
        return removeNode(element);
    }

    public boolean remove(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return remove((Element) node);
            case DEFAULT_CONTENT_LIST_SIZE:
                return remove((Comment) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return remove((ProcessingInstruction) node);
            default:
                invalidNodeTypeAddException(node);
                return false;
        }
    }

    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }

    protected abstract boolean removeNode(Node node);

    public void setProcessingInstructions(List<ProcessingInstruction> listOfPIs) {
        Iterator iter = listOfPIs.iterator();
        while (iter.hasNext()) {
            addNode((ProcessingInstruction) iter.next());
        }
    }
}