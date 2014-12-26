package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

public abstract class ContainerDeserializer<T> extends StdDeserializer<T> {
    protected ContainerDeserializer(Class selfType) {
        super(selfType);
    }

    public abstract JsonDeserializer<Object> getContentDeserializer();

    public abstract JavaType getContentType();
}