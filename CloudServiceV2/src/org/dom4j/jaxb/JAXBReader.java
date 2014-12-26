package org.dom4j.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class JAXBReader extends JAXBSupport {
    private boolean pruneElements;
    private SAXReader reader;

    private class PruningElementHandler implements ElementHandler {
        public void onEnd(ElementPath elementPath) {
            elementPath.getCurrent().detach();
        }

        public void onStart(ElementPath parm1) {
        }
    }

    private class UnmarshalElementHandler implements ElementHandler {
        private JAXBObjectHandler handler;
        private JAXBReader jaxbReader;

        public UnmarshalElementHandler(JAXBReader documentReader, JAXBObjectHandler handler) {
            this.jaxbReader = documentReader;
            this.handler = handler;
        }

        public void onEnd(ElementPath elementPath) {
            try {
                Element elem = elementPath.getCurrent();
                javax.xml.bind.Element jaxbObject = this.jaxbReader.unmarshal(elem);
                if (this.jaxbReader.isPruneElements()) {
                    elem.detach();
                }
                this.handler.handleObject(jaxbObject);
            } catch (Exception e) {
                throw new JAXBRuntimeException(e);
            }
        }

        public void onStart(ElementPath elementPath) {
        }
    }

    public JAXBReader(String contextPath) {
        super(contextPath);
    }

    public JAXBReader(String contextPath, ClassLoader classloader) {
        super(contextPath, classloader);
    }

    private SAXReader getReader() {
        if (this.reader == null) {
            this.reader = new SAXReader();
        }
        return this.reader;
    }

    public void addHandler(String path, ElementHandler handler) {
        getReader().addHandler(path, handler);
    }

    public void addObjectHandler(String path, JAXBObjectHandler handler) {
        getReader().addHandler(path, new UnmarshalElementHandler(this, handler));
    }

    public boolean isPruneElements() {
        return this.pruneElements;
    }

    public Document read(File source) throws DocumentException {
        return getReader().read(source);
    }

    public Document read(File file, Charset charset) throws DocumentException {
        try {
            return getReader().read(new InputStreamReader(new FileInputStream(file), charset));
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        } catch (FileNotFoundException e2) {
            FileNotFoundException ex = e2;
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    public Document read(InputStream source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(InputStream source, String systemId) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(Reader source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(Reader source, String systemId) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(String source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(URL source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public Document read(InputSource source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException e) {
            Throwable cause = e.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    public void removeHandler(String path) {
        getReader().removeHandler(path);
    }

    public void removeObjectHandler(String path) {
        getReader().removeHandler(path);
    }

    public void resetHandlers() {
        getReader().resetHandlers();
    }

    public void setPruneElements(boolean pruneElements) {
        this.pruneElements = pruneElements;
        if (pruneElements) {
            getReader().setDefaultHandler(new PruningElementHandler());
        }
    }
}