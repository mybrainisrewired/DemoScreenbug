package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

class StdKeyDeserializers {
    final HashMap<JavaType, KeyDeserializer> _keyDeserializers;

    private StdKeyDeserializers() {
        this._keyDeserializers = new HashMap();
        add(new BoolKD());
        add(new ByteKD());
        add(new CharKD());
        add(new ShortKD());
        add(new IntKD());
        add(new LongKD());
        add(new FloatKD());
        add(new DoubleKD());
    }

    private void add(StdKeyDeserializer kdeser) {
        this._keyDeserializers.put(TypeFactory.defaultInstance().constructType(kdeser.getKeyClass()), kdeser);
    }

    public static HashMap<JavaType, KeyDeserializer> constructAll() {
        return new StdKeyDeserializers()._keyDeserializers;
    }

    public static KeyDeserializer constructEnumKeyDeserializer(DeserializationConfig config, JavaType type) {
        return new EnumKD(EnumResolver.constructUnsafe(type.getRawClass(), config.getAnnotationIntrospector()));
    }

    public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig config, JavaType type) {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        Constructor<?> ctor = beanDesc.findSingleArgConstructor(new Class[]{String.class});
        if (ctor != null) {
            return new StringCtorKeyDeserializer(ctor);
        }
        Method m = beanDesc.findFactoryMethod(new Class[]{String.class});
        return m != null ? new StringFactoryKeyDeserializer(m) : null;
    }
}