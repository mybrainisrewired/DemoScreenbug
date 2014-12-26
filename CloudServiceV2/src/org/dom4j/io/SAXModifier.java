package org.dom4j.io;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXModifier {
    private HashMap modifiers;
    private SAXModifyReader modifyReader;
    private boolean pruneElements;
    private XMLReader xmlReader;
    private XMLWriter xmlWriter;

    public SAXModifier() {
        this.modifiers = new HashMap();
    }

    public SAXModifier(XMLReader xmlReader) {
        this.modifiers = new HashMap();
        this.xmlReader = xmlReader;
    }

    public SAXModifier(XMLReader xmlReader, boolean pruneElements) {
        this.modifiers = new HashMap();
        this.xmlReader = xmlReader;
    }

    public SAXModifier(boolean pruneElements) {
        this.modifiers = new HashMap();
        this.pruneElements = pruneElements;
    }

    private SAXModifyReader getSAXModifyReader() {
        if (this.modifyReader == null) {
            this.modifyReader = new SAXModifyReader();
        }
        return this.modifyReader;
    }

    private XMLReader getXMLReader() throws SAXException {
        if (this.xmlReader == null) {
            this.xmlReader = SAXHelper.createXMLReader(false);
        }
        return this.xmlReader;
    }

    private SAXReader installModifyReader() throws DocumentException {
        try {
            SAXModifyReader reader = getSAXModifyReader();
            if (isPruneElements()) {
                this.modifyReader.setDispatchHandler(new PruningDispatchHandler());
            }
            reader.resetHandlers();
            Iterator modifierIt = this.modifiers.entrySet().iterator();
            while (modifierIt.hasNext()) {
                Entry entry = (Entry) modifierIt.next();
                reader.addHandler((String) entry.getKey(), new SAXModifyElementHandler((ElementModifier) entry.getValue()));
            }
            reader.setXMLWriter(getXMLWriter());
            reader.setXMLReader(getXMLReader());
            return reader;
        } catch (SAXException e) {
            SAXException ex = e;
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    public void addModifier(String path, ElementModifier modifier) {
        this.modifiers.put(path, modifier);
    }

    public DocumentFactory getDocumentFactory() {
        return getSAXModifyReader().getDocumentFactory();
    }

    public XMLWriter getXMLWriter() {
        return this.xmlWriter;
    }

    public boolean isPruneElements() {
        return this.pruneElements;
    }

    public Document modify(File source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(InputStream source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(InputStream source, String systemId) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(Reader source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(Reader source, String systemId) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(String source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(URL source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(InputSource source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public void removeModifier(String path) {
        this.modifiers.remove(path);
        getSAXModifyReader().removeHandler(path);
    }

    public void resetModifiers() {
        this.modifiers.clear();
        getSAXModifyReader().resetHandlers();
    }

    public void setDocumentFactory(DocumentFactory factory) {
        getSAXModifyReader().setDocumentFactory(factory);
    }

    public void setXMLWriter(XMLWriter writer) {
        this.xmlWriter = writer;
    }
}