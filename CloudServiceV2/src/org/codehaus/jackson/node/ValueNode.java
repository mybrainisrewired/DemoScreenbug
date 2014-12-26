package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public abstract class ValueNode extends BaseJsonNode {
    protected ValueNode() {
    }

    public abstract JsonToken asToken();

    public boolean isValueNode() {
        return true;
    }

    public JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    public JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        typeSer.writeTypePrefixForScalar(this, jg);
        serialize(jg, provider);
        typeSer.writeTypeSuffixForScalar(this, jg);
    }

    public String toString() {
        return getValueAsText();
    }
}