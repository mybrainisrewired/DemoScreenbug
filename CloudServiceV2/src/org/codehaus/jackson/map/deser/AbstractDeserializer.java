package org.codehaus.jackson.map.deser;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;

public class AbstractDeserializer extends JsonDeserializer<Object> {
    protected final JavaType _baseType;

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
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 8;
        }
    }

    public AbstractDeserializer(JavaType bt) {
        this._baseType = bt;
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        throw ctxt.instantiationException(this._baseType.getRawClass(), "abstract types can only be instantiated with additional type information");
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
                return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
            default:
                return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
        }
    }
}