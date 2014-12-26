package org.dom4j.io;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dtd.AttributeDecl;
import org.dom4j.dtd.ElementDecl;
import org.dom4j.dtd.ExternalEntityDecl;
import org.dom4j.dtd.InternalEntityDecl;
import org.dom4j.tree.AbstractElement;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class SAXContentHandler extends DefaultHandler implements LexicalHandler, DeclHandler, DTDHandler {
    private Map availableNamespaceMap;
    private StringBuffer cdataText;
    private Element currentElement;
    private int declaredNamespaceIndex;
    private List declaredNamespaceList;
    private Document document;
    private DocumentFactory documentFactory;
    private ElementHandler elementHandler;
    private ElementStack elementStack;
    private String entity;
    private int entityLevel;
    private EntityResolver entityResolver;
    private List externalDTDDeclarations;
    private boolean ignoreComments;
    private boolean includeExternalDTDDeclarations;
    private boolean includeInternalDTDDeclarations;
    private InputSource inputSource;
    private boolean insideCDATASection;
    private boolean insideDTDSection;
    private List internalDTDDeclarations;
    private boolean internalDTDsubset;
    private Locator locator;
    private boolean mergeAdjacentText;
    private NamespaceStack namespaceStack;
    private boolean stripWhitespaceText;
    private StringBuffer textBuffer;
    private boolean textInTextBuffer;

    public SAXContentHandler() {
        this(DocumentFactory.getInstance());
    }

    public SAXContentHandler(DocumentFactory documentFactory) {
        this(documentFactory, null);
    }

    public SAXContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler) {
        this(documentFactory, elementHandler, null);
        this.elementStack = createElementStack();
    }

    public SAXContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler, ElementStack elementStack) {
        this.availableNamespaceMap = new HashMap();
        this.declaredNamespaceList = new ArrayList();
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.internalDTDsubset = false;
        this.mergeAdjacentText = false;
        this.textInTextBuffer = false;
        this.ignoreComments = false;
        this.stripWhitespaceText = false;
        this.documentFactory = documentFactory;
        this.elementHandler = elementHandler;
        this.elementStack = elementStack;
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    private String getEncoding() {
        if (this.locator == null) {
            return null;
        }
        try {
            Method m = this.locator.getClass().getMethod("getEncoding", new Class[0]);
            if (m != null) {
                return (String) m.invoke(this.locator, null);
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected void addAttributes(Element element, Attributes attributes) {
        if (element instanceof AbstractElement) {
            ((AbstractElement) element).setAttributes(attributes, this.namespaceStack, false);
        } else {
            int size = attributes.getLength();
            int i = 0;
            while (i < size) {
                String attributeQName = attributes.getQName(i);
                if (!(0 == 0 && attributeQName.startsWith("xmlns"))) {
                    String attributeURI = attributes.getURI(i);
                    String attributeLocalName = attributes.getLocalName(i);
                    element.addAttribute(this.namespaceStack.getAttributeQName(attributeURI, attributeLocalName, attributeQName), attributes.getValue(i));
                }
                i++;
            }
        }
    }

    protected void addDTDDeclaration(Object declaration) {
        if (this.internalDTDDeclarations == null) {
            this.internalDTDDeclarations = new ArrayList();
        }
        this.internalDTDDeclarations.add(declaration);
    }

    protected void addDeclaredNamespaces(Element element) {
        Namespace elementNamespace = element.getNamespace();
        int size = this.namespaceStack.size();
        while (this.declaredNamespaceIndex < size) {
            element.add(this.namespaceStack.getNamespace(this.declaredNamespaceIndex));
            this.declaredNamespaceIndex++;
        }
    }

    protected void addExternalDTDDeclaration(Object declaration) {
        if (this.externalDTDDeclarations == null) {
            this.externalDTDDeclarations = new ArrayList();
        }
        this.externalDTDDeclarations.add(declaration);
    }

    public void attributeDecl(String eName, String aName, String type, String valueDefault, String val) throws SAXException {
        if (this.internalDTDsubset) {
            if (this.includeInternalDTDDeclarations) {
                addDTDDeclaration(new AttributeDecl(eName, aName, type, valueDefault, val));
            }
        } else if (this.includeExternalDTDDeclarations) {
            addExternalDTDDeclaration(new AttributeDecl(eName, aName, type, valueDefault, val));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void characters(char[] r4_ch, int r5_start, int r6_end) throws org.xml.sax.SAXException {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.io.SAXContentHandler.characters(char[], int, int):void");
        /*
        r3 = this;
        if (r6 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = r3.currentElement;
        if (r0 == 0) goto L_0x0002;
    L_0x0007:
        r0 = r3.entity;
        if (r0 == 0) goto L_0x0026;
    L_0x000b:
        r0 = r3.mergeAdjacentText;
        if (r0 == 0) goto L_0x0016;
    L_0x000f:
        r0 = r3.textInTextBuffer;
        if (r0 == 0) goto L_0x0016;
    L_0x0013:
        r3.completeCurrentTextNode();
    L_0x0016:
        r0 = r3.currentElement;
        r1 = r3.entity;
        r2 = new java.lang.String;
        r2.<init>(r4, r5, r6);
        r0.addEntity(r1, r2);
        r0 = 0;
        r3.entity = r0;
        goto L_0x0002;
    L_0x0026:
        r0 = r3.insideCDATASection;
        if (r0 == 0) goto L_0x0040;
    L_0x002a:
        r0 = r3.mergeAdjacentText;
        if (r0 == 0) goto L_0x0035;
    L_0x002e:
        r0 = r3.textInTextBuffer;
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        r3.completeCurrentTextNode();
    L_0x0035:
        r0 = r3.cdataText;
        r1 = new java.lang.String;
        r1.<init>(r4, r5, r6);
        r0.append(r1);
        goto L_0x0002;
    L_0x0040:
        r0 = r3.mergeAdjacentText;
        if (r0 == 0) goto L_0x004d;
    L_0x0044:
        r0 = r3.textBuffer;
        r0.append(r4, r5, r6);
        r0 = 1;
        r3.textInTextBuffer = r0;
        goto L_0x0002;
    L_0x004d:
        r0 = r3.currentElement;
        r1 = new java.lang.String;
        r1.<init>(r4, r5, r6);
        r0.addText(r1);
        goto L_0x0002;
        */
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        if (!this.ignoreComments) {
            if (this.mergeAdjacentText && this.textInTextBuffer) {
                completeCurrentTextNode();
            }
            String text = new String(ch, start, end);
            if (!this.insideDTDSection && text.length() > 0) {
                if (this.currentElement != null) {
                    this.currentElement.addComment(text);
                } else {
                    getDocument().addComment(text);
                }
            }
        }
    }

    protected void completeCurrentTextNode() {
        if (this.stripWhitespaceText) {
            boolean whitespace = true;
            int i = 0;
            int size = this.textBuffer.length();
            while (i < size) {
                if (!Character.isWhitespace(this.textBuffer.charAt(i))) {
                    whitespace = false;
                    break;
                } else {
                    i++;
                }
            }
            if (!whitespace) {
                this.currentElement.addText(this.textBuffer.toString());
            }
        } else {
            this.currentElement.addText(this.textBuffer.toString());
        }
        this.textBuffer.setLength(0);
        this.textInTextBuffer = false;
    }

    protected Document createDocument() {
        Document doc = this.documentFactory.createDocument(getEncoding());
        doc.setEntityResolver(this.entityResolver);
        if (this.inputSource != null) {
            doc.setName(this.inputSource.getSystemId());
        }
        return doc;
    }

    protected ElementStack createElementStack() {
        return new ElementStack();
    }

    public void elementDecl(String name, String model) throws SAXException {
        if (this.internalDTDsubset) {
            if (this.includeInternalDTDDeclarations) {
                addDTDDeclaration(new ElementDecl(name, model));
            }
        } else if (this.includeExternalDTDDeclarations) {
            addExternalDTDDeclaration(new ElementDecl(name, model));
        }
    }

    public void endCDATA() throws SAXException {
        this.insideCDATASection = false;
        this.currentElement.addCDATA(this.cdataText.toString());
    }

    public void endDTD() throws SAXException {
        this.insideDTDSection = false;
        DocumentType docType = getDocument().getDocType();
        if (docType != null) {
            if (this.internalDTDDeclarations != null) {
                docType.setInternalDeclarations(this.internalDTDDeclarations);
            }
            if (this.externalDTDDeclarations != null) {
                docType.setExternalDeclarations(this.externalDTDDeclarations);
            }
        }
        this.internalDTDDeclarations = null;
        this.externalDTDDeclarations = null;
    }

    public void endDocument() throws SAXException {
        this.namespaceStack.clear();
        this.elementStack.clear();
        this.currentElement = null;
        this.textBuffer = null;
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (this.mergeAdjacentText && this.textInTextBuffer) {
            completeCurrentTextNode();
        }
        if (!(this.elementHandler == null || this.currentElement == null)) {
            this.elementHandler.onEnd(this.elementStack);
        }
        this.elementStack.popElement();
        this.currentElement = this.elementStack.peekElement();
    }

    public void endEntity(String name) throws SAXException {
        this.entityLevel--;
        this.entity = null;
        if (this.entityLevel == 0) {
            this.internalDTDsubset = true;
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        this.namespaceStack.pop(prefix);
        this.declaredNamespaceIndex = this.namespaceStack.size();
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void externalEntityDecl(String name, String publicId, String sysId) throws SAXException {
        ExternalEntityDecl declaration = new ExternalEntityDecl(name, publicId, sysId);
        if (this.internalDTDsubset) {
            if (this.includeInternalDTDDeclarations) {
                addDTDDeclaration(declaration);
            }
        } else if (this.includeExternalDTDDeclarations) {
            addExternalDTDDeclaration(declaration);
        }
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public Document getDocument() {
        if (this.document == null) {
            this.document = createDocument();
        }
        return this.document;
    }

    public ElementStack getElementStack() {
        return this.elementStack;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public InputSource getInputSource() {
        return this.inputSource;
    }

    public void internalEntityDecl(String name, String value) throws SAXException {
        if (this.internalDTDsubset) {
            if (this.includeInternalDTDDeclarations) {
                addDTDDeclaration(new InternalEntityDecl(name, value));
            }
        } else if (this.includeExternalDTDDeclarations) {
            addExternalDTDDeclaration(new InternalEntityDecl(name, value));
        }
    }

    protected boolean isIgnorableEntity(String name) {
        return "amp".equals(name) || "apos".equals(name) || "gt".equals(name) || "lt".equals(name) || "quot".equals(name);
    }

    public boolean isIgnoreComments() {
        return this.ignoreComments;
    }

    public boolean isIncludeExternalDTDDeclarations() {
        return this.includeExternalDTDDeclarations;
    }

    public boolean isIncludeInternalDTDDeclarations() {
        return this.includeInternalDTDDeclarations;
    }

    public boolean isMergeAdjacentText() {
        return this.mergeAdjacentText;
    }

    public boolean isStripWhitespaceText() {
        return this.stripWhitespaceText;
    }

    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (this.mergeAdjacentText && this.textInTextBuffer) {
            completeCurrentTextNode();
        }
        if (this.currentElement != null) {
            this.currentElement.addProcessingInstruction(target, data);
        } else {
            getDocument().addProcessingInstruction(target, data);
        }
    }

    public void setDocumentLocator(Locator documentLocator) {
        this.locator = documentLocator;
    }

    public void setElementStack(ElementStack elementStack) {
        this.elementStack = elementStack;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setIgnoreComments(boolean ignoreComments) {
        this.ignoreComments = ignoreComments;
    }

    public void setIncludeExternalDTDDeclarations(boolean include) {
        this.includeExternalDTDDeclarations = include;
    }

    public void setIncludeInternalDTDDeclarations(boolean include) {
        this.includeInternalDTDDeclarations = include;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public void setMergeAdjacentText(boolean mergeAdjacentText) {
        this.mergeAdjacentText = mergeAdjacentText;
    }

    public void setStripWhitespaceText(boolean stripWhitespaceText) {
        this.stripWhitespaceText = stripWhitespaceText;
    }

    public void startCDATA() throws SAXException {
        this.insideCDATASection = true;
        this.cdataText = new StringBuffer();
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        getDocument().addDocType(name, publicId, systemId);
        this.insideDTDSection = true;
        this.internalDTDsubset = true;
    }

    public void startDocument() throws SAXException {
        this.document = null;
        this.currentElement = null;
        this.elementStack.clear();
        if (this.elementHandler != null && this.elementHandler instanceof DispatchHandler) {
            this.elementStack.setDispatchHandler((DispatchHandler) this.elementHandler);
        }
        this.namespaceStack.clear();
        this.declaredNamespaceIndex = 0;
        if (this.mergeAdjacentText && this.textBuffer == null) {
            this.textBuffer = new StringBuffer();
        }
        this.textInTextBuffer = false;
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        if (this.mergeAdjacentText && this.textInTextBuffer) {
            completeCurrentTextNode();
        }
        QName qName = this.namespaceStack.getQName(namespaceURI, localName, qualifiedName);
        Branch branch = this.currentElement;
        if (branch == null) {
            branch = getDocument();
        }
        Element element = branch.addElement(qName);
        addDeclaredNamespaces(element);
        addAttributes(element, attributes);
        this.elementStack.pushElement(element);
        this.currentElement = element;
        this.entity = null;
        if (this.elementHandler != null) {
            this.elementHandler.onStart(this.elementStack);
        }
    }

    public void startEntity(String name) throws SAXException {
        this.entityLevel++;
        this.entity = null;
        if (!(this.insideDTDSection || isIgnorableEntity(name))) {
            this.entity = name;
        }
        this.internalDTDsubset = false;
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this.namespaceStack.push(prefix, uri);
    }

    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
    }

    public void warning(SAXParseException exception) throws SAXException {
    }
}