package org.codehaus.jackson.map;

import java.io.IOException;
import java.util.Iterator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.type.JavaType;

public class MappingIterator<T> implements Iterator<T> {
    protected static final MappingIterator<?> EMPTY_ITERATOR;
    protected final DeserializationContext _context;
    protected final JsonDeserializer<T> _deserializer;
    protected final JsonParser _parser;
    protected final JavaType _type;

    static {
        EMPTY_ITERATOR = new MappingIterator(null, null, null, null);
    }

    protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) {
        this._type = type;
        this._parser = jp;
        this._context = ctxt;
        this._deserializer = deser;
        if (jp != null && jp.getCurrentToken() == JsonToken.START_ARRAY && !jp.getParsingContext().inRoot()) {
            jp.clearCurrentToken();
        }
    }

    protected static <T> MappingIterator<T> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public boolean hasNext() {
        try {
            return hasNextValue();
        } catch (JsonMappingException e) {
            JsonMappingException e2 = e;
            throw new RuntimeJsonMappingException(e2.getMessage(), e2);
        } catch (IOException e3) {
            IOException e4 = e3;
            throw new RuntimeException(e4.getMessage(), e4);
        }
    }

    public boolean hasNextValue() throws IOException {
        if (this._parser == null) {
            return false;
        }
        if (this._parser.getCurrentToken() == null) {
            JsonToken t = this._parser.nextToken();
            if (t == null) {
                this._parser.close();
                return false;
            } else if (t == JsonToken.END_ARRAY) {
                return false;
            }
        }
        return true;
    }

    public T next() {
        try {
            return nextValue();
        } catch (JsonMappingException e) {
            JsonMappingException e2 = e;
            throw new RuntimeJsonMappingException(e2.getMessage(), e2);
        } catch (IOException e3) {
            IOException e4 = e3;
            throw new RuntimeException(e4.getMessage(), e4);
        }
    }

    public T nextValue() throws IOException {
        T result = this._deserializer.deserialize(this._parser, this._context);
        this._parser.clearCurrentToken();
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}