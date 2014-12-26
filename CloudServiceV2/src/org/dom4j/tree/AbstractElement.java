package org.dom4j.tree;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeHelper;
import org.dom4j.NodeType;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.Attributes;

public abstract class AbstractElement extends AbstractBranch implements Element {
    private static final DocumentFactory DOCUMENT_FACTORY;
    protected static final boolean USE_STRINGVALUE_SEPARATOR = false;
    protected static final boolean VERBOSE_TOSTRING = false;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.TEXT_NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.CDATA_SECTION_NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ENTITY_REFERENCE_NODE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.PROCESSING_INSTRUCTION_NODE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.COMMENT_NODE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.NAMESPACE_NODE.ordinal()] = 8;
        }
    }

    static {
        DOCUMENT_FACTORY = DocumentFactory.getInstance();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
        int i = 0;
        int size = attributeCount();
        while (i < size) {
            visitor.visit(attribute(i));
            i++;
        }
        i = 0;
        size = nodeCount();
        while (i < size) {
            node(i).accept(visitor);
            i++;
        }
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

    public void add(CDATA cdata) {
        addNode(cdata);
    }

    public void add(Comment comment) {
        addNode(comment);
    }

    public void add(Element element) {
        addNode(element);
    }

    public void add(Entity entity) {
        addNode(entity);
    }

    public void add(Namespace namespace) {
        addNode(namespace);
    }

    public void add(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                add((Element) node);
            case ClassWriter.COMPUTE_FRAMES:
                add((Attribute) node);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                add((Text) node);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                add((CDATA) node);
            case JsonWriteContext.STATUS_EXPECT_NAME:
                add((Entity) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                add((ProcessingInstruction) node);
            case Type.LONG:
                add((Comment) node);
            case Type.DOUBLE:
                add((Namespace) node);
            default:
                invalidNodeTypeAddException(node);
        }
    }

    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }

    public void add(Text text) {
        addNode(text);
    }

    public Element addAttribute(String name, String value) {
        Attribute attribute = attribute(name);
        if (value != null) {
            if (attribute == null) {
                add(getDocumentFactory().createAttribute(this, name, value));
            } else if (attribute.isReadOnly()) {
                remove(attribute);
                add(getDocumentFactory().createAttribute(this, name, value));
            } else {
                attribute.setValue(value);
            }
        } else if (attribute != null) {
            remove(attribute);
        }
        return this;
    }

    public Element addAttribute(QName qName, String value) {
        Attribute attribute = attribute(qName);
        if (value != null) {
            if (attribute == null) {
                add(getDocumentFactory().createAttribute(this, qName, value));
            } else if (attribute.isReadOnly()) {
                remove(attribute);
                add(getDocumentFactory().createAttribute(this, qName, value));
            } else {
                attribute.setValue(value);
            }
        } else if (attribute != null) {
            remove(attribute);
        }
        return this;
    }

    public Element addCDATA(String cdata) {
        addNewNode(getDocumentFactory().createCDATA(cdata));
        return this;
    }

    public Element addComment(String comment) {
        addNewNode(getDocumentFactory().createComment(comment));
        return this;
    }

    public Element addElement(String name) {
        Namespace namespace;
        Element node;
        DocumentFactory factory = getDocumentFactory();
        int index = name.indexOf(":");
        String str = "";
        String localName = name;
        if (index > 0) {
            str = name.substring(0, index);
            localName = name.substring(index + 1);
            namespace = getNamespaceForPrefix(str);
            if (namespace == null) {
                throw new IllegalAddException("No such namespace prefix: " + str + " is in scope on: " + this + " so cannot add element: " + name);
            }
        } else {
            namespace = getNamespaceForPrefix("");
        }
        if (namespace != null) {
            node = factory.createElement(factory.createQName(localName, namespace));
        } else {
            node = factory.createElement(name);
        }
        addNewNode(node);
        return node;
    }

    public Element addEntity(String name, String text) {
        addNewNode(getDocumentFactory().createEntity(name, text));
        return this;
    }

    public Element addNamespace(String prefix, String uri) {
        addNewNode(getDocumentFactory().createNamespace(prefix, uri));
        return this;
    }

    protected void addNewNode(int index, Node node) {
        contentList().add(index, node);
        childAdded(node);
    }

    protected void addNewNode(Node node) {
        contentList().add(node);
        childAdded(node);
    }

    protected void addNode(int index, Node node) {
        if (node.getParent() != null) {
            throw new IllegalAddException(this, node, "The Node already has an existing parent of \"" + node.getParent().getQualifiedName() + "\"");
        }
        addNewNode(index, node);
    }

    protected void addNode(Node node) {
        if (node.getParent() != null) {
            throw new IllegalAddException(this, node, "The Node already has an existing parent of \"" + node.getParent().getQualifiedName() + "\"");
        }
        addNewNode(node);
    }

    public Element addProcessingInstruction(String target, String data) {
        addNewNode(getDocumentFactory().createProcessingInstruction(target, data));
        return this;
    }

    public Element addProcessingInstruction(String target, Map data) {
        addNewNode(getDocumentFactory().createProcessingInstruction(target, data));
        return this;
    }

    public Element addText(String text) {
        addNewNode(getDocumentFactory().createText(text));
        return this;
    }

    public List<Namespace> additionalNamespaces() {
        List<? extends Node> list = contentList();
        int size = list.size();
        BackedList<Namespace> answer = createResultList();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof Namespace) {
                Namespace namespace = (Namespace) node;
                if (!namespace.equals(getNamespace())) {
                    answer.addLocal(namespace);
                }
            }
            i++;
        }
        return answer;
    }

    public List<Namespace> additionalNamespaces(String defaultNamespaceURI) {
        List<? extends Node> list = contentList();
        BackedList<Namespace> answer = createResultList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof Namespace) {
                Namespace namespace = (Namespace) node;
                if (!defaultNamespaceURI.equals(namespace.getURI())) {
                    answer.addLocal(namespace);
                }
            }
            i++;
        }
        return answer;
    }

    public void appendAttributes(Element element) {
        int i = 0;
        int size = element.attributeCount();
        while (i < size) {
            Attribute attribute = element.attribute(i);
            if (attribute.supportsParent()) {
                addAttribute(attribute.getQName(), attribute.getValue());
            } else {
                add(attribute);
            }
            i++;
        }
    }

    public String asXML() {
        try {
            Writer out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, new OutputFormat());
            writer.write(this);
            writer.flush();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException while generating textual representation: " + e.getMessage());
        }
    }

    public Attribute attribute(int index) {
        return (Attribute) attributeList().get(index);
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

    protected abstract List<Attribute> attributeList();

    protected abstract List<Attribute> attributeList(int i);

    public String attributeValue(String name) {
        return NodeHelper.getAttributeValue(attribute(name), null);
    }

    public String attributeValue(String name, String defaultValue) {
        return NodeHelper.getAttributeValue(attribute(name), defaultValue);
    }

    public String attributeValue(QName qName) {
        return NodeHelper.getAttributeValue(attribute(qName), null);
    }

    public String attributeValue(QName qName, String defaultValue) {
        return NodeHelper.getAttributeValue(attribute(qName), defaultValue);
    }

    public List<Attribute> attributes() {
        return new ContentListFacade(this, attributeList());
    }

    protected void childAdded(Node node) {
        if (node != null) {
            node.setParent(this);
        }
    }

    protected void childRemoved(Node node) {
        if (node != null) {
            node.setParent(null);
            node.setDocument(null);
        }
    }

    protected List<Attribute> createAttributeList() {
        return createAttributeList(JsonWriteContext.STATUS_EXPECT_NAME);
    }

    protected List<Attribute> createAttributeList(int size) {
        return new ArrayList(size);
    }

    public Element createCopy() {
        Element clone = createElement(getQName());
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }

    public Element createCopy(String name) {
        Element clone = createElement(name);
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }

    public Element createCopy(QName qName) {
        Element clone = createElement(qName);
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }

    protected Element createElement(String name) {
        return getDocumentFactory().createElement(name);
    }

    protected Element createElement(QName qName) {
        return getDocumentFactory().createElement(qName);
    }

    @Deprecated
    protected <T> Iterator<T> createSingleIterator(T result) {
        return new SingleIterator(result);
    }

    public List<Namespace> declaredNamespaces() {
        BackedList<Namespace> answer = createResultList();
        List<? extends Node> list = contentList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof Namespace) {
                answer.addLocal((Namespace) node);
            }
            i++;
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

    public Iterator<Element> elementIterator() {
        return elements().iterator();
    }

    public Iterator<Element> elementIterator(String name) {
        return elements(name).iterator();
    }

    public Iterator<Element> elementIterator(String name, Namespace ns) {
        return elementIterator(getDocumentFactory().createQName(name, ns));
    }

    public Iterator<Element> elementIterator(QName qName) {
        return elements(qName).iterator();
    }

    public String elementText(String name) {
        Element element = element(name);
        return element != null ? element.getText() : null;
    }

    public String elementText(QName qName) {
        Element element = element(qName);
        return element != null ? element.getText() : null;
    }

    public String elementTextTrim(String name) {
        Element element = element(name);
        return element != null ? element.getTextTrim() : null;
    }

    public String elementTextTrim(QName qName) {
        Element element = element(qName);
        return element != null ? element.getTextTrim() : null;
    }

    public List<Element> elements() {
        BackedList<Element> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            NodeHelper.appendElementLocal((Node) i$.next(), answer);
        }
        return answer;
    }

    public List<Element> elements(String name) {
        BackedList<Element> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            NodeHelper.appendElementNamedLocal((Node) i$.next(), answer, name);
        }
        return answer;
    }

    public List elements(String name, Namespace namespace) {
        return elements(getDocumentFactory().createQName(name, namespace));
    }

    public List<Element> elements(QName qName) {
        BackedList<Element> answer = createResultList();
        Iterator i$ = contentList().iterator();
        while (i$.hasNext()) {
            NodeHelper.appendElementQNamedLocal((Node) i$.next(), answer, qName);
        }
        return answer;
    }

    public void ensureAttributesCapacity(int minCapacity) {
        if (minCapacity > 1) {
            List<Attribute> list = attributeList();
            if (list instanceof ArrayList) {
                ((ArrayList) list).ensureCapacity(minCapacity);
            }
        }
    }

    public Object getData() {
        return getText();
    }

    protected DocumentFactory getDocumentFactory() {
        QName qName = getQName();
        if (qName != null) {
            DocumentFactory factory = qName.getDocumentFactory();
            if (factory != null) {
                return factory;
            }
        }
        return DOCUMENT_FACTORY;
    }

    public String getName() {
        return getQName().getName();
    }

    public Namespace getNamespace() {
        return getQName().getNamespace();
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
        List list = contentList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Object object = list.get(i);
            if (object instanceof Namespace) {
                Namespace namespace = (Namespace) object;
                if (prefix.equals(namespace.getPrefix())) {
                    return namespace;
                }
            }
            i++;
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
        List list = contentList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Object object = list.get(i);
            if (object instanceof Namespace) {
                Namespace namespace = (Namespace) object;
                if (uri.equals(namespace.getURI())) {
                    return namespace;
                }
            }
            i++;
        }
        return null;
    }

    public String getNamespacePrefix() {
        return getQName().getNamespacePrefix();
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public List<Namespace> getNamespacesForURI(String uri) {
        BackedList<Namespace> answer = createResultList();
        List<? extends Node> list = contentList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof Namespace && ((Namespace) node).getURI().equals(uri)) {
                answer.addLocal((Namespace) node);
            }
            i++;
        }
        return answer;
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.ELEMENT_NODE;
    }

    public String getPath(Element context) {
        if (this == context) {
            return ".";
        }
        Element parent = getParent();
        if (parent == null) {
            return "/" + getXPathNameStep();
        }
        return parent == context ? getXPathNameStep() : parent.getPath(context) + "/" + getXPathNameStep();
    }

    public QName getQName(String qualifiedName) {
        String prefix = "";
        String localName = qualifiedName;
        int index = qualifiedName.indexOf(":");
        if (index > 0) {
            prefix = qualifiedName.substring(0, index);
            localName = qualifiedName.substring(index + 1);
        }
        Namespace namespace = getNamespaceForPrefix(prefix);
        return namespace != null ? getDocumentFactory().createQName(localName, namespace) : getDocumentFactory().createQName(localName);
    }

    public String getQualifiedName() {
        return getQName().getQualifiedName();
    }

    public String getStringValue() {
        List<Node> list = contentList();
        int size = list.size();
        if (size <= 0) {
            return "";
        }
        if (size == 1) {
            return getContentAsStringValue((Node) list.get(0));
        }
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i < size) {
            String string = getContentAsStringValue((Node) list.get(i));
            if (string.length() > 0) {
                buffer.append(string);
            }
            i++;
        }
        return buffer.toString();
    }

    public String getUniquePath(Element context) {
        Element parent = getParent();
        if (parent == null) {
            return "/" + getXPathNameStep();
        }
        StringBuffer buffer = new StringBuffer();
        if (parent != context) {
            buffer.append(parent.getUniquePath(context));
            buffer.append("/");
        }
        buffer.append(getXPathNameStep());
        List mySiblings = parent.elements(getQName());
        if (mySiblings.size() > 1) {
            int idx = mySiblings.indexOf(this);
            if (idx >= 0) {
                buffer.append("[");
                buffer.append(Integer.toString(idx + 1));
                buffer.append("]");
            }
        }
        return buffer.toString();
    }

    public String getXPathNameStep() {
        String uri = getNamespaceURI();
        if (uri == null || uri.length() == 0) {
            return getName();
        }
        String prefix = getNamespacePrefix();
        return (prefix == null || prefix.length() == 0) ? "*[name()='" + getName() + "']" : getQualifiedName();
    }

    public Node getXPathResult(int index) {
        Node answer = node(index);
        return (answer == null || answer.supportsParent()) ? answer : answer.asXPathResult(this);
    }

    public boolean hasMixedContent() {
        List<? extends Node> content = contentList();
        if (content == null || content.isEmpty() || content.size() < 2) {
            return false;
        }
        Class prevClass = null;
        Iterator<? extends Node> iter = content.iterator();
        while (iter.hasNext()) {
            Class newClass = ((Node) iter.next()).getClass();
            if (newClass != prevClass && prevClass != null) {
                return true;
            }
            prevClass = newClass;
        }
        return false;
    }

    public int indexOf(Node node) {
        return contentList().indexOf(node);
    }

    public boolean isRootElement() {
        Document document = getDocument();
        return document != null && document.getRootElement() == this;
    }

    public boolean isTextOnly() {
        List<? extends Node> content = contentList();
        if (content == null || content.isEmpty()) {
            return true;
        }
        Iterator<? extends Node> iter = content.iterator();
        while (iter.hasNext()) {
            if (!((Node) iter.next()) instanceof CharacterData) {
                return false;
            }
        }
        return true;
    }

    public Node node(int index) {
        if (index < 0) {
            return null;
        }
        List<Node> list = contentList();
        return index >= list.size() ? null : (Node) list.get(index);
    }

    public int nodeCount() {
        return contentList().size();
    }

    public Iterator<Node> nodeIterator() {
        return contentList().iterator();
    }

    public void normalize() {
        List<Node> content = contentList();
        Text previousText = null;
        int i = 0;
        while (i < content.size()) {
            Node node = (Node) content.get(i);
            if (node instanceof Text) {
                Text text = (Text) node;
                if (previousText != null) {
                    previousText.appendText(text.getText());
                    remove(text);
                } else {
                    String value = text.getText();
                    if (value == null || value.length() <= 0) {
                        remove(text);
                    } else {
                        previousText = text;
                        i++;
                    }
                }
            } else {
                if (node instanceof Element) {
                    ((Element) node).normalize();
                }
                previousText = null;
                i++;
            }
        }
    }

    public ProcessingInstruction processingInstruction(String target) {
        List<? extends Node> list = contentList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                if (target.equals(pi.getName())) {
                    return pi;
                }
            }
            i++;
        }
        return null;
    }

    public List<ProcessingInstruction> processingInstructions() {
        List<? extends Node> list = contentList();
        BackedList<ProcessingInstruction> answer = createResultList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof ProcessingInstruction) {
                answer.addLocal((ProcessingInstruction) node);
            }
            i++;
        }
        return answer;
    }

    public List<ProcessingInstruction> processingInstructions(String target) {
        List<? extends Node> list = contentList();
        BackedList<ProcessingInstruction> answer = createResultList();
        int size = list.size();
        int i = 0;
        while (i < size) {
            Node node = (Node) list.get(i);
            if (node instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                if (target.equals(pi.getName())) {
                    answer.addLocal(pi);
                }
            }
            i++;
        }
        return answer;
    }

    public boolean remove(Attribute attribute) {
        List list = attributeList();
        boolean answer = list.remove(attribute);
        if (answer) {
            childRemoved(attribute);
            return answer;
        } else {
            Attribute copy = attribute(attribute.getQName());
            if (copy == null) {
                return answer;
            }
            list.remove(copy);
            return true;
        }
    }

    public boolean remove(CDATA cdata) {
        return removeNode(cdata);
    }

    public boolean remove(Comment comment) {
        return removeNode(comment);
    }

    public boolean remove(Element element) {
        return removeNode(element);
    }

    public boolean remove(Entity entity) {
        return removeNode(entity);
    }

    public boolean remove(Namespace namespace) {
        return removeNode(namespace);
    }

    public boolean remove(Node node) {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return remove((Element) node);
            case ClassWriter.COMPUTE_FRAMES:
                return remove((Attribute) node);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return remove((Text) node);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return remove((CDATA) node);
            case JsonWriteContext.STATUS_EXPECT_NAME:
                return remove((Entity) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return remove((ProcessingInstruction) node);
            case Type.LONG:
                return remove((Comment) node);
            case Type.DOUBLE:
                return remove((Namespace) node);
            default:
                return false;
        }
    }

    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }

    public boolean remove(Text text) {
        return removeNode(text);
    }

    protected boolean removeNode(Node node) {
        boolean answer = contentList().remove(node);
        if (answer) {
            childRemoved(node);
        }
        return answer;
    }

    public boolean removeProcessingInstruction(String target) {
        Iterator<? extends Node> iter = contentList().iterator();
        while (iter.hasNext()) {
            Node node = (Node) iter.next();
            if (node instanceof ProcessingInstruction && target.equals(((ProcessingInstruction) node).getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public void setAttributes(Attributes attributes, NamespaceStack namespaceStack, boolean noNamespaceAttributes) {
        int size = attributes.getLength();
        if (size > 0) {
            DocumentFactory factory = getDocumentFactory();
            String attributeURI;
            String attributeLocalName;
            if (size == 1) {
                String name = attributes.getQName(0);
                if (noNamespaceAttributes || !name.startsWith("xmlns")) {
                    attributeURI = attributes.getURI(0);
                    attributeLocalName = attributes.getLocalName(0);
                    add(factory.createAttribute(this, namespaceStack.getAttributeQName(attributeURI, attributeLocalName, name), attributes.getValue(0)));
                }
            } else {
                List<Attribute> list = attributeList(size);
                list.clear();
                int i = 0;
                while (i < size) {
                    String attributeName = attributes.getQName(i);
                    if (noNamespaceAttributes || !attributeName.startsWith("xmlns")) {
                        attributeURI = attributes.getURI(i);
                        attributeLocalName = attributes.getLocalName(i);
                        Attribute attribute = factory.createAttribute(this, namespaceStack.getAttributeQName(attributeURI, attributeLocalName, attributeName), attributes.getValue(i));
                        list.add(attribute);
                        childAdded(attribute);
                    }
                    i++;
                }
            }
        }
    }

    public void setData(Object data) {
    }

    public void setName(String name) {
        setQName(getDocumentFactory().createQName(name));
    }

    public void setNamespace(Namespace namespace) {
        setQName(getDocumentFactory().createQName(getName(), namespace));
    }

    public void setText(String text) {
        List allContent = contentList();
        if (allContent != null) {
            Iterator it = allContent.iterator();
            while (it.hasNext()) {
                switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[((Node) it.next()).getNodeTypeEnum().ordinal()]) {
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                    case JsonWriteContext.STATUS_EXPECT_NAME:
                        it.remove();
                        break;
                    default:
                        break;
                }
            }
        }
        addText(text);
    }

    protected void toString(StringBuilder builder) {
        String uri = getNamespaceURI();
        super.toString(builder);
        builder.append(" [Element: <");
        builder.append(getQualifiedName());
        if (uri != null && uri.length() > 0) {
            builder.append(" uri: ");
            builder.append(uri);
        }
        builder.append(" attributes: ");
        builder.append(attributeList());
        builder.append("/>]");
    }

    public void write(Writer out) throws IOException {
        new XMLWriter(out, new OutputFormat()).write(this);
    }
}