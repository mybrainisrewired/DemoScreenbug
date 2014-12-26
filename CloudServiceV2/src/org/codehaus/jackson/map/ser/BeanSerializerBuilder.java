package org.codehaus.jackson.map.ser;

import java.util.List;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;

public class BeanSerializerBuilder {
    private static final BeanPropertyWriter[] NO_PROPERTIES;
    protected AnyGetterWriter _anyGetter;
    protected final BasicBeanDescription _beanDesc;
    protected Object _filterId;
    protected BeanPropertyWriter[] _filteredProperties;
    protected List<BeanPropertyWriter> _properties;

    static {
        NO_PROPERTIES = new BeanPropertyWriter[0];
    }

    public BeanSerializerBuilder(BasicBeanDescription beanDesc) {
        this._beanDesc = beanDesc;
    }

    protected BeanSerializerBuilder(BeanSerializerBuilder src) {
        this._beanDesc = src._beanDesc;
        this._properties = src._properties;
        this._filteredProperties = src._filteredProperties;
        this._anyGetter = src._anyGetter;
        this._filterId = src._filterId;
    }

    public JsonSerializer<?> build() {
        BeanPropertyWriter[] properties = (this._properties == null || this._properties.isEmpty()) ? NO_PROPERTIES : (BeanPropertyWriter[]) this._properties.toArray(new BeanPropertyWriter[this._properties.size()]);
        return new BeanSerializer(this._beanDesc.getType(), properties, this._filteredProperties, this._anyGetter, this._filterId);
    }

    public BeanSerializer createDummy() {
        return BeanSerializer.createDummy(this._beanDesc.getBeanClass());
    }

    public BasicBeanDescription getBeanDescription() {
        return this._beanDesc;
    }

    public BeanPropertyWriter[] getFilteredProperties() {
        return this._filteredProperties;
    }

    public List<BeanPropertyWriter> getProperties() {
        return this._properties;
    }

    public void setAnyGetter(AnyGetterWriter anyGetter) {
        this._anyGetter = anyGetter;
    }

    public void setFilterId(Object filterId) {
        this._filterId = filterId;
    }

    public void setFilteredProperties(BeanPropertyWriter[] properties) {
        this._filteredProperties = properties;
    }

    public void setProperties(List<BeanPropertyWriter> properties) {
        this._properties = properties;
    }
}