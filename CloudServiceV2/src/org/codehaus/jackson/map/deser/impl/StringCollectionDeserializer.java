package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.deser.ContainerDeserializer;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public final class StringCollectionDeserializer extends ContainerDeserializer<Collection<String>> {
    protected final JavaType _collectionType;
    final Constructor<Collection<String>> _defaultCtor;
    protected final boolean _isDefaultDeserializer;
    protected final JsonDeserializer<String> _valueDeserializer;

    public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, Constructor<?> ctor) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeser;
        this._defaultCtor = ctor;
        this._isDefaultDeserializer = isDefaultSerializer(valueDeser);
    }

    private Collection<String> deserializeUsingCustom(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        JsonDeserializer<String> deser = this._valueDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            String value;
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = deser.deserialize(jp, ctxt);
            }
            result.add(value);
        }
    }

    private final Collection<String> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            String value;
            JsonDeserializer<String> valueDes = this._valueDeserializer;
            if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = valueDes == null ? jp.getText() : (String) valueDes.deserialize(jp, ctxt);
            }
            result.add(value);
            return result;
        } else {
            throw ctxt.mappingException(this._collectionType.getRawClass());
        }
    }

    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return deserialize(jp, ctxt, (Collection) this._defaultCtor.newInstance(new Object[0]));
        } catch (Exception e) {
            throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
        }
    }

    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt, result);
        }
        if (!this._isDefaultDeserializer) {
            return deserializeUsingCustom(jp, ctxt, result);
        }
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            result.add(t == JsonToken.VALUE_NULL ? null : jp.getText());
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    public JavaType getContentType() {
        return this._collectionType.getContentType();
    }
}