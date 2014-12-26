package org.codehaus.jackson.map.ser;

import com.wmt.data.LocalAudioAll;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.type.JavaType;

public class PropertyBuilder {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final BasicBeanDescription _beanDesc;
    protected final SerializationConfig _config;
    protected Object _defaultBean;
    protected final Inclusion _outputProps;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion;

        static {
            $SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion = new int[Inclusion.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion[Inclusion.NON_DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion[Inclusion.NON_NULL.ordinal()] = 2;
        }
    }

    public PropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        this._config = config;
        this._beanDesc = beanDesc;
        this._outputProps = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
        this._annotationIntrospector = this._config.getAnnotationIntrospector();
    }

    protected Object _throwWrapped(Throwable e, String propName, Object defaultBean) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        } else if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        } else {
            throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
        }
    }

    protected BeanPropertyWriter buildWriter(String name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, Annotated am, boolean defaultUseStaticTyping) {
        Method m;
        Field f;
        if (am instanceof AnnotatedField) {
            m = null;
            f = ((AnnotatedField) am).getAnnotated();
        } else {
            m = ((AnnotatedMethod) am).getAnnotated();
            f = null;
        }
        JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
        if (contentTypeSer != null) {
            if (serializationType == null) {
                serializationType = declaredType;
            }
            if (serializationType.getContentType() == null) {
                throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + name + "' (of type " + this._beanDesc.getType() + "); serialization type " + serializationType + " has no content");
            }
            serializationType = serializationType.withContentTypeHandler(contentTypeSer);
            serializationType.getContentType();
        }
        Object suppValue = null;
        boolean suppressNulls = false;
        Inclusion methodProps = this._annotationIntrospector.findSerializationInclusion(am, this._outputProps);
        if (methodProps != null) {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion[methodProps.ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    suppValue = getDefaultValue(name, m, f);
                    if (suppValue == null) {
                        suppressNulls = true;
                    }
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    suppressNulls = true;
                    break;
            }
        }
        return new BeanPropertyWriter((AnnotatedMember)am, this._beanDesc.getClassAnnotations(), name, declaredType, (JsonSerializer)ser, typeSer, serializationType, m, f, suppressNulls, suppValue);
    }

    protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) {
        Class<?> serClass = this._annotationIntrospector.findSerializationType(a);
        if (serClass != null) {
            Class<?> rawDeclared = declaredType.getRawClass();
            if (serClass.isAssignableFrom(rawDeclared)) {
                declaredType = declaredType.widenBy(serClass);
            } else if (rawDeclared.isAssignableFrom(serClass)) {
                declaredType = declaredType.forcedNarrowBy(serClass);
            } else {
                throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serClass.getName() + " not a super-type of (declared) class " + rawDeclared.getName());
            }
            useStaticTyping = true;
        }
        JavaType secondary = BeanSerializerFactory.modifySecondaryTypesByAnnotation(this._config, a, declaredType);
        if (secondary != declaredType) {
            useStaticTyping = true;
            declaredType = secondary;
        }
        if (!useStaticTyping) {
            Typing typing = this._annotationIntrospector.findSerializationTyping(a);
            if (typing != null) {
                useStaticTyping = typing == Typing.STATIC;
            }
        }
        return useStaticTyping ? declaredType : null;
    }

    public Annotations getClassAnnotations() {
        return this._beanDesc.getClassAnnotations();
    }

    protected Object getDefaultBean() {
        if (this._defaultBean == null) {
            this._defaultBean = this._beanDesc.instantiateBean(this._config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
            if (this._defaultBean == null) {
                throw new IllegalArgumentException("Class " + this._beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
            }
        }
        return this._defaultBean;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.Object getDefaultValue(java.lang.String r4_name, java.lang.reflect.Method r5_m, java.lang.reflect.Field r6_f) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ser.PropertyBuilder.getDefaultValue(java.lang.String, java.lang.reflect.Method, java.lang.reflect.Field):java.lang.Object");
        /*
        r3 = this;
        r0 = r3.getDefaultBean();
        if (r5 == 0) goto L_0x000e;
    L_0x0006:
        r2 = 0;
        r2 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x0013 }
        r2 = r5.invoke(r0, r2);	 Catch:{ Exception -> 0x0013 }
    L_0x000d:
        return r2;
    L_0x000e:
        r2 = r6.get(r0);	 Catch:{ Exception -> 0x0013 }
        goto L_0x000d;
    L_0x0013:
        r1 = move-exception;
        r2 = r3._throwWrapped(r1, r4, r0);
        goto L_0x000d;
        */
    }
}