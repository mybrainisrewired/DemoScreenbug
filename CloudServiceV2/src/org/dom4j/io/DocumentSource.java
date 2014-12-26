package org.dom4j.io;

import javax.xml.transform.sax.SAXSource;
import org.dom4j.Document;
import org.dom4j.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

public class DocumentSource extends SAXSource {
    public static final String DOM4J_FEATURE = "http://org.dom4j.io.DoucmentSource/feature";
    private XMLReader xmlReader;

    public DocumentSource(Document document) {
        this.xmlReader = new SAXWriter();
        setDocument(document);
    }

    public DocumentSource(Node node) {
        this.xmlReader = new SAXWriter();
        setDocument(node.getDocument());
    }

    public Document getDocument() {
        return ((DocumentInputSource) getInputSource()).getDocument();
    }

    public XMLReader getXMLReader() {
        return this.xmlReader;
    }

    public void setDocument(Document document) {
        super.setInputSource(new DocumentInputSource(document));
    }

    public void setInputSource(InputSource inputSource) throws UnsupportedOperationException {
        if (inputSource instanceof DocumentInputSource) {
            super.setInputSource((DocumentInputSource) inputSource);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setXMLReader(XMLReader reader) throws UnsupportedOperationException {
        if (reader instanceof SAXWriter) {
            this.xmlReader = (SAXWriter) reader;
        } else if (reader instanceof XMLFilter) {
            XMLFilter filter = (XMLFilter) reader;
            while (true) {
                XMLReader parent = filter.getParent();
                if (parent instanceof XMLFilter) {
                    filter = (XMLFilter) parent;
                } else {
                    filter.setParent(this.xmlReader);
                    this.xmlReader = filter;
                    return;
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}