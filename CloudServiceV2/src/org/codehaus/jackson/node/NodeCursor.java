package org.codehaus.jackson.node;

import java.util.Iterator;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;

abstract class NodeCursor extends JsonStreamContext {
    final NodeCursor _parent;

    protected static final class Array extends NodeCursor {
        Iterator<JsonNode> _contents;
        JsonNode _currentNode;

        public Array(JsonNode n, NodeCursor p) {
            super(1, p);
            this._contents = n.getElements();
        }

        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }

        public JsonNode currentNode() {
            return this._currentNode;
        }

        public JsonToken endToken() {
            return JsonToken.END_ARRAY;
        }

        public String getCurrentName() {
            return null;
        }

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public JsonToken nextToken() {
            if (this._contents.hasNext()) {
                this._currentNode = (JsonNode) this._contents.next();
                return this._currentNode.asToken();
            } else {
                this._currentNode = null;
                return null;
            }
        }

        public JsonToken nextValue() {
            return nextToken();
        }
    }

    protected static final class Object extends NodeCursor {
        Iterator<Entry<String, JsonNode>> _contents;
        Entry<String, JsonNode> _current;
        boolean _needEntry;

        public Object(JsonNode n, NodeCursor p) {
            super(2, p);
            this._contents = ((ObjectNode) n).getFields();
            this._needEntry = true;
        }

        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }

        public JsonNode currentNode() {
            return this._current == null ? null : (JsonNode) this._current.getValue();
        }

        public JsonToken endToken() {
            return JsonToken.END_OBJECT;
        }

        public String getCurrentName() {
            return this._current == null ? null : (String) this._current.getKey();
        }

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public JsonToken nextToken() {
            if (!this._needEntry) {
                this._needEntry = true;
                return ((JsonNode) this._current.getValue()).asToken();
            } else if (this._contents.hasNext()) {
                this._needEntry = false;
                this._current = (Entry) this._contents.next();
                return JsonToken.FIELD_NAME;
            } else {
                this._current = null;
                return null;
            }
        }

        public JsonToken nextValue() {
            JsonToken t = nextToken();
            return t == JsonToken.FIELD_NAME ? nextToken() : t;
        }
    }

    protected static final class RootValue extends NodeCursor {
        protected boolean _done;
        JsonNode _node;

        public RootValue(JsonNode n, NodeCursor p) {
            super(0, p);
            this._done = false;
            this._node = n;
        }

        public boolean currentHasChildren() {
            return false;
        }

        public JsonNode currentNode() {
            return this._node;
        }

        public JsonToken endToken() {
            return null;
        }

        public String getCurrentName() {
            return null;
        }

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public JsonToken nextToken() {
            if (this._done) {
                this._node = null;
                return null;
            } else {
                this._done = true;
                return this._node.asToken();
            }
        }

        public JsonToken nextValue() {
            return nextToken();
        }
    }

    public NodeCursor(int contextType, NodeCursor p) {
        this._type = contextType;
        this._index = -1;
        this._parent = p;
    }

    public abstract boolean currentHasChildren();

    public abstract JsonNode currentNode();

    public abstract JsonToken endToken();

    public abstract String getCurrentName();

    public final NodeCursor getParent() {
        return this._parent;
    }

    public final NodeCursor iterateChildren() {
        JsonNode n = currentNode();
        if (n == null) {
            throw new IllegalStateException("No current node");
        } else if (n.isArray()) {
            return new Array(n, this);
        } else {
            if (n.isObject()) {
                return new Object(n, this);
            }
            throw new IllegalStateException("Current node of type " + n.getClass().getName());
        }
    }

    public abstract JsonToken nextToken();

    public abstract JsonToken nextValue();
}