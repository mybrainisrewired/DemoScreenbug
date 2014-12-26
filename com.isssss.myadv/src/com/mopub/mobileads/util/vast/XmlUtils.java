package com.mopub.mobileads.util.vast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XmlUtils {
    private XmlUtils() {
    }

    static String getAttributeValue(Node node, String attributeName) {
        if (node == null || attributeName == null) {
            return null;
        }
        Node attrNode = node.getAttributes().getNamedItem(attributeName);
        return attrNode != null ? attrNode.getNodeValue() : null;
    }

    static Integer getAttributeValueAsInt(Node node, String attributeName) {
        if (node == null || attributeName == null) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(getAttributeValue(node, attributeName)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Node getFirstMatchingChildNode(Node node, String nodeName) {
        return getFirstMatchingChildNode(node, nodeName, null, null);
    }

    static Node getFirstMatchingChildNode(Node node, String nodeName, String attributeName, List<String> attributeValues) {
        if (node == null || nodeName == null) {
            return null;
        }
        List<Node> nodes = getMatchingChildNodes(node, nodeName, attributeName, attributeValues);
        return (nodes == null || nodes.isEmpty()) ? null : (Node) nodes.get(0);
    }

    static List<Node> getMatchingChildNodes(Node node, String nodeName, String attributeName, List<String> attributeValues) {
        if (node == null || nodeName == null) {
            return null;
        }
        List<Node> nodes = new ArrayList();
        NodeList nodeList = node.getChildNodes();
        int i = 0;
        while (i < nodeList.getLength()) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeName().equals(nodeName) && nodeMatchesAttributeFilter(childNode, attributeName, attributeValues)) {
                nodes.add(childNode);
            }
            i++;
        }
        return nodes;
    }

    static String getNodeValue(Node node) {
        return (node == null || node.getFirstChild() == null || node.getFirstChild().getNodeValue() == null) ? null : node.getFirstChild().getNodeValue().trim();
    }

    static List<String> getStringDataAsList(Document vastDoc, String elementName) {
        return getStringDataAsList(vastDoc, elementName, null, null);
    }

    static List<String> getStringDataAsList(Document vastDoc, String elementName, String attributeName, String attributeValue) {
        ArrayList<String> results = new ArrayList();
        if (vastDoc != null) {
            NodeList nodes = vastDoc.getElementsByTagName(elementName);
            if (nodes != null) {
                int i = 0;
                while (i < nodes.getLength()) {
                    Node node = nodes.item(i);
                    if (node != null) {
                        if (nodeMatchesAttributeFilter(node, attributeName, Arrays.asList(new String[]{attributeValue}))) {
                            String nodeValue = getNodeValue(node);
                            if (nodeValue != null) {
                                results.add(nodeValue);
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return results;
    }

    static boolean nodeMatchesAttributeFilter(Node node, String attributeName, List<String> attributeValues) {
        if (attributeName == null || attributeValues == null) {
            return true;
        }
        NamedNodeMap attrMap = node.getAttributes();
        if (attrMap != null) {
            Node attrNode = attrMap.getNamedItem(attributeName);
            if (attrNode != null && attributeValues.contains(attrNode.getNodeValue())) {
                return true;
            }
        }
        return false;
    }
}