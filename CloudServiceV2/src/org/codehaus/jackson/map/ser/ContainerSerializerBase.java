package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.map.TypeSerializer;

public abstract class ContainerSerializerBase<T> extends SerializerBase<T> {
    protected ContainerSerializerBase(Class t) {
        super(t);
    }

    protected ContainerSerializerBase(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    public abstract ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer typeSerializer);

    public ContainerSerializerBase<?> withValueTypeSerializer(TypeSerializer vts) {
        return vts == null ? this : _withValueTypeSerializer(vts);
    }
}