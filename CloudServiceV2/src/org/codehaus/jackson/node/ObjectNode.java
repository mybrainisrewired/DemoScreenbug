package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public class ObjectNode extends ContainerNode {
    protected LinkedHashMap<String, JsonNode> _children;

    protected static class NoFieldsIterator implements Iterator<Entry<String, JsonNode>> {
        static final NoFieldsIterator instance;

        static {
            instance = new NoFieldsIterator();
        }

        private NoFieldsIterator() {
        }

        public boolean hasNext() {
            return false;
        }

        public Entry<String, JsonNode> next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    public ObjectNode(JsonNodeFactory nc) {
        super(nc);
        this._children = null;
    }

    private final JsonNode _put(String fieldName, JsonNode value) {
        if (this._children == null) {
            this._children = new LinkedHashMap();
        }
        return (JsonNode) this._children.put(fieldName, value);
    }

    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }

    public boolean equals(ObjectNode o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        ObjectNode other = o;
        if (other.size() != size()) {
            return false;
        }
        if (this._children == null) {
            return true;
        }
        Iterator i$ = this._children.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, JsonNode> en = (Entry) i$.next();
            JsonNode value = (JsonNode) en.getValue();
            JsonNode otherValue = other.get((String) en.getKey());
            if (otherValue != null) {
                if (!otherValue.equals(value)) {
                }
            }
            return false;
        }
        return true;
    }

    public ObjectNode findParent(String fieldName) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> entry = (Entry) i$.next();
                if (fieldName.equals(entry.getKey())) {
                    return this;
                }
                JsonNode value = ((JsonNode) entry.getValue()).findParent(fieldName);
                if (value != null) {
                    return (ObjectNode) value;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> entry = (Entry) i$.next();
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(this);
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findParents(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public JsonNode findValue(String fieldName) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> entry = (Entry) i$.next();
                if (fieldName.equals(entry.getKey())) {
                    return (JsonNode) entry.getValue();
                }
                JsonNode value = ((JsonNode) entry.getValue()).findValue(fieldName);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> entry = (Entry) i$.next();
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(entry.getValue());
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findValues(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> entry = (Entry) i$.next();
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(((JsonNode) entry.getValue()).getValueAsText());
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findValuesAsText(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public JsonNode get(int index) {
        return null;
    }

    public JsonNode get(String fieldName) {
        return this._children != null ? (JsonNode) this._children.get(fieldName) : null;
    }

    public Iterator<JsonNode> getElements() {
        return this._children == null ? NoNodesIterator.instance() : this._children.values().iterator();
    }

    public Iterator<String> getFieldNames() {
        return this._children == null ? NoStringsIterator.instance() : this._children.keySet().iterator();
    }

    public Iterator<Entry<String, JsonNode>> getFields() {
        return this._children == null ? NoFieldsIterator.instance : this._children.entrySet().iterator();
    }

    public int hashCode() {
        return this._children == null ? -1 : this._children.hashCode();
    }

    public boolean isObject() {
        return true;
    }

    public JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    public JsonNode path(String fieldName) {
        if (this._children != null) {
            JsonNode n = (JsonNode) this._children.get(fieldName);
            if (n != null) {
                return n;
            }
        }
        return MissingNode.getInstance();
    }

    public JsonNode put(String fieldName, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        return _put(fieldName, value);
    }

    public void put(String fieldName, double v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, float v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, int v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, long v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, String v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, textNode(v));
        }
    }

    public void put(String fieldName, BigDecimal v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, numberNode(v));
        }
    }

    public void put(String fieldName, boolean v) {
        _put(fieldName, booleanNode(v));
    }

    public void put(String fieldName, byte[] v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, binaryNode(v));
        }
    }

    public JsonNode putAll(Map<String, JsonNode> properties) {
        if (this._children == null) {
            this._children = new LinkedHashMap(properties);
        } else {
            Iterator i$ = properties.entrySet().iterator();
            while (i$.hasNext()) {
                Object n;
                Entry<String, JsonNode> en = (Entry) i$.next();
                JsonNode n2 = (JsonNode) en.getValue();
                if (n2 == null) {
                    n = nullNode();
                }
                this._children.put(en.getKey(), n);
            }
        }
        return this;
    }

    public JsonNode putAll(ObjectNode other) {
        int len = other.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new LinkedHashMap(len);
            }
            other.putContentsTo(this._children);
        }
        return this;
    }

    public ArrayNode putArray(String fieldName) {
        ArrayNode n = arrayNode();
        _put(fieldName, n);
        return n;
    }

    protected void putContentsTo(Map<String, JsonNode> dst) {
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> en = (Entry) i$.next();
                dst.put(en.getKey(), en.getValue());
            }
        }
    }

    public void putNull(String fieldName) {
        _put(fieldName, nullNode());
    }

    public ObjectNode putObject(String fieldName) {
        ObjectNode n = objectNode();
        _put(fieldName, n);
        return n;
    }

    public void putPOJO(String fieldName, Object pojo) {
        _put(fieldName, POJONode(pojo));
    }

    public JsonNode remove(String fieldName) {
        return this._children != null ? (JsonNode) this._children.remove(fieldName) : null;
    }

    public ObjectNode remove(Collection<String> fieldNames) {
        if (this._children != null) {
            Iterator i$ = fieldNames.iterator();
            while (i$.hasNext()) {
                this._children.remove((String) i$.next());
            }
        }
        return this;
    }

    public ObjectNode removeAll() {
        this._children = null;
        return this;
    }

    public ObjectNode retain(Collection<String> fieldNames) {
        if (this._children != null) {
            Iterator<Entry<String, JsonNode>> entries = this._children.entrySet().iterator();
            while (entries.hasNext()) {
                if (!fieldNames.contains(((Entry) entries.next()).getKey())) {
                    entries.remove();
                }
            }
        }
        return this;
    }

    public ObjectNode retain(String... fieldNames) {
        return retain(Arrays.asList(fieldNames));
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeStartObject();
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> en = (Entry) i$.next();
                jg.writeFieldName((String) en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        jg.writeEndObject();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        typeSer.writeTypePrefixForObject(this, jg);
        if (this._children != null) {
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> en = (Entry) i$.next();
                jg.writeFieldName((String) en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        typeSer.writeTypeSuffixForObject(this, jg);
    }

    public int size() {
        return this._children == null ? 0 : this._children.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(size() << 4 + 32);
        sb.append("{");
        if (this._children != null) {
            int count = 0;
            Iterator i$ = this._children.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, JsonNode> en = (Entry) i$.next();
                if (count > 0) {
                    sb.append(",");
                }
                count++;
                TextNode.appendQuoted(sb, (String) en.getKey());
                sb.append(':');
                sb.append(((JsonNode) en.getValue()).toString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public ObjectNode with(String propertyName) {
        if (this._children == null) {
            this._children = new LinkedHashMap();
        } else {
            JsonNode n = (JsonNode) this._children.get(propertyName);
            if (n != null) {
                if (n instanceof ObjectNode) {
                    return (ObjectNode) n;
                }
                throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n.getClass().getName() + ")");
            }
        }
        ObjectNode result = objectNode();
        this._children.put(propertyName, result);
        return result;
    }
}