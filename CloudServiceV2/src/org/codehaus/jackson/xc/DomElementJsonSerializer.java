package org.codehaus.jackson.xc;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomElementJsonSerializer extends SerializerBase<Element> {
    public DomElementJsonSerializer() {
        super(Element.class);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        o.put("name", createSchemaNode("string"));
        o.put("namespace", createSchemaNode("string", true));
        o.put("attributes", createSchemaNode("array", true));
        o.put("children", createSchemaNode("array", true));
        return o;
    }

    public void serialize(Element value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        int i;
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getTagName());
        if (value.getNamespaceURI() != null) {
            jgen.writeStringField("namespace", value.getNamespaceURI());
        }
        NamedNodeMap attributes = value.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            jgen.writeArrayFieldStart("attributes");
            i = 0;
            while (i < attributes.getLength()) {
                Attr attribute = (Attr) attributes.item(i);
                jgen.writeStartObject();
                jgen.writeStringField("$", attribute.getValue());
                jgen.writeStringField("name", attribute.getName());
                String ns = attribute.getNamespaceURI();
                if (ns != null) {
                    jgen.writeStringField("namespace", ns);
                }
                jgen.writeEndObject();
                i++;
            }
            jgen.writeEndArray();
        }
        NodeList children = value.getChildNodes();
        if (children != null && children.getLength() > 0) {
            jgen.writeArrayFieldStart("children");
            i = 0;
            while (i < children.getLength()) {
                Node child = children.item(i);
                switch (child.getNodeType()) {
                    case LocalAudioAll.SORT_BY_DATE:
                        serialize((Element) child, jgen, provider);
                        break;
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        jgen.writeStartObject();
                        jgen.writeStringField("$", child.getNodeValue());
                        jgen.writeEndObject();
                        break;
                }
                i++;
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }
}