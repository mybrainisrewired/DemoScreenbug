package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.VersionUtil;

public abstract class JsonGeneratorBase extends JsonGenerator {
    protected boolean _cfgNumbersAsStrings;
    protected boolean _closed;
    protected int _features;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_INTEGER.ordinal()] = 2;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_DECIMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e15) {
            }
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.FLOAT.ordinal()] = 4;
        }
    }

    protected JsonGeneratorBase(int features, ObjectCodec codec) {
        this._features = features;
        this._writeContext = JsonWriteContext.createRootContext();
        this._objectCodec = codec;
        this._cfgNumbersAsStrings = isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    protected void _cantHappen() {
        throw new RuntimeException("Internal error: should never end up through this code path");
    }

    protected abstract void _releaseBuffers();

    protected void _reportError(String msg) throws JsonGenerationException {
        throw new JsonGenerationException(msg);
    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected abstract void _verifyValueWrite(String str) throws IOException, JsonGenerationException;

    @Deprecated
    protected void _writeEndArray() throws IOException, JsonGenerationException {
    }

    @Deprecated
    protected void _writeEndObject() throws IOException, JsonGenerationException {
    }

    protected void _writeSimpleObject(Object value) throws IOException, JsonGenerationException {
        if (value == null) {
            writeNull();
        } else if (value instanceof String) {
            writeString((String) value);
        } else {
            if (value instanceof Number) {
                Number n = (Number) value;
                if (n instanceof Integer) {
                    writeNumber(n.intValue());
                    return;
                } else if (n instanceof Long) {
                    writeNumber(n.longValue());
                    return;
                } else if (n instanceof Double) {
                    writeNumber(n.doubleValue());
                    return;
                } else if (n instanceof Float) {
                    writeNumber(n.floatValue());
                    return;
                } else if (n instanceof Short) {
                    writeNumber(n.shortValue());
                    return;
                } else if (n instanceof Byte) {
                    writeNumber(n.byteValue());
                    return;
                } else if (n instanceof BigInteger) {
                    writeNumber((BigInteger) n);
                    return;
                } else if (n instanceof BigDecimal) {
                    writeNumber((BigDecimal) n);
                    return;
                } else if (n instanceof AtomicInteger) {
                    writeNumber(((AtomicInteger) n).get());
                    return;
                } else if (n instanceof AtomicLong) {
                    writeNumber(((AtomicLong) n).get());
                    return;
                }
            } else if (value instanceof byte[]) {
                writeBinary((byte[]) value);
                return;
            } else if (value instanceof Boolean) {
                writeBoolean(((Boolean) value).booleanValue());
                return;
            } else if (value instanceof AtomicBoolean) {
                writeBoolean(((AtomicBoolean) value).get());
                return;
            }
            throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
        }
    }

    @Deprecated
    protected void _writeStartArray() throws IOException, JsonGenerationException {
    }

    @Deprecated
    protected void _writeStartObject() throws IOException, JsonGenerationException {
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public final void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            _reportError("No current event to copy");
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeStartObject();
            case ClassWriter.COMPUTE_FRAMES:
                writeEndObject();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeStartArray();
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                writeEndArray();
            case JsonWriteContext.STATUS_EXPECT_NAME:
                writeFieldName(jp.getCurrentName());
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                if (jp.hasTextCharacters()) {
                    writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
                } else {
                    writeString(jp.getText());
                }
            case Type.LONG:
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case LocalAudioAll.SORT_BY_DATE:
                        writeNumber(jp.getIntValue());
                    case ClassWriter.COMPUTE_FRAMES:
                        writeNumber(jp.getBigIntegerValue());
                    default:
                        writeNumber(jp.getLongValue());
                }
            case Type.DOUBLE:
                switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        writeNumber(jp.getDecimalValue());
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        writeNumber(jp.getFloatValue());
                    default:
                        writeNumber(jp.getDoubleValue());
                }
            case Type.ARRAY:
                writeBoolean(true);
            case Type.OBJECT:
                writeBoolean(false);
            case Opcodes.T_LONG:
                writeNull();
            case Opcodes.FCONST_1:
                writeObject(jp.getEmbeddedObject());
            default:
                _cantHappen();
        }
    }

    public final void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.FIELD_NAME) {
            writeFieldName(jp.getCurrentName());
            t = jp.nextToken();
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                writeStartObject();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    copyCurrentStructure(jp);
                }
                writeEndObject();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                writeStartArray();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    copyCurrentStructure(jp);
                }
                writeEndArray();
            default:
                copyCurrentEvent(jp);
        }
    }

    public JsonGenerator disable(Feature f) {
        this._features &= f.getMask() ^ -1;
        if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = false;
        } else if (f == Feature.ESCAPE_NON_ASCII) {
            setHighestNonEscapedChar(0);
        }
        return this;
    }

    public JsonGenerator enable(Feature f) {
        this._features |= f.getMask();
        if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = true;
        } else if (f == Feature.ESCAPE_NON_ASCII) {
            setHighestNonEscapedChar(Opcodes.LAND);
        }
        return this;
    }

    public abstract void flush() throws IOException;

    public final ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    public boolean isClosed() {
        return this._closed;
    }

    public final boolean isEnabled(Feature f) {
        return (this._features & f.getMask()) != 0;
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return setPrettyPrinter(new DefaultPrettyPrinter());
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            _writeEndArray();
        }
        this._writeContext = this._writeContext.getParent();
    }

    public void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            _writeEndObject();
        }
    }

    public void writeObject(Object value) throws IOException, JsonProcessingException {
        if (value == null) {
            writeNull();
        } else if (this._objectCodec != null) {
            this._objectCodec.writeValue(this, value);
        } else {
            _writeSimpleObject(value);
        }
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text);
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            _writeStartArray();
        }
    }

    public void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            _writeStartObject();
        }
    }

    public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException {
        if (rootNode == null) {
            writeNull();
        } else if (this._objectCodec == null) {
            throw new IllegalStateException("No ObjectCodec defined for the generator, can not serialize JsonNode-based trees");
        } else {
            this._objectCodec.writeTree(this, rootNode);
        }
    }
}