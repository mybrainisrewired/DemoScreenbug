package org.codehaus.jackson.map.ser.impl;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.FilterProvider;

public class SimpleFilterProvider extends FilterProvider {
    protected BeanPropertyFilter _defaultFilter;
    protected final Map<String, BeanPropertyFilter> _filtersById;

    public SimpleFilterProvider() {
        this._filtersById = new HashMap();
    }

    public SimpleFilterProvider(Map<String, BeanPropertyFilter> mapping) {
        this._filtersById = new HashMap();
    }

    public SimpleFilterProvider addFilter(String id, BeanPropertyFilter filter) {
        this._filtersById.put(id, filter);
        return this;
    }

    public BeanPropertyFilter findFilter(Object filterId) {
        BeanPropertyFilter f = (BeanPropertyFilter) this._filtersById.get(filterId);
        return f == null ? this._defaultFilter : f;
    }

    public BeanPropertyFilter removeFilter(String id) {
        return (BeanPropertyFilter) this._filtersById.remove(id);
    }

    public SimpleFilterProvider setDefaultFilter(BeanPropertyFilter f) {
        this._defaultFilter = f;
        return this;
    }
}