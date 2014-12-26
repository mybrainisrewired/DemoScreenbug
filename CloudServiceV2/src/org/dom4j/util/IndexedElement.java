package org.dom4j.util;

import com.wmt.data.LocalAudioAll;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.NodeHelper;
import org.dom4j.NodeType;
import org.dom4j.QName;
import org.dom4j.tree.BackedList;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.LazyList;

public class IndexedElement extends DefaultElement {
    private DoubleNameMap<Attribute> attributeIndex;
    private DoubleNameMap<List<Element>> elementIndex;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 2;
        }
    }

    public IndexedElement(String name) {
        super(name);
    }

    public IndexedElement(QName qname) {
        super(qname);
    }

    public IndexedElement(QName qname, int attributeCount) {
        super(qname, attributeCount);
    }

    protected static Element firstElement(List<Element> list) {
        return list.isEmpty() ? null : (Element) list.get(0);
    }

    protected void addNode(Node node) {
        super.addNode(node);
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                if (this.elementIndex != null) {
                    addToElementIndex((Element) node);
                }
            case ClassWriter.COMPUTE_FRAMES:
                if (this.attributeIndex != null) {
                    addToAttributeIndex((Attribute) node);
                }
            default:
                break;
        }
    }

    protected void addToAttributeIndex(Attribute attribute) {
        this.attributeIndex.put(attribute.getQName(), attribute);
    }

    protected void addToElementIndex(Element element) {
        QName qName = element.getQName();
        List<Element> list = (List) this.elementIndex.get(qName);
        if (list == null) {
            list = new LazyList();
            this.elementIndex.put(qName, list);
        }
        list.add(element);
    }

    protected List<Element> asElementList(List<Element> list) {
        BackedList<Element> answer = createResultList();
        Iterator i$ = list.iterator();
        while (i$.hasNext()) {
            answer.addLocal((Element) i$.next());
        }
        return answer;
    }

    public Attribute attribute(String name) {
        return (Attribute) attributeIndex().get(name);
    }

    public Attribute attribute(QName qName) {
        return (Attribute) attributeIndex().get(qName);
    }

    protected DoubleNameMap<Attribute> attributeIndex() {
        if (this.attributeIndex == null) {
            this.attributeIndex = new DoubleNameMap();
            Iterator i$ = attributeList().iterator();
            while (i$.hasNext()) {
                addToAttributeIndex((Attribute) i$.next());
            }
        }
        return this.attributeIndex;
    }

    public Element element(String name) {
        return firstElement((List) elementIndex().get(name));
    }

    public Element element(QName qName) {
        return firstElement((List) elementIndex().get(qName));
    }

    protected DoubleNameMap<List<Element>> elementIndex() {
        if (this.elementIndex == null) {
            this.elementIndex = new DoubleNameMap();
            Iterator i$ = contentList().iterator();
            while (i$.hasNext()) {
                Element element = NodeHelper.nodeAsElement((Node) i$.next());
                if (element != null) {
                    addToElementIndex(element);
                }
            }
        }
        return this.elementIndex;
    }

    public List<Element> elements(String name) {
        return asElementList((List) elementIndex().get(name));
    }

    public List<Element> elements(QName qName) {
        return asElementList((List) elementIndex().get(qName));
    }

    protected void removeFromAttributeIndex(Attribute attribute) {
        this.attributeIndex.remove(attribute.getQName());
    }

    protected void removeFromElementIndex(Element element) {
        QName qName = element.getQName();
        List<Element> list = (List) this.elementIndex.get(qName);
        if (list != null) {
            list.remove(element);
            if (list.isEmpty()) {
                this.elementIndex.remove(qName);
            }
        }
    }

    protected boolean removeNode(Node node) {
        if (!super.removeNode(node)) {
            return false;
        }
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                if (this.elementIndex != null) {
                    removeFromElementIndex((Element) node);
                }
                break;
            case ClassWriter.COMPUTE_FRAMES:
                if (this.attributeIndex != null) {
                    removeFromAttributeIndex((Attribute) node);
                }
                break;
        }
        return true;
    }
}