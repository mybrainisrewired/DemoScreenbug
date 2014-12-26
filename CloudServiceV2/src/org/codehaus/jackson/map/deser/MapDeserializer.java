package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class MapDeserializer extends ContainerDeserializer<Map<Object, Object>> implements ResolvableDeserializer {
    protected final Constructor<Map<Object, Object>> _defaultCtor;
    protected HashSet<String> _ignorableProperties;
    protected final KeyDeserializer _keyDeserializer;
    protected final JavaType _mapType;
    protected PropertyBased _propertyBasedCreator;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final TypeDeserializer _valueTypeDeserializer;

    public MapDeserializer(JavaType mapType, Constructor<Map<Object, Object>> defCtor, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(Map.class);
        this._mapType = mapType;
        this._defaultCtor = defCtor;
        this._keyDeserializer = keyDeser;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
    }

    public Map<Object, Object> _deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        PropertyBased creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            t = jp.nextToken();
            if (this._ignorableProperties == null || !this._ignorableProperties.contains(propName)) {
                SettableBeanProperty prop = creator.findCreatorProperty(propName);
                if (prop != null) {
                    if (buffer.assignParameter(prop.getCreatorIndex(), prop.deserialize(jp, ctxt))) {
                        jp.nextToken();
                        try {
                            Map<Object, Object> result = (Map) creator.build(buffer);
                            _readAndBind(jp, ctxt, result);
                            return result;
                        } catch (Exception e) {
                            wrapAndThrow(e, this._mapType.getRawClass());
                            return null;
                        }
                    }
                } else {
                    Object key;
                    Object value;
                    String fieldName = jp.getCurrentName();
                    if (this._keyDeserializer == null) {
                        String key2 = fieldName;
                    } else {
                        key = this._keyDeserializer.deserializeKey(fieldName, ctxt);
                    }
                    if (t == JsonToken.VALUE_NULL) {
                        value = null;
                    } else if (typeDeser == null) {
                        value = valueDes.deserialize(jp, ctxt);
                    } else {
                        value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
                    }
                    buffer.bufferMapProperty(key, value);
                }
            } else {
                jp.skipChildren();
            }
            t = jp.nextToken();
        }
        try {
            return (Map) creator.build(buffer);
        } catch (Exception e2) {
            wrapAndThrow(e2, this._mapType.getRawClass());
            return null;
        }
    }

    protected final void _readAndBind(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        KeyDeserializer keyDes = this._keyDeserializer;
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (t == JsonToken.FIELD_NAME) {
            String fieldName = jp.getCurrentName();
            if (keyDes == null) {
                String key = fieldName;
            } else {
                Object key2 = keyDes.deserializeKey(fieldName, ctxt);
            }
            t = jp.nextToken();
            if (this._ignorableProperties == null || !this._ignorableProperties.contains(fieldName)) {
                Object obj;
                if (t == JsonToken.VALUE_NULL) {
                    obj = null;
                } else if (typeDeser == null) {
                    obj = valueDes.deserialize(jp, ctxt);
                } else {
                    obj = valueDes.deserializeWithType(jp, ctxt, typeDeser);
                }
                result.put(key2, obj);
            } else {
                jp.skipChildren();
            }
            t = jp.nextToken();
        }
    }

    public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
            throw ctxt.mappingException(getMapClass());
        } else if (this._propertyBasedCreator != null) {
            return _deserializeUsingCreator(jp, ctxt);
        } else {
            if (this._defaultCtor == null) {
                throw ctxt.instantiationException(getMapClass(), "No default constructor found");
            }
            try {
                Map<Object, Object> result = (Map) this._defaultCtor.newInstance(new Object[0]);
                _readAndBind(jp, ctxt, result);
                return result;
            } catch (Exception e) {
                throw ctxt.instantiationException(getMapClass(), e);
            }
        }
    }

    public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT || t == JsonToken.FIELD_NAME) {
            _readAndBind(jp, ctxt, result);
            return result;
        } else {
            throw ctxt.mappingException(getMapClass());
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    public JavaType getContentType() {
        return this._mapType.getContentType();
    }

    public final Class<?> getMapClass() {
        return this._mapType.getRawClass();
    }

    public JavaType getValueType() {
        return this._mapType;
    }

    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        if (this._propertyBasedCreator != null) {
            Iterator i$ = this._propertyBasedCreator.properties().iterator();
            while (i$.hasNext()) {
                SettableBeanProperty prop = (SettableBeanProperty) i$.next();
                prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
            }
        }
    }

    public void setCreators(CreatorContainer creators) {
        this._propertyBasedCreator = creators.propertyBasedCreator();
    }

    public void setIgnorableProperties(String[] ignorable) {
        HashSet arrayToSet = (ignorable == null || ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable);
        this._ignorableProperties = arrayToSet;
    }

    protected void wrapAndThrow(Throwable t, Object ref) throws IOException {
        while (t instanceof InvocationTargetException && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        } else if (!t instanceof IOException || t instanceof JsonMappingException) {
            throw JsonMappingException.wrapWithPath(t, ref, null);
        } else {
            throw ((IOException) t);
        }
    }
}