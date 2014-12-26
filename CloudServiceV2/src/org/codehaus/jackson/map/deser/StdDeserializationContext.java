package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializationProblemHandler;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.LinkedNode;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

public class StdDeserializationContext extends DeserializationContext {
    static final int MAX_ERROR_STR_LEN = 500;
    protected ArrayBuilders _arrayBuilders;
    protected DateFormat _dateFormat;
    protected final DeserializerProvider _deserProvider;
    protected ObjectBuffer _objectBuffer;
    protected JsonParser _parser;

    public StdDeserializationContext(DeserializationConfig config, JsonParser jp, DeserializerProvider prov) {
        super(config);
        this._parser = jp;
        this._deserProvider = prov;
    }

    protected String _calcName(Class<?> cls) {
        return cls.isArray() ? _calcName(cls.getComponentType()) + "[]" : cls.getName();
    }

    protected String _desc(String desc) {
        return desc.length() > 500 ? desc.substring(0, MAX_ERROR_STR_LEN) + "]...[" + desc.substring(desc.length() - 500) : desc;
    }

    protected String _valueDesc() {
        try {
            return _desc(this._parser.getText());
        } catch (Exception e) {
            return "[N/A]";
        }
    }

    public Calendar constructCalendar(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }

    protected String determineClassName(Object instance) {
        return ClassUtil.getClassDescription(instance);
    }

    public final ArrayBuilders getArrayBuilders() {
        if (this._arrayBuilders == null) {
            this._arrayBuilders = new ArrayBuilders();
        }
        return this._arrayBuilders;
    }

    protected DateFormat getDateFormat() {
        if (this._dateFormat == null) {
            this._dateFormat = (DateFormat) this._config.getDateFormat().clone();
        }
        return this._dateFormat;
    }

    public DeserializerProvider getDeserializerProvider() {
        return this._deserProvider;
    }

    public JsonParser getParser() {
        return this._parser;
    }

    public boolean handleUnknownProperty(JsonParser jp, JsonDeserializer<?> deser, Object instanceOrClass, String propName) throws IOException, JsonProcessingException {
        LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
        if (h != null) {
            JsonParser oldParser = this._parser;
            this._parser = jp;
            while (h != null) {
                if (((DeserializationProblemHandler) h.value()).handleUnknownProperty(this, deser, instanceOrClass, propName)) {
                    this._parser = oldParser;
                    return true;
                } else {
                    h = h.next();
                }
            }
            this._parser = oldParser;
        }
        return false;
    }

    public JsonMappingException instantiationException(Class<?> instClass, String msg) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + msg);
    }

    public JsonMappingException instantiationException(Class<?> instClass, Throwable t) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + t.getMessage(), t);
    }

    public final ObjectBuffer leaseObjectBuffer() {
        ObjectBuffer buf = this._objectBuffer;
        if (buf == null) {
            return new ObjectBuffer();
        }
        this._objectBuffer = null;
        return buf;
    }

    public JsonMappingException mappingException(Class<?> targetClass) {
        return JsonMappingException.from(this._parser, "Can not deserialize instance of " + _calcName(targetClass) + " out of " + this._parser.getCurrentToken() + " token");
    }

    public Date parseDate(String dateStr) throws IllegalArgumentException {
        try {
            return getDateFormat().parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public final void returnObjectBuffer(ObjectBuffer buf) {
        if (this._objectBuffer == null || buf.initialCapacity() >= this._objectBuffer.initialCapacity()) {
            this._objectBuffer = buf;
        }
    }

    public JsonMappingException unknownFieldException(Object instanceOrClass, String fieldName) {
        return UnrecognizedPropertyException.from(this._parser, instanceOrClass, fieldName);
    }

    public JsonMappingException unknownTypeException(JavaType type, String id) {
        return JsonMappingException.from(this._parser, "Could not resolve type id '" + id + "' into a subtype of " + type);
    }

    public JsonMappingException weirdKeyException(Class<?> keyClass, String keyValue, String msg) {
        return JsonMappingException.from(this._parser, "Can not construct Map key of type " + keyClass.getName() + " from String \"" + _desc(keyValue) + "\": " + msg);
    }

    public JsonMappingException weirdNumberException(Class<?> instClass, String msg) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from number value (" + _valueDesc() + "): " + msg);
    }

    public JsonMappingException weirdStringException(Class<?> instClass, String msg) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from String value '" + _valueDesc() + "': " + msg);
    }

    public JsonMappingException wrongTokenException(JsonParser jp, JsonToken expToken, String msg) {
        return JsonMappingException.from(jp, "Unexpected token (" + jp.getCurrentToken() + "), expected " + expToken + ": " + msg);
    }
}