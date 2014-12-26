package org.dom4j.io;

import com.wmt.data.LocalAudioAll;
import java.io.BufferedReader;
import java.io.CharArrayReader;
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
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.QName;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XPP3Reader {
    private DispatchHandler dispatchHandler;
    private DocumentFactory factory;
    private XmlPullParserFactory xppFactory;
    private XmlPullParser xppParser;

    public XPP3Reader(DocumentFactory factory) {
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
        this.xppFactory.setNamespaceAware(true);
        return this.xppFactory;
    }

    public XmlPullParser getXPPParser() throws XmlPullParserException {
        if (this.xppParser == null) {
            this.xppParser = getXPPFactory().newPullParser();
        }
        return this.xppParser;
    }

    protected Document parseDocument() throws DocumentException, IOException, XmlPullParserException {
        DocumentFactory df = getDocumentFactory();
        Document document = df.createDocument();
        Element parent = null;
        XmlPullParser pp = getXPPParser();
        pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        while (true) {
            String text;
            String msg;
            DocumentException documentException;
            switch (pp.nextToken()) {
                case LocalAudioAll.SORT_BY_DATE:
                    return document;
                case ClassWriter.COMPUTE_FRAMES:
                    QName qname;
                    if (pp.getPrefix() == null) {
                        qname = df.createQName(pp.getName(), pp.getNamespace());
                    } else {
                        qname = df.createQName(pp.getName(), pp.getPrefix(), pp.getNamespace());
                    }
                    Element newElement = df.createElement(qname);
                    int nsStart = pp.getNamespaceCount(pp.getDepth() - 1);
                    int nsEnd = pp.getNamespaceCount(pp.getDepth());
                    int i = nsStart;
                    while (i < nsEnd) {
                        if (pp.getNamespacePrefix(i) != null) {
                            newElement.addNamespace(pp.getNamespacePrefix(i), pp.getNamespaceUri(i));
                        }
                        i++;
                    }
                    i = 0;
                    while (i < pp.getAttributeCount()) {
                        QName qa;
                        if (pp.getAttributePrefix(i) == null) {
                            qa = df.createQName(pp.getAttributeName(i));
                        } else {
                            qa = df.createQName(pp.getAttributeName(i), pp.getAttributePrefix(i), pp.getAttributeNamespace(i));
                        }
                        newElement.addAttribute(qa, pp.getAttributeValue(i));
                        i++;
                    }
                    if (parent != null) {
                        parent.add(newElement);
                    } else {
                        document.add(newElement);
                    }
                    parent = newElement;
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    if (parent != null) {
                        parent = parent.getParent();
                    }
                    break;
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    text = pp.getText();
                    if (parent != null) {
                        parent.addText(text);
                    } else {
                        msg = "Cannot have text content outside of the root document";
                        documentException = documentException;
                        throw documentException;
                    }
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    if (parent != null) {
                        parent.addCDATA(pp.getText());
                    } else {
                        msg = "Cannot have text content outside of the root document";
                        documentException = documentException;
                        throw documentException;
                    }
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    break;
                case Type.DOUBLE:
                    text = pp.getText();
                    int loc = text.indexOf(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    if (loc >= 0) {
                        document.addProcessingInstruction(text.substring(0, loc), text.substring(loc + 1));
                    } else {
                        document.addProcessingInstruction(text, "");
                    }
                    break;
                case Type.ARRAY:
                    if (parent != null) {
                        parent.addComment(pp.getText());
                    } else {
                        document.addComment(pp.getText());
                    }
                    break;
                default:
                    break;
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
        getXPPParser().setInput(new CharArrayReader(text));
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

    public void setXPPFactory(XmlPullParserFactory xPPfactory) {
        this.xppFactory = xPPfactory;
    }
}