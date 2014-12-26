package org.codehaus.jackson.map;

import com.wmt.data.LocalAudioAll;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.Module.SetupContext;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.type.TypeModifier;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.TokenBuffer;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectMapper extends ObjectCodec implements Versioned {
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR;
    protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR;
    private static final JavaType JSON_NODE_TYPE;
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER;
    protected DeserializationConfig _deserializationConfig;
    protected DeserializerProvider _deserializerProvider;
    protected final JsonFactory _jsonFactory;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected SerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected TypeFactory _typeFactory;

    static /* synthetic */ class AnonymousClass_2 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping;

        static {
            $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping = new int[org.codehaus.jackson.map.ObjectMapper.DefaultTyping.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[org.codehaus.jackson.map.ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[org.codehaus.jackson.map.ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[org.codehaus.jackson.map.ObjectMapper.DefaultTyping.NON_FINAL.ordinal()] = 3;
        }
    }

    public enum DefaultTyping {
        JAVA_LANG_OBJECT,
        OBJECT_AND_NON_CONCRETE,
        NON_CONCRETE_AND_ARRAYS,
        NON_FINAL
    }

    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder {
        protected final org.codehaus.jackson.map.ObjectMapper.DefaultTyping _appliesFor;

        public DefaultTypeResolverBuilder(org.codehaus.jackson.map.ObjectMapper.DefaultTyping t) {
            this._appliesFor = t;
        }

        public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes, property) : null;
        }

        public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes, property) : null;
        }

        public boolean useForType(JavaType t) {
            boolean z = 0;
            switch (AnonymousClass_2.$SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    if (t.getRawClass() == Object.class || !t.isConcrete()) {
                        z = true;
                    }
                    return z;
                case ClassWriter.COMPUTE_FRAMES:
                    z = true;
                    return z;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    return !t.isFinal();
                default:
                    return t.getRawClass() == Object.class;
            }
        }
    }

    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
        DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
        DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
        STD_VISIBILITY_CHECKER = Std.defaultInstance();
    }

    public ObjectMapper() {
        this(null, null, null);
    }

    public ObjectMapper(JsonFactory jf) {
        this(jf, null, null);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        this(jf, sp, dp, null, null);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig) {
        this._rootDeserializers = new ConcurrentHashMap(64, 0.6f, 2);
        if (jf == null) {
            jf = new MappingJsonFactory(this);
        }
        this._jsonFactory = jf;
        this._typeFactory = TypeFactory.defaultInstance();
        if (sconfig == null) {
            sconfig = new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
        }
        this._serializationConfig = sconfig;
        if (dconfig == null) {
            dconfig = new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
        }
        this._deserializationConfig = dconfig;
        if (sp == null) {
            sp = new StdSerializerProvider();
        }
        this._serializerProvider = sp;
        if (dp == null) {
            dp = new StdDeserializerProvider();
        }
        this._deserializerProvider = dp;
        this._serializerFactory = BeanSerializerFactory.instance;
    }

    @Deprecated
    public ObjectMapper(SerializerFactory sf) {
        this(null, null, null);
        setSerializerFactory(sf);
    }

    private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            jgen.close();
            toClose.close();
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e) {
                }
            }
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e3) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            if (cfg.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
            toClose.close();
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig();
        if (cfg.isEnabled(Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(Feature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
            _configAndWriteCloseable(jgen, value, cfg);
        } else {
            try {
                this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
                jgen.close();
                if (!true) {
                    try {
                        jgen.close();
                    } catch (IOException e) {
                    }
                }
            } catch (Throwable th) {
                if (!false) {
                    try {
                        jgen.close();
                    } catch (IOException e2) {
                    }
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig().withView(viewClass);
        if (cfg.isEnabled(Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(Feature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
            _configAndWriteCloseable(jgen, value, cfg);
        } else {
            try {
                this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
                jgen.close();
                if (!true) {
                    try {
                        jgen.close();
                    } catch (IOException e) {
                    }
                }
            } catch (Throwable th) {
                if (!false) {
                    try {
                        jgen.close();
                    } catch (IOException e2) {
                    }
                }
            }
        }
    }

    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            Object result = readValue(jp, toValueType);
            jp.close();
            return result;
        } catch (IOException e) {
            IOException e2 = e;
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._deserializerProvider);
    }

    protected PrettyPrinter _defaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = this._deserializerProvider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            t = jp.nextToken();
            if (t == null) {
                throw new EOFException("No content to map to Object due to end of input");
            }
        }
        return t;
    }

    protected Object _readMapAndClose(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        try {
            Object obj;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                obj = null;
            } else {
                DeserializationConfig cfg = copyDeserializationConfig();
                obj = _findRootDeserializer(cfg, valueType).deserialize(jp, _createDeserializationContext(jp, cfg));
            }
            jp.clearCurrentToken();
            try {
                jp.close();
            } catch (IOException e) {
            }
            return obj;
        } catch (Throwable th) {
            try {
                jp.close();
            } catch (IOException e2) {
            }
        }
    }

    protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        Object obj;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            obj = null;
        } else {
            obj = _findRootDeserializer(cfg, valueType).deserialize(jp, _createDeserializationContext(jp, cfg));
        }
        jp.clearCurrentToken();
        return obj;
    }

    public boolean canDeserialize(JavaType type) {
        return this._deserializerProvider.hasValueDeserializerFor(copyDeserializationConfig(), type);
    }

    public boolean canSerialize(Class<?> type) {
        return this._serializerProvider.hasSerializerFor(copySerializationConfig(), type, this._serializerFactory);
    }

    public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper configure(JsonParser.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper configure(DeserializationConfig.Feature f, boolean state) {
        this._deserializationConfig.set(f, state);
        return this;
    }

    public ObjectMapper configure(Feature f, boolean state) {
        this._serializationConfig.set(f, state);
        return this;
    }

    public JavaType constructType(Type t) {
        return this._typeFactory.constructType(t);
    }

    public <T> T convertValue(Object fromValue, Type toValueType) throws IllegalArgumentException {
        return _convert(fromValue, this._typeFactory.constructType(toValueType));
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        return _convert(fromValue, toValueType);
    }

    public <T> T convertValue(Object fromValue, TypeReference toValueTypeRef) throws IllegalArgumentException {
        return _convert(fromValue, this._typeFactory.constructType(toValueTypeRef));
    }

    public DeserializationConfig copyDeserializationConfig() {
        return this._deserializationConfig.createUnshared(this._subtypeResolver);
    }

    public SerializationConfig copySerializationConfig() {
        return this._serializationConfig.createUnshared(this._subtypeResolver);
    }

    public ArrayNode createArrayNode() {
        return this._deserializationConfig.getNodeFactory().arrayNode();
    }

    public ObjectNode createObjectNode() {
        return this._deserializationConfig.getNodeFactory().objectNode();
    }

    public ObjectWriter defaultPrettyPrintingWriter() {
        return new ObjectWriter(this, copySerializationConfig(), null, _defaultPrettyPrinter());
    }

    public ObjectMapper disableDefaultTyping() {
        return setDefaultTyping(null);
    }

    public ObjectMapper enableDefaultTyping() {
        return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return enableDefaultTyping(dti, As.WRAPPER_ARRAY);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(includeAs));
    }

    public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(As.PROPERTY).typeProperty(propertyName));
    }

    public ObjectWriter filteredWriter(FilterProvider filterProvider) {
        return new ObjectWriter(this, copySerializationConfig().withFilters(filterProvider));
    }

    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return generateJsonSchema(t, copySerializationConfig());
    }

    public JsonSchema generateJsonSchema(Class<?> t, SerializationConfig cfg) throws JsonMappingException {
        return this._serializerProvider.generateJsonSchema(t, cfg, this._serializerFactory);
    }

    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }

    public DeserializerProvider getDeserializerProvider() {
        return this._deserializerProvider;
    }

    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }

    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }

    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }

    public SerializerProvider getSerializerProvider() {
        return this._serializerProvider;
    }

    public SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public TypeFactory getTypeFactory() {
        return this._typeFactory;
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this._serializationConfig.getDefaultVisibilityChecker();
    }

    public ObjectWriter prettyPrintingWriter(PrettyPrinter pp) {
        if (pp == null) {
            pp = ObjectWriter.NULL_PRETTY_PRINTER;
        }
        return new ObjectWriter(this, copySerializationConfig(), null, pp);
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(in, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(r, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(content, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return readTree(jp, copyDeserializationConfig());
    }

    public JsonNode readTree(JsonParser jp, DeserializationConfig cfg) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public <T> T readValue(File src, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(File src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(InputStream src, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(InputStream src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(Reader src, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(Reader src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(Reader src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(String content, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(String content, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), valueType);
    }

    public <T> T readValue(String content, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(URL src, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(URL src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(JsonNode root, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(JsonNode root, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), valueType);
    }

    public <T> T readValue(JsonNode root, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(JsonParser jp, Type valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, valueType);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, valueType);
    }

    public <T> T readValue(JsonParser jp, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, TypeReference valueTypeRef, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, int offset, int len, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), valueType);
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, Type valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueType));
    }

    public <T> T readValue(byte[] src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(byte[] src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, Type valueType) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType(valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        DeserializationConfig config = copyDeserializationConfig();
        return new MappingIterator(valueType, jp, _createDeserializationContext(jp, config), _findRootDeserializer(config, valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, TypeReference valueTypeRef) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType(valueTypeRef));
    }

    public ObjectReader reader() {
        return new ObjectReader(this, copyDeserializationConfig());
    }

    public ObjectReader reader(Type type) {
        return reader(this._typeFactory.constructType(type));
    }

    public ObjectReader reader(JsonNodeFactory f) {
        return new ObjectReader(this, copyDeserializationConfig()).withNodeFactory(f);
    }

    public ObjectReader reader(JavaType type) {
        return new ObjectReader(this, copyDeserializationConfig(), type, null, null);
    }

    public ObjectReader reader(TypeReference type) {
        return reader(this._typeFactory.constructType(type));
    }

    public void registerModule(Module module) {
        if (module.getModuleName() == null) {
            throw new IllegalArgumentException("Module without defined name");
        } else if (module.version() == null) {
            throw new IllegalArgumentException("Module without defined version");
        } else {
            module.setupModule(new SetupContext() {
                public void addAbstractTypeResolver(AbstractTypeResolver resolver) {
                    ObjectMapper.this._deserializerProvider = ObjectMapper.this._deserializerProvider.withAbstractTypeResolver(resolver);
                }

                public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
                    ObjectMapper.this._deserializerProvider = ObjectMapper.this._deserializerProvider.withDeserializerModifier(modifier);
                }

                public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
                    ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withSerializerModifier(modifier);
                }

                public void addDeserializers(Deserializers d) {
                    ObjectMapper.this._deserializerProvider = ObjectMapper.this._deserializerProvider.withAdditionalDeserializers(d);
                }

                public void addKeyDeserializers(KeyDeserializers d) {
                    ObjectMapper.this._deserializerProvider = ObjectMapper.this._deserializerProvider.withAdditionalKeyDeserializers(d);
                }

                public void addKeySerializers(Serializers s) {
                    ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withAdditionalKeySerializers(s);
                }

                public void addSerializers(Serializers s) {
                    ObjectMapper.this._serializerFactory = ObjectMapper.this._serializerFactory.withAdditionalSerializers(s);
                }

                public void addTypeModifier(TypeModifier modifier) {
                    ObjectMapper.this.setTypeFactory(ObjectMapper.this._typeFactory.withModifier(modifier));
                }

                public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
                    ObjectMapper.this._deserializationConfig.appendAnnotationIntrospector(ai);
                    ObjectMapper.this._serializationConfig.appendAnnotationIntrospector(ai);
                }

                public DeserializationConfig getDeserializationConfig() {
                    return ObjectMapper.this.getDeserializationConfig();
                }

                public Version getMapperVersion() {
                    return ObjectMapper.this.version();
                }

                public SerializationConfig getSerializationConfig() {
                    return ObjectMapper.this.getSerializationConfig();
                }

                public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
                    ObjectMapper.this._deserializationConfig.insertAnnotationIntrospector(ai);
                    ObjectMapper.this._serializationConfig.insertAnnotationIntrospector(ai);
                }

                public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
                    ObjectMapper.this._deserializationConfig.addMixInAnnotations(target, mixinSource);
                    ObjectMapper.this._serializationConfig.addMixInAnnotations(target, mixinSource);
                }
            });
        }
    }

    public void registerSubtypes(Class... classes) {
        getSubtypeResolver().registerSubtypes(classes);
    }

    public void registerSubtypes(NamedType... types) {
        getSubtypeResolver().registerSubtypes(types);
    }

    public ObjectReader schemaBasedReader(FormatSchema schema) {
        return new ObjectReader(this, copyDeserializationConfig(), null, null, schema);
    }

    public ObjectWriter schemaBasedWriter(FormatSchema schema) {
        return new ObjectWriter(this, copySerializationConfig(), schema);
    }

    public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._serializationConfig = this._serializationConfig.withAnnotationIntrospector(ai);
        this._deserializationConfig = this._deserializationConfig.withAnnotationIntrospector(ai);
        return this;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this._deserializationConfig = this._deserializationConfig.withDateFormat(dateFormat);
        this._serializationConfig = this._serializationConfig.withDateFormat(dateFormat);
    }

    public ObjectMapper setDefaultTyping(TypeResolverBuilder typer) {
        this._deserializationConfig = this._deserializationConfig.withTypeResolverBuilder(typer);
        this._serializationConfig = this._serializationConfig.withTypeResolverBuilder(typer);
        return this;
    }

    public ObjectMapper setDeserializationConfig(DeserializationConfig cfg) {
        this._deserializationConfig = cfg;
        return this;
    }

    public ObjectMapper setDeserializerProvider(DeserializerProvider p) {
        this._deserializerProvider = p;
        return this;
    }

    public void setFilters(FilterProvider filterProvider) {
        this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
    }

    public void setHandlerInstantiator(HandlerInstantiator hi) {
        this._deserializationConfig = this._deserializationConfig.withHandlerInstantiator(hi);
        this._serializationConfig = this._serializationConfig.withHandlerInstantiator(hi);
    }

    public ObjectMapper setNodeFactory(JsonNodeFactory f) {
        this._deserializationConfig = this._deserializationConfig.withNodeFactory(f);
        return this;
    }

    public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        this._serializationConfig = this._serializationConfig.withPropertyNamingStrategy(s);
        this._deserializationConfig = this._deserializationConfig.withPropertyNamingStrategy(s);
        return this;
    }

    public ObjectMapper setSerializationConfig(SerializationConfig cfg) {
        this._serializationConfig = cfg;
        return this;
    }

    public ObjectMapper setSerializerFactory(SerializerFactory f) {
        this._serializerFactory = f;
        return this;
    }

    public ObjectMapper setSerializerProvider(SerializerProvider p) {
        this._serializerProvider = p;
        return this;
    }

    public void setSubtypeResolver(SubtypeResolver r) {
        this._subtypeResolver = r;
    }

    public ObjectMapper setTypeFactory(TypeFactory f) {
        this._typeFactory = f;
        this._deserializationConfig = this._deserializationConfig.withTypeFactory(f);
        this._serializationConfig = this._serializationConfig.withTypeFactory(f);
        return this;
    }

    public void setVisibilityChecker(VisibilityChecker vc) {
        this._deserializationConfig = this._deserializationConfig.withVisibilityChecker(vc);
        this._serializationConfig = this._serializationConfig.withVisibilityChecker(vc);
    }

    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    public <T> T treeToValue(JsonNode n, Class valueType) throws IOException, JsonParseException, JsonMappingException {
        return readValue(treeAsTokens(n), valueType);
    }

    public ObjectWriter typedWriter(Type rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
    }

    public ObjectWriter typedWriter(JavaType rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType, null);
    }

    public ObjectWriter typedWriter(TypeReference rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
    }

    public ObjectReader updatingReader(Object valueToUpdate) {
        return new ObjectReader(this, copyDeserializationConfig(), this._typeFactory.constructType(valueToUpdate.getClass()), valueToUpdate, null);
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            T result = readTree(jp);
            jp.close();
            return result;
        } catch (IOException e) {
            IOException e2 = e;
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectWriter viewWriter(Class<?> serializationView) {
        return new ObjectWriter(this, copySerializationConfig().withView(serializationView));
    }

    public ObjectMapper withModule(Module module) {
        registerModule(module);
        return this;
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        SerializationConfig config = copySerializationConfig();
        this._serializerProvider.serializeValue(config, jgen, rootNode, this._serializerFactory);
        if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode, SerializationConfig cfg) throws IOException, JsonProcessingException {
        this._serializerProvider.serializeValue(cfg, jgen, rootNode, this._serializerFactory);
        if (cfg.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
    }

    public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig config = copySerializationConfig();
        if (config.isEnabled(Feature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
            _writeCloseableValue(jgen, value, config);
        } else {
            this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
            if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
        }
    }

    public void writeValue(JsonGenerator jgen, Object value, SerializationConfig config) throws IOException, JsonGenerationException, JsonMappingException {
        if (config.isEnabled(Feature.CLOSE_CLOSEABLE) && value instanceof Closeable) {
            _writeCloseableValue(jgen, value, config);
        } else {
            this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
            if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
        }
    }

    public byte[] writeValueAsBytes(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        OutputStream bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
        byte[] result = bb.toByteArray();
        bb.release();
        return result;
    }

    public String writeValueAsString(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        Writer sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
        return sw.getAndClear();
    }

    @Deprecated
    public void writeValueUsingView(OutputStream out, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(Writer w, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(jgen, value, viewClass);
    }

    public ObjectWriter writer() {
        return new ObjectWriter(this, copySerializationConfig());
    }
}