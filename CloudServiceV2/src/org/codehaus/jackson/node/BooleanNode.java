package org.codehaus.jackson.node;

import com.wmt.data.MediaItem;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class BooleanNode extends ValueNode {
    public static final BooleanNode FALSE;
    public static final BooleanNode TRUE;

    static {
        TRUE = new BooleanNode();
        FALSE = new BooleanNode();
    }

    private BooleanNode() {
    }

    public static BooleanNode getFalse() {
        return FALSE;
    }

    public static BooleanNode getTrue() {
        return TRUE;
    }

    public static BooleanNode valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public JsonToken asToken() {
        return this == TRUE ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    public boolean equals(BooleanNode o) {
        return o == this;
    }

    public boolean getBooleanValue() {
        return this == TRUE;
    }

    public boolean getValueAsBoolean() {
        return this == TRUE;
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return this == TRUE;
    }

    public double getValueAsDouble(double defaultValue) {
        return this == TRUE ? 1.0d : MediaItem.INVALID_LATLNG;
    }

    public int getValueAsInt(int defaultValue) {
        return this == TRUE ? 1 : 0;
    }

    public long getValueAsLong(long defaultValue) {
        return this == TRUE ? 1 : 0;
    }

    public String getValueAsText() {
        return this == TRUE ? "true" : "false";
    }

    public boolean isBoolean() {
        return true;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeBoolean(this == TRUE);
    }
}