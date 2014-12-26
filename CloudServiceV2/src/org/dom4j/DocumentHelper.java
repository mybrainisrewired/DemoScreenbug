package org.dom4j;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.io.SAXReader;
import org.dom4j.rule.Pattern;
import org.jaxen.VariableContext;
import org.xml.sax.InputSource;

public final class DocumentHelper {
    private DocumentHelper() {
    }

    public static Attribute createAttribute(Element owner, String name, String value) {
        return getDocumentFactory().createAttribute(owner, name, value);
    }

    public static Attribute createAttribute(Element owner, QName qname, String value) {
        return getDocumentFactory().createAttribute(owner, qname, value);
    }

    public static CDATA createCDATA(String text) {
        return DocumentFactory.getInstance().createCDATA(text);
    }

    public static Comment createComment(String text) {
        return DocumentFactory.getInstance().createComment(text);
    }

    public static Document createDocument() {
        return getDocumentFactory().createDocument();
    }

    public static Document createDocument(Element rootElement) {
        return getDocumentFactory().createDocument(rootElement);
    }

    public static Element createElement(String name) {
        return getDocumentFactory().createElement(name);
    }

    public static Element createElement(QName qname) {
        return getDocumentFactory().createElement(qname);
    }

    public static Entity createEntity(String name, String text) {
        return DocumentFactory.getInstance().createEntity(name, text);
    }

    public static Namespace createNamespace(String prefix, String uri) {
        return DocumentFactory.getInstance().createNamespace(prefix, uri);
    }

    public static Pattern createPattern(String xpathPattern) {
        return getDocumentFactory().createPattern(xpathPattern);
    }

    public static ProcessingInstruction createProcessingInstruction(String pi, String d) {
        return getDocumentFactory().createProcessingInstruction(pi, d);
    }

    public static ProcessingInstruction createProcessingInstruction(String pi, Map data) {
        return getDocumentFactory().createProcessingInstruction(pi, data);
    }

    public static QName createQName(String localName) {
        return getDocumentFactory().createQName(localName);
    }

    public static QName createQName(String localName, Namespace namespace) {
        return getDocumentFactory().createQName(localName, namespace);
    }

    public static Text createText(String text) {
        return DocumentFactory.getInstance().createText(text);
    }

    public static XPath createXPath(String xpathExpression) throws InvalidXPathException {
        return getDocumentFactory().createXPath(xpathExpression);
    }

    public static XPath createXPath(String xpathExpression, VariableContext context) throws InvalidXPathException {
        return getDocumentFactory().createXPath(xpathExpression, context);
    }

    public static NodeFilter createXPathFilter(String xpathFilterExpression) {
        return getDocumentFactory().createXPathFilter(xpathFilterExpression);
    }

    private static DocumentFactory getDocumentFactory() {
        return DocumentFactory.getInstance();
    }

    private static String getEncoding(String text) {
        String xml = text.trim();
        if (!xml.startsWith("<?xml")) {
            return null;
        }
        StringTokenizer tokens = new StringTokenizer(xml.substring(0, xml.indexOf("?>")), " =\"'");
        while (tokens.hasMoreTokens()) {
            if ("encoding".equals(tokens.nextToken())) {
                return tokens.hasMoreTokens() ? tokens.nextToken() : null;
            }
        }
        return null;
    }

    public static Element makeElement(Branch source, String path) {
        Element parent;
        String name;
        StringTokenizer tokens = new StringTokenizer(path, "/");
        if (source instanceof Document) {
            Document document = (Document) source;
            parent = document.getRootElement();
            name = tokens.nextToken();
            if (parent == null) {
                parent = document.addElement(name);
            }
        } else {
            parent = (Element) source;
        }
        Element element = null;
        while (tokens.hasMoreTokens()) {
            name = tokens.nextToken();
            if (name.indexOf(Opcodes.ASTORE) > 0) {
                element = parent.element(parent.getQName(name));
            } else {
                element = parent.element(name);
            }
            if (element == null) {
                element = parent.addElement(name);
            }
            parent = element;
        }
        return element;
    }

    public static Document parseText(String text) throws DocumentException {
        SAXReader reader = new SAXReader();
        String encoding = getEncoding(text);
        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding(encoding);
        Document result = reader.read(source);
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }
        return result;
    }

    public static List<? extends Node> selectNodes(String xpathFilterExpression, List<? extends Node> nodes) {
        return createXPath(xpathFilterExpression).selectNodes(nodes);
    }

    public static List<? extends Node> selectNodes(String xpathFilterExpression, Node node) {
        return createXPath(xpathFilterExpression).selectNodes(node);
    }

    public static void sort(List<? extends Node> list, String xpathExpression) {
        createXPath(xpathExpression).sort(list);
    }

    public static void sort(List<? extends Node> list, String expression, boolean distinct) {
        createXPath(expression).sort(list, distinct);
    }
}