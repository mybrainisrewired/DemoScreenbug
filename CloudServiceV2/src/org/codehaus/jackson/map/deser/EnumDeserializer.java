package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.util.ClassUtil;

@JsonCachable
public class EnumDeserializer extends StdScalarDeserializer<Enum<?>> {
    final EnumResolver<?> _resolver;

    protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object> {
        protected final Class<?> _enumClass;
        protected final Method _factory;

        public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f) {
            super(Enum.class);
            this._enumClass = cls;
            this._factory = f.getAnnotated();
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
                throw ctxt.mappingException(this._enumClass);
            }
            String value = jp.getText();
            try {
                return this._factory.invoke(this._enumClass, new Object[]{value});
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
                return null;
            }
        }
    }

    public EnumDeserializer(EnumResolver<?> res) {
        super(Enum.class);
        this._resolver = res;
    }

    public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory) {
        if (factory.getParameterType(0) != String.class) {
            throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
        }
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            ClassUtil.checkAndFixAccess(factory.getMember());
        }
        return new FactoryBasedDeserializer(enumClass, factory);
    }

    public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken curr = jp.getCurrentToken();
        Enum<?> result;
        if (curr == JsonToken.VALUE_STRING) {
            result = this._resolver.findEnum(jp.getText());
            if (result != null) {
                return result;
            }
            throw ctxt.weirdStringException(this._resolver.getEnumClass(), "value not one of declared Enum instance names");
        } else if (curr != JsonToken.VALUE_NUMBER_INT) {
            throw ctxt.mappingException(this._resolver.getEnumClass());
        } else if (ctxt.isEnabled(Feature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
            throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
        } else {
            result = this._resolver.getEnum(jp.getIntValue());
            if (result != null) {
                return result;
            }
            throw ctxt.weirdNumberException(this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
        }
    }
}