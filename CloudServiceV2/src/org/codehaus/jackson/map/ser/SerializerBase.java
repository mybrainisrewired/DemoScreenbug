package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerBase<T> extends JsonSerializer<T> implements SchemaAware {
    protected final Class<T> _handledType;

    protected SerializerBase(Class<T> t) {
        this._handledType = t;
    }

    protected SerializerBase(Class<?> t, boolean dummy) {
        this._handledType = t;
    }

    protected SerializerBase(JavaType type) {
        this._handledType = type.getRawClass();
    }

    protected ObjectNode createObjectNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    protected ObjectNode createSchemaNode(String type) {
        ObjectNode schema = createObjectNode();
        schema.put("type", type);
        return schema;
    }

    protected ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = createSchemaNode(type);
        if (!isOptional) {
            schema.put("required", !isOptional);
        }
        return schema;
    }

    public abstract JsonNode getSchema(SerializerProvider serializerProvider, Type type) throws JsonMappingException;

    public final Class<T> handledType() {
        return this._handledType;
    }

    protected boolean isDefaultSerializer(JsonSerializer<?> serializer) {
        return (serializer == null || serializer.getClass().getAnnotation(JacksonStdImpl.class) == null) ? false : true;
    }

    public abstract void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException;

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index) throws IOException {
        wrapAndThrow(null, t, bean, index);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, String fieldName) throws IOException {
        wrapAndThrow(null, t, bean, fieldName);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void wrapAndThrow(org.codehaus.jackson.map.SerializerProvider r3_provider, java.lang.Throwable r4_t, java.lang.Object r5_bean, int r6_index) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.SerializerBase.wrapAndThrow(org.codehaus.jackson.map.SerializerProvider, java.lang.Throwable, java.lang.Object, int):void");
        /*
        r2 = this;
    L_0x0000:
        r1 = r4 instanceof java.lang.reflect.InvocationTargetException;
        if (r1 == 0) goto L_0x000f;
    L_0x0004:
        r1 = r4.getCause();
        if (r1 == 0) goto L_0x000f;
    L_0x000a:
        r4 = r4.getCause();
        goto L_0x0000;
    L_0x000f:
        r1 = r4 instanceof java.lang.Error;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r4 = (java.lang.Error) r4;
        throw r4;
    L_0x0016:
        if (r3 == 0) goto L_0x0020;
    L_0x0018:
        r1 = org.codehaus.jackson.map.SerializationConfig.Feature.WRAP_EXCEPTIONS;
        r1 = r3.isEnabled(r1);
        if (r1 == 0) goto L_0x002e;
    L_0x0020:
        r0 = 1;
    L_0x0021:
        r1 = r4 instanceof java.io.IOException;
        if (r1 == 0) goto L_0x0030;
    L_0x0025:
        if (r0 == 0) goto L_0x002b;
    L_0x0027:
        r1 = r4 instanceof org.codehaus.jackson.map.JsonMappingException;
        if (r1 != 0) goto L_0x0039;
    L_0x002b:
        r4 = (java.io.IOException) r4;
        throw r4;
    L_0x002e:
        r0 = 0;
        goto L_0x0021;
    L_0x0030:
        if (r0 != 0) goto L_0x0039;
    L_0x0032:
        r1 = r4 instanceof java.lang.RuntimeException;
        if (r1 == 0) goto L_0x0039;
    L_0x0036:
        r4 = (java.lang.RuntimeException) r4;
        throw r4;
    L_0x0039:
        r1 = org.codehaus.jackson.map.JsonMappingException.wrapWithPath(r4, r5, r6);
        throw r1;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void wrapAndThrow(org.codehaus.jackson.map.SerializerProvider r3_provider, java.lang.Throwable r4_t, java.lang.Object r5_bean, java.lang.String r6_fieldName) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.SerializerBase.wrapAndThrow(org.codehaus.jackson.map.SerializerProvider, java.lang.Throwable, java.lang.Object, java.lang.String):void");
        /*
        r2 = this;
    L_0x0000:
        r1 = r4 instanceof java.lang.reflect.InvocationTargetException;
        if (r1 == 0) goto L_0x000f;
    L_0x0004:
        r1 = r4.getCause();
        if (r1 == 0) goto L_0x000f;
    L_0x000a:
        r4 = r4.getCause();
        goto L_0x0000;
    L_0x000f:
        r1 = r4 instanceof java.lang.Error;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r4 = (java.lang.Error) r4;
        throw r4;
    L_0x0016:
        if (r3 == 0) goto L_0x0020;
    L_0x0018:
        r1 = org.codehaus.jackson.map.SerializationConfig.Feature.WRAP_EXCEPTIONS;
        r1 = r3.isEnabled(r1);
        if (r1 == 0) goto L_0x002e;
    L_0x0020:
        r0 = 1;
    L_0x0021:
        r1 = r4 instanceof java.io.IOException;
        if (r1 == 0) goto L_0x0030;
    L_0x0025:
        if (r0 == 0) goto L_0x002b;
    L_0x0027:
        r1 = r4 instanceof org.codehaus.jackson.map.JsonMappingException;
        if (r1 != 0) goto L_0x0039;
    L_0x002b:
        r4 = (java.io.IOException) r4;
        throw r4;
    L_0x002e:
        r0 = 0;
        goto L_0x0021;
    L_0x0030:
        if (r0 != 0) goto L_0x0039;
    L_0x0032:
        r1 = r4 instanceof java.lang.RuntimeException;
        if (r1 == 0) goto L_0x0039;
    L_0x0036:
        r4 = (java.lang.RuntimeException) r4;
        throw r4;
    L_0x0039:
        r1 = org.codehaus.jackson.map.JsonMappingException.wrapWithPath(r4, r5, r6);
        throw r1;
        */
    }
}