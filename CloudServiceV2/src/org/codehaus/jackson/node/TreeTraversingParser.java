package org.codehaus.jackson.node;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.JsonParserMinimalBase;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class TreeTraversingParser extends JsonParserMinimalBase {
    protected boolean _closed;
    protected JsonToken _nextToken;
    protected NodeCursor _nodeCursor;
    protected ObjectCodec _objectCodec;
    protected boolean _startContainer;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 5;
        }
    }

    public TreeTraversingParser(JsonNode n) {
        this(n, null);
    }

    public TreeTraversingParser(JsonNode n, ObjectCodec codec) {
        super(0);
        this._objectCodec = codec;
        if (n.isArray()) {
            this._nextToken = JsonToken.START_ARRAY;
            this._nodeCursor = new Array(n, null);
        } else if (n.isObject()) {
            this._nextToken = JsonToken.START_OBJECT;
            this._nodeCursor = new Object(n, null);
        } else {
            this._nodeCursor = new RootValue(n, null);
        }
    }

    protected void _handleEOF() throws JsonParseException {
        _throwInternal();
    }

    public void close() throws IOException {
        if (!this._closed) {
            this._closed = true;
            this._nodeCursor = null;
            this._currToken = null;
        }
    }

    protected JsonNode currentNode() {
        return (this._closed || this._nodeCursor == null) ? null : this._nodeCursor.currentNode();
    }

    protected JsonNode currentNumericNode() throws JsonParseException {
        JsonNode n = currentNode();
        if (n != null && n.isNumber()) {
            return n;
        }
        throw _constructError("Current token (" + (n == null ? null : n.asToken()) + ") not numeric, can not use numeric value accessors");
    }

    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        return currentNumericNode().getBigIntegerValue();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        JsonNode n = currentNode();
        if (n != null) {
            byte[] data = n.getBinaryValue();
            if (data != null) {
                return data;
            }
            if (n.isPojo()) {
                Object ob = ((POJONode) n).getPojo();
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
        }
        return null;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public JsonLocation getCurrentLocation() {
        return JsonLocation.NA;
    }

    public String getCurrentName() {
        return this._nodeCursor == null ? null : this._nodeCursor.getCurrentName();
    }

    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        return currentNumericNode().getDecimalValue();
    }

    public double getDoubleValue() throws IOException, JsonParseException {
        return currentNumericNode().getDoubleValue();
    }

    public Object getEmbeddedObject() {
        if (!this._closed) {
            JsonNode n = currentNode();
            if (n != null && n.isPojo()) {
                return ((POJONode) n).getPojo();
            }
        }
        return null;
    }

    public float getFloatValue() throws IOException, JsonParseException {
        return (float) currentNumericNode().getDoubleValue();
    }

    public int getIntValue() throws IOException, JsonParseException {
        return currentNumericNode().getIntValue();
    }

    public long getLongValue() throws IOException, JsonParseException {
        return currentNumericNode().getLongValue();
    }

    public NumberType getNumberType() throws IOException, JsonParseException {
        JsonNode n = currentNumericNode();
        return n == null ? null : n.getNumberType();
    }

    public Number getNumberValue() throws IOException, JsonParseException {
        return currentNumericNode().getNumberValue();
    }

    public JsonStreamContext getParsingContext() {
        return this._nodeCursor;
    }

    public String getText() {
        if (this._closed) {
            return null;
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return this._nodeCursor.getCurrentName();
            case ClassWriter.COMPUTE_FRAMES:
                return currentNode().getTextValue();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return String.valueOf(currentNode().getNumberValue());
            case JsonWriteContext.STATUS_EXPECT_NAME:
                JsonNode n = currentNode();
                if (n != null && n.isBinary()) {
                    return n.getValueAsText();
                }
                return this._currToken == null ? null : this._currToken.asString();
            default:
                if (this._currToken == null) {
                }
        }
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        return getText().toCharArray();
    }

    public int getTextLength() throws IOException, JsonParseException {
        return getText().length();
    }

    public int getTextOffset() throws IOException, JsonParseException {
        return 0;
    }

    public JsonLocation getTokenLocation() {
        return JsonLocation.NA;
    }

    public boolean hasTextCharacters() {
        return false;
    }

    public boolean isClosed() {
        return this._closed;
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        if (this._nextToken != null) {
            this._currToken = this._nextToken;
            this._nextToken = null;
            return this._currToken;
        } else if (this._startContainer) {
            this._startContainer = false;
            if (this._nodeCursor.currentHasChildren()) {
                this._nodeCursor = this._nodeCursor.iterateChildren();
                this._currToken = this._nodeCursor.nextToken();
                if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
                    this._startContainer = true;
                }
                return this._currToken;
            } else {
                this._currToken = this._currToken == JsonToken.START_OBJECT ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
                return this._currToken;
            }
        } else if (this._nodeCursor == null) {
            this._closed = true;
            return null;
        } else {
            this._currToken = this._nodeCursor.nextToken();
            if (this._currToken != null) {
                if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
                    this._startContainer = true;
                }
                return this._currToken;
            } else {
                this._currToken = this._nodeCursor.endToken();
                this._nodeCursor = this._nodeCursor.getParent();
                return this._currToken;
            }
        }
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    public JsonParser skipChildren() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.START_OBJECT) {
            this._startContainer = false;
            this._currToken = JsonToken.END_OBJECT;
        } else if (this._currToken == JsonToken.START_ARRAY) {
            this._startContainer = false;
            this._currToken = JsonToken.END_ARRAY;
        }
        return this;
    }
}