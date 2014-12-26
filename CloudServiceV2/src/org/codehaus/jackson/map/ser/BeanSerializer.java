package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonMappingException.Reference;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public class BeanSerializer extends SerializerBase<Object> implements ResolvableSerializer, SchemaAware {
    protected static final BeanPropertyWriter[] NO_PROPS;
    protected final AnyGetterWriter _anyGetterWriter;
    protected final BeanPropertyWriter[] _filteredProps;
    protected final Object _propertyFilterId;
    protected final BeanPropertyWriter[] _props;

    static {
        NO_PROPS = new BeanPropertyWriter[0];
    }

    public BeanSerializer(Class rawType, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId) {
        super(rawType);
        this._props = properties;
        this._filteredProps = filteredProperties;
        this._anyGetterWriter = anyGetterWriter;
        this._propertyFilterId = filterId;
    }

    protected BeanSerializer(BeanSerializer src) {
        this(src._handledType, src._props, src._filteredProps, src._anyGetterWriter, src._propertyFilterId);
    }

    public BeanSerializer(JavaType type, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId) {
        super(type);
        this._props = properties;
        this._filteredProps = filteredProperties;
        this._anyGetterWriter = anyGetterWriter;
        this._propertyFilterId = filterId;
    }

    public static BeanSerializer createDummy(Class<?> forType) {
        return new BeanSerializer((Class)forType, NO_PROPS, null, null, null);
    }

    protected BeanPropertyFilter findFilter(SerializerProvider provider) throws JsonMappingException {
        Object filterId = this._propertyFilterId;
        FilterProvider filters = provider.getFilterProvider();
        if (filters == null) {
            throw new JsonMappingException("Can not resolve BeanPropertyFilter with id '" + filterId + "'; no FilterProvider configured");
        }
        BeanPropertyFilter filter = filters.findFilter(filterId);
        if (filter != null) {
            return filter;
        }
        throw new JsonMappingException("No filter configured with id '" + filterId + "' (type " + filterId.getClass().getName() + ")");
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        JsonNode propertiesNode = o.objectNode();
        int i = 0;
        while (i < this._props.length) {
            BeanProperty prop = this._props[i];
            JavaType propType = prop.getSerializationType();
            Type hint = propType == null ? prop.getGenericPropertyType() : propType.getRawClass();
            JsonSerializer<Object> ser = prop.getSerializer();
            if (ser == null) {
                Class<?> serType = prop.getRawSerializationType();
                if (serType == null) {
                    serType = prop.getPropertyType();
                }
                ser = provider.findValueSerializer(serType, prop);
            }
            propertiesNode.put(prop.getName(), ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(provider, hint) : JsonSchema.getDefaultSchemaNode());
            i++;
        }
        o.put("properties", propertiesNode);
        return o;
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        int filteredCount = this._filteredProps == null ? 0 : this._filteredProps.length;
        int i = 0;
        int len = this._props.length;
        while (i < len) {
            BeanProperty prop = this._props[i];
            if (!prop.hasSerializer()) {
                JavaType type = prop.getSerializationType();
                if (type == null) {
                    type = provider.constructType(prop.getGenericPropertyType());
                    if (!type.isFinal()) {
                        if (type.isContainerType() || type.containedTypeCount() > 0) {
                            prop.setNonTrivialBaseType(type);
                        }
                    }
                }
                JsonSerializer<Object> ser = provider.findValueSerializer(type, prop);
                if (type.isContainerType()) {
                    TypeSerializer typeSer = (TypeSerializer) type.getContentType().getTypeHandler();
                    if (typeSer != null && ser instanceof ContainerSerializerBase) {
                        ser = ((ContainerSerializerBase) ser).withValueTypeSerializer(typeSer);
                    }
                }
                this._props[i] = prop.withSerializer(ser);
                if (i < filteredCount) {
                    BeanPropertyWriter w2 = this._filteredProps[i];
                    if (w2 != null) {
                        this._filteredProps[i] = w2.withSerializer(ser);
                    }
                }
            }
            i++;
        }
        if (this._anyGetterWriter != null) {
            this._anyGetterWriter.resolve(provider);
        }
    }

    public final void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, jgen, provider);
        } else {
            serializeFields(bean, jgen, provider);
        }
        jgen.writeEndObject();
    }

    protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getSerializationView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    prop.serializeAsField(bean, jgen, provider);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
            }
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, i == props.length ? "[anySetter]" : props[i].getName());
        } catch (StackOverflowError e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
            mapE.prependPath(new Reference(bean, i == props.length ? "[anySetter]" : props[i].getName()));
            throw mapE;
        }
    }

    protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] props;
        if (this._filteredProps == null || provider.getSerializationView() == null) {
            props = this._props;
        } else {
            props = this._filteredProps;
        }
        BeanPropertyFilter filter = findFilter(provider);
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    filter.serializeAsField(bean, jgen, provider, prop);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
            }
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, i == props.length ? "[anySetter]" : props[i].getName());
        } catch (StackOverflowError e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
            mapE.prependPath(new Reference(bean, i == props.length ? "[anySetter]" : props[i].getName()));
            throw mapE;
        }
    }

    public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForObject(bean, jgen);
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, jgen, provider);
        } else {
            serializeFields(bean, jgen, provider);
        }
        typeSer.writeTypeSuffixForObject(bean, jgen);
    }

    public String toString() {
        return "BeanSerializer for " + handledType().getName();
    }
}