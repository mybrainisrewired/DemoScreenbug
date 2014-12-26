package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;

public class ThrowableDeserializer extends BeanDeserializer {
    protected static final String PROP_NAME_MESSAGE = "message";

    public ThrowableDeserializer(BeanDeserializer baseDeserializer) {
        super(baseDeserializer);
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(jp, ctxt);
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        if (this._beanType.isAbstract()) {
            throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
        } else if (this._stringCreator == null) {
            throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having either single-String-arg constructor; or explicit @JsonCreator");
        } else {
            int len;
            int i;
            Object throwable = null;
            Object[] pending = null;
            int pendingIx = 0;
            while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
                String propName = jp.getCurrentName();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                jp.nextToken();
                if (prop != null) {
                    if (throwable != null) {
                        prop.deserializeAndSet(jp, ctxt, throwable);
                    } else {
                        if (pending == null) {
                            len = this._beanProperties.size();
                            pending = new Object[(len + len)];
                        }
                        int pendingIx2 = pendingIx + 1;
                        pending[pendingIx] = prop;
                        pendingIx = pendingIx2 + 1;
                        pending[pendingIx2] = prop.deserialize(jp, ctxt);
                    }
                } else if (PROP_NAME_MESSAGE.equals(propName)) {
                    throwable = this._stringCreator.construct(jp.getText());
                    if (pending != null) {
                        i = 0;
                        len = pendingIx;
                        while (i < len) {
                            pending[i].set(throwable, pending[i + 1]);
                            i += 2;
                        }
                        pending = null;
                    }
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    this._anySetter.deserializeAndSet(jp, ctxt, throwable, propName);
                } else {
                    handleUnknownProperty(jp, ctxt, throwable, propName);
                }
                jp.nextToken();
            }
            if (throwable != null) {
                return throwable;
            }
            throwable = this._stringCreator.construct(null);
            if (pending == null) {
                return throwable;
            }
            i = 0;
            len = pendingIx;
            while (i < len) {
                pending[i].set(throwable, pending[i + 1]);
                i += 2;
            }
            return throwable;
        }
    }
}