package org.codehaus.jackson.map.deser;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;

@JacksonStdImpl
public class UntypedObjectDeserializer extends StdDeserializer<Object> {

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 12;
        }
    }

    public UntypedObjectDeserializer() {
        super(Object.class);
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return jp.getText();
            case ClassWriter.COMPUTE_FRAMES:
                return ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS) ? jp.getBigIntegerValue() : jp.getNumberValue();
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS) ? jp.getDecimalValue() : Double.valueOf(jp.getDoubleValue());
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return Boolean.TRUE;
            case JsonWriteContext.STATUS_EXPECT_NAME:
                return Boolean.FALSE;
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return jp.getEmbeddedObject();
            case Type.LONG:
                return null;
            case Type.DOUBLE:
                return mapArray(jp, ctxt);
            case Type.ARRAY:
            case Type.OBJECT:
                return mapObject(jp, ctxt);
            default:
                throw ctxt.mappingException(Object.class);
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return jp.getText();
            case ClassWriter.COMPUTE_FRAMES:
                return ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS) ? jp.getBigIntegerValue() : Integer.valueOf(jp.getIntValue());
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS) ? jp.getDecimalValue() : Double.valueOf(jp.getDoubleValue());
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return Boolean.TRUE;
            case JsonWriteContext.STATUS_EXPECT_NAME:
                return Boolean.FALSE;
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return jp.getEmbeddedObject();
            case Type.LONG:
                return null;
            case Type.DOUBLE:
            case Type.ARRAY:
            case Type.OBJECT:
                return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
            default:
                throw ctxt.mappingException(Object.class);
        }
    }

    protected List<Object> mapArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.nextToken() == JsonToken.END_ARRAY) {
            return new ArrayList(4);
        }
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] values = buffer.resetAndStart();
        int ptr = 0;
        int totalSize = 0;
        while (true) {
            Object value = deserialize(jp, ctxt);
            totalSize++;
            if (ptr >= values.length) {
                values = buffer.appendCompletedChunk(values);
                ptr = 0;
            }
            int ptr2 = ptr + 1;
            values[ptr] = value;
            if (jp.nextToken() == JsonToken.END_ARRAY) {
                List result = new ArrayList(totalSize >> 3 + totalSize + 1);
                buffer.completeAndClearBuffer(values, ptr2, result);
                return result;
            } else {
                ptr = ptr2;
            }
        }
    }

    protected Map<String, Object> mapObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        if (t != JsonToken.FIELD_NAME) {
            return new LinkedHashMap(4);
        }
        String field1 = jp.getText();
        jp.nextToken();
        Object value1 = deserialize(jp, ctxt);
        Map<String, Object> result;
        if (jp.nextToken() != JsonToken.FIELD_NAME) {
            result = new LinkedHashMap(4);
            result.put(field1, value1);
            return result;
        } else {
            String field2 = jp.getText();
            jp.nextToken();
            Object value2 = deserialize(jp, ctxt);
            if (jp.nextToken() != JsonToken.FIELD_NAME) {
                result = new LinkedHashMap(4);
                result.put(field1, value1);
                result.put(field2, value2);
                return result;
            } else {
                result = new LinkedHashMap();
                result.put(field1, value1);
                result.put(field2, value2);
                do {
                    String fieldName = jp.getText();
                    jp.nextToken();
                    result.put(fieldName, deserialize(jp, ctxt));
                } while (jp.nextToken() != JsonToken.END_OBJECT);
                return result;
            }
        }
    }
}