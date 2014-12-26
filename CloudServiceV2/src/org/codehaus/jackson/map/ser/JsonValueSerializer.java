package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public final class JsonValueSerializer extends SerializerBase<Object> implements ResolvableSerializer, SchemaAware {
    protected final Method _accessorMethod;
    protected boolean _forceTypeInformation;
    protected final BeanProperty _property;
    protected JsonSerializer<Object> _valueSerializer;

    public JsonValueSerializer(Method valueMethod, JsonSerializer<Object> ser, BeanProperty property) {
        super(Object.class);
        this._accessorMethod = valueMethod;
        this._valueSerializer = ser;
        this._property = property;
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return this._valueSerializer instanceof SchemaAware ? ((SchemaAware) this._valueSerializer).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean isNaturalTypeWithStdHandling(org.codehaus.jackson.type.JavaType r5_type, org.codehaus.jackson.map.JsonSerializer<?> r6_ser) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.JsonValueSerializer.isNaturalTypeWithStdHandling(org.codehaus.jackson.type.JavaType, org.codehaus.jackson.map.JsonSerializer):boolean");
        /*
        r4 = this;
        r1 = 0;
        r0 = r5.getRawClass();
        r2 = r5.isPrimitive();
        if (r2 == 0) goto L_0x0018;
    L_0x000b:
        r2 = java.lang.Integer.TYPE;
        if (r0 == r2) goto L_0x0028;
    L_0x000f:
        r2 = java.lang.Boolean.TYPE;
        if (r0 == r2) goto L_0x0028;
    L_0x0013:
        r2 = java.lang.Double.TYPE;
        if (r0 == r2) goto L_0x0028;
    L_0x0017:
        return r1;
    L_0x0018:
        r2 = java.lang.String.class;
        if (r0 == r2) goto L_0x0028;
    L_0x001c:
        r2 = java.lang.Integer.class;
        if (r0 == r2) goto L_0x0028;
    L_0x0020:
        r2 = java.lang.Boolean.class;
        if (r0 == r2) goto L_0x0028;
    L_0x0024:
        r2 = java.lang.Double.class;
        if (r0 != r2) goto L_0x0017;
    L_0x0028:
        r2 = r6.getClass();
        r3 = org.codehaus.jackson.map.annotate.JacksonStdImpl.class;
        r2 = r2.getAnnotation(r3);
        if (r2 == 0) goto L_0x0017;
    L_0x0034:
        r1 = 1;
        goto L_0x0017;
        */
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._valueSerializer != null) {
            return;
        }
        if (provider.isEnabled(Feature.USE_STATIC_TYPING) || Modifier.isFinal(this._accessorMethod.getReturnType().getModifiers())) {
            JavaType t = provider.constructType(this._accessorMethod.getGenericReturnType());
            this._valueSerializer = provider.findTypedValueSerializer(t, false, this._property);
            this._forceTypeInformation = isNaturalTypeWithStdHandling(t, this._valueSerializer);
        }
    }

    public void serialize(Object bean, JsonGenerator jgen, SerializerProvider prov) throws IOException, JsonGenerationException {
        Throwable t;
        try {
            Object value = this._accessorMethod.invoke(bean, new Object[0]);
            if (value == null) {
                prov.defaultSerializeNull(jgen);
            } else {
                JsonSerializer<Object> ser = this._valueSerializer;
                if (ser == null) {
                    ser = prov.findTypedValueSerializer(value.getClass(), true, this._property);
                }
                ser.serialize(value, jgen, prov);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e2) {
            t = e2;
            while (t instanceof InvocationTargetException && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
        }
    }

    public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        Throwable t;
        try {
            Object value = this._accessorMethod.invoke(bean, new Object[0]);
            if (value == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                JsonSerializer<Object> ser = this._valueSerializer;
                if (ser != null) {
                    if (this._forceTypeInformation) {
                        typeSer.writeTypePrefixForScalar(bean, jgen);
                    }
                    ser.serializeWithType(value, jgen, provider, typeSer);
                    if (this._forceTypeInformation) {
                        typeSer.writeTypeSuffixForScalar(bean, jgen);
                    }
                } else {
                    provider.findTypedValueSerializer(value.getClass(), true, this._property).serialize(value, jgen, provider);
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e2) {
            t = e2;
            while (t instanceof InvocationTargetException && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
        }
    }

    public String toString() {
        return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
    }
}