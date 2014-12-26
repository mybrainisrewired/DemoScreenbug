package org.codehaus.jackson.xc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdScalarDeserializer;

public class DataHandlerJsonDeserializer extends StdScalarDeserializer<DataHandler> {

    class AnonymousClass_1 implements DataSource {
        final /* synthetic */ byte[] val$value;

        AnonymousClass_1(byte[] bArr) {
            this.val$value = bArr;
        }

        public String getContentType() {
            return "application/octet-stream";
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.val$value);
        }

        public String getName() {
            return "json-binary-data";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException();
        }
    }

    public DataHandlerJsonDeserializer() {
        super(DataHandler.class);
    }

    public DataHandler deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new DataHandler(new AnonymousClass_1(jp.getBinaryValue()));
    }
}