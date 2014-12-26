package org.codehaus.jackson.map;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectReader extends ObjectCodec implements Versioned {
    private static final JavaType JSON_NODE_TYPE;
    protected final DeserializationConfig _config;
    protected final JsonFactory _jsonFactory;
    protected final DeserializerProvider _provider;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final FormatSchema _schema;
    protected final Object _valueToUpdate;
    protected final JavaType _valueType;

    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config) {
        this(mapper, config, null, null, null);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema) {
        this._config = config;
        this._rootDeserializers = mapper._rootDeserializers;
        this._provider = mapper._deserializerProvider;
        this._jsonFactory = mapper._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
        } else {
            throw new IllegalArgumentException("Can not update an array value");
        }
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema) {
        this._config = config;
        this._rootDeserializers = base._rootDeserializers;
        this._provider = base._provider;
        this._jsonFactory = base._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
        } else {
            throw new IllegalArgumentException("Can not update an array value");
        }
    }

    protected static JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            t = jp.nextToken();
            if (t == null) {
                throw new EOFException("No content to map to Object due to end of input");
            }
        }
        return t;
    }

    protected Object _bind(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        Object result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = this._valueToUpdate;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            if (this._valueToUpdate == null) {
                result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
            } else {
                _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                result = this._valueToUpdate;
            }
        }
        jp.clearCurrentToken();
        return result;
    }

    protected Object _bindAndClose(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            Object result;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                result = this._valueToUpdate;
            } else {
                DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
                if (this._valueToUpdate == null) {
                    result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
                } else {
                    _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
            }
            try {
                jp.close();
            } catch (IOException e) {
            }
            return result;
        } catch (Throwable th) {
            try {
                jp.close();
            } catch (IOException e2) {
            }
        }
    }

    protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            JsonNode _bindAsTree = _bindAsTree(jp);
            try {
                jp.close();
            } catch (IOException e) {
            }
            return _bindAsTree;
        } catch (Throwable th) {
            try {
                jp.close();
            } catch (IOException e2) {
            }
        }
    }

    protected JsonNode _bindAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonNode result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = NullNode.instance;
        } else {
            result = _findRootDeserializer(this._config, JSON_NODE_TYPE).deserialize(jp, _createDeserializationContext(jp, this._config));
        }
        jp.clearCurrentToken();
        return result;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._provider);
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = this._provider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    public JsonNode createArrayNode() {
        return this._config.getNodeFactory().arrayNode();
    }

    public JsonNode createObjectNode() {
        return this._config.getNodeFactory().objectNode();
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(in));
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(r));
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(content));
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return _bindAsTree(jp);
    }

    public <T> T readValue(File src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(InputStream src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(Reader src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(String src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(URL src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(JsonNode src) throws IOException, JsonProcessingException {
        return _bindAndClose(treeAsTokens(src));
    }

    public <T> T readValue(JsonParser jp) throws IOException, JsonProcessingException {
        return _bind(jp);
    }

    public <T> T readValue(JsonParser jp, Class valueType) throws IOException, JsonProcessingException {
        return withType(valueType).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        return withType(valueType).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, TypeReference valueTypeRef) throws IOException, JsonProcessingException {
        return withType(valueTypeRef).readValue(jp);
    }

    public <T> T readValue(byte[] src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src, offset, length));
    }

    public <T> MappingIterator<T> readValues(File src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(InputStream src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(Reader src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(String json) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(json);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(URL src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp) throws IOException, JsonProcessingException {
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src, offset, length);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType));
    }

    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    public <T> T treeToValue(JsonNode n, Class valueType) throws IOException, JsonProcessingException {
        return readValue(treeAsTokens(n), valueType);
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectReader withNodeFactory(JsonNodeFactory f) {
        return f == this._config.getNodeFactory() ? this : new ObjectReader(this, this._config.withNodeFactory(f), this._valueType, this._valueToUpdate, this._schema);
    }

    public ObjectReader withSchema(FormatSchema schema) {
        return this._schema == schema ? this : new ObjectReader(this, this._config, this._valueType, this._valueToUpdate, schema);
    }

    public ObjectReader withType(Class<?> valueType) {
        return withType(this._config.constructType(valueType));
    }

    public ObjectReader withType(Type valueType) {
        return withType(this._config.getTypeFactory().constructType(valueType));
    }

    public ObjectReader withType(JavaType valueType) {
        if (valueType == this._valueType) {
            return this;
        }
        return new ObjectReader(this, this._config, valueType, this._valueToUpdate, this._schema);
    }

    public ObjectReader withType(TypeReference<?> valueTypeRef) {
        return withType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
    }

    public ObjectReader withValueToUpdate(Object value) {
        if (value == this._valueToUpdate) {
            return this;
        }
        if (value == null) {
            throw new IllegalArgumentException("cat not update null value");
        }
        return new ObjectReader(this, this._config, this._config.constructType(value.getClass()), value, this._schema);
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }

    public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }
}