package org.dom4j.io;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMCDATA;
import org.dom4j.dom.DOMComment;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.dom.DOMElement;
import org.dom4j.dom.DOMText;
import org.dom4j.tree.NamespaceStack;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class DOMSAXContentHandler extends DefaultHandler implements LexicalHandler {
    private Map availableNamespaceMap;
    private StringBuffer cdataText;
    private Element currentElement;
    private int declaredNamespaceIndex;
    private List declaredNamespaceList;
    private Document document;
    private DOMDocumentFactory documentFactory;
    private ElementStack elementStack;
    private EntityResolver entityResolver;
    private boolean ignoreComments;
    private InputSource inputSource;
    private boolean insideCDATASection;
    private Locator locator;
    private boolean mergeAdjacentText;
    private NamespaceStack namespaceStack;
    private boolean stripWhitespaceText;
    private StringBuffer textBuffer;
    private boolean textInTextBuffer;

    public DOMSAXContentHandler() {
        this((DOMDocumentFactory) DOMDocumentFactory.getInstance());
    }

    public DOMSAXContentHandler(DOMDocumentFactory documentFactory) {
        this.availableNamespaceMap = new HashMap();
        this.declaredNamespaceList = new ArrayList();
        this.mergeAdjacentText = false;
        this.textInTextBuffer = false;
        this.ignoreComments = false;
        this.stripWhitespaceText = false;
        this.documentFactory = documentFactory;
        this.elementStack = createElementStack();
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
        int size = attributes.getLength();
        int i = 0;
        while (i < size) {
            String attributeQName = attributes.getQName(i);
            if (!attributeQName.startsWith("xmlns")) {
                String attributeURI = attributes.getURI(i);
                String attributeLocalName = attributes.getLocalName(i);
                DOMElement dOMElement = (DOMElement) element;
                dOMElement.setAttributeNode(new DOMAttribute(this.namespaceStack.getAttributeQName(attributeURI, attributeLocalName, attributeQName), attributes.getValue(i)));
            }
            i++;
        }
    }

    protected void addDeclaredNamespaces(Element element) {
        int size = this.namespaceStack.size();
        while (this.declaredNamespaceIndex < size) {
            Namespace namespace = this.namespaceStack.getNamespace(this.declaredNamespaceIndex);
            ((DOMElement) element).setAttribute(attributeNameForNamespace(namespace), namespace.getURI());
            this.declaredNamespaceIndex++;
        }
    }

    protected String attributeNameForNamespace(Namespace namespace) {
        String xmlns = "xmlns";
        String prefix = namespace.getPrefix();
        return prefix.length() > 0 ? xmlns + ":" + prefix : xmlns;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void characters(char[] r4_ch, int r5_start, int r6_end) throws org.xml.sax.SAXException {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.io.DOMSAXContentHandler.characters(char[], int, int):void");
        /*
        r3 = this;
        if (r6 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r1 = r3.currentElement;
        if (r1 == 0) goto L_0x0002;
    L_0x0007:
        r1 = r3.insideCDATASection;
        if (r1 == 0) goto L_0x0021;
    L_0x000b:
        r1 = r3.mergeAdjacentText;
        if (r1 == 0) goto L_0x0016;
    L_0x000f:
        r1 = r3.textInTextBuffer;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r3.completeCurrentTextNode();
    L_0x0016:
        r1 = r3.cdataText;
        r2 = new java.lang.String;
        r2.<init>(r4, r5, r6);
        r1.append(r2);
        goto L_0x0002;
    L_0x0021:
        r1 = r3.mergeAdjacentText;
        if (r1 == 0) goto L_0x002e;
    L_0x0025:
        r1 = r3.textBuffer;
        r1.append(r4, r5, r6);
        r1 = 1;
        r3.textInTextBuffer = r1;
        goto L_0x0002;
    L_0x002e:
        r0 = new org.dom4j.dom.DOMText;
        r1 = new java.lang.String;
        r1.<init>(r4, r5, r6);
        r0.<init>(r1);
        r1 = r3.currentElement;
        r1 = (org.dom4j.dom.DOMElement) r1;
        r1.add(r0);
        goto L_0x0002;
        */
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        if (!this.ignoreComments) {
            if (this.mergeAdjacentText && this.textInTextBuffer) {
                completeCurrentTextNode();
            }
            String text = new String(ch, start, end);
            if (text.length() > 0) {
                DOMComment domComment = new DOMComment(text);
                if (this.currentElement != null) {
                    ((DOMElement) this.currentElement).add(domComment);
                } else {
                    getDocument().appendChild(domComment);
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
                ((DOMElement) this.currentElement).add(new DOMText(this.textBuffer.toString()));
            }
        } else {
            ((DOMElement) this.currentElement).add(new DOMText(this.textBuffer.toString()));
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

    public void endCDATA() throws SAXException {
        this.insideCDATASection = false;
        ((DOMElement) this.currentElement).add(new DOMCDATA(this.cdataText.toString()));
    }

    public void endDTD() throws SAXException {
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
        this.elementStack.popElement();
        this.currentElement = this.elementStack.peekElement();
    }

    public void endEntity(String name) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        this.namespaceStack.pop(prefix);
        this.declaredNamespaceIndex = this.namespaceStack.size();
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public org.w3c.dom.Document getDocument() {
        if (this.document == null) {
            this.document = createDocument();
        }
        return (org.w3c.dom.Document) this.document;
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

    public boolean isIgnoreComments() {
        return this.ignoreComments;
    }

    public boolean isMergeAdjacentText() {
        return this.mergeAdjacentText;
    }

    public boolean isStripWhitespaceText() {
        return this.stripWhitespaceText;
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (this.mergeAdjacentText && this.textInTextBuffer) {
            completeCurrentTextNode();
        }
        ProcessingInstruction pi = (ProcessingInstruction) this.documentFactory.createProcessingInstruction(target, data);
        if (this.currentElement != null) {
            ((org.w3c.dom.Element) this.currentElement).appendChild(pi);
        } else {
            getDocument().appendChild(pi);
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
    }

    public void startDocument() throws SAXException {
        this.document = null;
        this.currentElement = null;
        this.elementStack.clear();
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
            Document branch2 = (Document) getDocument();
        }
        Element element = new DOMElement(qName);
        branch.add(element);
        addDeclaredNamespaces(element);
        addAttributes(element, attributes);
        this.elementStack.pushElement(element);
        this.currentElement = element;
    }

    public void startEntity(String name) throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this.namespaceStack.push(prefix, uri);
    }

    public void warning(SAXParseException exception) throws SAXException {
    }
}