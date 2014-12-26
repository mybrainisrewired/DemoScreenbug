package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.XSDatatype;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

class NamedTypeResolver {
    protected Map<QName, DocumentFactory> complexTypeMap;
    protected DocumentFactory documentFactory;
    protected Map<Element, DocumentFactory> elementFactoryMap;
    protected Map<QName, XSDatatype> simpleTypeMap;
    protected Map<Element, QName> typedElementMap;

    NamedTypeResolver(DocumentFactory documentFactory) {
        this.complexTypeMap = new HashMap();
        this.simpleTypeMap = new HashMap();
        this.typedElementMap = new HashMap();
        this.elementFactoryMap = new HashMap();
        this.documentFactory = documentFactory;
    }

    private QName getQName(String name) {
        return this.documentFactory.createQName(name);
    }

    private QName getQNameOfSchemaElement(Element element) {
        return getQName(element.attributeValue("name"));
    }

    void registerComplexType(QName type, DocumentFactory factory) {
        this.complexTypeMap.put(type, factory);
    }

    void registerSimpleType(QName type, XSDatatype datatype) {
        this.simpleTypeMap.put(type, datatype);
    }

    void registerTypedElement(Element element, QName type, DocumentFactory parentFactory) {
        this.typedElementMap.put(element, type);
        this.elementFactoryMap.put(element, parentFactory);
    }

    void resolveElementTypes() {
        Iterator i$ = this.typedElementMap.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<Element, QName> entry = (Entry) i$.next();
            Element element = (Element) entry.getKey();
            QName elementQName = getQNameOfSchemaElement(element);
            QName type = (QName) entry.getValue();
            if (this.complexTypeMap.containsKey(type)) {
                elementQName.setDocumentFactory((DocumentFactory) this.complexTypeMap.get(type));
            } else if (this.simpleTypeMap.containsKey(type)) {
                XSDatatype datatype = (XSDatatype) this.simpleTypeMap.get(type);
                DocumentFactory factory = this.elementFactoryMap.get(element);
                if (factory instanceof DatatypeElementFactory) {
                    ((DatatypeElementFactory) factory).setChildElementXSDatatype(elementQName, datatype);
                }
            }
        }
    }

    void resolveNamedTypes() {
        resolveElementTypes();
    }
}