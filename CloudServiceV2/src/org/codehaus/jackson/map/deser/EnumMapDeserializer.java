package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.EnumMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;

public final class EnumMapDeserializer extends StdDeserializer<EnumMap<?, ?>> {
    final EnumResolver<?> _enumResolver;
    final JsonDeserializer<Object> _valueDeserializer;

    public EnumMapDeserializer(EnumResolver<?> enumRes, JsonDeserializer<Object> valueDes) {
        super(EnumMap.class);
        this._enumResolver = enumRes;
        this._valueDeserializer = valueDes;
    }

    private EnumMap<?, ?> constructMap() {
        return new EnumMap(this._enumResolver.getEnumClass());
    }

    public EnumMap<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw ctxt.mappingException(EnumMap.class);
        }
        EnumMap result = constructMap();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            Enum<?> key = this._enumResolver.findEnum(jp.getCurrentName());
            if (key == null) {
                throw ctxt.weirdStringException(this._enumResolver.getEnumClass(), "value not one of declared Enum instance names");
            }
            result.put(key, jp.nextToken() == JsonToken.VALUE_NULL ? null : this._valueDeserializer.deserialize(jp, ctxt));
        }
        return result;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }
}