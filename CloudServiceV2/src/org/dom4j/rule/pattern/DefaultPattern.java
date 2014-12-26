package org.dom4j.rule.pattern;

import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.NodeType;
import org.dom4j.rule.Pattern;

public class DefaultPattern implements Pattern {
    private NodeFilter filter;

    public DefaultPattern(NodeFilter filter) {
        this.filter = filter;
    }

    public NodeType getMatchType() {
        return NodeType.ANY_NODE;
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
        return this.filter.matches(node);
    }
}