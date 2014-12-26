package org.dom4j.rule;

import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.NodeType;

public interface Pattern extends NodeFilter {
    public static final double DEFAULT_PRIORITY = 0.5d;

    NodeType getMatchType();

    String getMatchesNodeName();

    double getPriority();

    Pattern[] getUnionPatterns();

    boolean matches(Node node);
}