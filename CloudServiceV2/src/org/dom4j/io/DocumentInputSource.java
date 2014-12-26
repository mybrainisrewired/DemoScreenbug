package org.dom4j.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.dom4j.Document;
import org.xml.sax.InputSource;

class DocumentInputSource extends InputSource {
    private Document document;

    class AnonymousClass_1 extends Reader {
        final /* synthetic */ IOException val$e;

        AnonymousClass_1(IOException iOException) {
            this.val$e = iOException;
        }

        public void close() throws IOException {
        }

        public int read(char[] ch, int offset, int length) throws IOException {
            throw this.val$e;
        }
    }

    public DocumentInputSource(Document document) {
        this.document = document;
        setSystemId(document.getName());
    }

    public Reader getCharacterStream() {
        try {
            Writer out = new StringWriter();
            XMLWriter writer = new XMLWriter(out);
            writer.write(this.document);
            writer.flush();
            return new StringReader(out.toString());
        } catch (IOException e) {
            return new AnonymousClass_1(e);
        }
    }

    public Document getDocument() {
        return this.document;
    }

    public void setCharacterStream(Reader characterStream) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setDocument(Document document) {
        this.document = document;
        setSystemId(document.getName());
    }
}