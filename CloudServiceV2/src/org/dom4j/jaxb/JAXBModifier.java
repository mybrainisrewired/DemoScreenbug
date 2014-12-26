package org.dom4j.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.ElementModifier;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXModifier;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

public class JAXBModifier extends JAXBSupport {
    private SAXModifier modifier;
    private HashMap modifiers;
    private OutputFormat outputFormat;
    private boolean pruneElements;
    private XMLWriter xmlWriter;

    private class JAXBElementModifier implements ElementModifier {
        private JAXBModifier jaxbModifier;
        private JAXBObjectModifier objectModifier;

        public JAXBElementModifier(JAXBModifier jaxbModifier, JAXBObjectModifier objectModifier) {
            this.jaxbModifier = jaxbModifier;
            this.objectModifier = objectModifier;
        }

        public Element modifyElement(Element element) throws Exception {
            return this.jaxbModifier.marshal(this.objectModifier.modifyObject(this.jaxbModifier.unmarshal(element)));
        }
    }

    public JAXBModifier(String contextPath) {
        super(contextPath);
        this.modifiers = new HashMap();
        this.outputFormat = new OutputFormat();
    }

    public JAXBModifier(String contextPath, ClassLoader classloader) {
        super(contextPath, classloader);
        this.modifiers = new HashMap();
        this.outputFormat = new OutputFormat();
    }

    public JAXBModifier(String contextPath, ClassLoader classloader, OutputFormat outputFormat) {
        super(contextPath, classloader);
        this.modifiers = new HashMap();
        this.outputFormat = outputFormat;
    }

    public JAXBModifier(String contextPath, OutputFormat outputFormat) {
        super(contextPath);
        this.modifiers = new HashMap();
        this.outputFormat = outputFormat;
    }

    private XMLWriter createXMLWriter() throws IOException {
        if (this.xmlWriter == null) {
            this.xmlWriter = new XMLWriter(this.outputFormat);
        }
        return this.xmlWriter;
    }

    private SAXModifier getModifier() {
        if (this.modifier == null) {
            this.modifier = new SAXModifier(isPruneElements());
        }
        return this.modifier;
    }

    private XMLWriter getXMLWriter() {
        return this.xmlWriter;
    }

    private SAXModifier installModifier() throws IOException {
        this.modifier = new SAXModifier(isPruneElements());
        this.modifier.resetModifiers();
        Iterator modifierIt = this.modifiers.entrySet().iterator();
        while (modifierIt.hasNext()) {
            Entry entry = (Entry) modifierIt.next();
            getModifier().addModifier((String) entry.getKey(), new JAXBElementModifier(this, (JAXBObjectModifier) entry.getValue()));
        }
        this.modifier.setXMLWriter(getXMLWriter());
        return this.modifier;
    }

    public void addObjectModifier(String path, JAXBObjectModifier mod) {
        this.modifiers.put(path, mod);
    }

    public boolean isPruneElements() {
        return this.pruneElements;
    }

    public Document modify(File source) throws DocumentException, IOException {
        return installModifier().modify(source);
    }

    public Document modify(File source, Charset charset) throws DocumentException, IOException {
        try {
            return installModifier().modify(new InputStreamReader(new FileInputStream(source), charset));
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        } catch (FileNotFoundException e2) {
            FileNotFoundException ex = e2;
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    public Document modify(InputStream source) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(InputStream source, String systemId) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(Reader r) throws DocumentException, IOException {
        try {
            return installModifier().modify(r);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(Reader source, String systemId) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(String url) throws DocumentException, IOException {
        try {
            return installModifier().modify(url);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(URL source) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document modify(InputSource source) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public void removeObjectModifier(String path) {
        this.modifiers.remove(path);
        getModifier().removeModifier(path);
    }

    public void resetObjectModifiers() {
        this.modifiers.clear();
        getModifier().resetModifiers();
    }

    public void setOutput(File file) throws IOException {
        createXMLWriter().setOutputStream(new FileOutputStream(file));
    }

    public void setOutput(OutputStream outputStream) throws IOException {
        createXMLWriter().setOutputStream(outputStream);
    }

    public void setOutput(Writer writer) throws IOException {
        createXMLWriter().setWriter(writer);
    }

    public void setPruneElements(boolean pruneElements) {
        this.pruneElements = pruneElements;
    }
}