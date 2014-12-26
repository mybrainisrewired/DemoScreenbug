package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.type.JavaType;

public final class SettableAnyProperty {
    protected final BeanProperty _property;
    protected final Method _setter;
    protected final JavaType _type;
    protected JsonDeserializer<Object> _valueDeserializer;

    public SettableAnyProperty(BeanProperty property, AnnotatedMethod setter, JavaType type) {
        this._property = property;
        this._type = type;
        this._setter = setter.getAnnotated();
    }

    private String getClassName() {
        return this._setter.getDeclaringClass().getName();
    }

    protected void _throwAsIOE(Throwable e, String propName, Object value) throws IOException {
        if (e instanceof IllegalArgumentException) {
            String actType = value == null ? "[NULL]" : value.getClass().getName();
            StringBuilder msg = new StringBuilder("Problem deserializing \"any\" property '").append(propName);
            msg.append("' of class " + getClassName() + " (expected type: ").append(this._type);
            msg.append("; actual type: ").append(actType).append(")");
            String origMsg = e.getMessage();
            if (origMsg != null) {
                msg.append(", problem: ").append(origMsg);
            } else {
                msg.append(" (no error message provided)");
            }
            throw new JsonMappingException(msg.toString(), null, e);
        } else if (e instanceof IOException) {
            throw ((IOException) e);
        } else if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        } else {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            throw new JsonMappingException(t.getMessage(), null, t);
        }
    }

    public final Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : this._valueDeserializer.deserialize(jp, ctxt);
    }

    public final void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance, String propName) throws IOException, JsonProcessingException {
        set(instance, propName, deserialize(jp, ctxt));
    }

    public BeanProperty getProperty() {
        return this._property;
    }

    public JavaType getType() {
        return this._type;
    }

    public boolean hasValueDeserializer() {
        return this._valueDeserializer != null;
    }

    public final void set(Object instance, String propName, Object value) throws IOException {
        try {
            this._setter.invoke(instance, new Object[]{propName, value});
        } catch (Exception e) {
            _throwAsIOE(e, propName, value);
        }
    }

    public void setValueDeserializer(JsonDeserializer<Object> deser) {
        if (this._valueDeserializer != null) {
            throw new IllegalStateException("Already had assigned deserializer for SettableAnyProperty");
        }
        this._valueDeserializer = deser;
    }

    public String toString() {
        return "[any property on class " + getClassName() + "]";
    }
}