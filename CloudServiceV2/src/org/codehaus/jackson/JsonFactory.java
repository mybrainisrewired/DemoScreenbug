package org.codehaus.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.net.URL;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.impl.ByteSourceBootstrapper;
import org.codehaus.jackson.impl.ReaderBasedParser;
import org.codehaus.jackson.impl.Utf8Generator;
import org.codehaus.jackson.impl.WriterBasedGenerator;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.InputDecorator;
import org.codehaus.jackson.io.OutputDecorator;
import org.codehaus.jackson.io.UTF8Writer;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.VersionUtil;

public class JsonFactory implements Versioned {
    static final int DEFAULT_GENERATOR_FEATURE_FLAGS;
    static final int DEFAULT_PARSER_FEATURE_FLAGS;
    public static final String FORMAT_NAME_JSON = "JSON";
    protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;
    protected CharacterEscapes _characterEscapes;
    protected int _generatorFeatures;
    protected InputDecorator _inputDecorator;
    protected ObjectCodec _objectCodec;
    protected OutputDecorator _outputDecorator;
    protected int _parserFeatures;
    protected BytesToNameCanonicalizer _rootByteSymbols;
    protected CharsToNameCanonicalizer _rootCharSymbols;

    static {
        DEFAULT_PARSER_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
        _recyclerRef = new ThreadLocal();
    }

    public JsonFactory() {
        this(null);
    }

    public JsonFactory(ObjectCodec oc) {
        this._rootCharSymbols = CharsToNameCanonicalizer.createRoot();
        this._rootByteSymbols = BytesToNameCanonicalizer.createRoot();
        this._parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
        this._generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
        this._objectCodec = oc;
    }

    protected IOContext _createContext(Object srcRef, boolean resourceManaged) {
        return new IOContext(_getBufferRecycler(), srcRef, resourceManaged);
    }

    protected JsonGenerator _createJsonGenerator(Writer out, IOContext ctxt) throws IOException {
        WriterBasedGenerator gen = new WriterBasedGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
        if (this._characterEscapes != null) {
            gen.setCharacterEscapes(this._characterEscapes);
        }
        return gen;
    }

    protected JsonParser _createJsonParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return new ByteSourceBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
    }

    protected JsonParser _createJsonParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        return new ReaderBasedParser(ctxt, this._parserFeatures, r, this._objectCodec, this._rootCharSymbols.makeChild(isEnabled(Feature.CANONICALIZE_FIELD_NAMES), isEnabled(Feature.INTERN_FIELD_NAMES)));
    }

    protected JsonParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return new ByteSourceBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
    }

    protected JsonGenerator _createUTF8JsonGenerator(OutputStream out, IOContext ctxt) throws IOException {
        Utf8Generator gen = new Utf8Generator(ctxt, this._generatorFeatures, this._objectCodec, out);
        if (this._characterEscapes != null) {
            gen.setCharacterEscapes(this._characterEscapes);
        }
        return gen;
    }

    protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt) throws IOException {
        return enc == JsonEncoding.UTF8 ? new UTF8Writer(ctxt, out) : new OutputStreamWriter(out, enc.getJavaName());
    }

    public BufferRecycler _getBufferRecycler() {
        SoftReference<BufferRecycler> ref = (SoftReference) _recyclerRef.get();
        BufferRecycler br = ref == null ? null : (BufferRecycler) ref.get();
        if (br != null) {
            return br;
        }
        br = new BufferRecycler();
        _recyclerRef.set(new SoftReference(br));
        return br;
    }

    protected InputStream _optimizedStreamFromURL(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            String host = url.getHost();
            if (host == null || host.length() == 0) {
                return new FileInputStream(url.getPath());
            }
        }
        return url.openStream();
    }

    public final JsonFactory configure(JsonGenerator.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public final JsonFactory configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public JsonGenerator createJsonGenerator(File f, JsonEncoding enc) throws IOException {
        OutputStream out = new FileOutputStream(f);
        IOContext ctxt = _createContext(out, true);
        ctxt.setEncoding(enc);
        if (enc == JsonEncoding.UTF8) {
            if (this._outputDecorator != null) {
                out = this._outputDecorator.decorate(ctxt, out);
            }
            return _createUTF8JsonGenerator(out, ctxt);
        } else {
            Writer w = _createWriter(out, enc, ctxt);
            if (this._outputDecorator != null) {
                w = this._outputDecorator.decorate(ctxt, w);
            }
            return _createJsonGenerator(w, ctxt);
        }
    }

    public JsonGenerator createJsonGenerator(OutputStream out) throws IOException {
        return createJsonGenerator(out, JsonEncoding.UTF8);
    }

    public JsonGenerator createJsonGenerator(OutputStream out, JsonEncoding enc) throws IOException {
        IOContext ctxt = _createContext(out, false);
        ctxt.setEncoding(enc);
        if (enc == JsonEncoding.UTF8) {
            if (this._outputDecorator != null) {
                out = this._outputDecorator.decorate(ctxt, out);
            }
            return _createUTF8JsonGenerator(out, ctxt);
        } else {
            Writer w = _createWriter(out, enc, ctxt);
            if (this._outputDecorator != null) {
                w = this._outputDecorator.decorate(ctxt, w);
            }
            return _createJsonGenerator(w, ctxt);
        }
    }

    public JsonGenerator createJsonGenerator(Writer out) throws IOException {
        IOContext ctxt = _createContext(out, false);
        if (this._outputDecorator != null) {
            out = this._outputDecorator.decorate(ctxt, out);
        }
        return _createJsonGenerator(out, ctxt);
    }

    public JsonParser createJsonParser(File f) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(f, true);
        InputStream in = new FileInputStream(f);
        if (this._inputDecorator != null) {
            in = this._inputDecorator.decorate(ctxt, in);
        }
        return _createJsonParser(in, ctxt);
    }

    public JsonParser createJsonParser(InputStream in) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(in, false);
        if (this._inputDecorator != null) {
            in = this._inputDecorator.decorate(ctxt, in);
        }
        return _createJsonParser(in, ctxt);
    }

    public JsonParser createJsonParser(Reader r) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(r, false);
        if (this._inputDecorator != null) {
            r = this._inputDecorator.decorate(ctxt, r);
        }
        return _createJsonParser(r, ctxt);
    }

    public JsonParser createJsonParser(String content) throws IOException, JsonParseException {
        Reader r = new StringReader(content);
        IOContext ctxt = _createContext(r, true);
        if (this._inputDecorator != null) {
            r = this._inputDecorator.decorate(ctxt, r);
        }
        return _createJsonParser(r, ctxt);
    }

    public JsonParser createJsonParser(URL url) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(url, true);
        InputStream in = _optimizedStreamFromURL(url);
        if (this._inputDecorator != null) {
            in = this._inputDecorator.decorate(ctxt, in);
        }
        return _createJsonParser(in, ctxt);
    }

    public JsonParser createJsonParser(byte[] data) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(data, true);
        if (this._inputDecorator != null) {
            InputStream in = this._inputDecorator.decorate(ctxt, data, DEFAULT_PARSER_FEATURE_FLAGS, data.length);
            if (in != null) {
                return _createJsonParser(in, ctxt);
            }
        }
        return _createJsonParser(data, DEFAULT_PARSER_FEATURE_FLAGS, data.length, ctxt);
    }

    public JsonParser createJsonParser(byte[] data, int offset, int len) throws IOException, JsonParseException {
        IOContext ctxt = _createContext(data, true);
        if (this._inputDecorator != null) {
            InputStream in = this._inputDecorator.decorate(ctxt, data, offset, len);
            if (in != null) {
                return _createJsonParser(in, ctxt);
            }
        }
        return _createJsonParser(data, offset, len, ctxt);
    }

    public JsonFactory disable(JsonGenerator.Feature f) {
        this._generatorFeatures &= f.getMask() ^ -1;
        return this;
    }

    public JsonFactory disable(Feature f) {
        this._parserFeatures &= f.getMask() ^ -1;
        return this;
    }

    @Deprecated
    public final void disableGeneratorFeature(JsonGenerator.Feature f) {
        disable(f);
    }

    public final void disableParserFeature(Feature f) {
        disable(f);
    }

    public JsonFactory enable(JsonGenerator.Feature f) {
        this._generatorFeatures |= f.getMask();
        return this;
    }

    public JsonFactory enable(Feature f) {
        this._parserFeatures |= f.getMask();
        return this;
    }

    @Deprecated
    public final void enableGeneratorFeature(JsonGenerator.Feature f) {
        enable(f);
    }

    public final void enableParserFeature(Feature f) {
        enable(f);
    }

    public CharacterEscapes getCharacterEscapes() {
        return this._characterEscapes;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public String getFormatName() {
        return getClass() == JsonFactory.class ? FORMAT_NAME_JSON : null;
    }

    public InputDecorator getInputDecorator() {
        return this._inputDecorator;
    }

    public OutputDecorator getOutputDecorator() {
        return this._outputDecorator;
    }

    public MatchStrength hasFormat(InputAccessor acc) throws IOException {
        return getClass() == JsonFactory.class ? hasJSONFormat(acc) : null;
    }

    protected MatchStrength hasJSONFormat(InputAccessor acc) throws IOException {
        return ByteSourceBootstrapper.hasJSONFormat(acc);
    }

    public final boolean isEnabled(JsonGenerator.Feature f) {
        return (this._generatorFeatures & f.getMask()) != 0;
    }

    public final boolean isEnabled(Feature f) {
        return (this._parserFeatures & f.getMask()) != 0;
    }

    @Deprecated
    public final boolean isGeneratorFeatureEnabled(JsonGenerator.Feature f) {
        return isEnabled(f);
    }

    public final boolean isParserFeatureEnabled(Feature f) {
        return (this._parserFeatures & f.getMask()) != 0;
    }

    public JsonFactory setCharacterEscapes(CharacterEscapes esc) {
        this._characterEscapes = esc;
        return this;
    }

    public JsonFactory setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    @Deprecated
    public final void setGeneratorFeature(JsonGenerator.Feature f, boolean state) {
        configure(f, state);
    }

    public JsonFactory setInputDecorator(InputDecorator d) {
        this._inputDecorator = d;
        return this;
    }

    public JsonFactory setOutputDecorator(OutputDecorator d) {
        this._outputDecorator = d;
        return this;
    }

    public final void setParserFeature(Feature f, boolean state) {
        configure(f, state);
    }

    public Version version() {
        return VersionUtil.versionFor(Utf8Generator.class);
    }
}