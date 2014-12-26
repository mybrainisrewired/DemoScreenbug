package org.codehaus.jackson.map.deser;

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
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class CollectionDeserializer extends ContainerDeserializer<Collection<Object>> {
    protected final JavaType _collectionType;
    final Constructor<Collection<Object>> _defaultCtor;
    final JsonDeserializer<Object> _valueDeserializer;
    final TypeDeserializer _valueTypeDeserializer;

    public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, Constructor<Collection<Object>> ctor) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
        if (ctor == null) {
            throw new IllegalArgumentException("No default constructor found for container class " + collectionType.getRawClass().getName());
        }
        this._defaultCtor = ctor;
    }

    private final Collection<Object> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<Object> result) throws IOException, JsonProcessingException {
        if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            Object obj;
            JsonDeserializer<Object> valueDes = this._valueDeserializer;
            TypeDeserializer typeDeser = this._valueTypeDeserializer;
            if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
                obj = null;
            } else if (typeDeser == null) {
                obj = valueDes.deserialize(jp, ctxt);
            } else {
                obj = valueDes.deserializeWithType(jp, ctxt, typeDeser);
            }
            result.add(obj);
            return result;
        } else {
            throw ctxt.mappingException(this._collectionType.getRawClass());
        }
    }

    public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return deserialize(jp, ctxt, (Collection) this._defaultCtor.newInstance(new Object[0]));
        } catch (Exception e) {
            throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
        }
    }

    public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<Object> result) throws IOException, JsonProcessingException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt, result);
        }
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            Object obj;
            if (t == JsonToken.VALUE_NULL) {
                obj = null;
            } else if (typeDeser == null) {
                obj = valueDes.deserialize(jp, ctxt);
            } else {
                obj = valueDes.deserializeWithType(jp, ctxt, typeDeser);
            }
            result.add(obj);
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