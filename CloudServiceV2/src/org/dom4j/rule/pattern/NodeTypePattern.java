package org.dom4j.rule.pattern;

import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.rule.Pattern;

public class NodeTypePattern implements Pattern {
    public static final NodeTypePattern ANY_ATTRIBUTE;
    public static final NodeTypePattern ANY_COMMENT;
    public static final NodeTypePattern ANY_DOCUMENT;
    public static final NodeTypePattern ANY_ELEMENT;
    public static final NodeTypePattern ANY_PROCESSING_INSTRUCTION;
    public static final NodeTypePattern ANY_TEXT;
    private NodeType nodeType;

    static {
        ANY_ATTRIBUTE = new NodeTypePattern(NodeType.ATTRIBUTE_NODE);
        ANY_COMMENT = new NodeTypePattern(NodeType.COMMENT_NODE);
        ANY_DOCUMENT = new NodeTypePattern(NodeType.DOCUMENT_NODE);
        ANY_ELEMENT = new NodeTypePattern(NodeType.ELEMENT_NODE);
        ANY_PROCESSING_INSTRUCTION = new NodeTypePattern(NodeType.PROCESSING_INSTRUCTION_NODE);
        ANY_TEXT = new NodeTypePattern(NodeType.TEXT_NODE);
    }

    public NodeTypePattern(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getMatchType() {
        return this.nodeType;
    }

    public String getMatchesNodeName() {
        return null;
    }

    public double getPriority() {
        return Pattern.DEFAULT_PRIORITY;
    }

    public Pattern[] getUnionPatterns() {
        return null;
    }

    public boolean matches(Node node) {
        return node.getNodeTypeEnum() == this.nodeType;
    }
}