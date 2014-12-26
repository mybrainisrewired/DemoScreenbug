package org.codehaus.jackson.map;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;

@Deprecated
public class TreeMapper extends JsonNodeFactory {
    protected ObjectMapper _objectMapper;

    public TreeMapper() {
        this(null);
    }

    public TreeMapper(ObjectMapper m) {
        this._objectMapper = m;
    }

    public JsonFactory getJsonFactory() {
        return objectMapper().getJsonFactory();
    }

    protected synchronized ObjectMapper objectMapper() {
        if (this._objectMapper == null) {
            this._objectMapper = new ObjectMapper();
        }
        return this._objectMapper;
    }

    public JsonNode readTree(File src) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(InputStream src) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader src) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String jsonContent) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(jsonContent, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(URL src) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonParseException {
        return (jp.getCurrentToken() == null && jp.nextToken() == null) ? null : objectMapper().readTree(jp);
    }

    public JsonNode readTree(byte[] jsonContent) throws IOException, JsonParseException {
        JsonNode n = (JsonNode) objectMapper().readValue(jsonContent, 0, jsonContent.length, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public void writeTree(Object rootNode, File dst) throws IOException, JsonParseException {
        objectMapper().writeValue(dst, rootNode);
    }

    public void writeTree(Object rootNode, OutputStream dst) throws IOException, JsonParseException {
        objectMapper().writeValue(dst, rootNode);
    }

    public void writeTree(Object rootNode, Writer dst) throws IOException, JsonParseException {
        objectMapper().writeValue(dst, rootNode);
    }
}