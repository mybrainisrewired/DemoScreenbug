package org.dom4j.io;

import java.io.IOException;
import org.dom4j.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXValidator {
    private ErrorHandler errorHandler;
    private XMLReader xmlReader;

    public SAXValidator(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    protected void configureReader() throws SAXException {
        if (this.xmlReader.getContentHandler() == null) {
            this.xmlReader.setContentHandler(new DefaultHandler());
        }
        this.xmlReader.setFeature("http://xml.org/sax/features/validation", true);
        this.xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
        this.xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
    }

    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader(true);
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public XMLReader getXMLReader() throws SAXException {
        if (this.xmlReader == null) {
            this.xmlReader = createXMLReader();
            configureReader();
        }
        return this.xmlReader;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setXMLReader(XMLReader reader) throws SAXException {
        this.xmlReader = reader;
        configureReader();
    }

    public void validate(Document document) throws SAXException {
        if (document != null) {
            XMLReader reader = getXMLReader();
            if (this.errorHandler != null) {
                reader.setErrorHandler(this.errorHandler);
            }
            try {
                reader.parse(new DocumentInputSource(document));
            } catch (IOException e) {
                throw new RuntimeException("Caught and exception that should never happen: " + e);
            }
        }
    }
}