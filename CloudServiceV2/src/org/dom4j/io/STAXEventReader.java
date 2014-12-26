package org.dom4j.io;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;

public class STAXEventReader {
    private DocumentFactory factory;
    private XMLInputFactory inputFactory;

    public STAXEventReader() {
        this.inputFactory = XMLInputFactory.newInstance();
        this.factory = DocumentFactory.getInstance();
    }

    public STAXEventReader(DocumentFactory factory) {
        this.inputFactory = XMLInputFactory.newInstance();
        if (factory != null) {
            this.factory = factory;
        } else {
            this.factory = DocumentFactory.getInstance();
        }
    }

    public Attribute createAttribute(Element elem, javax.xml.stream.events.Attribute attr) {
        return this.factory.createAttribute(elem, createQName(attr.getName()), attr.getValue());
    }

    public CharacterData createCharacterData(Characters characters) {
        String data = characters.getData();
        return characters.isCData() ? this.factory.createCDATA(data) : this.factory.createText(data);
    }

    public Comment createComment(javax.xml.stream.events.Comment comment) {
        return this.factory.createComment(comment.getText());
    }

    public Element createElement(StartElement startEvent) {
        Element elem = this.factory.createElement(createQName(startEvent.getName()));
        Iterator i = startEvent.getAttributes();
        while (i.hasNext()) {
            javax.xml.stream.events.Attribute attr = (javax.xml.stream.events.Attribute) i.next();
            elem.addAttribute(createQName(attr.getName()), attr.getValue());
        }
        i = startEvent.getNamespaces();
        while (i.hasNext()) {
            Namespace ns = (Namespace) i.next();
            elem.addNamespace(ns.getPrefix(), ns.getNamespaceURI());
        }
        return elem;
    }

    public Entity createEntity(EntityReference entityRef) {
        return this.factory.createEntity(entityRef.getName(), entityRef.getDeclaration().getReplacementText());
    }

    public org.dom4j.Namespace createNamespace(Namespace ns) {
        return this.factory.createNamespace(ns.getPrefix(), ns.getNamespaceURI());
    }

    public ProcessingInstruction createProcessingInstruction(javax.xml.stream.events.ProcessingInstruction pi) {
        return this.factory.createProcessingInstruction(pi.getTarget(), pi.getData());
    }

    public QName createQName(javax.xml.namespace.QName qname) {
        return this.factory.createQName(qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
    }

    public Attribute readAttribute(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isAttribute()) {
            return createAttribute(null, (javax.xml.stream.events.Attribute) reader.nextEvent());
        }
        throw new XMLStreamException("Expected Attribute event, found: " + event);
    }

    public CharacterData readCharacters(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isCharacters()) {
            return createCharacterData(reader.nextEvent().asCharacters());
        }
        throw new XMLStreamException("Expected Characters event, found: " + event);
    }

    public Comment readComment(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event instanceof javax.xml.stream.events.Comment) {
            return createComment((javax.xml.stream.events.Comment) reader.nextEvent());
        }
        throw new XMLStreamException("Expected Comment event, found: " + event);
    }

    public Document readDocument(InputStream is) throws XMLStreamException {
        return readDocument(is, null);
    }

    public Document readDocument(InputStream is, String systemId) throws XMLStreamException {
        XMLEventReader eventReader = this.inputFactory.createXMLEventReader(systemId, is);
        Document readDocument = readDocument(eventReader);
        eventReader.close();
        return readDocument;
    }

    public Document readDocument(Reader reader) throws XMLStreamException {
        return readDocument(reader, null);
    }

    public Document readDocument(Reader reader, String systemId) throws XMLStreamException {
        XMLEventReader eventReader = this.inputFactory.createXMLEventReader(systemId, reader);
        Document readDocument = readDocument(eventReader);
        eventReader.close();
        return readDocument;
    }

    public Document readDocument(XMLEventReader reader) throws XMLStreamException {
        Document doc = null;
        while (reader.hasNext()) {
            switch (reader.peek().getEventType()) {
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                case Type.DOUBLE:
                    reader.nextEvent();
                    break;
                case Type.LONG:
                    StartDocument event = (StartDocument) reader.nextEvent();
                    if (doc != null) {
                        throw new XMLStreamException("Unexpected StartDocument event", event.getLocation());
                    } else if (event.encodingSet()) {
                        doc = this.factory.createDocument(event.getCharacterEncodingScheme());
                    } else {
                        doc = this.factory.createDocument();
                    }
                    break;
                default:
                    if (doc == null) {
                        doc = this.factory.createDocument();
                    }
                    doc.add(readNode(reader));
                    break;
            }
        }
        return doc;
    }

    public Element readElement(XMLEventReader eventReader) throws XMLStreamException {
        XMLEvent event = eventReader.peek();
        if (event.isStartElement()) {
            StartElement startTag = eventReader.nextEvent().asStartElement();
            Element elem = createElement(startTag);
            while (eventReader.hasNext()) {
                if (eventReader.peek().isEndElement()) {
                    EndElement endElem = eventReader.nextEvent().asEndElement();
                    if (endElem.getName().equals(startTag.getName())) {
                        return elem;
                    }
                    throw new XMLStreamException("Expected " + startTag.getName() + " end-tag, but found" + endElem.getName());
                } else {
                    elem.add(readNode(eventReader));
                }
            }
            throw new XMLStreamException("Unexpected end of stream while reading element content");
        } else {
            throw new XMLStreamException("Expected Element event, found: " + event);
        }
    }

    public Entity readEntityReference(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isEntityReference()) {
            return createEntity((EntityReference) reader.nextEvent());
        }
        throw new XMLStreamException("Expected EntityRef event, found: " + event);
    }

    public org.dom4j.Namespace readNamespace(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isNamespace()) {
            return createNamespace((Namespace) reader.nextEvent());
        }
        throw new XMLStreamException("Expected Namespace event, found: " + event);
    }

    public Node readNode(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isStartElement()) {
            return readElement(reader);
        }
        if (event.isCharacters()) {
            return readCharacters(reader);
        }
        if (event.isStartDocument()) {
            return readDocument(reader);
        }
        if (event.isProcessingInstruction()) {
            return readProcessingInstruction(reader);
        }
        if (event.isEntityReference()) {
            return readEntityReference(reader);
        }
        if (event.isAttribute()) {
            return readAttribute(reader);
        }
        if (event.isNamespace()) {
            return readNamespace(reader);
        }
        throw new XMLStreamException("Unsupported event: " + event);
    }

    public ProcessingInstruction readProcessingInstruction(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();
        if (event.isProcessingInstruction()) {
            return createProcessingInstruction((javax.xml.stream.events.ProcessingInstruction) reader.nextEvent());
        }
        throw new XMLStreamException("Expected PI event, found: " + event);
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        if (documentFactory != null) {
            this.factory = documentFactory;
        } else {
            this.factory = DocumentFactory.getInstance();
        }
    }
}