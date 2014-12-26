package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

abstract class Creator {

    static final class Delegating {
        protected final AnnotatedMember _creator;
        protected final Constructor<?> _ctor;
        protected JsonDeserializer<Object> _deserializer;
        protected final Method _factoryMethod;
        protected final JavaType _valueType;

        public Delegating(BasicBeanDescription beanDesc, AnnotatedConstructor ctor, AnnotatedMethod factory) {
            TypeBindings bindings = beanDesc.bindingsForBeanType();
            if (ctor != null) {
                this._creator = ctor;
                this._ctor = ctor.getAnnotated();
                this._factoryMethod = null;
                this._valueType = bindings.resolveType(ctor.getParameterType(0));
            } else if (factory != null) {
                this._creator = factory;
                this._ctor = null;
                this._factoryMethod = factory.getAnnotated();
                this._valueType = bindings.resolveType(factory.getParameterType(0));
            } else {
                throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
            }
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Object obj = null;
            Object value = this._deserializer.deserialize(jp, ctxt);
            try {
                if (this._ctor != null) {
                    return this._ctor.newInstance(new Object[]{value});
                } else {
                    return this._factoryMethod.invoke(null, new Object[]{value});
                }
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
                return obj;
            }
        }

        public AnnotatedMember getCreator() {
            return this._creator;
        }

        public JavaType getValueType() {
            return this._valueType;
        }

        public void setDeserializer(JsonDeserializer<Object> deser) {
            this._deserializer = deser;
        }
    }

    static final class NumberBased {
        protected final Constructor<?> _intCtor;
        protected final Method _intFactoryMethod;
        protected final Constructor<?> _longCtor;
        protected final Method _longFactoryMethod;
        protected final Class<?> _valueClass;

        public NumberBased(Class<?> valueClass, AnnotatedConstructor intCtor, AnnotatedMethod ifm, AnnotatedConstructor longCtor, AnnotatedMethod lfm) {
            Method method = null;
            this._valueClass = valueClass;
            this._intCtor = intCtor == null ? null : intCtor.getAnnotated();
            this._longCtor = longCtor == null ? null : longCtor.getAnnotated();
            this._intFactoryMethod = ifm == null ? null : ifm.getAnnotated();
            if (lfm != null) {
                method = lfm.getAnnotated();
            }
            this._longFactoryMethod = method;
        }

        public Object construct(int value) {
            try {
                if (this._intCtor != null) {
                    return this._intCtor.newInstance(new Object[]{Integer.valueOf(value)});
                } else {
                    if (this._intFactoryMethod != null) {
                        return this._intFactoryMethod.invoke(this._valueClass, new Object[]{Integer.valueOf(value)});
                    }
                    return construct((long) value);
                }
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
            }
        }

        public Object construct(long value) {
            try {
                if (this._longCtor != null) {
                    return this._longCtor.newInstance(new Object[]{Long.valueOf(value)});
                } else {
                    if (this._longFactoryMethod != null) {
                        return this._longFactoryMethod.invoke(this._valueClass, new Object[]{Long.valueOf(value)});
                    }
                    return null;
                }
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
            }
        }
    }

    static final class PropertyBased {
        protected final Constructor<?> _ctor;
        protected final Object[] _defaultValues;
        protected final Method _factoryMethod;
        protected final HashMap<String, SettableBeanProperty> _properties;

        public PropertyBased(AnnotatedConstructor ctor, SettableBeanProperty[] ctorProps, AnnotatedMethod factory, SettableBeanProperty[] factoryProps) {
            SettableBeanProperty[] props;
            if (ctor != null) {
                this._ctor = ctor.getAnnotated();
                this._factoryMethod = null;
                props = ctorProps;
            } else if (factory != null) {
                this._ctor = null;
                this._factoryMethod = factory.getAnnotated();
                props = factoryProps;
            } else {
                throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
            }
            this._properties = new HashMap();
            Object[] defValues = null;
            int i = 0;
            int len = props.length;
            while (i < len) {
                SettableBeanProperty prop = props[i];
                this._properties.put(prop.getName(), prop);
                if (prop.getType().isPrimitive()) {
                    if (defValues == null) {
                        defValues = new Object[len];
                    }
                    defValues[i] = ClassUtil.defaultValue(prop.getType().getRawClass());
                }
                i++;
            }
            this._defaultValues = defValues;
        }

        public Object build(PropertyValueBuffer buffer) throws Exception {
            Object obj = null;
            try {
                if (this._ctor != null) {
                    obj = this._ctor.newInstance(buffer.getParameters(this._defaultValues));
                } else {
                    obj = this._factoryMethod.invoke(null, buffer.getParameters(this._defaultValues));
                }
                PropertyValue pv = buffer.buffered();
                while (pv != null) {
                    pv.assign(obj);
                    pv = pv.next;
                }
            } catch (Exception e) {
                ClassUtil.throwRootCause(e);
            }
            return obj;
        }

        public SettableBeanProperty findCreatorProperty(String name) {
            return (SettableBeanProperty) this._properties.get(name);
        }

        public Collection<SettableBeanProperty> properties() {
            return this._properties.values();
        }

        public PropertyValueBuffer startBuilding(JsonParser jp, DeserializationContext ctxt) {
            return new PropertyValueBuffer(jp, ctxt, this._properties.size());
        }
    }

    static final class StringBased {
        protected final Constructor<?> _ctor;
        protected final Method _factoryMethod;
        protected final Class<?> _valueClass;

        public StringBased(Class<?> valueClass, AnnotatedConstructor ctor, AnnotatedMethod factoryMethod) {
            Method method = null;
            this._valueClass = valueClass;
            this._ctor = ctor == null ? null : ctor.getAnnotated();
            if (factoryMethod != null) {
                method = factoryMethod.getAnnotated();
            }
            this._factoryMethod = method;
        }

        public Object construct(String value) {
            try {
                if (this._ctor != null) {
                    return this._ctor.newInstance(new Object[]{value});
                } else {
                    if (this._factoryMethod != null) {
                        return this._factoryMethod.invoke(this._valueClass, new Object[]{value});
                    }
                    return null;
                }
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
            }
        }
    }

    private Creator() {
    }
}