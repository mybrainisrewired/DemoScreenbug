package org.codehaus.jackson.xc;

import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomElementJsonDeserializer extends StdDeserializer<Element> {
    private final DocumentBuilder builder;

    public DomElementJsonDeserializer() {
        super(Element.class);
        try {
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setNamespaceAware(true);
            this.builder = bf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        }
    }

    public DomElementJsonDeserializer(DocumentBuilder builder) {
        super(Element.class);
        this.builder = builder;
    }

    public Element deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return fromNode(this.builder.newDocument(), jp.readValueAsTree());
    }

    protected Element fromNode(Document document, JsonNode jsonNode) throws IOException {
        String ns;
        String name;
        if (jsonNode.get("namespace") != null) {
            ns = jsonNode.get("namespace").getValueAsText();
        } else {
            ns = null;
        }
        if (jsonNode.get("name") != null) {
            name = jsonNode.get("name").getValueAsText();
        } else {
            name = null;
        }
        if (name == null) {
            throw new JsonMappingException("No name for DOM element was provided in the JSON object.");
        }
        JsonNode node;
        String value;
        Element element = document.createElementNS(ns, name);
        JsonNode attributesNode = jsonNode.get("attributes");
        if (attributesNode != null && attributesNode instanceof ArrayNode) {
            Iterator<JsonNode> atts = attributesNode.getElements();
            while (atts.hasNext()) {
                node = (JsonNode) atts.next();
                if (node.get("namespace") != null) {
                    ns = node.get("namespace").getValueAsText();
                } else {
                    ns = null;
                }
                if (node.get("name") != null) {
                    name = node.get("name").getValueAsText();
                } else {
                    name = null;
                }
                if (node.get("$") != null) {
                    value = node.get("$").getValueAsText();
                } else {
                    value = null;
                }
                if (name != null) {
                    element.setAttributeNS(ns, name, value);
                }
            }
        }
        JsonNode childsNode = jsonNode.get("children");
        if (childsNode != null && childsNode instanceof ArrayNode) {
            Iterator<JsonNode> els = childsNode.getElements();
            while (els.hasNext()) {
                node = els.next();
                if (node.get("name") != null) {
                    name = node.get("name").getValueAsText();
                } else {
                    name = null;
                }
                if (node.get("$") != null) {
                    value = node.get("$").getValueAsText();
                } else {
                    value = null;
                }
                if (value != null) {
                    element.appendChild(document.createTextNode(value));
                } else if (name != null) {
                    element.appendChild(fromNode(document, node));
                }
            }
        }
        return element;
    }
}