package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.util.XMLEventConsumer;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.Text;

public class STAXEventWriter {
    private XMLEventConsumer consumer;
    private XMLEventFactory factory;
    private XMLOutputFactory outputFactory;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.TEXT_NODE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.NAMESPACE_NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.COMMENT_NODE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.CDATA_SECTION_NODE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.PROCESSING_INSTRUCTION_NODE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ENTITY_REFERENCE_NODE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_NODE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_TYPE_NODE.ordinal()] = 10;
        }
    }

    private class AttributeIterator implements Iterator {
        private Iterator iter;

        public AttributeIterator(Iterator iter) {
            this.iter = iter;
        }

        public boolean hasNext() {
            return this.iter.hasNext();
        }

        public Object next() {
            Attribute attr = (Attribute) this.iter.next();
            return STAXEventWriter.this.factory.createAttribute(STAXEventWriter.this.createQName(attr.getQName()), attr.getValue());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class NamespaceIterator implements Iterator {
        private Iterator iter;

        public NamespaceIterator(Iterator iter) {
            this.iter = iter;
        }

        public boolean hasNext() {
            return this.iter.hasNext();
        }

        public Object next() {
            Namespace ns = (Namespace) this.iter.next();
            return STAXEventWriter.this.factory.createNamespace(ns.getPrefix(), ns.getURI());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public STAXEventWriter() {
        this.factory = XMLEventFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
    }

    public STAXEventWriter(File file) throws XMLStreamException, IOException {
        this.factory = XMLEventFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
        this.consumer = this.outputFactory.createXMLEventWriter(new FileWriter(file));
    }

    public STAXEventWriter(OutputStream stream) throws XMLStreamException {
        this.factory = XMLEventFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
        this.consumer = this.outputFactory.createXMLEventWriter(stream);
    }

    public STAXEventWriter(Writer writer) throws XMLStreamException {
        this.factory = XMLEventFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
        this.consumer = this.outputFactory.createXMLEventWriter(writer);
    }

    public STAXEventWriter(XMLEventConsumer consumer) {
        this.factory = XMLEventFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
        this.consumer = consumer;
    }

    private EntityReference createEntityReference(Entity entity) {
        return this.factory.createEntityReference(entity.getName(), null);
    }

    public javax.xml.stream.events.Attribute createAttribute(Attribute attr) {
        return this.factory.createAttribute(createQName(attr.getQName()), attr.getValue());
    }

    public Characters createCharacters(CDATA cdata) {
        return this.factory.createCData(cdata.getText());
    }

    public Characters createCharacters(Text text) {
        return this.factory.createCharacters(text.getText());
    }

    public Comment createComment(org.dom4j.Comment comment) {
        return this.factory.createComment(comment.getText());
    }

    public DTD createDTD(DocumentType docType) {
        StringWriter decl = new StringWriter();
        try {
            docType.write(decl);
            return this.factory.createDTD(decl.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error writing DTD", e);
        }
    }

    public EndDocument createEndDocument(Document doc) {
        return this.factory.createEndDocument();
    }

    public EndElement createEndElement(Element elem) {
        return this.factory.createEndElement(createQName(elem.getQName()), new NamespaceIterator(elem.declaredNamespaces().iterator()));
    }

    public javax.xml.stream.events.Namespace createNamespace(Namespace ns) {
        return this.factory.createNamespace(ns.getPrefix(), ns.getURI());
    }

    public ProcessingInstruction createProcessingInstruction(org.dom4j.ProcessingInstruction pi) {
        return this.factory.createProcessingInstruction(pi.getTarget(), pi.getText());
    }

    public QName createQName(org.dom4j.QName qname) {
        return new QName(qname.getNamespaceURI(), qname.getName(), qname.getNamespacePrefix());
    }

    public StartDocument createStartDocument(Document doc) {
        String encoding = doc.getXMLEncoding();
        return encoding != null ? this.factory.createStartDocument(encoding) : this.factory.createStartDocument();
    }

    public StartElement createStartElement(Element elem) {
        return this.factory.createStartElement(createQName(elem.getQName()), new AttributeIterator(elem.attributeIterator()), new NamespaceIterator(elem.declaredNamespaces().iterator()));
    }

    public XMLEventConsumer getConsumer() {
        return this.consumer;
    }

    public XMLEventFactory getEventFactory() {
        return this.factory;
    }

    public void setConsumer(XMLEventConsumer consumer) {
        this.consumer = consumer;
    }

    public void setEventFactory(XMLEventFactory eventFactory) {
        this.factory = eventFactory;
    }

    public void writeAttribute(Attribute attr) throws XMLStreamException {
        this.consumer.add(createAttribute(attr));
    }

    public void writeCDATA(CDATA cdata) throws XMLStreamException {
        this.consumer.add(createCharacters(cdata));
    }

    public void writeChildNodes(Branch branch) throws XMLStreamException {
        int i = 0;
        int s = branch.nodeCount();
        while (i < s) {
            writeNode(branch.node(i));
            i++;
        }
    }

    public void writeComment(org.dom4j.Comment comment) throws XMLStreamException {
        this.consumer.add(createComment(comment));
    }

    public void writeDocument(Document doc) throws XMLStreamException {
        this.consumer.add(createStartDocument(doc));
        writeChildNodes(doc);
        this.consumer.add(createEndDocument(doc));
    }

    public void writeDocumentType(DocumentType docType) throws XMLStreamException {
        this.consumer.add(createDTD(docType));
    }

    public void writeElement(Element elem) throws XMLStreamException {
        this.consumer.add(createStartElement(elem));
        writeChildNodes(elem);
        this.consumer.add(createEndElement(elem));
    }

    public void writeEntity(Entity entity) throws XMLStreamException {
        this.consumer.add(createEntityReference(entity));
    }

    public void writeNamespace(Namespace ns) throws XMLStreamException {
        this.consumer.add(createNamespace(ns));
    }

    public void writeNode(Node n) throws XMLStreamException {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[n.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeElement((Element) n);
            case ClassWriter.COMPUTE_FRAMES:
                writeText((Text) n);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeAttribute((Attribute) n);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                writeNamespace((Namespace) n);
            case JsonWriteContext.STATUS_EXPECT_NAME:
                writeComment((org.dom4j.Comment) n);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                writeCDATA((CDATA) n);
            case Type.LONG:
                writeProcessingInstruction((org.dom4j.ProcessingInstruction) n);
            case Type.DOUBLE:
                writeEntity((Entity) n);
            case Type.ARRAY:
                writeDocument((Document) n);
            case Type.OBJECT:
                writeDocumentType((DocumentType) n);
            default:
                throw new XMLStreamException("Unsupported DOM4J Node: " + n);
        }
    }

    public void writeProcessingInstruction(org.dom4j.ProcessingInstruction pi) throws XMLStreamException {
        this.consumer.add(createProcessingInstruction(pi));
    }

    public void writeText(Text text) throws XMLStreamException {
        this.consumer.add(createCharacters(text));
    }
}