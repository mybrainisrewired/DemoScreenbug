package org.dom4j.io;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;

class JAXPHelper {
    protected JAXPHelper() {
    }

    public static Document createDocument(boolean validating, boolean namespaceAware) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);
        return factory.newDocumentBuilder().newDocument();
    }

    public static XMLReader createXMLReader(boolean validating, boolean namespaceAware) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);
        return factory.newSAXParser().getXMLReader();
    }
}