package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

@JacksonStdImpl
public class EnumSerializer extends ScalarSerializerBase<Enum<?>> {
    protected final EnumValues _values;

    public EnumSerializer(EnumValues v) {
        super(Enum.class, false);
        this._values = v;
    }

    public static EnumSerializer construct(Class<Enum<?>> enumClass, SerializationConfig config, BasicBeanDescription beanDesc) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        return new EnumSerializer(config.isEnabled(Feature.WRITE_ENUMS_USING_TO_STRING) ? EnumValues.constructFromToString(enumClass, intr) : EnumValues.constructFromName(enumClass, intr));
    }

    public EnumValues getEnumValues() {
        return this._values;
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        ObjectNode objectNode = createSchemaNode("string", true);
        if (typeHint != null && provider.constructType(typeHint).isEnumType()) {
            ArrayNode enumNode = objectNode.putArray("enum");
            Iterator i$ = this._values.values().iterator();
            while (i$.hasNext()) {
                enumNode.add(((SerializedString) i$.next()).getValue());
            }
        }
        return objectNode;
    }

    public final void serialize(Enum<?> en, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(this._values.serializedValueFor(en));
    }
}