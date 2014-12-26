package org.dom4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.dom4j.rule.Pattern;
import org.dom4j.tree.AbstractDocument;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultDocumentType;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.dom4j.tree.DefaultText;
import org.dom4j.tree.QNameCache;
import org.dom4j.util.SimpleSingleton;
import org.dom4j.util.SingletonStrategy;
import org.dom4j.xpath.DefaultXPath;
import org.dom4j.xpath.XPathPattern;
import org.jaxen.VariableContext;

public class DocumentFactory implements Serializable {
    private static SingletonStrategy singleton;
    protected transient QNameCache cache;
    private Map xpathNamespaceURIs;

    static {
        singleton = null;
    }

    public DocumentFactory() {
        init();
    }

    protected static DocumentFactory createSingleton(String className) {
        try {
            return (DocumentFactory) Class.forName(className, true, DocumentFactory.class.getClassLoader()).newInstance();
        } catch (Throwable th) {
            System.out.println("WARNING: Cannot load DocumentFactory: " + className);
            return new DocumentFactory();
        }
    }

    private static SingletonStrategy createSingleton() {
        String documentFactoryClassName;
        SingletonStrategy result;
        try {
            documentFactoryClassName = System.getProperty("org.dom4j.factory", "org.dom4j.DocumentFactory");
        } catch (Exception e) {
            documentFactoryClassName = "org.dom4j.DocumentFactory";
        }
        try {
            result = Class.forName(System.getProperty("org.dom4j.DocumentFactory.singleton.strategy", "org.dom4j.util.SimpleSingleton")).newInstance();
        } catch (Exception e2) {
            result = new SimpleSingleton();
        }
        result.setSingletonClassName(documentFactoryClassName);
        return result;
    }

    public static synchronized DocumentFactory getInstance() {
        DocumentFactory documentFactory;
        synchronized (DocumentFactory.class) {
            if (singleton == null) {
                singleton = createSingleton();
            }
            documentFactory = (DocumentFactory) singleton.instance();
        }
        return documentFactory;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    public Attribute createAttribute(Element owner, String name, String value) {
        return createAttribute(owner, createQName(name), value);
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DefaultAttribute(qname, value);
    }

    public CDATA createCDATA(String text) {
        return new DefaultCDATA(text);
    }

    public Comment createComment(String text) {
        return new DefaultComment(text);
    }

    public DocumentType createDocType(String name, String publicId, String systemId) {
        return new DefaultDocumentType(name, publicId, systemId);
    }

    public Document createDocument() {
        DefaultDocument answer = new DefaultDocument();
        answer.setDocumentFactory(this);
        return answer;
    }

    public Document createDocument(String encoding) {
        Document answer = createDocument();
        if (answer instanceof AbstractDocument) {
            ((AbstractDocument) answer).setXMLEncoding(encoding);
        }
        return answer;
    }

    public Document createDocument(Element rootElement) {
        Document answer = createDocument();
        answer.setRootElement(rootElement);
        return answer;
    }

    public Element createElement(String name) {
        return createElement(createQName(name));
    }

    public Element createElement(String qualifiedName, String namespaceURI) {
        return createElement(createQName(qualifiedName, namespaceURI));
    }

    public Element createElement(QName qname) {
        return new DefaultElement(qname);
    }

    public Entity createEntity(String name, String text) {
        return new DefaultEntity(name, text);
    }

    public Namespace createNamespace(String prefix, String uri) {
        return Namespace.get(prefix, uri);
    }

    public Pattern createPattern(String xpathPattern) {
        return new XPathPattern(xpathPattern);
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DefaultProcessingInstruction(target, data);
    }

    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DefaultProcessingInstruction(target, data);
    }

    public QName createQName(String localName) {
        return this.cache.get(localName);
    }

    public QName createQName(String qualifiedName, String uri) {
        return this.cache.get(qualifiedName, uri);
    }

    public QName createQName(String name, String prefix, String uri) {
        return this.cache.get(name, Namespace.get(prefix, uri));
    }

    public QName createQName(String localName, Namespace namespace) {
        return this.cache.get(localName, namespace);
    }

    protected QNameCache createQNameCache() {
        return new QNameCache(this);
    }

    public Text createText(String text) {
        if (text != null) {
            return new DefaultText(text);
        }
        throw new IllegalArgumentException("Adding text to an XML document must not be null");
    }

    public XPath createXPath(String xpathExpression) throws InvalidXPathException {
        DefaultXPath xpath = new DefaultXPath(xpathExpression);
        if (this.xpathNamespaceURIs != null) {
            xpath.setNamespaceURIs(this.xpathNamespaceURIs);
        }
        return xpath;
    }

    public XPath createXPath(String xpathExpression, VariableContext variableContext) {
        XPath xpath = createXPath(xpathExpression);
        xpath.setVariableContext(variableContext);
        return xpath;
    }

    public NodeFilter createXPathFilter(String xpathFilterExpression) {
        return createXPath(xpathFilterExpression);
    }

    public NodeFilter createXPathFilter(String xpathFilterExpression, VariableContext variableContext) {
        XPath answer = createXPath(xpathFilterExpression);
        answer.setVariableContext(variableContext);
        return answer;
    }

    public List<QName> getQNames() {
        return this.cache.getQNames();
    }

    public Map<String, String> getXPathNamespaceURIs() {
        return this.xpathNamespaceURIs;
    }

    protected void init() {
        this.cache = createQNameCache();
    }

    protected QName intern(QName qname) {
        return this.cache.intern(qname);
    }

    public void setXPathNamespaceURIs(Map<String, String> namespaceURIs) {
        this.xpathNamespaceURIs = namespaceURIs;
    }
}