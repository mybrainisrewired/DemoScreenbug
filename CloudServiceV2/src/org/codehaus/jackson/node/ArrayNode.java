package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public final class ArrayNode extends ContainerNode {
    protected ArrayList<JsonNode> _children;

    public ArrayNode(JsonNodeFactory nc) {
        super(nc);
    }

    private void _add(JsonNode node) {
        if (this._children == null) {
            this._children = new ArrayList();
        }
        this._children.add(node);
    }

    private void _insert(int index, JsonNode node) {
        if (this._children == null) {
            this._children = new ArrayList();
            this._children.add(node);
        } else if (index < 0) {
            this._children.add(0, node);
        } else if (index >= this._children.size()) {
            this._children.add(node);
        } else {
            this._children.add(index, node);
        }
    }

    private boolean _sameChildren(ArrayList<JsonNode> otherChildren) {
        int len = otherChildren.size();
        if (size() != len) {
            return false;
        }
        int i = 0;
        while (i < len) {
            if (!((JsonNode) this._children.get(i)).equals(otherChildren.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public JsonNode _set(int index, JsonNode value) {
        if (this._children != null && index >= 0 && index < this._children.size()) {
            return (JsonNode) this._children.set(index, value);
        }
        throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
    }

    public void add(double v) {
        _add(numberNode(v));
    }

    public void add(float v) {
        _add(numberNode(v));
    }

    public void add(int v) {
        _add(numberNode(v));
    }

    public void add(long v) {
        _add(numberNode(v));
    }

    public void add(String v) {
        if (v == null) {
            addNull();
        } else {
            _add(textNode(v));
        }
    }

    public void add(BigDecimal v) {
        if (v == null) {
            addNull();
        } else {
            _add(numberNode(v));
        }
    }

    public void add(JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _add(value);
    }

    public void add(boolean v) {
        _add(booleanNode(v));
    }

    public void add(byte[] v) {
        if (v == null) {
            addNull();
        } else {
            _add(binaryNode(v));
        }
    }

    public JsonNode addAll(Collection<JsonNode> nodes) {
        if (nodes.size() > 0) {
            if (this._children == null) {
                this._children = new ArrayList(nodes);
            } else {
                this._children.addAll(nodes);
            }
        }
        return this;
    }

    public JsonNode addAll(ArrayNode other) {
        int len = other.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new ArrayList(len + 2);
            }
            other.addContentsTo(this._children);
        }
        return this;
    }

    public ArrayNode addArray() {
        ArrayNode n = arrayNode();
        _add(n);
        return n;
    }

    protected void addContentsTo(List<JsonNode> dst) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                dst.add((JsonNode) i$.next());
            }
        }
    }

    public void addNull() {
        _add(nullNode());
    }

    public ObjectNode addObject() {
        ObjectNode n = objectNode();
        _add(n);
        return n;
    }

    public void addPOJO(Object value) {
        if (value == null) {
            addNull();
        } else {
            _add(POJONode(value));
        }
    }

    public JsonToken asToken() {
        return JsonToken.START_ARRAY;
    }

    public boolean equals(ArrayNode o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        ArrayNode other = o;
        if (this._children == null || this._children.size() == 0) {
            return other.size() == 0;
        } else {
            return other._sameChildren(this._children);
        }
    }

    public ObjectNode findParent(String fieldName) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                JsonNode parent = ((JsonNode) i$.next()).findParent(fieldName);
                if (parent != null) {
                    return (ObjectNode) parent;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                foundSoFar = ((JsonNode) i$.next()).findParents(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    public JsonNode findValue(String fieldName) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                JsonNode value = ((JsonNode) i$.next()).findValue(fieldName);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                foundSoFar = ((JsonNode) i$.next()).findValues(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                foundSoFar = ((JsonNode) i$.next()).findValuesAsText(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    public JsonNode get(int index) {
        return (index < 0 || this._children == null || index >= this._children.size()) ? null : (JsonNode) this._children.get(index);
    }

    public JsonNode get(String fieldName) {
        return null;
    }

    public Iterator<JsonNode> getElements() {
        return this._children == null ? NoNodesIterator.instance() : this._children.iterator();
    }

    public int hashCode() {
        if (this._children == null) {
            return 1;
        }
        int hash = this._children.size();
        Iterator i$ = this._children.iterator();
        while (i$.hasNext()) {
            JsonNode n = (JsonNode) i$.next();
            if (n != null) {
                hash ^= n.hashCode();
            }
        }
        return hash;
    }

    public void insert(int index, double v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, float v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, int v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, long v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, String v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, textNode(v));
        }
    }

    public void insert(int index, BigDecimal v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, numberNode(v));
        }
    }

    public void insert(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _insert(index, value);
    }

    public void insert(int index, boolean v) {
        _insert(index, booleanNode(v));
    }

    public void insert(int index, byte[] v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, binaryNode(v));
        }
    }

    public ArrayNode insertArray(int index) {
        ArrayNode n = arrayNode();
        _insert(index, n);
        return n;
    }

    public void insertNull(int index) {
        _insert(index, nullNode());
    }

    public ObjectNode insertObject(int index) {
        ObjectNode n = objectNode();
        _insert(index, n);
        return n;
    }

    public void insertPOJO(int index, Object value) {
        if (value == null) {
            insertNull(index);
        } else {
            _insert(index, POJONode(value));
        }
    }

    public boolean isArray() {
        return true;
    }

    public JsonNode path(int index) {
        return (index < 0 || this._children == null || index >= this._children.size()) ? MissingNode.getInstance() : (JsonNode) this._children.get(index);
    }

    public JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    public JsonNode remove(int index) {
        return (index < 0 || this._children == null || index >= this._children.size()) ? null : (JsonNode) this._children.remove(index);
    }

    public ArrayNode removeAll() {
        this._children = null;
        return this;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                ((BaseJsonNode) ((JsonNode) i$.next())).writeTo(jg);
            }
        }
        jg.writeEndArray();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        typeSer.writeTypePrefixForArray(this, jg);
        if (this._children != null) {
            Iterator i$ = this._children.iterator();
            while (i$.hasNext()) {
                ((BaseJsonNode) ((JsonNode) i$.next())).writeTo(jg);
            }
        }
        typeSer.writeTypeSuffixForArray(this, jg);
    }

    public JsonNode set(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        return _set(index, value);
    }

    public int size() {
        return this._children == null ? 0 : this._children.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(size() << 4 + 16);
        sb.append('[');
        if (this._children != null) {
            int i = 0;
            int len = this._children.size();
            while (i < len) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(((JsonNode) this._children.get(i)).toString());
                i++;
            }
        }
        sb.append(']');
        return sb.toString();
    }
}