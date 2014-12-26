package org.dom4j;

import java.util.EnumSet;
import org.dom4j.tree.BackedList;

public final class NodeHelper {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final EnumSet<NodeType> BRANCH_TYPE;

    static {
        $assertionsDisabled = !NodeHelper.class.desiredAssertionStatus();
        BRANCH_TYPE = EnumSet.of(NodeType.ELEMENT_NODE, NodeType.DOCUMENT_NODE);
    }

    private NodeHelper() {
    }

    public static BackedList<Element> appendElementLocal(Node node, BackedList<Element> list) {
        Element element = nodeAsElement(node);
        if (element != null) {
            list.addLocal(element);
        }
        return list;
    }

    public static BackedList<Element> appendElementNamedLocal(Node node, BackedList<Element> list, String name) {
        Element element = nodeAsElement(node);
        if (element != null && name.equals(element.getName())) {
            list.addLocal(element);
        }
        return list;
    }

    public static BackedList<Element> appendElementQNamedLocal(Node node, BackedList<Element> list, QName qname) {
        Element element = nodeAsElement(node);
        if (element != null && qname.equals(element.getQName())) {
            list.addLocal(element);
        }
        return list;
    }

    public static String getAttributeValue(Attribute attribute, String defaultValue) {
        return attribute == null ? defaultValue : attribute.getValue();
    }

    public static Attribute nodeAsAttribute(Node node) {
        if (node.getNodeTypeEnum() != NodeType.ATTRIBUTE_NODE) {
            return null;
        }
        if ($assertionsDisabled || node instanceof Attribute) {
            return (Attribute) node;
        }
        throw new AssertionError();
    }

    public static Branch nodeAsBranch(Node node) {
        if (!BRANCH_TYPE.contains(Short.valueOf(node.getNodeType()))) {
            return null;
        }
        if ($assertionsDisabled || node instanceof Branch) {
            return (Branch) node;
        }
        throw new AssertionError();
    }

    public static Document nodeAsDocument(Node node) {
        if (node.getNodeTypeEnum() != NodeType.DOCUMENT_NODE) {
            return null;
        }
        if ($assertionsDisabled || node instanceof Document) {
            return (Document) node;
        }
        throw new AssertionError();
    }

    public static Element nodeAsElement(Node node) {
        if (node.getNodeTypeEnum() != NodeType.ELEMENT_NODE) {
            return null;
        }
        if ($assertionsDisabled || node instanceof Element) {
            return (Element) node;
        }
        throw new AssertionError();
    }

    public static Namespace nodeAsNamespace(Node node) {
        if (node.getNodeTypeEnum() != NodeType.NAMESPACE_NODE) {
            return null;
        }
        if ($assertionsDisabled || node instanceof Namespace) {
            return (Namespace) node;
        }
        throw new AssertionError();
    }

    public static ProcessingInstruction nodeAsProcessingInstruction(Node node) {
        if (node.getNodeTypeEnum() != NodeType.PROCESSING_INSTRUCTION_NODE) {
            return null;
        }
        if ($assertionsDisabled || node instanceof ProcessingInstruction) {
            return (ProcessingInstruction) node;
        }
        throw new AssertionError();
    }
}