package org.dom4j.dom;

import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.util.SingletonStrategy;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

public class DOMDocumentFactory extends DocumentFactory implements DOMImplementation {
    private static SingletonStrategy singleton;

    static {
        singleton = null;
        String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
        Class clazz = null;
        try {
            clazz = Class.forName(System.getProperty("org.dom4j.dom.DOMDocumentFactory.singleton.strategy", defaultSingletonClass));
        } catch (Exception e) {
            try {
                clazz = Class.forName(defaultSingletonClass);
            } catch (Exception e2) {
            }
        }
        singleton = (SingletonStrategy) clazz.newInstance();
        singleton.setSingletonClassName(DOMDocumentFactory.class.getName());
    }

    public static DocumentFactory getInstance() {
        return (DOMDocumentFactory) singleton.instance();
    }

    protected DOMDocumentType asDocumentType(DocumentType docType) {
        return docType instanceof DOMDocumentType ? (DOMDocumentType) docType : new DOMDocumentType(docType.getName(), docType.getPublicId(), docType.getSystemId());
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DOMAttribute(qname, value);
    }

    public CDATA createCDATA(String text) {
        return new DOMCDATA(text);
    }

    public Comment createComment(String text) {
        return new DOMComment(text);
    }

    public org.dom4j.DocumentType createDocType(String name, String publicId, String systemId) {
        return new DOMDocumentType(name, publicId, systemId);
    }

    public Document createDocument() {
        DOMDocument answer = new DOMDocument();
        answer.setDocumentFactory(this);
        return answer;
    }

    public org.w3c.dom.Document createDocument(String namespaceURI, String qualifiedName, DocumentType docType) throws DOMException {
        DOMDocument document;
        if (docType != null) {
            document = new DOMDocument(asDocumentType(docType));
        } else {
            document = new DOMDocument();
        }
        document.addElement(createQName(qualifiedName, namespaceURI));
        return document;
    }

    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws DOMException {
        return new DOMDocumentType(qualifiedName, publicId, systemId);
    }

    public Element createElement(QName qname) {
        return new DOMElement(qname);
    }

    public Element createElement(QName qname, int attributeCount) {
        return new DOMElement(qname, attributeCount);
    }

    public Entity createEntity(String name) {
        return new DOMEntityReference(name);
    }

    public Entity createEntity(String name, String text) {
        return new DOMEntityReference(name, text);
    }

    public Namespace createNamespace(String prefix, String uri) {
        return new DOMNamespace(prefix, uri);
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DOMProcessingInstruction(target, data);
    }

    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DOMProcessingInstruction(target, data);
    }

    public Text createText(String text) {
        return new DOMText(text);
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasFeature(String feat, String version) {
        if ("XML".equalsIgnoreCase(feat) || "Core".equalsIgnoreCase(feat)) {
            return version == null || version.length() == 0 || "1.0".equals(version) || "2.0".equals(version);
        } else {
            return false;
        }
    }
}