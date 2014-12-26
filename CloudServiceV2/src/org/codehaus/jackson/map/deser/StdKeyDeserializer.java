package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;

public abstract class StdKeyDeserializer extends KeyDeserializer {
    protected final Class<?> _keyClass;

    static final class BoolKD extends StdKeyDeserializer {
        BoolKD() {
            super(Boolean.class);
        }

        public Boolean _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if ("true".equals(key)) {
                return Boolean.TRUE;
            }
            if ("false".equals(key)) {
                return Boolean.FALSE;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
        }
    }

    static final class ByteKD extends StdKeyDeserializer {
        ByteKD() {
            super(Byte.class);
        }

        public Byte _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value >= -128 && value <= 127) {
                return Byte.valueOf((byte) value);
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
        }
    }

    static final class CharKD extends StdKeyDeserializer {
        CharKD() {
            super(Character.class);
        }

        public Character _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if (key.length() == 1) {
                return Character.valueOf(key.charAt(0));
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
        }
    }

    static final class DoubleKD extends StdKeyDeserializer {
        DoubleKD() {
            super(Double.class);
        }

        public Double _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Double.valueOf(_parseDouble(key));
        }
    }

    static final class EnumKD extends StdKeyDeserializer {
        final EnumResolver<?> _resolver;

        EnumKD(EnumResolver<?> er) {
            super(er.getEnumClass());
            this._resolver = er;
        }

        public Enum<?> _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            Enum<?> e = this._resolver.findEnum(key);
            if (e != null) {
                return e;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
        }
    }

    static final class FloatKD extends StdKeyDeserializer {
        FloatKD() {
            super(Float.class);
        }

        public Float _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Float.valueOf((float) _parseDouble(key));
        }
    }

    static final class IntKD extends StdKeyDeserializer {
        IntKD() {
            super(Integer.class);
        }

        public Integer _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Integer.valueOf(_parseInt(key));
        }
    }

    static final class LongKD extends StdKeyDeserializer {
        LongKD() {
            super(Long.class);
        }

        public Long _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Long.valueOf(_parseLong(key));
        }
    }

    static final class ShortKD extends StdKeyDeserializer {
        ShortKD() {
            super(Integer.class);
        }

        public Short _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value >= -32768 && value <= 32767) {
                return Short.valueOf((short) value);
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
        }
    }

    static final class StringCtorKeyDeserializer extends StdKeyDeserializer {
        final Constructor<?> _ctor;

        public StringCtorKeyDeserializer(Constructor<?> ctor) {
            super(ctor.getDeclaringClass());
            this._ctor = ctor;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._ctor.newInstance(new Object[]{key});
        }
    }

    static final class StringFactoryKeyDeserializer extends StdKeyDeserializer {
        final Method _factoryMethod;

        public StringFactoryKeyDeserializer(Method fm) {
            super(fm.getDeclaringClass());
            this._factoryMethod = fm;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._factoryMethod.invoke(null, new Object[]{key});
        }
    }

    protected StdKeyDeserializer(Class<?> cls) {
        this._keyClass = cls;
    }

    protected abstract Object _parse(String str, DeserializationContext deserializationContext) throws Exception;

    protected double _parseDouble(String key) throws IllegalArgumentException {
        return NumberInput.parseDouble(key);
    }

    protected int _parseInt(String key) throws IllegalArgumentException {
        return Integer.parseInt(key);
    }

    protected long _parseLong(String key) throws IllegalArgumentException {
        return Long.parseLong(key);
    }

    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (key == null) {
            return null;
        }
        try {
            Object result = _parse(key, ctxt);
            if (result != null) {
                return result;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
        } catch (Exception e) {
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + e.getMessage());
        }
    }

    public Class<?> getKeyClass() {
        return this._keyClass;
    }
}