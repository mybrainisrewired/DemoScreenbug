package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.xpp.ProxyXmlStartTag;
import org.gjt.xpp.XmlEndTag;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlPullParserFactory;

public class XPPReader {
    private DispatchHandler dispatchHandler;
    private DocumentFactory factory;
    private XmlPullParserFactory xppFactory;
    private XmlPullParser xppParser;

    public XPPReader(DocumentFactory factory) {
        this.factory = factory;
    }

    public void addHandler(String path, ElementHandler handler) {
        getDispatchHandler().addHandler(path, handler);
    }

    protected Reader createReader(InputStream in) throws IOException {
        return new BufferedReader(new InputStreamReader(in));
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

    public XmlPullParserFactory getXPPFactory() throws XmlPullParserException {
        if (this.xppFactory == null) {
            this.xppFactory = XmlPullParserFactory.newInstance();
        }
        return this.xppFactory;
    }

    public XmlPullParser getXPPParser() throws XmlPullParserException {
        if (this.xppParser == null) {
            this.xppParser = getXPPFactory().newPullParser();
        }
        return this.xppParser;
    }

    protected Document parseDocument() throws DocumentException, IOException, XmlPullParserException {
        Document document = getDocumentFactory().createDocument();
        Element parent = null;
        XmlPullParser parser = getXPPParser();
        parser.setNamespaceAware(true);
        ProxyXmlStartTag startTag = new ProxyXmlStartTag();
        XmlEndTag endTag = this.xppFactory.newEndTag();
        while (true) {
            int type = parser.next();
            switch (type) {
                case LocalAudioAll.SORT_BY_DATE:
                    return document;
                case ClassWriter.COMPUTE_FRAMES:
                    parser.readStartTag(startTag);
                    Element newElement = startTag.getElement();
                    if (parent != null) {
                        parent.add(newElement);
                    } else {
                        document.add(newElement);
                    }
                    parent = newElement;
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    parser.readEndTag(endTag);
                    if (parent != null) {
                        parent = parent.getParent();
                    }
                    break;
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    String text = parser.readContent();
                    if (parent != null) {
                        parent.addText(text);
                    } else {
                        throw new DocumentException("Cannot have text content outside of the root document");
                    }
                default:
                    throw new DocumentException("Error: unknown type: " + type);
            }
        }
    }

    public Document read(File file) throws DocumentException, IOException, XmlPullParserException {
        return read(new BufferedReader(new FileReader(file)), file.getAbsolutePath());
    }

    public Document read(InputStream in) throws DocumentException, IOException, XmlPullParserException {
        return read(createReader(in));
    }

    public Document read(InputStream in, String systemID) throws DocumentException, IOException, XmlPullParserException {
        return read(createReader(in), systemID);
    }

    public Document read(Reader reader) throws DocumentException, IOException, XmlPullParserException {
        getXPPParser().setInput(reader);
        return parseDocument();
    }

    public Document read(Reader reader, String systemID) throws DocumentException, IOException, XmlPullParserException {
        Document document = read(reader);
        document.setName(systemID);
        return document;
    }

    public Document read(String systemID) throws DocumentException, IOException, XmlPullParserException {
        return systemID.indexOf(Opcodes.ASTORE) >= 0 ? read(new URL(systemID)) : read(new File(systemID));
    }

    public Document read(URL url) throws DocumentException, IOException, XmlPullParserException {
        return read(createReader(url.openStream()), url.toExternalForm());
    }

    public Document read(char[] text) throws DocumentException, IOException, XmlPullParserException {
        getXPPParser().setInput(text);
        return parseDocument();
    }

    public void removeHandler(String path) {
        getDispatchHandler().removeHandler(path);
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

    public void setXPPFactory(XmlPullParserFactory xPPFactory) {
        this.xppFactory = xPPFactory;
    }
}