package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;

public final class PropertyValueBuffer {
    private PropertyValue _buffered;
    final DeserializationContext _context;
    final Object[] _creatorParameters;
    private int _paramsNeeded;
    final JsonParser _parser;

    public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount) {
        this._parser = jp;
        this._context = ctxt;
        this._paramsNeeded = paramCount;
        this._creatorParameters = new Object[paramCount];
    }

    public boolean assignParameter(int index, Object value) {
        this._creatorParameters[index] = value;
        int i = this._paramsNeeded - 1;
        this._paramsNeeded = i;
        return i <= 0;
    }

    public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
        this._buffered = new Any(this._buffered, value, prop, propName);
    }

    public void bufferMapProperty(Object key, Object value) {
        this._buffered = new Map(this._buffered, value, key);
    }

    public void bufferProperty(SettableBeanProperty prop, Object value) {
        this._buffered = new Regular(this._buffered, value, prop);
    }

    protected PropertyValue buffered() {
        return this._buffered;
    }

    protected final Object[] getParameters(Object[] defaults) {
        if (defaults != null) {
            int i = 0;
            int len = this._creatorParameters.length;
            while (i < len) {
                if (this._creatorParameters[i] == null) {
                    Object value = defaults[i];
                    if (value != null) {
                        this._creatorParameters[i] = value;
                    }
                }
                i++;
            }
        }
        return this._creatorParameters;
    }
}