package org.codehaus.jackson.map.deser;

import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.deser.SettableBeanProperty.ManagedReferenceProperty;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

@JsonCachable
public class BeanDeserializer extends StdDeserializer<Object> implements ResolvableDeserializer {
    protected final SettableAnyProperty _anySetter;
    protected final Map<String, SettableBeanProperty> _backRefs;
    protected final BeanPropertyMap _beanProperties;
    protected final JavaType _beanType;
    protected final Constructor<?> _defaultConstructor;
    protected final Delegating _delegatingCreator;
    protected final AnnotatedClass _forClass;
    protected final HashSet<String> _ignorableProps;
    protected final boolean _ignoreAllUnknown;
    protected final NumberBased _numberCreator;
    protected final BeanProperty _property;
    protected final PropertyBased _propertyBasedCreator;
    protected final StringBased _stringCreator;
    protected HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
        }
    }

    protected BeanDeserializer(BeanDeserializer src) {
        super(src._beanType);
        this._forClass = src._forClass;
        this._beanType = src._beanType;
        this._property = src._property;
        this._beanProperties = src._beanProperties;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        this._ignoreAllUnknown = src._ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._defaultConstructor = src._defaultConstructor;
        this._stringCreator = src._stringCreator;
        this._numberCreator = src._numberCreator;
        this._delegatingCreator = src._delegatingCreator;
        this._propertyBasedCreator = src._propertyBasedCreator;
    }

    public BeanDeserializer(AnnotatedClass forClass, JavaType type, BeanProperty property, CreatorContainer creators, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, SettableAnyProperty anySetter) {
        super(type);
        this._forClass = forClass;
        this._beanType = type;
        this._property = property;
        this._beanProperties = properties;
        this._backRefs = backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = anySetter;
        this._stringCreator = creators.stringCreator();
        this._numberCreator = creators.numberCreator();
        this._delegatingCreator = creators.delegatingCreator();
        this._propertyBasedCreator = creators.propertyBasedCreator();
        if (this._delegatingCreator == null && this._propertyBasedCreator == null) {
            this._defaultConstructor = creators.getDefaultConstructor();
        } else {
            this._defaultConstructor = null;
        }
    }

    protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Object bean;
        PropertyBased creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
        TokenBuffer unknown = null;
        JsonToken t = jp.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = creator.findCreatorProperty(propName);
            if (prop != null) {
                if (buffer.assignParameter(prop.getCreatorIndex(), prop.deserialize(jp, ctxt))) {
                    jp.nextToken();
                    try {
                        bean = creator.build(buffer);
                        if (bean.getClass() != this._beanType.getRawClass()) {
                            return handlePolymorphic(jp, ctxt, bean, unknown);
                        }
                        if (unknown != null) {
                            bean = handleUnknownProperties(ctxt, bean, unknown);
                        }
                        return deserialize(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
                    }
                }
            } else {
                prop = this._beanProperties.find(propName);
                if (prop != null) {
                    buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
                } else {
                    if (unknown == null) {
                        unknown = new TokenBuffer(jp.getCodec());
                    }
                    unknown.writeFieldName(propName);
                    unknown.copyCurrentStructure(jp);
                }
            }
            t = jp.nextToken();
        }
        try {
            bean = creator.build(buffer);
            if (unknown != null) {
                return bean.getClass() != this._beanType.getRawClass() ? handlePolymorphic(null, ctxt, bean, unknown) : handleUnknownProperties(ctxt, bean, unknown);
            } else {
                return bean;
            }
        } catch (Exception e2) {
            wrapInstantiationProblem(e2, ctxt);
            return null;
        }
    }

    protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        synchronized (this) {
            JsonDeserializer<Object> subDeser = this._subDeserializers == null ? null : (JsonDeserializer) this._subDeserializers.get(new ClassKey(bean.getClass()));
        }
        if (subDeser != null) {
            return subDeser;
        }
        DeserializerProvider deserProv = ctxt.getDeserializerProvider();
        if (deserProv != null) {
            subDeser = deserProv.findValueDeserializer(ctxt.getConfig(), ctxt.constructType(bean.getClass()), this._property);
            if (subDeser != null) {
                synchronized (this) {
                    if (this._subDeserializers == null) {
                        this._subDeserializers = new HashMap();
                    }
                    this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
                }
            }
        }
        return subDeser;
    }

    protected Object constructDefaultInstance() {
        try {
            return this._defaultConstructor.newInstance(new Object[0]);
        } catch (Exception e) {
            ClassUtil.unwrapAndThrowAsIAE(e);
            return null;
        }
    }

    public final Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            jp.nextToken();
            return deserializeFromObject(jp, ctxt);
        } else {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    return deserializeFromString(jp, ctxt);
                case ClassWriter.COMPUTE_FRAMES:
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    return deserializeFromNumber(jp, ctxt);
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    return jp.getEmbeddedObject();
                case JsonWriteContext.STATUS_EXPECT_NAME:
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                case Type.LONG:
                    return deserializeUsingCreator(jp, ctxt);
                case Type.DOUBLE:
                case Type.ARRAY:
                    return deserializeFromObject(jp, ctxt);
                default:
                    throw ctxt.mappingException(getBeanClass());
            }
        }
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            jp.nextToken();
            if (prop != null) {
                try {
                    prop.deserializeAndSet(jp, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow(e, bean, propName, ctxt);
                }
            } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                jp.skipChildren();
            } else if (this._anySetter != null) {
                this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
            } else {
                handleUnknownProperty(jp, ctxt, bean, propName);
            }
            t = jp.nextToken();
        }
        return bean;
    }

    public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._numberCreator != null) {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    return this._numberCreator.construct(jp.getIntValue());
                case ClassWriter.COMPUTE_FRAMES:
                    return this._numberCreator.construct(jp.getLongValue());
            }
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON Number");
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._defaultConstructor != null) {
            Object bean = constructDefaultInstance();
            while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
                String propName = jp.getCurrentName();
                jp.nextToken();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    try {
                        prop.deserializeAndSet(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow(e, bean, propName, ctxt);
                    }
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                    } catch (Exception e2) {
                        wrapAndThrow(e2, bean, propName, ctxt);
                    }
                } else {
                    handleUnknownProperty(jp, ctxt, bean, propName);
                }
                jp.nextToken();
            }
            return bean;
        } else if (this._propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(jp, ctxt);
        } else {
            if (this._delegatingCreator != null) {
                return this._delegatingCreator.deserialize(jp, ctxt);
            }
            if (this._beanType.isAbstract()) {
                throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
            }
            throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
        }
    }

    public Object deserializeFromString(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._stringCreator != null) {
            return this._stringCreator.construct(jp.getText());
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        if (ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getTextLength() == 0) {
            return null;
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON String");
    }

    public Object deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._delegatingCreator != null) {
            try {
                return this._delegatingCreator.deserialize(jp, ctxt);
            } catch (Exception e) {
                wrapInstantiationProblem(e, ctxt);
            }
        }
        throw ctxt.mappingException(getBeanClass());
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public SettableBeanProperty findBackReference(String logicalName) {
        return this._backRefs == null ? null : (SettableBeanProperty) this._backRefs.get(logicalName);
    }

    public final Class<?> getBeanClass() {
        return this._beanType.getRawClass();
    }

    public int getPropertyCount() {
        return this._beanProperties.size();
    }

    public JavaType getValueType() {
        return this._beanType;
    }

    protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
        if (subDeser != null) {
            if (unknownTokens != null) {
                unknownTokens.writeEndObject();
                JsonParser p2 = unknownTokens.asParser();
                p2.nextToken();
                bean = subDeser.deserialize(p2, ctxt, bean);
            }
            if (jp != null) {
                bean = subDeser.deserialize(jp, ctxt, bean);
            }
            return bean;
        } else {
            if (unknownTokens != null) {
                bean = handleUnknownProperties(ctxt, bean, unknownTokens);
            }
            if (jp != null) {
                bean = deserialize(jp, ctxt, bean);
            }
            return bean;
        }
    }

    protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        unknownTokens.writeEndObject();
        JsonParser bufferParser = unknownTokens.asParser();
        while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
            String propName = bufferParser.getCurrentName();
            bufferParser.nextToken();
            handleUnknownProperty(bufferParser, ctxt, bean, propName);
        }
        return bean;
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException, JsonProcessingException {
        if (this._ignoreAllUnknown || (this._ignorableProps != null && this._ignorableProps.contains(propName))) {
            jp.skipChildren();
        } else {
            super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
        }
    }

    public boolean hasProperty(String propertyName) {
        return this._beanProperties.find(propertyName) != null;
    }

    public Iterator<SettableBeanProperty> properties() {
        if (this._beanProperties != null) {
            return this._beanProperties.allProperties();
        }
        throw new IllegalStateException("Can only call before BeanDeserializer has been resolved");
    }

    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        Iterator<SettableBeanProperty> it = this._beanProperties.allProperties();
        while (it.hasNext()) {
            SettableBeanProperty prop = (SettableBeanProperty) it.next();
            if (!prop.hasValueDeserializer()) {
                prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
            }
            String refName = prop.getManagedReferenceName();
            if (refName != null) {
                SettableBeanProperty backProp;
                JsonDeserializer<?> valueDeser = prop._valueDeserializer;
                boolean isContainer = false;
                if (valueDeser instanceof BeanDeserializer) {
                    backProp = ((BeanDeserializer) valueDeser).findBackReference(refName);
                } else if (valueDeser instanceof ContainerDeserializer) {
                    JsonDeserializer<?> contentDeser = ((ContainerDeserializer) valueDeser).getContentDeserializer();
                    if (contentDeser instanceof BeanDeserializer) {
                        backProp = ((BeanDeserializer) contentDeser).findBackReference(refName);
                        isContainer = true;
                    } else {
                        throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': value deserializer is of type ContainerDeserializer, but content type is not handled by a BeanDeserializer " + " (instead it's of type " + contentDeser.getClass().getName() + ")");
                    }
                } else if (valueDeser instanceof AbstractDeserializer) {
                    throw new IllegalArgumentException("Can not handle managed/back reference for abstract types (property " + this._beanType.getRawClass().getName() + "." + prop.getName() + ")");
                } else {
                    throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': type for value deserializer is not BeanDeserializer or ContainerDeserializer, but " + valueDeser.getClass().getName());
                }
                if (backProp == null) {
                    throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
                }
                JavaType referredType = this._beanType;
                JavaType backRefType = backProp.getType();
                if (backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
                    this._beanProperties.replace(new ManagedReferenceProperty(refName, prop, backProp, this._forClass.getAnnotations(), isContainer));
                } else {
                    throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
                }
            }
        }
        if (!(this._anySetter == null || this._anySetter.hasValueDeserializer())) {
            this._anySetter.setValueDeserializer(findDeserializer(config, provider, this._anySetter.getType(), this._anySetter.getProperty()));
        }
        if (this._delegatingCreator != null) {
            DeserializationConfig deserializationConfig = config;
            DeserializerProvider deserializerProvider = provider;
            this._delegatingCreator.setDeserializer(findDeserializer(deserializationConfig, deserializerProvider, this._delegatingCreator.getValueType(), new Std(null, this._delegatingCreator.getValueType(), this._forClass.getAnnotations(), this._delegatingCreator.getCreator())));
        }
        if (this._propertyBasedCreator != null) {
            Iterator i$ = this._propertyBasedCreator.properties().iterator();
            while (i$.hasNext()) {
                prop = i$.next();
                if (!prop.hasValueDeserializer()) {
                    prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
                }
            }
        }
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index) throws IOException {
        wrapAndThrow(t, bean, index, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void wrapAndThrow(java.lang.Throwable r3_t, java.lang.Object r4_bean, int r5_index, org.codehaus.jackson.map.DeserializationContext r6_ctxt) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.BeanDeserializer.wrapAndThrow(java.lang.Throwable, java.lang.Object, int, org.codehaus.jackson.map.DeserializationContext):void");
        /*
        r2 = this;
    L_0x0000:
        r1 = r3 instanceof java.lang.reflect.InvocationTargetException;
        if (r1 == 0) goto L_0x000f;
    L_0x0004:
        r1 = r3.getCause();
        if (r1 == 0) goto L_0x000f;
    L_0x000a:
        r3 = r3.getCause();
        goto L_0x0000;
    L_0x000f:
        r1 = r3 instanceof java.lang.Error;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r3 = (java.lang.Error) r3;
        throw r3;
    L_0x0016:
        if (r6 == 0) goto L_0x0020;
    L_0x0018:
        r1 = org.codehaus.jackson.map.DeserializationConfig.Feature.WRAP_EXCEPTIONS;
        r1 = r6.isEnabled(r1);
        if (r1 == 0) goto L_0x002e;
    L_0x0020:
        r0 = 1;
    L_0x0021:
        r1 = r3 instanceof java.io.IOException;
        if (r1 == 0) goto L_0x0030;
    L_0x0025:
        if (r0 == 0) goto L_0x002b;
    L_0x0027:
        r1 = r3 instanceof org.codehaus.jackson.map.JsonMappingException;
        if (r1 != 0) goto L_0x0039;
    L_0x002b:
        r3 = (java.io.IOException) r3;
        throw r3;
    L_0x002e:
        r0 = 0;
        goto L_0x0021;
    L_0x0030:
        if (r0 != 0) goto L_0x0039;
    L_0x0032:
        r1 = r3 instanceof java.lang.RuntimeException;
        if (r1 == 0) goto L_0x0039;
    L_0x0036:
        r3 = (java.lang.RuntimeException) r3;
        throw r3;
    L_0x0039:
        r1 = org.codehaus.jackson.map.JsonMappingException.wrapWithPath(r3, r4, r5);
        throw r1;
        */
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, String fieldName) throws IOException {
        wrapAndThrow(t, bean, fieldName, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void wrapAndThrow(java.lang.Throwable r3_t, java.lang.Object r4_bean, java.lang.String r5_fieldName, org.codehaus.jackson.map.DeserializationContext r6_ctxt) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.BeanDeserializer.wrapAndThrow(java.lang.Throwable, java.lang.Object, java.lang.String, org.codehaus.jackson.map.DeserializationContext):void");
        /*
        r2 = this;
    L_0x0000:
        r1 = r3 instanceof java.lang.reflect.InvocationTargetException;
        if (r1 == 0) goto L_0x000f;
    L_0x0004:
        r1 = r3.getCause();
        if (r1 == 0) goto L_0x000f;
    L_0x000a:
        r3 = r3.getCause();
        goto L_0x0000;
    L_0x000f:
        r1 = r3 instanceof java.lang.Error;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r3 = (java.lang.Error) r3;
        throw r3;
    L_0x0016:
        if (r6 == 0) goto L_0x0020;
    L_0x0018:
        r1 = org.codehaus.jackson.map.DeserializationConfig.Feature.WRAP_EXCEPTIONS;
        r1 = r6.isEnabled(r1);
        if (r1 == 0) goto L_0x002e;
    L_0x0020:
        r0 = 1;
    L_0x0021:
        r1 = r3 instanceof java.io.IOException;
        if (r1 == 0) goto L_0x0030;
    L_0x0025:
        if (r0 == 0) goto L_0x002b;
    L_0x0027:
        r1 = r3 instanceof org.codehaus.jackson.map.JsonMappingException;
        if (r1 != 0) goto L_0x0039;
    L_0x002b:
        r3 = (java.io.IOException) r3;
        throw r3;
    L_0x002e:
        r0 = 0;
        goto L_0x0021;
    L_0x0030:
        if (r0 != 0) goto L_0x0039;
    L_0x0032:
        r1 = r3 instanceof java.lang.RuntimeException;
        if (r1 == 0) goto L_0x0039;
    L_0x0036:
        r3 = (java.lang.RuntimeException) r3;
        throw r3;
    L_0x0039:
        r1 = org.codehaus.jackson.map.JsonMappingException.wrapWithPath(r3, r4, r5);
        throw r1;
        */
    }

    protected void wrapInstantiationProblem(Throwable t, DeserializationContext ctxt) throws IOException {
        while (t instanceof InvocationTargetException && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            throw ((IOException) t);
        } else if (wrap || !t instanceof RuntimeException) {
            throw ctxt.instantiationException(this._beanType.getRawClass(), t);
        } else {
            throw ((RuntimeException) t);
        }
    }
}