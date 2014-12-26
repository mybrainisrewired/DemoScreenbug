package org.codehaus.jackson;

import com.wmt.data.LocalAudioAll;

public abstract class JsonStreamContext {
    protected static final int TYPE_ARRAY = 1;
    protected static final int TYPE_OBJECT = 2;
    protected static final int TYPE_ROOT = 0;
    protected int _index;
    protected int _type;

    protected JsonStreamContext() {
    }

    public final int getCurrentIndex() {
        return this._index < 0 ? 0 : this._index;
    }

    public abstract String getCurrentName();

    public final int getEntryCount() {
        return this._index + 1;
    }

    public abstract JsonStreamContext getParent();

    public final String getTypeDesc() {
        switch (this._type) {
            case LocalAudioAll.SORT_BY_TITLE:
                return "ROOT";
            case TYPE_ARRAY:
                return "ARRAY";
            case TYPE_OBJECT:
                return "OBJECT";
            default:
                return "?";
        }
    }

    public final boolean inArray() {
        return this._type == 1;
    }

    public final boolean inObject() {
        return this._type == 2;
    }

    public final boolean inRoot() {
        return this._type == 0;
    }
}