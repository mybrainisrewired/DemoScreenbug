package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.msv.datatype.xsd.TypeIncubator;
import com.sun.msv.datatype.xsd.XSDatatype;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.util.AttributeHelper;
import org.relaxng.datatype.DatatypeException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class SchemaParser {
    private static final QName XSD_ALL;
    private static final QName XSD_ATTRIBUTE;
    private static final QName XSD_CHOICE;
    private static final QName XSD_COMPLEXTYPE;
    private static final QName XSD_ELEMENT;
    private static final QName XSD_INCLUDE;
    private static final Namespace XSD_NAMESPACE;
    private static final QName XSD_RESTRICTION;
    private static final QName XSD_SEQUENCE;
    private static final QName XSD_SIMPLETYPE;
    private Map dataTypeCache;
    private DatatypeDocumentFactory documentFactory;
    private NamedTypeResolver namedTypeResolver;
    private Namespace targetNamespace;

    static {
        XSD_NAMESPACE = Namespace.get("xsd", "http://www.w3.org/2001/XMLSchema");
        XSD_ELEMENT = QName.get("element", XSD_NAMESPACE);
        XSD_ATTRIBUTE = QName.get("attribute", XSD_NAMESPACE);
        XSD_SIMPLETYPE = QName.get("simpleType", XSD_NAMESPACE);
        XSD_COMPLEXTYPE = QName.get("complexType", XSD_NAMESPACE);
        XSD_RESTRICTION = QName.get("restriction", XSD_NAMESPACE);
        XSD_SEQUENCE = QName.get("sequence", XSD_NAMESPACE);
        XSD_CHOICE = QName.get("choice", XSD_NAMESPACE);
        XSD_ALL = QName.get("all", XSD_NAMESPACE);
        XSD_INCLUDE = QName.get("include", XSD_NAMESPACE);
    }

    public SchemaParser() {
        this(DatatypeDocumentFactory.singleton);
    }

    public SchemaParser(DatatypeDocumentFactory documentFactory) {
        this.dataTypeCache = new HashMap();
        this.documentFactory = documentFactory;
        this.namedTypeResolver = new NamedTypeResolver(documentFactory);
    }

    private XSDatatype dataTypeForXsdAttribute(Element xsdAttribute) {
        String type = xsdAttribute.attributeValue("type");
        if (type != null) {
            return getTypeByName(type);
        }
        Element xsdSimpleType = xsdAttribute.element(XSD_SIMPLETYPE);
        if (xsdSimpleType != null) {
            return loadXSDatatypeFromSimpleType(xsdSimpleType);
        }
        throw new InvalidSchemaException("The attribute: " + xsdAttribute.attributeValue("name") + " has no type attribute and does not contain a " + "<simpleType/> element");
    }

    private XSDatatype deriveSimpleType(XSDatatype baseType, Element xsdRestriction) {
        TypeIncubator incubator = new TypeIncubator(baseType);
        try {
            Iterator iter = xsdRestriction.elementIterator();
            while (iter.hasNext()) {
                Element element = (Element) iter.next();
                incubator.addFacet(element.getName(), element.attributeValue("value"), AttributeHelper.booleanValue(element, "fixed"), null);
            }
            return incubator.derive("", null);
        } catch (DatatypeException e) {
            onSchemaError("Invalid restriction: " + e.getMessage() + " when trying to build restriction: " + xsdRestriction);
            return null;
        }
    }

    private DatatypeElementFactory getDatatypeElementFactory(QName name) {
        DatatypeElementFactory factory = this.documentFactory.getElementFactory(name);
        if (factory != null) {
            return factory;
        }
        factory = new DatatypeElementFactory(name);
        name.setDocumentFactory(factory);
        return factory;
    }

    private QName getQName(String name) {
        return this.targetNamespace == null ? this.documentFactory.createQName(name) : this.documentFactory.createQName(name, this.targetNamespace);
    }

    private XSDatatype getTypeByName(String type) {
        XSDatatype dataType = (XSDatatype) this.dataTypeCache.get(type);
        if (dataType == null) {
            int idx = type.indexOf(Opcodes.ASTORE);
            if (idx >= 0) {
                try {
                    dataType = DatatypeFactory.getTypeByName(type.substring(idx + 1));
                } catch (DatatypeException e) {
                }
            }
            if (dataType == null) {
                try {
                    dataType = DatatypeFactory.getTypeByName(type);
                } catch (DatatypeException e2) {
                }
            }
            if (dataType == null) {
                dataType = this.namedTypeResolver.simpleTypeMap.get(getQName(type));
            }
            if (dataType != null) {
                this.dataTypeCache.put(type, dataType);
            }
        }
        return dataType;
    }

    private synchronized void internalBuild(Document schemaDocument) {
        try {
            String inclSchemaInstanceURI;
            Element root = schemaDocument.getRootElement();
            if (root != null) {
                Iterator includeIter = root.elementIterator(XSD_INCLUDE);
                while (includeIter.hasNext()) {
                    inclSchemaInstanceURI = ((Element) includeIter.next()).attributeValue("schemaLocation");
                    EntityResolver resolver = schemaDocument.getEntityResolver();
                    if (resolver == null) {
                        throw new InvalidSchemaException("No EntityResolver available");
                    }
                    InputSource inputSource = resolver.resolveEntity(null, inclSchemaInstanceURI);
                    if (inputSource == null) {
                        throw new InvalidSchemaException("Could not resolve the schema URI: " + inclSchemaInstanceURI);
                    }
                    build(new SAXReader().read(inputSource));
                }
                Iterator iter = root.elementIterator(XSD_ELEMENT);
                while (iter.hasNext()) {
                    onDatatypeElement((Element) iter.next(), this.documentFactory);
                }
                iter = root.elementIterator(XSD_SIMPLETYPE);
                while (iter.hasNext()) {
                    onNamedSchemaSimpleType((Element) iter.next());
                }
                iter = root.elementIterator(XSD_COMPLEXTYPE);
                while (iter.hasNext()) {
                    onNamedSchemaComplexType((Element) iter.next());
                }
                this.namedTypeResolver.resolveNamedTypes();
            }
        } catch (Exception e) {
            Exception e2 = e;
            System.out.println("Failed to load schema: " + inclSchemaInstanceURI);
            System.out.println("Caught: " + e2);
            e2.printStackTrace();
            throw new InvalidSchemaException("Failed to load schema: " + inclSchemaInstanceURI);
        } catch (Throwable th) {
        }
    }

    private XSDatatype loadXSDatatypeFromSimpleType(Element xsdSimpleType) {
        Element xsdRestriction = xsdSimpleType.element(XSD_RESTRICTION);
        if (xsdRestriction != null) {
            String base = xsdRestriction.attributeValue("base");
            if (base != null) {
                XSDatatype baseType = getTypeByName(base);
                if (baseType != null) {
                    return deriveSimpleType(baseType, xsdRestriction);
                }
                onSchemaError("Invalid base type: " + base + " when trying to build restriction: " + xsdRestriction);
            } else {
                Element xsdSubType = xsdSimpleType.element(XSD_SIMPLETYPE);
                if (xsdSubType != null) {
                    return loadXSDatatypeFromSimpleType(xsdSubType);
                }
                onSchemaError("The simpleType element: " + xsdSimpleType + " must contain a base attribute or simpleType" + " element");
            }
        } else {
            onSchemaError("No <restriction>. Could not create XSDatatype for simpleType: " + xsdSimpleType);
        }
        return null;
    }

    private void onChildElements(Element element, DatatypeElementFactory fact) {
        Iterator iter = element.elementIterator(XSD_ELEMENT);
        while (iter.hasNext()) {
            onDatatypeElement((Element) iter.next(), fact);
        }
    }

    private void onDatatypeAttribute(Element xsdElement, DatatypeElementFactory elementFactory, Element xsdAttribute) {
        String name = xsdAttribute.attributeValue("name");
        QName qname = getQName(name);
        XSDatatype dataType = dataTypeForXsdAttribute(xsdAttribute);
        if (dataType != null) {
            elementFactory.setAttributeXSDatatype(qname, dataType);
        } else {
            System.out.println("Warning: Couldn't find XSDatatype for type: " + xsdAttribute.attributeValue("type") + " attribute: " + name);
        }
    }

    private void onDatatypeElement(Element xsdElement, DocumentFactory parentFactory) {
        String name = xsdElement.attributeValue("name");
        String type = xsdElement.attributeValue("type");
        QName qname = getQName(name);
        DatatypeElementFactory factory = getDatatypeElementFactory(qname);
        XSDatatype dataType;
        if (type != null) {
            dataType = getTypeByName(type);
            if (dataType != null) {
                factory.setChildElementXSDatatype(qname, dataType);
            } else {
                this.namedTypeResolver.registerTypedElement(xsdElement, getQName(type), parentFactory);
            }
        } else {
            Element xsdSimpleType = xsdElement.element(XSD_SIMPLETYPE);
            if (xsdSimpleType != null) {
                dataType = loadXSDatatypeFromSimpleType(xsdSimpleType);
                if (dataType != null) {
                    factory.setChildElementXSDatatype(qname, dataType);
                }
            }
            Element schemaComplexType = xsdElement.element(XSD_COMPLEXTYPE);
            if (schemaComplexType != null) {
                onSchemaComplexType(schemaComplexType, factory);
            }
            Iterator iter = xsdElement.elementIterator(XSD_ATTRIBUTE);
            if (!iter.hasNext()) {
                return;
            }
            do {
                onDatatypeAttribute(xsdElement, factory, (Element) iter.next());
            } while (iter.hasNext());
        }
    }

    private void onNamedSchemaComplexType(Element schemaComplexType) {
        Attribute nameAttr = schemaComplexType.attribute("name");
        if (nameAttr != null) {
            QName qname = getQName(nameAttr.getText());
            DatatypeElementFactory factory = getDatatypeElementFactory(qname);
            onSchemaComplexType(schemaComplexType, factory);
            this.namedTypeResolver.registerComplexType(qname, factory);
        }
    }

    private void onNamedSchemaSimpleType(Element schemaSimpleType) {
        Attribute nameAttr = schemaSimpleType.attribute("name");
        if (nameAttr != null) {
            this.namedTypeResolver.registerSimpleType(getQName(nameAttr.getText()), loadXSDatatypeFromSimpleType(schemaSimpleType));
        }
    }

    private void onSchemaComplexType(Element schemaComplexType, DatatypeElementFactory elementFactory) {
        Iterator iter = schemaComplexType.elementIterator(XSD_ATTRIBUTE);
        while (iter.hasNext()) {
            Element xsdAttribute = (Element) iter.next();
            QName qname = getQName(xsdAttribute.attributeValue("name"));
            XSDatatype dataType = dataTypeForXsdAttribute(xsdAttribute);
            if (dataType != null) {
                elementFactory.setAttributeXSDatatype(qname, dataType);
            }
        }
        Element schemaSequence = schemaComplexType.element(XSD_SEQUENCE);
        if (schemaSequence != null) {
            onChildElements(schemaSequence, elementFactory);
        }
        Element schemaChoice = schemaComplexType.element(XSD_CHOICE);
        if (schemaChoice != null) {
            onChildElements(schemaChoice, elementFactory);
        }
        Element schemaAll = schemaComplexType.element(XSD_ALL);
        if (schemaAll != null) {
            onChildElements(schemaAll, elementFactory);
        }
    }

    private void onSchemaError(String message) {
        throw new InvalidSchemaException(message);
    }

    public void build(Document schemaDocument) {
        this.targetNamespace = null;
        internalBuild(schemaDocument);
    }

    public void build(Document schemaDocument, Namespace namespace) {
        this.targetNamespace = namespace;
        internalBuild(schemaDocument);
    }
}