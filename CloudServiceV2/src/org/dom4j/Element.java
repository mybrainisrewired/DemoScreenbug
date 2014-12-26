package org.dom4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface Element extends Branch {
    void add(Attribute attribute);

    void add(CDATA cdata);

    void add(Entity entity);

    void add(Namespace namespace);

    void add(Text text);

    Element addAttribute(String str, String str2);

    Element addAttribute(QName qName, String str);

    Element addCDATA(String str);

    Element addComment(String str);

    Element addEntity(String str, String str2);

    Element addNamespace(String str, String str2);

    Element addProcessingInstruction(String str, String str2);

    Element addProcessingInstruction(String str, Map map);

    Element addText(String str);

    List<Namespace> additionalNamespaces();

    void appendAttributes(Element element);

    Attribute attribute(int i);

    Attribute attribute(String str);

    Attribute attribute(QName qName);

    int attributeCount();

    Iterator<Attribute> attributeIterator();

    String attributeValue(String str);

    String attributeValue(String str, String str2);

    String attributeValue(QName qName);

    String attributeValue(QName qName, String str);

    List<Attribute> attributes();

    Element createCopy();

    Element createCopy(String str);

    Element createCopy(QName qName);

    List<Namespace> declaredNamespaces();

    Element element(String str);

    Element element(QName qName);

    Iterator<Element> elementIterator();

    Iterator<Element> elementIterator(String str);

    Iterator<Element> elementIterator(QName qName);

    String elementText(String str);

    String elementText(QName qName);

    String elementTextTrim(String str);

    String elementTextTrim(QName qName);

    List<Element> elements();

    List<Element> elements(String str);

    List<Element> elements(QName qName);

    Object getData();

    Namespace getNamespace();

    Namespace getNamespaceForPrefix(String str);

    Namespace getNamespaceForURI(String str);

    String getNamespacePrefix();

    String getNamespaceURI();

    List<Namespace> getNamespacesForURI(String str);

    QName getQName();

    QName getQName(String str);

    String getQualifiedName();

    String getStringValue();

    String getText();

    String getTextTrim();

    Node getXPathResult(int i);

    boolean hasMixedContent();

    boolean isRootElement();

    boolean isTextOnly();

    boolean remove(Attribute attribute);

    boolean remove(CDATA cdata);

    boolean remove(Entity entity);

    boolean remove(Namespace namespace);

    boolean remove(Text text);

    void setAttributes(List<Attribute> list);

    void setData(Object obj);

    void setQName(QName qName);
}