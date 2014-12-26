package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

public class SAXWriter implements XMLReader {
    protected static final String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String[] LEXICAL_HANDLER_NAMES;
    private AttributesImpl attributes;
    private ContentHandler contentHandler;
    private boolean declareNamespaceAttributes;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;
    private Map<String, Boolean> features;
    private LexicalHandler lexicalHandler;
    private Map<String, Object> properties;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.TEXT_NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.CDATA_SECTION_NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ENTITY_REFERENCE_NODE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.PROCESSING_INSTRUCTION_NODE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.COMMENT_NODE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_NODE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_TYPE_NODE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.NAMESPACE_NODE.ordinal()] = 10;
        }
    }

    static {
        LEXICAL_HANDLER_NAMES = new String[]{"http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/handlers/LexicalHandler"};
    }

    public SAXWriter() {
        this.attributes = new AttributesImpl();
        this.features = new HashMap();
        this.properties = new HashMap();
        this.properties.put(FEATURE_NAMESPACE_PREFIXES, Boolean.valueOf(false));
        this.properties.put(FEATURE_NAMESPACE_PREFIXES, Boolean.valueOf(true));
    }

    public SAXWriter(ContentHandler contentHandler) {
        this();
        this.contentHandler = contentHandler;
    }

    public SAXWriter(ContentHandler contentHandler, LexicalHandler lexicalHandler) {
        this();
        this.contentHandler = contentHandler;
        this.lexicalHandler = lexicalHandler;
    }

    public SAXWriter(ContentHandler contentHandler, LexicalHandler lexicalHandler, EntityResolver entityResolver) {
        this();
        this.contentHandler = contentHandler;
        this.lexicalHandler = lexicalHandler;
        this.entityResolver = entityResolver;
    }

    protected AttributesImpl addNamespaceAttribute(AttributesImpl attrs, Namespace namespace) {
        if (this.declareNamespaceAttributes) {
            if (attrs == null) {
                attrs = new AttributesImpl();
            }
            String prefix = namespace.getPrefix();
            String qualifiedName = "xmlns";
            if (prefix != null && prefix.length() > 0) {
                qualifiedName = "xmlns:" + prefix;
            }
            attrs.addAttribute("", prefix, qualifiedName, "CDATA", namespace.getURI());
        }
        return attrs;
    }

    protected void checkForNullHandlers() {
    }

    protected Attributes createAttributes(Element element, Attributes namespaceAttributes) throws SAXException {
        this.attributes.clear();
        if (namespaceAttributes != null) {
            this.attributes.setAttributes(namespaceAttributes);
        }
        Iterator iter = element.attributeIterator();
        while (iter.hasNext()) {
            Attribute attribute = (Attribute) iter.next();
            this.attributes.addAttribute(attribute.getNamespaceURI(), attribute.getName(), attribute.getQualifiedName(), "CDATA", attribute.getValue());
        }
        return this.attributes;
    }

    protected void documentLocator(Document document) throws SAXException {
        LocatorImpl locator = new LocatorImpl();
        String publicID = null;
        String systemID = null;
        DocumentType docType = document.getDocType();
        if (docType != null) {
            publicID = docType.getPublicID();
            systemID = docType.getSystemID();
        }
        if (publicID != null) {
            locator.setPublicId(publicID);
        }
        if (systemID != null) {
            locator.setSystemId(systemID);
        }
        locator.setLineNumber(-1);
        locator.setColumnNumber(-1);
        this.contentHandler.setDocumentLocator(locator);
    }

    protected void dtdHandler(Document document) throws SAXException {
    }

    protected void endDocument() throws SAXException {
        this.contentHandler.endDocument();
    }

    protected void endElement(Element element) throws SAXException {
        this.contentHandler.endElement(element.getNamespaceURI(), element.getName(), element.getQualifiedName());
    }

    protected void endPrefixMapping(NamespaceStack stack, int stackSize) throws SAXException {
        while (stack.size() > stackSize) {
            Namespace namespace = stack.pop();
            if (namespace != null) {
                this.contentHandler.endPrefixMapping(namespace.getPrefix());
            }
        }
    }

    protected void entityResolver(Document document) throws SAXException {
        if (this.entityResolver != null) {
            DocumentType docType = document.getDocType();
            if (docType != null) {
                String publicID = docType.getPublicID();
                String systemID = docType.getSystemID();
                if (publicID != null || systemID != null) {
                    try {
                        this.entityResolver.resolveEntity(publicID, systemID);
                    } catch (IOException e) {
                        throw new SAXException("Could not resolve publicID: " + publicID + " systemID: " + systemID, e);
                    }
                }
            }
        }
    }

    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        Boolean answer = (Boolean) this.features.get(name);
        return answer != null && answer.booleanValue();
    }

    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        int i = 0;
        while (i < LEXICAL_HANDLER_NAMES.length) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                return getLexicalHandler();
            }
            i++;
        }
        return this.properties.get(name);
    }

    public boolean isDeclareNamespaceAttributes() {
        return this.declareNamespaceAttributes;
    }

    protected boolean isIgnoreableNamespace(Namespace namespace, NamespaceStack namespaceStack) {
        if (namespace.equals(Namespace.NO_NAMESPACE) || namespace.equals(Namespace.XML_NAMESPACE)) {
            return true;
        }
        String uri = namespace.getURI();
        return (uri == null || uri.length() <= 0) ? true : namespaceStack.contains(namespace);
    }

    public void parse(String systemId) throws SAXNotSupportedException {
        throw new SAXNotSupportedException("This XMLReader can only accept <dom4j> InputSource objects");
    }

    public void parse(InputSource input) throws SAXException {
        if (input instanceof DocumentInputSource) {
            write(((DocumentInputSource) input).getDocument());
        } else {
            throw new SAXNotSupportedException("This XMLReader can only accept <dom4j> InputSource objects");
        }
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public void setDTDHandler(DTDHandler handler) {
        this.dtdHandler = handler;
    }

    public void setDeclareNamespaceAttributes(boolean declareNamespaceAttrs) {
        this.declareNamespaceAttributes = declareNamespaceAttrs;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (FEATURE_NAMESPACE_PREFIXES.equals(name)) {
            setDeclareNamespaceAttributes(value);
        } else if (FEATURE_NAMESPACE_PREFIXES.equals(name) && !value) {
            throw new SAXNotSupportedException("Namespace feature is always supported in dom4j");
        }
        this.features.put(name, Boolean.valueOf(value));
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }

    public void setProperty(String name, Object value) {
        int i = 0;
        while (i < LEXICAL_HANDLER_NAMES.length) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                setLexicalHandler((LexicalHandler) value);
                return;
            } else {
                i++;
            }
        }
        this.properties.put(name, value);
    }

    public void setXMLReader(XMLReader xmlReader) {
        setContentHandler(xmlReader.getContentHandler());
        setDTDHandler(xmlReader.getDTDHandler());
        setEntityResolver(xmlReader.getEntityResolver());
        setErrorHandler(xmlReader.getErrorHandler());
    }

    protected void startDocument() throws SAXException {
        this.contentHandler.startDocument();
    }

    protected void startElement(Element element, AttributesImpl namespaceAttributes) throws SAXException {
        this.contentHandler.startElement(element.getNamespaceURI(), element.getName(), element.getQualifiedName(), createAttributes(element, namespaceAttributes));
    }

    protected AttributesImpl startPrefixMapping(Element element, NamespaceStack namespaceStack) throws SAXException {
        AttributesImpl namespaceAttributes = null;
        Namespace elementNamespace = element.getNamespace();
        if (!(elementNamespace == null || isIgnoreableNamespace(elementNamespace, namespaceStack))) {
            namespaceStack.push(elementNamespace);
            this.contentHandler.startPrefixMapping(elementNamespace.getPrefix(), elementNamespace.getURI());
            namespaceAttributes = addNamespaceAttribute(null, elementNamespace);
        }
        List declaredNamespaces = element.declaredNamespaces();
        int i = 0;
        int size = declaredNamespaces.size();
        while (i < size) {
            Namespace namespace = (Namespace) declaredNamespaces.get(i);
            if (!isIgnoreableNamespace(namespace, namespaceStack)) {
                namespaceStack.push(namespace);
                this.contentHandler.startPrefixMapping(namespace.getPrefix(), namespace.getURI());
                namespaceAttributes = addNamespaceAttribute(namespaceAttributes, namespace);
            }
            i++;
        }
        return namespaceAttributes;
    }

    public void write(String text) throws SAXException {
        if (text != null) {
            char[] chars = text.toCharArray();
            this.contentHandler.characters(chars, 0, chars.length);
        }
    }

    public void write(CDATA cdata) throws SAXException {
        String text = cdata.getText();
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
            write(text);
            this.lexicalHandler.endCDATA();
        } else {
            write(text);
        }
    }

    public void write(Comment comment) throws SAXException {
        if (this.lexicalHandler != null) {
            char[] chars = comment.getText().toCharArray();
            this.lexicalHandler.comment(chars, 0, chars.length);
        }
    }

    public void write(Document document) throws SAXException {
        if (document != null) {
            checkForNullHandlers();
            documentLocator(document);
            startDocument();
            entityResolver(document);
            dtdHandler(document);
            writeContent(document, new NamespaceStack());
            endDocument();
        }
    }

    public void write(Element element) throws SAXException {
        write(element, new NamespaceStack());
    }

    protected void write(Element element, NamespaceStack namespaceStack) throws SAXException {
        int stackSize = namespaceStack.size();
        startElement(element, startPrefixMapping(element, namespaceStack));
        writeContent(element, namespaceStack);
        endElement(element);
        endPrefixMapping(namespaceStack, stackSize);
    }

    public void write(Entity entity) throws SAXException {
        String text = entity.getText();
        if (this.lexicalHandler != null) {
            String name = entity.getName();
            this.lexicalHandler.startEntity(name);
            write(text);
            this.lexicalHandler.endEntity(name);
        } else {
            write(text);
        }
    }

    public void write(Node node) throws SAXException {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                write((Element) node);
            case ClassWriter.COMPUTE_FRAMES:
                write((Attribute) node);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                write(node.getText());
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                write((CDATA) node);
            case JsonWriteContext.STATUS_EXPECT_NAME:
                write((Entity) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                write((ProcessingInstruction) node);
            case Type.LONG:
                write((Comment) node);
            case Type.DOUBLE:
                write((Document) node);
            case Type.ARRAY:
                write((DocumentType) node);
            case Type.OBJECT:
                break;
            default:
                throw new SAXException("Invalid node type: " + node);
        }
    }

    public void write(ProcessingInstruction pi) throws SAXException {
        this.contentHandler.processingInstruction(pi.getTarget(), pi.getText());
    }

    public void writeClose(Element element) throws SAXException {
        endElement(element);
    }

    protected void writeContent(Branch branch, NamespaceStack namespaceStack) throws SAXException {
        Iterator iter = branch.nodeIterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof Element) {
                write((Element) object, namespaceStack);
            } else if (object instanceof CharacterData) {
                if (object instanceof Text) {
                    write(((Text) object).getText());
                } else if (object instanceof CDATA) {
                    write((CDATA) object);
                } else if (object instanceof Comment) {
                    write((Comment) object);
                } else {
                    throw new SAXException("Invalid Node in DOM4J content: " + object + " of type: " + object.getClass());
                }
            } else if (object instanceof String) {
                write((String) object);
            } else if (object instanceof Entity) {
                write((Entity) object);
            } else if (object instanceof ProcessingInstruction) {
                write((ProcessingInstruction) object);
            } else if (object instanceof Namespace) {
                write((Namespace) object);
            } else {
                throw new SAXException("Invalid Node in DOM4J content: " + object);
            }
        }
    }

    public void writeOpen(Element element) throws SAXException {
        startElement(element, null);
    }
}