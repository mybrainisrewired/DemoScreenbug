package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.CDATA;
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
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

public class XMLWriter extends XMLFilterImpl implements LexicalHandler {
    protected static final OutputFormat DEFAULT_FORMAT;
    protected static final String[] LEXICAL_HANDLER_NAMES;
    private static final String PAD_TEXT = " ";
    private boolean autoFlush;
    private StringBuffer buffer;
    private boolean charsAdded;
    private boolean escapeText;
    private OutputFormat format;
    private boolean inDTD;
    private int indentLevel;
    private char lastChar;
    private boolean lastElementClosed;
    protected NodeType lastOutputNodeType;
    private LexicalHandler lexicalHandler;
    private int maximumAllowedCharacter;
    private NamespaceStack namespaceStack;
    private Map<String, String> namespacesMap;
    protected boolean preserve;
    private boolean resolveEntityRefs;
    private boolean showCommentsInDTDs;
    protected Writer writer;

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
        DEFAULT_FORMAT = new OutputFormat();
    }

    public XMLWriter() {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = DEFAULT_FORMAT;
        this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    public XMLWriter(OutputStream out) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = DEFAULT_FORMAT;
        this.writer = createWriter(out, this.format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    public XMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = format;
        this.writer = createWriter(out, format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    public XMLWriter(Writer writer) {
        this(writer, DEFAULT_FORMAT);
    }

    public XMLWriter(Writer writer, OutputFormat format) {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.writer = writer;
        this.format = format;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    public XMLWriter(OutputFormat format) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = format;
        this.writer = createWriter(System.out, format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (ch != null && ch.length != 0 && length > 0) {
            try {
                String string = String.valueOf(ch, start, length);
                if (this.escapeText) {
                    string = escapeElementEntities(string);
                }
                if (this.format.isTrimText()) {
                    if (this.lastOutputNodeType == NodeType.TEXT_NODE && !this.charsAdded) {
                        this.writer.write(Opcodes.ACC_SYNCHRONIZED);
                    } else if (this.charsAdded && Character.isWhitespace(this.lastChar)) {
                        this.writer.write(Opcodes.ACC_SYNCHRONIZED);
                    } else if (this.lastOutputNodeType == NodeType.ELEMENT_NODE && this.format.isPadText() && this.lastElementClosed && Character.isWhitespace(ch[0])) {
                        this.writer.write(PAD_TEXT);
                    }
                    String delim = "";
                    StringTokenizer tokens = new StringTokenizer(string);
                    while (tokens.hasMoreTokens()) {
                        this.writer.write(delim);
                        this.writer.write(tokens.nextToken());
                        delim = PAD_TEXT;
                    }
                } else {
                    this.writer.write(string);
                }
                this.charsAdded = true;
                this.lastChar = ch[start + length - 1];
                this.lastOutputNodeType = NodeType.TEXT_NODE;
                super.characters(ch, start, length);
            } catch (IOException e) {
                handleException(e);
            }
        }
    }

    public void close() throws IOException {
        this.writer.close();
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.showCommentsInDTDs || !this.inDTD) {
            try {
                this.charsAdded = false;
                writeComment(new String(ch, start, length));
            } catch (IOException e) {
                handleException(e);
            }
        }
        if (this.lexicalHandler != null) {
            this.lexicalHandler.comment(ch, start, length);
        }
    }

    protected Writer createWriter(OutputStream outStream, String encoding) throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(outStream, encoding));
    }

    protected int defaultMaximumAllowedCharacter() {
        String encoding = this.format.getEncoding();
        return (encoding == null || !encoding.equals("US-ASCII")) ? -1 : Opcodes.LAND;
    }

    public void endCDATA() throws SAXException {
        try {
            this.writer.write("]]>");
        } catch (IOException e) {
            handleException(e);
        }
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endCDATA();
        }
    }

    public void endDTD() throws SAXException {
        this.inDTD = false;
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endDTD();
        }
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        if (this.autoFlush) {
            try {
                flush();
            } catch (IOException e) {
            }
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            this.charsAdded = false;
            this.indentLevel--;
            if (this.lastElementClosed) {
                writePrintln();
                indent();
            }
            if (true) {
                writeClose(qName);
            } else {
                writeEmptyElementClose(qName);
            }
            this.lastOutputNodeType = NodeType.ELEMENT_NODE;
            this.lastElementClosed = true;
            super.endElement(namespaceURI, localName, qName);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void endEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endEntity(name);
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        super.endPrefixMapping(prefix);
    }

    protected String escapeAttributeEntities(String text) {
        char quote = this.format.getAttributeQuoteCharacter();
        char[] block = null;
        int last = 0;
        int size = text.length();
        int i = 0;
        while (i < size) {
            String entity = null;
            char c = text.charAt(i);
            switch (c) {
                case Type.ARRAY:
                case Type.OBJECT:
                case Opcodes.FCONST_2:
                    break;
                case '\"':
                    if (quote == '\"') {
                        entity = "&quot;";
                    }
                    break;
                case '&':
                    entity = "&amp;";
                    break;
                case '\'':
                    if (quote == '\'') {
                        entity = "&apos;";
                    }
                    break;
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                default:
                    if (c < ' ' || shouldEncodeChar(c)) {
                        entity = "&#" + c + ";";
                    }
                    break;
            }
            if (entity != null) {
                if (block == null) {
                    block = text.toCharArray();
                }
                this.buffer.append(block, last, i - last);
                this.buffer.append(entity);
                last = i + 1;
            }
            i++;
        }
        if (last == 0) {
            return text;
        }
        if (last < size) {
            if (block == null) {
                block = text.toCharArray();
            }
            this.buffer.append(block, last, i - last);
        }
        String answer = this.buffer.toString();
        this.buffer.setLength(0);
        return answer;
    }

    protected String escapeElementEntities(String text) {
        char[] block = null;
        int last = 0;
        int size = text.length();
        int i = 0;
        while (i < size) {
            String entity = null;
            char c = text.charAt(i);
            switch (c) {
                case Type.ARRAY:
                case Type.OBJECT:
                case Opcodes.FCONST_2:
                    if (this.preserve) {
                        entity = String.valueOf(c);
                    }
                    break;
                case '&':
                    entity = "&amp;";
                    break;
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                default:
                    if (c < ' ' || shouldEncodeChar(c)) {
                        entity = "&#" + c + ";";
                    }
                    break;
            }
            if (entity != null) {
                if (block == null) {
                    block = text.toCharArray();
                }
                this.buffer.append(block, last, i - last);
                this.buffer.append(entity);
                last = i + 1;
            }
            i++;
        }
        if (last == 0) {
            return text;
        }
        if (last < size) {
            if (block == null) {
                block = text.toCharArray();
            }
            this.buffer.append(block, last, i - last);
        }
        String answer = this.buffer.toString();
        this.buffer.setLength(0);
        return answer;
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    public int getMaximumAllowedCharacter() {
        if (this.maximumAllowedCharacter == 0) {
            this.maximumAllowedCharacter = defaultMaximumAllowedCharacter();
        }
        return this.maximumAllowedCharacter;
    }

    protected OutputFormat getOutputFormat() {
        return this.format;
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        int i = 0;
        while (i < LEXICAL_HANDLER_NAMES.length) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                return getLexicalHandler();
            }
            i++;
        }
        return super.getProperty(name);
    }

    protected void handleException(IOException e) throws SAXException {
        throw new SAXException(e);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        super.ignorableWhitespace(ch, start, length);
    }

    protected void indent() throws IOException {
        String indent = this.format.getIndent();
        if (indent != null && indent.length() > 0) {
            int i = 0;
            while (i < this.indentLevel) {
                this.writer.write(indent);
                i++;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void installLexicalHandler() {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.io.XMLWriter.installLexicalHandler():void");
        /*
        r4 = this;
        r1 = r4.getParent();
        if (r1 != 0) goto L_0x000e;
    L_0x0006:
        r2 = new java.lang.NullPointerException;
        r3 = "No parent for filter";
        r2.<init>(r3);
        throw r2;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        r2 = LEXICAL_HANDLER_NAMES;
        r2 = r2.length;
        if (r0 >= r2) goto L_0x001b;
    L_0x0014:
        r2 = LEXICAL_HANDLER_NAMES;	 Catch:{ SAXNotRecognizedException -> 0x0020, SAXNotSupportedException -> 0x001c }
        r2 = r2[r0];	 Catch:{ SAXNotRecognizedException -> 0x0020, SAXNotSupportedException -> 0x001c }
        r1.setProperty(r2, r4);	 Catch:{ SAXNotRecognizedException -> 0x0020, SAXNotSupportedException -> 0x001c }
    L_0x001b:
        return;
    L_0x001c:
        r2 = move-exception;
    L_0x001d:
        r0 = r0 + 1;
        goto L_0x000f;
    L_0x0020:
        r2 = move-exception;
        goto L_0x001d;
        */
    }

    protected final boolean isElementSpacePreserved(Element element) {
        Attribute attr = element.attribute("space");
        boolean preserveFound = this.preserve;
        if (attr != null) {
            return "xml".equals(attr.getNamespacePrefix()) && "preserve".equals(attr.getText());
        } else {
            return preserveFound;
        }
    }

    public boolean isEscapeText() {
        return this.escapeText;
    }

    protected boolean isExpandEmptyElements() {
        return this.format.isExpandEmptyElements();
    }

    protected boolean isNamespaceDeclaration(Namespace ns) {
        return (ns == null || ns == Namespace.XML_NAMESPACE || ns.getURI() == null || this.namespaceStack.contains(ns)) ? false : true;
    }

    public void notationDecl(String name, String publicID, String systemID) throws SAXException {
        super.notationDecl(name, publicID, systemID);
    }

    public void parse(InputSource source) throws IOException, SAXException {
        installLexicalHandler();
        super.parse(source);
    }

    public void println() throws IOException {
        this.writer.write(this.format.getLineSeparator());
    }

    public void processingInstruction(String target, String data) throws SAXException {
        try {
            indent();
            this.writer.write("<?");
            this.writer.write(target);
            this.writer.write(PAD_TEXT);
            this.writer.write(data);
            this.writer.write("?>");
            writePrintln();
            this.lastOutputNodeType = NodeType.PROCESSING_INSTRUCTION_NODE;
            super.processingInstruction(target, data);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public boolean resolveEntityRefs() {
        return this.resolveEntityRefs;
    }

    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
    }

    public void setEscapeText(boolean escapeText) {
        this.escapeText = escapeText;
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    public void setLexicalHandler(LexicalHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null lexical handler");
        }
        this.lexicalHandler = handler;
    }

    public void setMaximumAllowedCharacter(int maximumAllowedCharacter) {
        this.maximumAllowedCharacter = maximumAllowedCharacter;
    }

    public void setOutputStream(OutputStream out) throws UnsupportedEncodingException {
        this.writer = createWriter(out, this.format.getEncoding());
        this.autoFlush = true;
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        int i = 0;
        while (i < LEXICAL_HANDLER_NAMES.length) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                setLexicalHandler((LexicalHandler) value);
                return;
            } else {
                i++;
            }
        }
        super.setProperty(name, value);
    }

    public void setResolveEntityRefs(boolean resolve) {
        this.resolveEntityRefs = resolve;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
        this.autoFlush = false;
    }

    protected boolean shouldEncodeChar(char c) {
        char max = getMaximumAllowedCharacter();
        return max > '\u0000' && c > max;
    }

    public void startCDATA() throws SAXException {
        try {
            this.writer.write("<![CDATA[");
        } catch (IOException e) {
            handleException(e);
        }
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
        }
    }

    public void startDTD(String name, String publicID, String systemID) throws SAXException {
        this.inDTD = true;
        try {
            writeDocType(name, publicID, systemID);
        } catch (IOException e) {
            handleException(e);
        }
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startDTD(name, publicID, systemID);
        }
    }

    public void startDocument() throws SAXException {
        try {
            writeDeclaration();
            super.startDocument();
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            this.charsAdded = false;
            writePrintln();
            indent();
            this.writer.write("<");
            this.writer.write(qName);
            writeNamespaces();
            writeAttributes(attributes);
            this.writer.write(">");
            this.indentLevel++;
            this.lastOutputNodeType = NodeType.ELEMENT_NODE;
            this.lastElementClosed = false;
            super.startElement(namespaceURI, localName, qName, attributes);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void startEntity(String name) throws SAXException {
        try {
            writeEntityRef(name);
        } catch (IOException e) {
            handleException(e);
        }
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startEntity(name);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.namespacesMap == null) {
            this.namespacesMap = new HashMap();
        }
        this.namespacesMap.put(prefix, uri);
        super.startPrefixMapping(prefix, uri);
    }

    public void unparsedEntityDecl(String name, String publicID, String systemID, String notationName) throws SAXException {
        super.unparsedEntityDecl(name, publicID, systemID, notationName);
    }

    public void write(Object object) throws IOException {
        if (object instanceof Node) {
            write((Node) object);
        } else if (object instanceof String) {
            write((String) object);
        } else if (object instanceof List) {
            List list = (List) object;
            int i = 0;
            int size = list.size();
            while (i < size) {
                write(list.get(i));
                i++;
            }
        } else if (object != null) {
            throw new IOException("Invalid object: " + object);
        }
    }

    public void write(String text) throws IOException {
        writeString(text);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Attribute attribute) throws IOException {
        writeAttribute(attribute);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(CDATA cdata) throws IOException {
        writeCDATA(cdata.getText());
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Comment comment) throws IOException {
        writeComment(comment.getText());
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Document doc) throws IOException {
        writeDeclaration();
        if (doc.getDocType() != null) {
            indent();
            writeDocType(doc.getDocType());
        }
        int i = 0;
        int size = doc.nodeCount();
        while (i < size) {
            writeNode(doc.node(i));
            i++;
        }
        writePrintln();
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(DocumentType docType) throws IOException {
        writeDocType(docType);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Element element) throws IOException {
        writeElement(element);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Entity entity) throws IOException {
        writeEntity(entity);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Namespace namespace) throws IOException {
        writeNamespace(namespace);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Node node) throws IOException {
        writeNode(node);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(ProcessingInstruction processingInstruction) throws IOException {
        writeProcessingInstruction(processingInstruction);
        if (this.autoFlush) {
            flush();
        }
    }

    public void write(Text text) throws IOException {
        writeString(text.getText());
        if (this.autoFlush) {
            flush();
        }
    }

    protected void writeAttribute(Attribute attribute) throws IOException {
        this.writer.write(PAD_TEXT);
        this.writer.write(attribute.getQualifiedName());
        this.writer.write("=");
        char quote = this.format.getAttributeQuoteCharacter();
        this.writer.write(quote);
        writeEscapeAttributeEntities(attribute.getValue());
        this.writer.write(quote);
        this.lastOutputNodeType = NodeType.ATTRIBUTE_NODE;
    }

    protected void writeAttribute(Attributes attributes, int index) throws IOException {
        char quote = this.format.getAttributeQuoteCharacter();
        this.writer.write(PAD_TEXT);
        this.writer.write(attributes.getQName(index));
        this.writer.write("=");
        this.writer.write(quote);
        writeEscapeAttributeEntities(attributes.getValue(index));
        this.writer.write(quote);
    }

    protected void writeAttributes(Element element) throws IOException {
        int i = 0;
        int size = element.attributeCount();
        while (i < size) {
            Attribute attribute = element.attribute(i);
            Namespace ns = attribute.getNamespace();
            if (!(ns == null || ns == Namespace.NO_NAMESPACE || ns == Namespace.XML_NAMESPACE)) {
                if (!ns.getURI().equals(this.namespaceStack.getURI(ns.getPrefix()))) {
                    writeNamespace(ns);
                    this.namespaceStack.push(ns);
                }
            }
            String attName = attribute.getName();
            String uri;
            if (attName.startsWith("xmlns:")) {
                String prefix = attName.substring(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT);
                if (this.namespaceStack.getNamespaceForPrefix(prefix) == null) {
                    uri = attribute.getValue();
                    this.namespaceStack.push(prefix, uri);
                    writeNamespace(prefix, uri);
                }
            } else if (!attName.equals("xmlns")) {
                char quote = this.format.getAttributeQuoteCharacter();
                this.writer.write(PAD_TEXT);
                this.writer.write(attribute.getQualifiedName());
                this.writer.write("=");
                this.writer.write(quote);
                writeEscapeAttributeEntities(attribute.getValue());
                this.writer.write(quote);
            } else if (this.namespaceStack.getDefaultNamespace() == null) {
                uri = attribute.getValue();
                this.namespaceStack.push(null, uri);
                writeNamespace(null, uri);
            }
            i++;
        }
    }

    protected void writeAttributes(Attributes attributes) throws IOException {
        int i = 0;
        int size = attributes.getLength();
        while (i < size) {
            writeAttribute(attributes, i);
            i++;
        }
    }

    protected void writeCDATA(String text) throws IOException {
        this.writer.write("<![CDATA[");
        if (text != null) {
            this.writer.write(text);
        }
        this.writer.write("]]>");
        this.lastOutputNodeType = NodeType.CDATA_SECTION_NODE;
    }

    protected void writeClose(String qualifiedName) throws IOException {
        this.writer.write("</");
        this.writer.write(qualifiedName);
        this.writer.write(">");
    }

    public void writeClose(Element element) throws IOException {
        writeClose(element.getQualifiedName());
    }

    protected void writeComment(String text) throws IOException {
        if (this.format.isNewlines()) {
            println();
            indent();
        }
        this.writer.write("<!--");
        this.writer.write(text);
        this.writer.write("-->");
        this.lastOutputNodeType = NodeType.COMMENT_NODE;
    }

    protected void writeDeclaration() throws IOException {
        String encoding = this.format.getEncoding();
        if (!this.format.isSuppressDeclaration()) {
            if (encoding.equals("UTF8")) {
                this.writer.write("<?xml version=\"1.0\"");
                if (!this.format.isOmitEncoding()) {
                    this.writer.write(" encoding=\"UTF-8\"");
                }
                this.writer.write("?>");
            } else {
                this.writer.write("<?xml version=\"1.0\"");
                if (!this.format.isOmitEncoding()) {
                    this.writer.write(" encoding=\"" + encoding + "\"");
                }
                this.writer.write("?>");
            }
            if (this.format.isNewLineAfterDeclaration()) {
                println();
            }
        }
    }

    protected void writeDocType(String name, String publicID, String systemID) throws IOException {
        boolean hasPublic = false;
        this.writer.write("<!DOCTYPE ");
        this.writer.write(name);
        if (!(publicID == null || publicID.equals(""))) {
            this.writer.write(" PUBLIC \"");
            this.writer.write(publicID);
            this.writer.write("\"");
            hasPublic = true;
        }
        if (!(systemID == null || systemID.equals(""))) {
            if (!hasPublic) {
                this.writer.write(" SYSTEM");
            }
            this.writer.write(" \"");
            this.writer.write(systemID);
            this.writer.write("\"");
        }
        this.writer.write(">");
        writePrintln();
    }

    protected void writeDocType(DocumentType docType) throws IOException {
        if (docType != null) {
            docType.write(this.writer);
            writePrintln();
        }
    }

    protected void writeElement(Element element) throws IOException {
        int size = element.nodeCount();
        String qualifiedName = element.getQualifiedName();
        writePrintln();
        indent();
        this.writer.write("<");
        this.writer.write(qualifiedName);
        int previouslyDeclaredNamespaces = this.namespaceStack.size();
        Namespace ns = element.getNamespace();
        if (isNamespaceDeclaration(ns)) {
            this.namespaceStack.push(ns);
            writeNamespace(ns);
        }
        boolean textOnly = true;
        int i = 0;
        while (i < size) {
            Node node = element.node(i);
            if (node instanceof Namespace) {
                Namespace additional = (Namespace) node;
                if (isNamespaceDeclaration(additional)) {
                    this.namespaceStack.push(additional);
                    writeNamespace(additional);
                }
            } else if (node instanceof Element) {
                textOnly = false;
            } else if (node instanceof Comment) {
                textOnly = false;
            }
            i++;
        }
        writeAttributes(element);
        this.lastOutputNodeType = NodeType.ELEMENT_NODE;
        if (size <= 0) {
            writeEmptyElementClose(qualifiedName);
        } else {
            this.writer.write(">");
            if (textOnly) {
                writeElementContent(element);
            } else {
                this.indentLevel++;
                writeElementContent(element);
                this.indentLevel--;
                writePrintln();
                indent();
            }
            this.writer.write("</");
            this.writer.write(qualifiedName);
            this.writer.write(">");
        }
        while (this.namespaceStack.size() > previouslyDeclaredNamespaces) {
            this.namespaceStack.pop();
        }
        this.lastOutputNodeType = NodeType.ELEMENT_NODE;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void writeElementContent(org.dom4j.Element r15_element) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.io.XMLWriter.writeElementContent(org.dom4j.Element):void");
        /*
        r14 = this;
        r11 = 0;
        r12 = r14.format;
        r9 = r12.isTrimText();
        r6 = r14.preserve;
        if (r9 == 0) goto L_0x0016;
    L_0x000b:
        r12 = r14.isElementSpacePreserved(r15);
        r14.preserve = r12;
        r12 = r14.preserve;
        if (r12 != 0) goto L_0x0032;
    L_0x0015:
        r9 = 1;
    L_0x0016:
        if (r9 == 0) goto L_0x0100;
    L_0x0018:
        r4 = 0;
        r0 = 0;
        r8 = 1;
        r2 = 0;
        r7 = r15.nodeCount();
    L_0x0020:
        if (r2 >= r7) goto L_0x00c1;
    L_0x0022:
        r5 = r15.node(r2);
        r12 = r5 instanceof org.dom4j.Text;
        if (r12 == 0) goto L_0x0049;
    L_0x002a:
        if (r4 != 0) goto L_0x0034;
    L_0x002c:
        r4 = r5;
        r4 = (org.dom4j.Text) r4;
    L_0x002f:
        r2 = r2 + 1;
        goto L_0x0020;
    L_0x0032:
        r9 = r11;
        goto L_0x0016;
    L_0x0034:
        if (r0 != 0) goto L_0x003f;
    L_0x0036:
        r0 = new java.lang.StringBuffer;
        r12 = r4.getText();
        r0.<init>(r12);
    L_0x003f:
        r5 = (org.dom4j.Text) r5;
        r12 = r5.getText();
        r0.append(r12);
        goto L_0x002f;
    L_0x0049:
        if (r8 != 0) goto L_0x0068;
    L_0x004b:
        r12 = r14.format;
        r12 = r12.isPadText();
        if (r12 == 0) goto L_0x0068;
    L_0x0053:
        r1 = 97;
        if (r0 == 0) goto L_0x009d;
    L_0x0057:
        r1 = r0.charAt(r11);
    L_0x005b:
        r12 = java.lang.Character.isWhitespace(r1);
        if (r12 == 0) goto L_0x0068;
    L_0x0061:
        r12 = r14.writer;
        r13 = " ";
        r12.write(r13);
    L_0x0068:
        if (r4 == 0) goto L_0x0098;
    L_0x006a:
        if (r0 == 0) goto L_0x00a8;
    L_0x006c:
        r12 = r0.toString();
        r14.writeString(r12);
        r0 = 0;
    L_0x0074:
        r12 = r14.format;
        r12 = r12.isPadText();
        if (r12 == 0) goto L_0x0097;
    L_0x007c:
        r3 = 97;
        if (r0 == 0) goto L_0x00b0;
    L_0x0080:
        r12 = r0.length();
        r12 = r12 + -1;
        r3 = r0.charAt(r12);
    L_0x008a:
        r12 = java.lang.Character.isWhitespace(r3);
        if (r12 == 0) goto L_0x0097;
    L_0x0090:
        r12 = r14.writer;
        r13 = " ";
        r12.write(r13);
    L_0x0097:
        r4 = 0;
    L_0x0098:
        r8 = 0;
        r14.writeNode(r5);
        goto L_0x002f;
    L_0x009d:
        if (r4 == 0) goto L_0x005b;
    L_0x009f:
        r12 = r4.getText();
        r1 = r12.charAt(r11);
        goto L_0x005b;
    L_0x00a8:
        r12 = r4.getText();
        r14.writeString(r12);
        goto L_0x0074;
    L_0x00b0:
        if (r4 == 0) goto L_0x008a;
    L_0x00b2:
        r10 = r4.getText();
        r12 = r10.length();
        r12 = r12 + -1;
        r3 = r10.charAt(r12);
        goto L_0x008a;
    L_0x00c1:
        if (r4 == 0) goto L_0x00ec;
    L_0x00c3:
        if (r8 != 0) goto L_0x00e2;
    L_0x00c5:
        r12 = r14.format;
        r12 = r12.isPadText();
        if (r12 == 0) goto L_0x00e2;
    L_0x00cd:
        r1 = 97;
        if (r0 == 0) goto L_0x00ef;
    L_0x00d1:
        r1 = r0.charAt(r11);
    L_0x00d5:
        r11 = java.lang.Character.isWhitespace(r1);
        if (r11 == 0) goto L_0x00e2;
    L_0x00db:
        r11 = r14.writer;
        r12 = " ";
        r11.write(r12);
    L_0x00e2:
        if (r0 == 0) goto L_0x00f8;
    L_0x00e4:
        r11 = r0.toString();
        r14.writeString(r11);
        r0 = 0;
    L_0x00ec:
        r14.preserve = r6;
        return;
    L_0x00ef:
        r12 = r4.getText();
        r1 = r12.charAt(r11);
        goto L_0x00d5;
    L_0x00f8:
        r11 = r4.getText();
        r14.writeString(r11);
        goto L_0x00ec;
    L_0x0100:
        r4 = 0;
        r2 = 0;
        r7 = r15.nodeCount();
    L_0x0106:
        if (r2 >= r7) goto L_0x00ec;
    L_0x0108:
        r5 = r15.node(r2);
        r11 = r5 instanceof org.dom4j.Text;
        if (r11 == 0) goto L_0x0117;
    L_0x0110:
        r14.writeNode(r5);
        r4 = r5;
    L_0x0114:
        r2 = r2 + 1;
        goto L_0x0106;
    L_0x0117:
        if (r4 == 0) goto L_0x013c;
    L_0x0119:
        r11 = r14.format;
        r11 = r11.isPadText();
        if (r11 == 0) goto L_0x013c;
    L_0x0121:
        r10 = r4.getText();
        r11 = r10.length();
        r11 = r11 + -1;
        r3 = r10.charAt(r11);
        r11 = java.lang.Character.isWhitespace(r3);
        if (r11 == 0) goto L_0x013c;
    L_0x0135:
        r11 = r14.writer;
        r12 = " ";
        r11.write(r12);
    L_0x013c:
        r14.writeNode(r5);
        r4 = 0;
        goto L_0x0114;
        */
    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        if (this.format.isExpandEmptyElements()) {
            this.writer.write("></");
            this.writer.write(qualifiedName);
            this.writer.write(">");
        } else {
            this.writer.write("/>");
        }
    }

    protected void writeEntity(Entity entity) throws IOException {
        if (resolveEntityRefs()) {
            this.writer.write(entity.getText());
        } else {
            writeEntityRef(entity.getName());
        }
    }

    protected void writeEntityRef(String name) throws IOException {
        this.writer.write("&");
        this.writer.write(name);
        this.writer.write(";");
        this.lastOutputNodeType = NodeType.ENTITY_REFERENCE_NODE;
    }

    protected void writeEscapeAttributeEntities(String txt) throws IOException {
        if (txt != null) {
            this.writer.write(escapeAttributeEntities(txt));
        }
    }

    protected void writeNamespace(String prefix, String uri) throws IOException {
        if (prefix == null || prefix.length() <= 0) {
            this.writer.write(" xmlns=\"");
        } else {
            this.writer.write(" xmlns:");
            this.writer.write(prefix);
            this.writer.write("=\"");
        }
        this.writer.write(uri);
        this.writer.write("\"");
    }

    protected void writeNamespace(Namespace namespace) throws IOException {
        if (namespace != null) {
            writeNamespace(namespace.getPrefix(), namespace.getURI());
        }
    }

    protected void writeNamespaces() throws IOException {
        if (this.namespacesMap != null) {
            Iterator iter = this.namespacesMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = (Entry) iter.next();
                writeNamespace((String) entry.getKey(), (String) entry.getValue());
            }
            this.namespacesMap = null;
        }
    }

    protected void writeNode(Node node) throws IOException {
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[node.getNodeTypeEnum().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeElement((Element) node);
            case ClassWriter.COMPUTE_FRAMES:
                writeAttribute((Attribute) node);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeNodeText(node);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                writeCDATA(node.getText());
            case JsonWriteContext.STATUS_EXPECT_NAME:
                writeEntity((Entity) node);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                writeProcessingInstruction((ProcessingInstruction) node);
            case Type.LONG:
                writeComment(node.getText());
            case Type.DOUBLE:
                write((Document) node);
            case Type.ARRAY:
                writeDocType((DocumentType) node);
            case Type.OBJECT:
                break;
            default:
                throw new IOException("Invalid node type: " + node);
        }
    }

    protected void writeNodeText(Node node) throws IOException {
        String text = node.getText();
        if (text != null && text.length() > 0) {
            if (this.escapeText) {
                text = escapeElementEntities(text);
            }
            this.lastOutputNodeType = NodeType.TEXT_NODE;
            this.writer.write(text);
        }
    }

    public void writeOpen(Element element) throws IOException {
        this.writer.write("<");
        this.writer.write(element.getQualifiedName());
        writeAttributes(element);
        this.writer.write(">");
    }

    protected void writePrintln() throws IOException {
        if (this.format.isNewlines()) {
            this.writer.write(this.format.getLineSeparator());
        }
    }

    protected void writeProcessingInstruction(ProcessingInstruction pi) throws IOException {
        this.writer.write("<?");
        this.writer.write(pi.getName());
        this.writer.write(PAD_TEXT);
        this.writer.write(pi.getText());
        this.writer.write("?>");
        writePrintln();
        this.lastOutputNodeType = NodeType.PROCESSING_INSTRUCTION_NODE;
    }

    protected void writeString(String text) throws IOException {
        if (text != null && text.length() > 0) {
            if (this.escapeText) {
                text = escapeElementEntities(text);
            }
            if (this.format.isTrimText()) {
                boolean first = true;
                StringTokenizer tokenizer = new StringTokenizer(text);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (first) {
                        first = false;
                        if (this.lastOutputNodeType == NodeType.TEXT_NODE) {
                            this.writer.write(PAD_TEXT);
                        }
                    } else {
                        this.writer.write(PAD_TEXT);
                    }
                    this.writer.write(token);
                    this.lastOutputNodeType = NodeType.TEXT_NODE;
                }
            } else {
                this.lastOutputNodeType = NodeType.TEXT_NODE;
                this.writer.write(text);
            }
        }
    }
}