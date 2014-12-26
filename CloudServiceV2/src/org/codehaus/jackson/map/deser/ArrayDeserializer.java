package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class ArrayDeserializer extends ContainerDeserializer<Object[]> {
    protected final JavaType _arrayType;
    protected final Class<?> _elementClass;
    protected final JsonDeserializer<Object> _elementDeserializer;
    final TypeDeserializer _elementTypeDeserializer;
    protected final boolean _untyped;

    @Deprecated
    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser) {
        this(arrayType, elemDeser, null);
    }

    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser) {
        super(Object[].class);
        this._arrayType = arrayType;
        this._elementClass = arrayType.getContentType().getRawClass();
        this._untyped = this._elementClass == Object.class;
        this._elementDeserializer = elemDeser;
        this._elementTypeDeserializer = elemTypeDeser;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Object[] handleNonArray(org.codehaus.jackson.JsonParser r7_jp, org.codehaus.jackson.map.DeserializationContext r8_ctxt) throws java.io.IOException, org.codehaus.jackson.JsonProcessingException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.ArrayDeserializer.handleNonArray(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext):java.lang.Object[]");
        /*
        r6 = this;
        r5 = 1;
        r3 = org.codehaus.jackson.map.DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        r3 = r8.isEnabled(r3);
        if (r3 != 0) goto L_0x0027;
    L_0x0009:
        r3 = r7.getCurrentToken();
        r4 = org.codehaus.jackson.JsonToken.VALUE_STRING;
        if (r3 != r4) goto L_0x001c;
    L_0x0011:
        r3 = r6._elementClass;
        r4 = java.lang.Byte.class;
        if (r3 != r4) goto L_0x001c;
    L_0x0017:
        r0 = r6.deserializeFromBase64(r7, r8);
    L_0x001b:
        return r0;
    L_0x001c:
        r3 = r6._arrayType;
        r3 = r3.getRawClass();
        r3 = r8.mappingException(r3);
        throw r3;
    L_0x0027:
        r1 = r7.getCurrentToken();
        r3 = org.codehaus.jackson.JsonToken.VALUE_NULL;
        if (r1 != r3) goto L_0x003a;
    L_0x002f:
        r2 = 0;
    L_0x0030:
        r3 = r6._untyped;
        if (r3 == 0) goto L_0x004e;
    L_0x0034:
        r0 = new java.lang.Object[r5];
    L_0x0036:
        r3 = 0;
        r0[r3] = r2;
        goto L_0x001b;
    L_0x003a:
        r3 = r6._elementTypeDeserializer;
        if (r3 != 0) goto L_0x0045;
    L_0x003e:
        r3 = r6._elementDeserializer;
        r2 = r3.deserialize(r7, r8);
        goto L_0x0030;
    L_0x0045:
        r3 = r6._elementDeserializer;
        r4 = r6._elementTypeDeserializer;
        r2 = r3.deserializeWithType(r7, r8, r4);
        goto L_0x0030;
    L_0x004e:
        r3 = r6._elementClass;
        r3 = java.lang.reflect.Array.newInstance(r3, r5);
        r3 = (java.lang.Object[]) r3;
        r0 = r3;
        r0 = (java.lang.Object[]) r0;
        goto L_0x0036;
        */
    }

    public Object[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt);
        }
        Object[] result;
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();
        int ix = 0;
        TypeDeserializer typeDeser = this._elementTypeDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                break;
            }
            Object obj;
            if (t == JsonToken.VALUE_NULL) {
                obj = null;
            } else if (typeDeser == null) {
                obj = this._elementDeserializer.deserialize(jp, ctxt);
            } else {
                obj = this._elementDeserializer.deserializeWithType(jp, ctxt, typeDeser);
            }
            if (ix >= chunk.length) {
                chunk = buffer.appendCompletedChunk(chunk);
                ix = 0;
            }
            int ix2 = ix + 1;
            chunk[ix] = obj;
            ix = ix2;
        }
        if (this._untyped) {
            result = buffer.completeAndClearBuffer(chunk, ix);
        } else {
            result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
        }
        ctxt.returnObjectBuffer(buffer);
        return result;
    }

    protected Byte[] deserializeFromBase64(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        byte[] b = jp.getBinaryValue(ctxt.getBase64Variant());
        Byte[] result = new Byte[b.length];
        int i = 0;
        int len = b.length;
        while (i < len) {
            result[i] = Byte.valueOf(b[i]);
            i++;
        }
        return result;
    }

    public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return (Object[]) typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._elementDeserializer;
    }

    public JavaType getContentType() {
        return this._arrayType.getContentType();
    }
}