package org.dom4j.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXReader {
    private static final String SAX_DECL_HANDLER = "http://xml.org/sax/properties/declaration-handler";
    private static final String SAX_LEXICALHANDLER = "http://xml.org/sax/handlers/LexicalHandler";
    private static final String SAX_LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
    private static final String SAX_NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String SAX_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    private static final String SAX_STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    private DispatchHandler dispatchHandler;
    private String encoding;
    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;
    private DocumentFactory factory;
    private boolean ignoreComments;
    private boolean includeExternalDTDDeclarations;
    private boolean includeInternalDTDDeclarations;
    private boolean mergeAdjacentText;
    private boolean stringInternEnabled;
    private boolean stripWhitespaceText;
    private boolean validating;
    private XMLFilter xmlFilter;
    private XMLReader xmlReader;

    protected static class SAXEntityResolver implements EntityResolver, Serializable {
        protected String uriPrefix;

        public SAXEntityResolver(String uriPrefix) {
            this.uriPrefix = uriPrefix;
        }

        public InputSource resolveEntity(String publicId, String systemId) {
            if (systemId != null && systemId.length() > 0 && this.uriPrefix != null && systemId.indexOf(Opcodes.ASTORE) <= 0) {
                systemId = this.uriPrefix + systemId;
            }
            return new InputSource(systemId);
        }
    }

    public SAXReader() {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
    }

    public SAXReader(String xmlReaderClassName) throws SAXException {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        if (xmlReaderClassName != null) {
            this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        }
    }

    public SAXReader(String xmlReaderClassName, boolean validating) throws SAXException {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        if (xmlReaderClassName != null) {
            this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        }
        this.validating = validating;
    }

    public SAXReader(DocumentFactory factory) {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        this.factory = factory;
    }

    public SAXReader(DocumentFactory factory, boolean validating) {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        this.factory = factory;
        this.validating = validating;
    }

    public SAXReader(XMLReader xmlReader) {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        this.xmlReader = xmlReader;
    }

    public SAXReader(XMLReader xmlReader, boolean validating) {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        this.xmlReader = xmlReader;
        this.validating = validating;
    }

    public SAXReader(boolean validating) {
        this.stringInternEnabled = true;
        this.includeInternalDTDDeclarations = false;
        this.includeExternalDTDDeclarations = false;
        this.mergeAdjacentText = false;
        this.stripWhitespaceText = false;
        this.ignoreComments = false;
        this.encoding = null;
        this.validating = validating;
    }

    public void addHandler(String path, ElementHandler handler) {
        getDispatchHandler().addHandler(path, handler);
    }

    protected void configureReader(XMLReader reader, DefaultHandler handler) throws DocumentException {
        SAXHelper.setParserProperty(reader, SAX_LEXICALHANDLER, handler);
        SAXHelper.setParserProperty(reader, SAX_LEXICAL_HANDLER, handler);
        if (this.includeInternalDTDDeclarations || this.includeExternalDTDDeclarations) {
            SAXHelper.setParserProperty(reader, SAX_DECL_HANDLER, handler);
        }
        SAXHelper.setParserFeature(reader, SAX_NAMESPACES, true);
        SAXHelper.setParserFeature(reader, SAX_NAMESPACE_PREFIXES, false);
        SAXHelper.setParserFeature(reader, SAX_STRING_INTERNING, isStringInternEnabled());
        SAXHelper.setParserFeature(reader, "http://xml.org/sax/features/use-locator2", true);
        try {
            reader.setFeature("http://xml.org/sax/features/validation", isValidating());
            if (this.errorHandler != null) {
                reader.setErrorHandler(this.errorHandler);
            } else {
                reader.setErrorHandler(handler);
            }
        } catch (Exception e) {
            e = e;
            if (isValidating()) {
                Exception e2;
                throw new DocumentException("Validation not supported for XMLReader: " + reader, e2);
            }
        }
    }

    protected SAXContentHandler createContentHandler(XMLReader reader) {
        return new SAXContentHandler(getDocumentFactory(), this.dispatchHandler);
    }

    protected EntityResolver createDefaultEntityResolver(String systemId) {
        String str = null;
        if (systemId != null && systemId.length() > 0) {
            int idx = systemId.lastIndexOf(Opcodes.V1_3);
            if (idx > 0) {
                str = systemId.substring(0, idx + 1);
            }
        }
        return new SAXEntityResolver(str);
    }

    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader(isValidating());
    }

    protected DispatchHandler getDispatchHandler() {
        if (this.dispatchHandler == null) {
            this.dispatchHandler = new DispatchHandler();
        }
        return this.dispatchHandler;
    }

    public DocumentFactory getDocumentFactory() {
        if (this.factory == null) {
            this.factory = DocumentFactory.getInstance();
        }
        return this.factory;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public XMLFilter getXMLFilter() {
        return this.xmlFilter;
    }

    public XMLReader getXMLReader() throws SAXException {
        if (this.xmlReader == null) {
            this.xmlReader = createXMLReader();
        }
        return this.xmlReader;
    }

    protected XMLReader installXMLFilter(XMLReader reader) {
        XMLFilter filter = getXMLFilter();
        if (filter == null) {
            return reader;
        }
        XMLFilter root = filter;
        while (true) {
            XMLReader parent = root.getParent();
            if (parent instanceof XMLFilter) {
                root = (XMLFilter) parent;
            } else {
                root.setParent(reader);
                return filter;
            }
        }
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

    public boolean isStringInternEnabled() {
        return this.stringInternEnabled;
    }

    public boolean isStripWhitespaceText() {
        return this.stripWhitespaceText;
    }

    public boolean isValidating() {
        return this.validating;
    }

    public Document read(File file) throws DocumentException {
        try {
            InputSource source = new InputSource(new FileInputStream(file));
            if (this.encoding != null) {
                source.setEncoding(this.encoding);
            }
            String path = file.getAbsolutePath();
            if (path != null) {
                StringBuffer sb = new StringBuffer("file://");
                if (!path.startsWith(File.separator)) {
                    sb.append("/");
                }
                sb.append(path.replace('\\', '/'));
                source.setSystemId(sb.toString());
            }
            return read(source);
        } catch (FileNotFoundException e) {
            FileNotFoundException e2 = e;
            throw new DocumentException(e2.getMessage(), e2);
        }
    }

    public Document read(InputStream in) throws DocumentException {
        InputSource source = new InputSource(in);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(InputStream in, String systemId) throws DocumentException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(Reader reader) throws DocumentException {
        InputSource source = new InputSource(reader);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(Reader reader, String systemId) throws DocumentException {
        InputSource source = new InputSource(reader);
        source.setSystemId(systemId);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(String systemId) throws DocumentException {
        InputSource source = new InputSource(systemId);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(URL url) throws DocumentException {
        InputSource source = new InputSource(url.toExternalForm());
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }
        return read(source);
    }

    public Document read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = installXMLFilter(getXMLReader());
            EntityResolver thatEntityResolver = this.entityResolver;
            if (thatEntityResolver == null) {
                thatEntityResolver = createDefaultEntityResolver(in.getSystemId());
                this.entityResolver = thatEntityResolver;
            }
            reader.setEntityResolver(thatEntityResolver);
            SAXContentHandler contentHandler = createContentHandler(reader);
            contentHandler.setEntityResolver(thatEntityResolver);
            contentHandler.setInputSource(in);
            boolean internal = isIncludeInternalDTDDeclarations();
            boolean external = isIncludeExternalDTDDeclarations();
            contentHandler.setIncludeInternalDTDDeclarations(internal);
            contentHandler.setIncludeExternalDTDDeclarations(external);
            contentHandler.setMergeAdjacentText(isMergeAdjacentText());
            contentHandler.setStripWhitespaceText(isStripWhitespaceText());
            contentHandler.setIgnoreComments(isIgnoreComments());
            reader.setContentHandler(contentHandler);
            configureReader(reader, contentHandler);
            reader.parse(in);
            return contentHandler.getDocument();
        } catch (Exception e) {
            e = e;
            Exception e2;
            if (e2 instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) e2;
                String systemId = parseException.getSystemId();
                if (systemId == null) {
                    systemId = "";
                }
                throw new DocumentException("Error on line " + parseException.getLineNumber() + " of document " + systemId + " : " + parseException.getMessage(), e2);
            } else {
                throw new DocumentException(e2.getMessage(), e2);
            }
        }
    }

    public void removeHandler(String path) {
        getDispatchHandler().removeHandler(path);
    }

    public void resetHandlers() {
        getDispatchHandler().resetHandlers();
    }

    public void setDefaultHandler(ElementHandler handler) {
        getDispatchHandler().setDefaultHandler(handler);
    }

    protected void setDispatchHandler(DispatchHandler dispatchHandler) {
        this.dispatchHandler = dispatchHandler;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.factory = documentFactory;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setFeature(String name, boolean value) throws SAXException {
        getXMLReader().setFeature(name, value);
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

    public void setMergeAdjacentText(boolean mergeAdjacentText) {
        this.mergeAdjacentText = mergeAdjacentText;
    }

    public void setProperty(String name, Object value) throws SAXException {
        getXMLReader().setProperty(name, value);
    }

    public void setStringInternEnabled(boolean stringInternEnabled) {
        this.stringInternEnabled = stringInternEnabled;
    }

    public void setStripWhitespaceText(boolean stripWhitespaceText) {
        this.stripWhitespaceText = stripWhitespaceText;
    }

    public void setValidation(boolean validation) {
        this.validating = validation;
    }

    public void setXMLFilter(XMLFilter filter) {
        this.xmlFilter = filter;
    }

    public void setXMLReader(XMLReader reader) {
        this.xmlReader = reader;
    }

    public void setXMLReaderClassName(String xmlReaderClassName) throws SAXException {
        setXMLReader(XMLReaderFactory.createXMLReader(xmlReaderClassName));
    }
}