package org.codehaus.jackson.xc;

import java.io.IOException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public class XmlAdapterJsonDeserializer extends StdDeserializer<Object> {
    protected static final JavaType ADAPTER_TYPE;
    protected JsonDeserializer<?> _deserializer;
    protected final BeanProperty _property;
    protected final JavaType _valueType;
    protected final XmlAdapter<Object, Object> _xmlAdapter;

    static {
        ADAPTER_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(XmlAdapter.class);
    }

    public XmlAdapterJsonDeserializer(XmlAdapter<Object, Object> xmlAdapter, BeanProperty property) {
        super(Object.class);
        this._property = property;
        this._xmlAdapter = xmlAdapter;
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType[] rawTypes = typeFactory.findTypeParameters(typeFactory.constructType(xmlAdapter.getClass()), XmlAdapter.class);
        JavaType unknownType = (rawTypes == null || rawTypes.length == 0) ? TypeFactory.unknownType() : rawTypes[0];
        this._valueType = unknownType;
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonDeserializer<?> deser = this._deserializer;
        if (deser == null) {
            deser = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), this._valueType, this._property);
            this._deserializer = deser;
        }
        try {
            return this._xmlAdapter.unmarshal(deser.deserialize(jp, ctxt));
        } catch (Exception e) {
            Throwable e2 = e;
            throw new JsonMappingException("Unable to unmarshal (to type " + this._valueType + "): " + e2.getMessage(), e2);
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }
}