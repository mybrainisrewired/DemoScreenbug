package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.util.Collection;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;

@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> implements ResolvableSerializer {
    protected JsonSerializer<String> _serializer;

    public StringCollectionSerializer(BeanProperty property) {
        super(Collection.class, property);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void serializeContents(java.util.Collection<java.lang.String> r6_value, org.codehaus.jackson.JsonGenerator r7_jgen, org.codehaus.jackson.map.SerializerProvider r8_provider) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.impl.StringCollectionSerializer.serializeContents(java.util.Collection, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider):void");
        /*
        r5 = this;
        r4 = r5._serializer;
        if (r4 == 0) goto L_0x0008;
    L_0x0004:
        r5.serializeUsingCustom(r6, r7, r8);
    L_0x0007:
        return;
    L_0x0008:
        r1 = 0;
        r2 = r6.iterator();
    L_0x000d:
        r4 = r2.hasNext();
        if (r4 == 0) goto L_0x0007;
    L_0x0013:
        r3 = r2.next();
        r3 = (java.lang.String) r3;
        if (r3 != 0) goto L_0x0021;
    L_0x001b:
        r8.defaultSerializeNull(r7);	 Catch:{ Exception -> 0x0025 }
    L_0x001e:
        r1 = r1 + 1;
        goto L_0x000d;
    L_0x0021:
        r7.writeString(r3);	 Catch:{ Exception -> 0x0025 }
        goto L_0x001e;
    L_0x0025:
        r0 = move-exception;
        r5.wrapAndThrow(r8, r0, r6, r1);
        goto L_0x000d;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void serializeUsingCustom(java.util.Collection<java.lang.String> r7_value, org.codehaus.jackson.JsonGenerator r8_jgen, org.codehaus.jackson.map.SerializerProvider r9_provider) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.impl.StringCollectionSerializer.serializeUsingCustom(java.util.Collection, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider):void");
        /*
        r6 = this;
        r3 = r6._serializer;
        r1 = 0;
        r2 = r7.iterator();
    L_0x0007:
        r5 = r2.hasNext();
        if (r5 == 0) goto L_0x0022;
    L_0x000d:
        r4 = r2.next();
        r4 = (java.lang.String) r4;
        if (r4 != 0) goto L_0x001e;
    L_0x0015:
        r9.defaultSerializeNull(r8);	 Catch:{ Exception -> 0x0019 }
        goto L_0x0007;
    L_0x0019:
        r0 = move-exception;
        r6.wrapAndThrow(r9, r0, r7, r1);
        goto L_0x0007;
    L_0x001e:
        r3.serialize(r4, r8, r9);	 Catch:{ Exception -> 0x0019 }
        goto L_0x0007;
    L_0x0022:
        return;
        */
    }

    protected JsonNode contentSchema() {
        return createSchemaNode("string", true);
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        JsonSerializer<?> ser = provider.findValueSerializer(String.class, this._property);
        if (!isDefaultSerializer(ser)) {
            this._serializer = ser;
        }
    }

    public void serialize(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        jgen.writeEndArray();
    }

    public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForArray(value, jgen);
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForArray(value, jgen);
    }
}