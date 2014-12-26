package org.dom4j;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface Node extends Cloneable {
    void accept(Visitor visitor);

    String asXML();

    Node asXPathResult(Element element);

    Object clone();

    XPath createXPath(String str) throws InvalidXPathException;

    Node detach();

    Document getDocument();

    String getName();

    short getNodeType();

    NodeType getNodeTypeEnum();

    String getNodeTypeName();

    Element getParent();

    String getPath();

    String getPath(Element element);

    String getStringValue();

    String getText();

    String getUniquePath();

    String getUniquePath(Element element);

    boolean hasContent();

    boolean isReadOnly();

    boolean matches(String str);

    Number numberValueOf(String str);

    List<? extends Node> selectNodes(String str);

    List<? extends Node> selectNodes(String str, String str2);

    List<? extends Node> selectNodes(String str, String str2, boolean z);

    Object selectObject(String str);

    Node selectSingleNode(String str);

    void setDocument(Document document);

    void setName(String str);

    void setParent(Element element);

    void setText(String str);

    boolean supportsParent();

    String valueOf(String str);

    void write(Writer writer) throws IOException;
}