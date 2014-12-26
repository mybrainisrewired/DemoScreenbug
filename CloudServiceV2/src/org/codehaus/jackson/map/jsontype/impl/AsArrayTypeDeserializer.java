package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.type.JavaType;

public class AsArrayTypeDeserializer extends TypeDeserializerBase {
    public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property) {
        super(bt, idRes, property);
    }

    private final Object _deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Object value = _findDeserializer(ctxt, _locateTypeId(jp, ctxt)).deserialize(jp, ctxt);
        if (jp.nextToken() == JsonToken.END_ARRAY) {
            return value;
        }
        throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value");
    }

    protected final String _locateTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (!jp.isExpectedStartArrayToken()) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + baseTypeName());
        } else if (jp.nextToken() != JsonToken.VALUE_STRING) {
            throw ctxt.wrongTokenException(jp, JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
        } else {
            String result = jp.getText();
            jp.nextToken();
            return result;
        }
    }

    public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public As getTypeInclusion() {
        return As.WRAPPER_ARRAY;
    }
}