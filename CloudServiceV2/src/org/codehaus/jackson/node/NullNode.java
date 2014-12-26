package org.codehaus.jackson.node;

import com.wmt.data.MediaItem;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class NullNode extends ValueNode {
    public static final NullNode instance;

    static {
        instance = new NullNode();
    }

    private NullNode() {
    }

    public static NullNode getInstance() {
        return instance;
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NULL;
    }

    public boolean equals(NullNode o) {
        return o == this;
    }

    public double getValueAsDouble(double defaultValue) {
        return MediaItem.INVALID_LATLNG;
    }

    public int getValueAsInt(int defaultValue) {
        return 0;
    }

    public long getValueAsLong(long defaultValue) {
        return 0;
    }

    public String getValueAsText() {
        return "null";
    }

    public boolean isNull() {
        return true;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNull();
    }
}