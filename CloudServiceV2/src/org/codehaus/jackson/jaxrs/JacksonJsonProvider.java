package org.codehaus.jackson.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

@Provider
@Produces({"application/json", "text/json"})
@Consumes({"application/json", "text/json"})
public class JacksonJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object>, Versioned {
    public static final Annotations[] BASIC_ANNOTATIONS;
    public static final Class<?>[] _unreadableClasses;
    public static final HashSet<ClassKey> _untouchables;
    public static final Class<?>[] _unwritableClasses;
    protected boolean _cfgCheckCanDeserialize;
    protected boolean _cfgCheckCanSerialize;
    protected HashSet<ClassKey> _cfgCustomUntouchables;
    protected String _jsonpFunctionName;
    protected final MapperConfigurator _mapperConfig;
    @Context
    protected Providers _providers;

    static {
        BASIC_ANNOTATIONS = new Annotations[]{Annotations.JACKSON};
        _untouchables = new HashSet();
        _untouchables.add(new ClassKey(InputStream.class));
        _untouchables.add(new ClassKey(Reader.class));
        _untouchables.add(new ClassKey(OutputStream.class));
        _untouchables.add(new ClassKey(Writer.class));
        _untouchables.add(new ClassKey(byte[].class));
        _untouchables.add(new ClassKey(char[].class));
        _untouchables.add(new ClassKey(String.class));
        _untouchables.add(new ClassKey(StreamingOutput.class));
        _untouchables.add(new ClassKey(Response.class));
        _unreadableClasses = new Class[]{InputStream.class, Reader.class};
        _unwritableClasses = new Class[]{OutputStream.class, Writer.class, StreamingOutput.class, Response.class};
    }

    public JacksonJsonProvider() {
        this(null, BASIC_ANNOTATIONS);
    }

    public JacksonJsonProvider(ObjectMapper mapper) {
        this(mapper, BASIC_ANNOTATIONS);
    }

    public JacksonJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse) {
        this._cfgCheckCanSerialize = false;
        this._cfgCheckCanDeserialize = false;
        this._mapperConfig = new MapperConfigurator(mapper, annotationsToUse);
    }

    public JacksonJsonProvider(Annotations... annotationsToUse) {
        this(null, annotationsToUse);
    }

    protected static boolean _containedIn(Class<?> mainType, HashSet<ClassKey> set) {
        if (set != null) {
            ClassKey key = new ClassKey(mainType);
            if (set.contains(key)) {
                return true;
            }
            Iterator i$ = ClassUtil.findSuperTypes(mainType, null).iterator();
            while (i$.hasNext()) {
                key.reset((Class) i$.next());
                if (set.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addUntouchable(Class<?> type) {
        if (this._cfgCustomUntouchables == null) {
            this._cfgCustomUntouchables = new HashSet();
        }
        this._cfgCustomUntouchables.add(new ClassKey(type));
    }

    public void checkCanDeserialize(boolean state) {
        this._cfgCheckCanDeserialize = state;
    }

    public void checkCanSerialize(boolean state) {
        this._cfgCheckCanSerialize = state;
    }

    public JacksonJsonProvider configure(Feature f, boolean state) {
        this._mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(JsonParser.Feature f, boolean state) {
        this._mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(DeserializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(SerializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider disable(Feature f, boolean state) {
        this._mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(JsonParser.Feature f, boolean state) {
        this._mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(DeserializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(SerializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider enable(Feature f, boolean state) {
        this._mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(JsonParser.Feature f, boolean state) {
        this._mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(DeserializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(SerializationConfig.Feature f, boolean state) {
        this._mapperConfig.configure(f, true);
        return this;
    }

    protected JsonEncoding findEncoding(MediaType mediaType, MultivaluedMap<String, Object> httpHeaders) {
        return JsonEncoding.UTF8;
    }

    public long getSize(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    protected boolean isJsonType(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        String subtype = mediaType.getSubtype();
        return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json");
    }

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!isJsonType(mediaType) || _untouchables.contains(new ClassKey(type))) {
            return false;
        }
        Class[] arr$ = _unreadableClasses;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].isAssignableFrom(type)) {
                return false;
            }
            i$++;
        }
        if (_containedIn(type, this._cfgCustomUntouchables)) {
            return false;
        }
        if (this._cfgCheckCanSerialize) {
            ObjectMapper mapper = locateMapper(type, mediaType);
            if (!mapper.canDeserialize(mapper.constructType(type))) {
                return false;
            }
        }
        return true;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!isJsonType(mediaType) || _untouchables.contains(new ClassKey(type))) {
            return false;
        }
        Class[] arr$ = _unwritableClasses;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].isAssignableFrom(type)) {
                return false;
            }
            i$++;
        }
        if (_containedIn(type, this._cfgCustomUntouchables)) {
            return false;
        }
        return !this._cfgCheckCanSerialize || locateMapper(type, mediaType).canSerialize(type);
    }

    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
        ObjectMapper m = this._mapperConfig.getConfiguredMapper();
        if (m != null) {
            return m;
        }
        if (this._providers != null) {
            ContextResolver<ObjectMapper> resolver = this._providers.getContextResolver(ObjectMapper.class, mediaType);
            if (resolver == null) {
                resolver = this._providers.getContextResolver(ObjectMapper.class, null);
            }
            if (resolver != null) {
                m = resolver.getContext(type);
            }
        }
        return m == null ? this._mapperConfig.getDefaultMapper() : m;
    }

    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        ObjectMapper mapper = locateMapper(type, mediaType);
        JsonParser jp = mapper.getJsonFactory().createJsonParser(entityStream);
        jp.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        return mapper.readValue(jp, mapper.constructType(genericType));
    }

    public void setAnnotationsToUse(Annotations[] annotationsToUse) {
        this._mapperConfig.setAnnotationsToUse(annotationsToUse);
    }

    public void setJSONPFunctionName(String fname) {
        this._jsonpFunctionName = fname;
    }

    public void setMapper(ObjectMapper m) {
        this._mapperConfig.setMapper(m);
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        ObjectMapper mapper = locateMapper(type, mediaType);
        JsonGenerator jg = mapper.getJsonFactory().createJsonGenerator(entityStream, findEncoding(mediaType, httpHeaders));
        jg.disable(Feature.AUTO_CLOSE_TARGET);
        if (mapper.getSerializationConfig().isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jg.useDefaultPrettyPrinter();
        }
        JavaType rootType = null;
        if (!(genericType == null || value == null || genericType.getClass() == Class.class)) {
            rootType = mapper.getTypeFactory().constructType(genericType);
            if (rootType.getRawClass() == Object.class) {
                rootType = null;
            }
        }
        if (this._jsonpFunctionName != null) {
            mapper.writeValue(jg, new JSONPObject(this._jsonpFunctionName, value, rootType));
        } else if (rootType != null) {
            mapper.typedWriter(rootType).writeValue(jg, value);
        } else {
            mapper.writeValue(jg, value);
        }
    }
}