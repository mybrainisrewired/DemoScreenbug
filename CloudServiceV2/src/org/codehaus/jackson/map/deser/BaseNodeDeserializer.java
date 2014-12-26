package org.codehaus.jackson.map.deser;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;

// compiled from: JsonNodeDeserializer.java
abstract class BaseNodeDeserializer<N extends JsonNode> extends StdDeserializer<N> {

    // compiled from: JsonNodeDeserializer.java
    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 11;
        }
    }

    public BaseNodeDeserializer(Class nodeClass) {
        super(nodeClass);
    }

    protected void _handleDuplicateField(String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
    }

    protected void _reportProblem(JsonParser jp, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg, jp.getTokenLocation());
    }

    protected final JsonNode deserializeAny(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
            case ClassWriter.COMPUTE_FRAMES:
                return deserializeObject(jp, ctxt);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return deserializeArray(jp, ctxt);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return nodeFactory.textNode(jp.getText());
            case JsonWriteContext.STATUS_EXPECT_NAME:
                NumberType nt = jp.getNumberType();
                if (nt == NumberType.BIG_INTEGER || ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS)) {
                    return nodeFactory.numberNode(jp.getBigIntegerValue());
                }
                return nt == NumberType.INT ? nodeFactory.numberNode(jp.getIntValue()) : nodeFactory.numberNode(jp.getLongValue());
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return (jp.getNumberType() == NumberType.BIG_DECIMAL || ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS)) ? nodeFactory.numberNode(jp.getDecimalValue()) : nodeFactory.numberNode(jp.getDoubleValue());
            case Type.LONG:
                return nodeFactory.booleanNode(true);
            case Type.DOUBLE:
                return nodeFactory.booleanNode(false);
            case Type.ARRAY:
                return nodeFactory.nullNode();
            default:
                throw ctxt.mappingException(getValueClass());
        }
    }

    protected final ArrayNode deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayNode node = ctxt.getNodeFactory().arrayNode();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            node.add(deserializeAny(jp, ctxt));
        }
        return node;
    }

    protected final ObjectNode deserializeObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectNode node = ctxt.getNodeFactory().objectNode();
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            JsonNode value = deserializeAny(jp, ctxt);
            JsonNode old = node.put(fieldName, value);
            if (old != null) {
                _handleDuplicateField(fieldName, node, old, value);
            }
            t = jp.nextToken();
        }
        return node;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }
}