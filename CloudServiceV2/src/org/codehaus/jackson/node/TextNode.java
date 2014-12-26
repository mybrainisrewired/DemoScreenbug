package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;

public final class TextNode extends ValueNode {
    static final TextNode EMPTY_STRING_NODE;
    static final int INT_SPACE = 32;
    final String _value;

    static {
        EMPTY_STRING_NODE = new TextNode("");
    }

    public TextNode(String v) {
        this._value = v;
    }

    protected static void appendQuoted(StringBuilder sb, String content) {
        sb.append('\"');
        CharTypes.appendQuoted(sb, content);
        sb.append('\"');
    }

    public static TextNode valueOf(String v) {
        if (v == null) {
            return null;
        }
        return v.length() == 0 ? EMPTY_STRING_NODE : new TextNode(v);
    }

    protected void _reportBase64EOF() throws JsonParseException {
        throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.NA);
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex) throws JsonParseException {
        _reportInvalidBase64(b64variant, ch, bindex, null);
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg) throws JsonParseException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        throw new JsonParseException(base, JsonLocation.NA);
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_STRING;
    }

    public boolean equals(TextNode o) {
        if (o == this) {
            return true;
        }
        return (o == null || o.getClass() != getClass()) ? false : o._value.equals(this._value);
    }

    public byte[] getBinaryValue() throws IOException {
        return getBinaryValue(Base64Variants.getDefaultVariant());
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        ByteArrayBuilder builder = new ByteArrayBuilder(100);
        String str = this._value;
        int ptr = 0;
        int len = str.length();
        while (ptr < len) {
            while (true) {
                int ptr2 = ptr + 1;
                char ch = str.charAt(ptr);
                if (ptr2 >= len) {
                    break;
                } else if (ch > ' ') {
                    int bits = b64variant.decodeBase64Char(ch);
                    if (bits < 0) {
                        _reportInvalidBase64(b64variant, ch, 0);
                    }
                    int decodedData = bits;
                    if (ptr2 >= len) {
                        _reportBase64EOF();
                    }
                    ptr = ptr2 + 1;
                    ch = str.charAt(ptr2);
                    bits = b64variant.decodeBase64Char(ch);
                    if (bits < 0) {
                        _reportInvalidBase64(b64variant, ch, 1);
                    }
                    decodedData = (decodedData << 6) | bits;
                    if (ptr >= len && !b64variant.usesPadding()) {
                        builder.append(decodedData >> 4);
                        break;
                    } else {
                        _reportBase64EOF();
                    }
                    ptr2 = ptr + 1;
                    ch = str.charAt(ptr);
                    bits = b64variant.decodeBase64Char(ch);
                    if (bits < 0) {
                        if (bits != -2) {
                            _reportInvalidBase64(b64variant, ch, ClassWriter.COMPUTE_FRAMES);
                        }
                        if (ptr2 >= len) {
                            _reportBase64EOF();
                        }
                        ptr = ptr2 + 1;
                        ch = str.charAt(ptr2);
                        if (!b64variant.usesPaddingChar(ch)) {
                            _reportInvalidBase64(b64variant, ch, JsonWriteContext.STATUS_OK_AFTER_SPACE, "expected padding character '" + b64variant.getPaddingChar() + "'");
                        }
                        builder.append(decodedData >> 4);
                    } else {
                        decodedData = (decodedData << 6) | bits;
                        if (ptr2 >= len && !b64variant.usesPadding()) {
                            builder.appendTwoBytes(decodedData >> 2);
                            break;
                        } else {
                            _reportBase64EOF();
                        }
                        ptr = ptr2 + 1;
                        ch = str.charAt(ptr2);
                        bits = b64variant.decodeBase64Char(ch);
                        if (bits < 0) {
                            if (bits != -2) {
                                _reportInvalidBase64(b64variant, ch, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                            }
                            builder.appendTwoBytes(decodedData >> 2);
                        } else {
                            builder.appendThreeBytes((decodedData << 6) | bits);
                        }
                    }
                } else {
                    ptr = ptr2;
                }
            }
        }
        return builder.toByteArray();
    }

    public String getTextValue() {
        return this._value;
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return (this._value == null || !"true".equals(this._value.trim())) ? defaultValue : true;
    }

    public double getValueAsDouble(double defaultValue) {
        return NumberInput.parseAsDouble(this._value, defaultValue);
    }

    public int getValueAsInt(int defaultValue) {
        return NumberInput.parseAsInt(this._value, defaultValue);
    }

    public long getValueAsLong(long defaultValue) {
        return NumberInput.parseAsLong(this._value, defaultValue);
    }

    public String getValueAsText() {
        return this._value;
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    public boolean isTextual() {
        return true;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (this._value == null) {
            jg.writeNull();
        } else {
            jg.writeString(this._value);
        }
    }

    public String toString() {
        int len = this._value.length();
        StringBuilder sb = new StringBuilder(len + 2 + len >> 4);
        appendQuoted(sb, this._value);
        return sb.toString();
    }
}