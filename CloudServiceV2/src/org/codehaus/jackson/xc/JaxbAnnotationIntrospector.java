package org.codehaus.jackson.xc;

import com.wmt.data.LocalAudioAll;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

public class JaxbAnnotationIntrospector extends AnnotationIntrospector implements Versioned {
    protected static final String MARKER_FOR_DEFAULT = "##default";
    private static final ThreadLocal<SoftReference<PropertyDescriptors>> _propertyDescriptors;
    protected final JsonDeserializer<?> _dataHandlerDeserializer;
    protected final JsonSerializer<?> _dataHandlerSerializer;
    protected final String _jaxbPackageName;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$javax$xml$bind$annotation$XmlAccessType;

        static {
            $SwitchMap$javax$xml$bind$annotation$XmlAccessType = new int[XmlAccessType.values().length];
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PUBLIC_MEMBER.ordinal()] = 4;
        }
    }

    private static class AnnotatedProperty implements AnnotatedElement {
        private final PropertyDescriptor pd;

        private AnnotatedProperty(PropertyDescriptor pd) {
            this.pd = pd;
        }

        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            Method m = this.pd.getReadMethod();
            if (m != null) {
                T ann = m.getAnnotation(annotationClass);
                if (ann != null) {
                    return ann;
                }
            }
            m = this.pd.getWriteMethod();
            return m != null ? m.getAnnotation(annotationClass) : null;
        }

        public Annotation[] getAnnotations() {
            throw new UnsupportedOperationException();
        }

        public Annotation[] getDeclaredAnnotations() {
            throw new UnsupportedOperationException();
        }

        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            Method m = this.pd.getReadMethod();
            if (m != null && m.isAnnotationPresent(annotationClass)) {
                return true;
            }
            m = this.pd.getWriteMethod();
            return m != null && m.isAnnotationPresent(annotationClass);
        }
    }

    protected static final class PropertyDescriptors {
        private Map<String, PropertyDescriptor> _byMethodName;
        private Map<String, PropertyDescriptor> _byPropertyName;
        private final Class<?> _forClass;
        private final List<PropertyDescriptor> _properties;

        public PropertyDescriptors(Class<?> forClass, List<PropertyDescriptor> properties) {
            this._forClass = forClass;
            this._properties = properties;
        }

        private static Map<String, PropertyDescriptor> _processReadMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap();
            } else {
                PropertyDescriptor pd = partials.get(propertyName);
                if (pd != null) {
                    pd.setReadMethod(method);
                    if (pd.getWriteMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                        return partials;
                    }
                }
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, method, null));
            return partials;
        }

        private static Map<String, PropertyDescriptor> _processWriteMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap();
            } else {
                PropertyDescriptor pd = (PropertyDescriptor) partials.get(propertyName);
                if (pd != null) {
                    pd.setWriteMethod(method);
                    if (pd.getReadMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                        return partials;
                    }
                }
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, null, method));
            return partials;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static org.codehaus.jackson.xc.JaxbAnnotationIntrospector.PropertyDescriptors find(java.lang.Class<?> r15_forClass) throws java.beans.IntrospectionException {
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.xc.JaxbAnnotationIntrospector.PropertyDescriptors.find(java.lang.Class):org.codehaus.jackson.xc.JaxbAnnotationIntrospector$PropertyDescriptors");
            /*
            r13 = 0;
            r1 = java.beans.Introspector.getBeanInfo(r15);
            r8 = r1.getPropertyDescriptors();
            r14 = r8.length;
            if (r14 != 0) goto L_0x0016;
        L_0x000c:
            r2 = java.util.Collections.emptyList();
        L_0x0010:
            r13 = new org.codehaus.jackson.xc.JaxbAnnotationIntrospector$PropertyDescriptors;
            r13.<init>(r15, r2);
            return r13;
        L_0x0016:
            r2 = new java.util.ArrayList;
            r2.<init>();
            r6 = 0;
            r0 = r1.getPropertyDescriptors();
            r4 = r0.length;
            r3 = 0;
        L_0x0022:
            if (r3 >= r4) goto L_0x0010;
        L_0x0024:
            r7 = r0[r3];
            r9 = r7.getReadMethod();
            if (r9 == 0) goto L_0x0035;
        L_0x002c:
            r14 = javax.xml.bind.annotation.XmlTransient.class;
            r14 = r9.getAnnotation(r14);
            if (r14 == 0) goto L_0x0035;
        L_0x0034:
            r9 = 0;
        L_0x0035:
            if (r9 != 0) goto L_0x004e;
        L_0x0037:
            r10 = r13;
        L_0x0038:
            r11 = r7.getWriteMethod();
            if (r11 == 0) goto L_0x0047;
        L_0x003e:
            r14 = javax.xml.bind.annotation.XmlTransient.class;
            r14 = r11.getAnnotation(r14);
            if (r14 == 0) goto L_0x0047;
        L_0x0046:
            r11 = 0;
        L_0x0047:
            if (r9 != 0) goto L_0x0057;
        L_0x0049:
            if (r11 != 0) goto L_0x0057;
        L_0x004b:
            r3 = r3 + 1;
            goto L_0x0022;
        L_0x004e:
            r14 = r7.getPropertyType();
            r10 = org.codehaus.jackson.xc.JaxbAnnotationIntrospector.findJaxbPropertyName(r9, r14, r13);
            goto L_0x0038;
        L_0x0057:
            if (r11 != 0) goto L_0x0067;
        L_0x0059:
            r12 = r13;
        L_0x005a:
            if (r11 != 0) goto L_0x0070;
        L_0x005c:
            if (r10 != 0) goto L_0x0062;
        L_0x005e:
            r10 = r7.getName();
        L_0x0062:
            r6 = _processReadMethod(r6, r9, r10, r2);
            goto L_0x004b;
        L_0x0067:
            r14 = r7.getPropertyType();
            r12 = org.codehaus.jackson.xc.JaxbAnnotationIntrospector.findJaxbPropertyName(r11, r14, r13);
            goto L_0x005a;
        L_0x0070:
            if (r9 != 0) goto L_0x007d;
        L_0x0072:
            if (r12 != 0) goto L_0x0078;
        L_0x0074:
            r12 = r7.getName();
        L_0x0078:
            r6 = _processWriteMethod(r6, r11, r12, r2);
            goto L_0x004b;
        L_0x007d:
            if (r10 == 0) goto L_0x0090;
        L_0x007f:
            if (r12 == 0) goto L_0x0090;
        L_0x0081:
            r14 = r10.equals(r12);
            if (r14 != 0) goto L_0x0090;
        L_0x0087:
            r6 = _processReadMethod(r6, r9, r10, r2);
            r6 = _processWriteMethod(r6, r11, r12, r2);
            goto L_0x004b;
        L_0x0090:
            if (r10 == 0) goto L_0x009c;
        L_0x0092:
            r5 = r10;
        L_0x0093:
            r14 = new java.beans.PropertyDescriptor;
            r14.<init>(r5, r9, r11);
            r2.add(r14);
            goto L_0x004b;
        L_0x009c:
            if (r12 == 0) goto L_0x00a0;
        L_0x009e:
            r5 = r12;
            goto L_0x0093;
        L_0x00a0:
            r5 = r7.getName();
            goto L_0x0093;
            */
        }

        public PropertyDescriptor findByMethodName(String name) {
            if (this._byMethodName == null) {
                this._byMethodName = new HashMap(this._properties.size());
                Iterator i$ = this._properties.iterator();
                while (i$.hasNext()) {
                    PropertyDescriptor desc = (PropertyDescriptor) i$.next();
                    Method getter = desc.getReadMethod();
                    if (getter != null) {
                        this._byMethodName.put(getter.getName(), desc);
                    }
                    Method setter = desc.getWriteMethod();
                    if (setter != null) {
                        this._byMethodName.put(setter.getName(), desc);
                    }
                }
            }
            return (PropertyDescriptor) this._byMethodName.get(name);
        }

        public PropertyDescriptor findByPropertyName(String name) {
            if (this._byPropertyName == null) {
                this._byPropertyName = new HashMap(this._properties.size());
                Iterator i$ = this._properties.iterator();
                while (i$.hasNext()) {
                    PropertyDescriptor desc = (PropertyDescriptor) i$.next();
                    this._byPropertyName.put(desc.getName(), desc);
                }
            }
            return (PropertyDescriptor) this._byPropertyName.get(name);
        }

        public Class<?> getBeanClass() {
            return this._forClass;
        }
    }

    static {
        _propertyDescriptors = new ThreadLocal();
    }

    public JaxbAnnotationIntrospector() {
        this._jaxbPackageName = XmlElement.class.getPackage().getName();
        JsonSerializer<?> dataHandlerSerializer = null;
        JsonDeserializer<?> dataHandlerDeserializer = null;
        try {
            dataHandlerSerializer = (JsonSerializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonSerializer").newInstance();
            dataHandlerDeserializer = (JsonDeserializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonDeserializer").newInstance();
        } catch (Throwable th) {
        }
        this._dataHandlerSerializer = dataHandlerSerializer;
        this._dataHandlerDeserializer = dataHandlerDeserializer;
    }

    private final XmlAdapter<Object, Object> checkAdapter(XmlJavaTypeAdapter adapterInfo, Class<?> typeNeeded) {
        Class<?> adaptedType = adapterInfo.type();
        return (adaptedType == DEFAULT.class || adaptedType.isAssignableFrom(typeNeeded)) ? (XmlAdapter) ClassUtil.createInstance(adapterInfo.value(), false) : null;
    }

    protected static String findJaxbPropertyName(AnnotatedElement ae, Class<?> aeType, String defaultName) {
        XmlElementWrapper elementWrapper = (XmlElementWrapper) ae.getAnnotation(XmlElementWrapper.class);
        String name;
        if (elementWrapper != null) {
            name = elementWrapper.name();
            return !MARKER_FOR_DEFAULT.equals(name) ? name : defaultName;
        } else {
            XmlAttribute attribute = (XmlAttribute) ae.getAnnotation(XmlAttribute.class);
            if (attribute != null) {
                name = attribute.name();
                return MARKER_FOR_DEFAULT.equals(name) ? defaultName : name;
            } else {
                XmlElement element = (XmlElement) ae.getAnnotation(XmlElement.class);
                if (element != null) {
                    name = element.name();
                    return MARKER_FOR_DEFAULT.equals(name) ? defaultName : name;
                } else {
                    XmlElementRef elementRef = (XmlElementRef) ae.getAnnotation(XmlElementRef.class);
                    if (elementRef != null) {
                        name = elementRef.name();
                        if (!MARKER_FOR_DEFAULT.equals(name)) {
                            return name;
                        }
                        if (aeType != null) {
                            XmlRootElement rootElement = (XmlRootElement) aeType.getAnnotation(XmlRootElement.class);
                            if (rootElement != null) {
                                name = rootElement.name();
                                return MARKER_FOR_DEFAULT.equals(name) ? Introspector.decapitalize(aeType.getSimpleName()) : name;
                            }
                        }
                    }
                    return ((XmlValue) ae.getAnnotation(XmlValue.class)) != null ? "value" : null;
                }
            }
        }
    }

    private XmlRootElement findRootElementAnnotation(Annotated ac) {
        return (XmlRootElement) findAnnotation(XmlRootElement.class, ac, true, false, true);
    }

    private boolean isDataHandler(Class<?> type) {
        return (type == null || Object.class == type || (!"javax.activation.DataHandler".equals(type.getName()) && !isDataHandler(type.getSuperclass()))) ? false : true;
    }

    protected Class<?> _doFindDeserializationType(Annotated a, JavaType baseType, String propName) {
        if (a.hasAnnotation(XmlJavaTypeAdapter.class)) {
            return null;
        }
        XmlElement annotation = (XmlElement) findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation != null) {
            Class<?> type = annotation.type();
            if (type != XmlElement.DEFAULT.class) {
                return type;
            }
        }
        if (a instanceof AnnotatedMethod && propName != null) {
            annotation = findFieldAnnotation(XmlElement.class, ((AnnotatedMethod) a).getDeclaringClass(), propName);
            if (!(annotation == null || annotation.type() == XmlElement.DEFAULT.class)) {
                return annotation.type();
            }
        }
        return null;
    }

    protected TypeResolverBuilder<?> _typeResolverFromXmlElements(Annotated am) {
        return (((XmlElements) findAnnotation(XmlElements.class, am, false, false, false)) == null && ((XmlElementRefs) findAnnotation(XmlElementRefs.class, am, false, false, false)) == null) ? null : new StdTypeResolverBuilder().init(Id.NAME, null).inclusion(As.WRAPPER_OBJECT);
    }

    protected XmlAccessType findAccessType(Annotated ac) {
        XmlAccessorType at = (XmlAccessorType) findAnnotation(XmlAccessorType.class, ac, true, true, true);
        return at == null ? null : at.value();
    }

    protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
        if (am instanceof AnnotatedClass) {
            return findAdapterForClass((AnnotatedClass) am, forSerialization);
        }
        XmlJavaTypeAdapter adapterInfo;
        XmlAdapter<Object, Object> adapter;
        Class<?> memberType = am.getRawType();
        if (memberType == Void.TYPE && am instanceof AnnotatedMethod) {
            memberType = ((AnnotatedMethod) am).getParameterClass(0);
        }
        Member member = (Member) am.getAnnotated();
        if (member != null) {
            Class<?> potentialAdaptee = member.getDeclaringClass();
            if (potentialAdaptee != null) {
                adapterInfo = (XmlJavaTypeAdapter) potentialAdaptee.getAnnotation(XmlJavaTypeAdapter.class);
                if (adapterInfo != null) {
                    adapter = checkAdapter(adapterInfo, memberType);
                    if (adapter != null) {
                        return adapter;
                    }
                }
            }
        }
        adapterInfo = findAnnotation(XmlJavaTypeAdapter.class, am, true, false, false);
        if (adapterInfo != null) {
            adapter = checkAdapter(adapterInfo, memberType);
            if (adapter != null) {
                return adapter;
            }
        }
        XmlJavaTypeAdapters adapters = (XmlJavaTypeAdapters) findAnnotation(XmlJavaTypeAdapters.class, am, true, false, false);
        if (adapters != null) {
            XmlJavaTypeAdapter[] arr$ = adapters.value();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                adapter = checkAdapter(arr$[i$], memberType);
                if (adapter != null) {
                    return adapter;
                }
                i$++;
            }
        }
        return null;
    }

    protected XmlAdapter<Object, Object> findAdapterForClass(AnnotatedClass ac, boolean forSerialization) {
        XmlJavaTypeAdapter adapterInfo = (XmlJavaTypeAdapter) ac.getAnnotated().getAnnotation(XmlJavaTypeAdapter.class);
        return adapterInfo != null ? (XmlAdapter) ClassUtil.createInstance(adapterInfo.value(), false) : null;
    }

    protected <A extends Annotation> A findAnnotation(Class<A> annotationClass, Annotated annotated, boolean includePackage, boolean includeClass, boolean includeSuperclasses) {
        A annotation;
        Class memberClass;
        if (annotated instanceof AnnotatedMethod) {
            PropertyDescriptor pd = findPropertyDescriptor((AnnotatedMethod) annotated);
            if (pd != null) {
                annotation = new AnnotatedProperty(null).getAnnotation(annotationClass);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        AnnotatedElement annType = annotated.getAnnotated();
        if (annotated instanceof AnnotatedParameter) {
            AnnotatedParameter param = (AnnotatedParameter) annotated;
            annotation = param.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
            memberClass = param.getMember().getDeclaringClass();
        } else {
            annotation = annType.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
            if (annType instanceof Member) {
                memberClass = ((Member) annType).getDeclaringClass();
                if (includeClass) {
                    annotation = memberClass.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            } else if (annType instanceof Class) {
                memberClass = (Class) annType;
            } else {
                throw new IllegalStateException("Unsupported annotated member: " + annotated.getClass().getName());
            }
        }
        if (memberClass != null) {
            if (includeSuperclasses) {
                Class superclass = memberClass.getSuperclass();
                while (superclass != null && superclass != Object.class) {
                    annotation = superclass.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                    superclass = superclass.getSuperclass();
                }
            }
            if (includePackage) {
                return memberClass.getPackage().getAnnotation(annotationClass);
            }
        }
        return null;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        XmlAccessType at = findAccessType(ac);
        if (at == null) {
            return checker;
        }
        switch (AnonymousClass_1.$SwitchMap$javax$xml$bind$annotation$XmlAccessType[at.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return checker.withFieldVisibility(Visibility.ANY).withSetterVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE);
            case ClassWriter.COMPUTE_FRAMES:
                return checker.withFieldVisibility(Visibility.NONE).withSetterVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return checker.withFieldVisibility(Visibility.NONE).withSetterVisibility(Visibility.PUBLIC_ONLY).withGetterVisibility(Visibility.PUBLIC_ONLY).withIsGetterVisibility(Visibility.PUBLIC_ONLY);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return checker.withFieldVisibility(Visibility.PUBLIC_ONLY).withSetterVisibility(Visibility.PUBLIC_ONLY).withGetterVisibility(Visibility.PUBLIC_ONLY).withIsGetterVisibility(Visibility.PUBLIC_ONLY);
            default:
                return checker;
        }
    }

    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann != null) {
            return ann.value() ? Boolean.TRUE : Boolean.FALSE;
        } else {
            return null;
        }
    }

    public Class<JsonDeserializer<?>> findContentDeserializer(Annotated am) {
        return null;
    }

    public String findDeserializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), "");
        return name == null ? field.getName() : name;
    }

    public Class<?> findDeserializationContentType(Annotated a, JavaType baseContentType, String propName) {
        return _doFindDeserializationType(a, baseContentType, propName);
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        return null;
    }

    public Class<?> findDeserializationType(Annotated a, JavaType baseType, String propName) {
        return !baseType.isContainerType() ? _doFindDeserializationType(a, baseType, propName) : null;
    }

    public JsonDeserializer<?> findDeserializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, false);
        if (adapter != null) {
            return new XmlAdapterJsonDeserializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        return (type == null || this._dataHandlerDeserializer == null || !isDataHandler(type)) ? null : this._dataHandlerDeserializer;
    }

    public String findEnumValue(Enum<?> e) {
        Class<?> enumClass = e.getDeclaringClass();
        String enumValue = e.name();
        try {
            XmlEnumValue xmlEnumValue = (XmlEnumValue) enumClass.getDeclaredField(enumValue).getAnnotation(XmlEnumValue.class);
            return xmlEnumValue != null ? xmlEnumValue.value() : enumValue;
        } catch (NoSuchFieldException e2) {
            throw new IllegalStateException("Could not locate Enum entry '" + enumValue + "' (Enum class " + enumClass.getName() + ")", e2);
        }
    }

    protected <A extends Annotation> A findFieldAnnotation(Class<A> annotationType, Class<?> cls, String fieldName) {
        do {
            Field[] arr$ = cls.getDeclaredFields();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Field f = arr$[i$];
                if (fieldName.equals(f.getName())) {
                    return f.getAnnotation(annotationType);
                }
                i$++;
            }
            if (cls.isInterface() || cls == Object.class) {
                break;
            }
        } while (cls.getSuperclass() != null);
        return null;
    }

    public String findGettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        return desc != null ? findJaxbSpecifiedPropertyName(desc) : null;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return null;
    }

    protected String findJaxbSpecifiedPropertyName(PropertyDescriptor prop) {
        return findJaxbPropertyName(new AnnotatedProperty(null), prop.getPropertyType(), prop.getName());
    }

    public Class<KeyDeserializer> findKeyDeserializer(Annotated am) {
        return null;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        return null;
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType) {
        if (containerType.isContainerType()) {
            return _typeResolverFromXmlElements(am);
        }
        throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
    }

    protected PropertyDescriptor findPropertyDescriptor(AnnotatedMethod m) {
        return getDescriptors(m.getDeclaringClass()).findByMethodName(m.getName());
    }

    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        return baseType.isContainerType() ? null : _typeResolverFromXmlElements(am);
    }

    public String findRootName(AnnotatedClass ac) {
        XmlRootElement elem = findRootElementAnnotation(ac);
        if (elem == null) {
            return null;
        }
        String name = elem.name();
        return MARKER_FOR_DEFAULT.equals(name) ? "" : name;
    }

    public String findSerializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), "");
        return name == null ? field.getName() : name;
    }

    public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
        XmlElementWrapper w = (XmlElementWrapper) a.getAnnotation(XmlElementWrapper.class);
        if (w != null) {
            return w.nillable() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
        } else {
            XmlElement e = (XmlElement) a.getAnnotation(XmlElement.class);
            if (e != null) {
                return e.nillable() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
            } else {
                return defValue;
            }
        }
    }

    public String[] findSerializationPropertyOrder(Annotated ac) {
        XmlType type = (XmlType) findAnnotation(XmlType.class, ac, true, true, true);
        if (type == null) {
            return null;
        }
        String[] order = type.propOrder();
        if (order == null || order.length == 0) {
            return null;
        }
        PropertyDescriptors props = getDescriptors(ac.getRawType());
        int i = 0;
        int len = order.length;
        while (i < len) {
            String propName = order[i];
            if (props.findByPropertyName(propName) == null && propName.length() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("get");
                sb.append(Character.toUpperCase(propName.charAt(0)));
                if (propName.length() > 1) {
                    sb.append(propName.substring(1));
                }
                PropertyDescriptor desc = props.findByMethodName(sb.toString());
                if (desc != null) {
                    order[i] = desc.getName();
                }
            }
            i++;
        }
        return order;
    }

    public Boolean findSerializationSortAlphabetically(Annotated ac) {
        boolean z = true;
        XmlAccessorOrder order = (XmlAccessorOrder) findAnnotation(XmlAccessorOrder.class, ac, true, true, true);
        if (order == null) {
            return null;
        }
        if (order.value() != XmlAccessOrder.ALPHABETICAL) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public Class<?> findSerializationType(Annotated a) {
        XmlElement annotation = (XmlElement) findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation == null || annotation.type() == XmlElement.DEFAULT.class) {
            return null;
        }
        return isIndexedType(a.getRawType()) ? null : annotation.type();
    }

    public Typing findSerializationTyping(Annotated a) {
        return null;
    }

    public Class<?>[] findSerializationViews(Annotated a) {
        return null;
    }

    public JsonSerializer<?> findSerializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, true);
        if (adapter != null) {
            return new XmlAdapterJsonSerializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        return (type == null || this._dataHandlerSerializer == null || !isDataHandler(type)) ? null : this._dataHandlerSerializer;
    }

    public String findSettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        return desc != null ? findJaxbSpecifiedPropertyName(desc) : null;
    }

    public List<NamedType> findSubtypes(Annotated a) {
        XmlElements elems = (XmlElements) findAnnotation(XmlElements.class, a, false, false, false);
        ArrayList<NamedType> result;
        int len$;
        int i$;
        String name;
        if (elems != null) {
            result = new ArrayList();
            XmlElement[] arr$ = elems.value();
            len$ = arr$.length;
            i$ = 0;
            while (i$ < len$) {
                XmlElement elem = arr$[i$];
                name = elem.name();
                if (MARKER_FOR_DEFAULT.equals(name)) {
                    name = null;
                }
                result.add(new NamedType(elem.type(), name));
                i$++;
            }
            return result;
        } else {
            XmlElementRefs elemRefs = (XmlElementRefs) findAnnotation(XmlElementRefs.class, a, false, false, false);
            if (elemRefs == null) {
                return null;
            }
            result = new ArrayList();
            XmlElementRef[] arr$2 = elemRefs.value();
            len$ = arr$2.length;
            i$ = 0;
            while (i$ < len$) {
                XmlElementRef elemRef = arr$2[i$];
                Class<?> refType = elemRef.type();
                if (!JAXBElement.class.isAssignableFrom(refType)) {
                    name = elemRef.name();
                    if (name == null || MARKER_FOR_DEFAULT.equals(name)) {
                        XmlRootElement rootElement = (XmlRootElement) refType.getAnnotation(XmlRootElement.class);
                        if (rootElement != null) {
                            name = rootElement.name();
                        }
                    }
                    if (name == null || MARKER_FOR_DEFAULT.equals(name)) {
                        name = Introspector.decapitalize(refType.getSimpleName());
                    }
                    result.add(new NamedType(refType, name));
                }
                i$++;
            }
            return result;
        }
    }

    public String findTypeName(Annotated ac) {
        XmlType type = (XmlType) findAnnotation(XmlType.class, ac, false, false, false);
        if (type != null) {
            String name = type.name();
            if (!MARKER_FOR_DEFAULT.equals(name)) {
                return name;
            }
        }
        return null;
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        return null;
    }

    protected PropertyDescriptors getDescriptors(Class<?> forClass) {
        SoftReference<PropertyDescriptors> ref = (SoftReference) _propertyDescriptors.get();
        PropertyDescriptors descriptors = ref == null ? null : (PropertyDescriptors) ref.get();
        if (descriptors != null && descriptors.getBeanClass() == forClass) {
            return descriptors;
        }
        try {
            descriptors = PropertyDescriptors.find(forClass);
            _propertyDescriptors.set(new SoftReference(descriptors));
            return descriptors;
        } catch (IntrospectionException e) {
            IntrospectionException e2 = e;
            throw new IllegalArgumentException("Problem introspecting bean properties: " + e2.getMessage(), e2);
        }
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return false;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    public boolean hasCreatorAnnotation(Annotated am) {
        return false;
    }

    public boolean isHandled(Annotation ann) {
        Class<?> cls = ann.annotationType();
        Package pkg = cls.getPackage();
        return (pkg != null ? pkg.getName() : cls.getName()).startsWith(this._jaxbPackageName) || cls == JsonCachable.class;
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return false;
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return f.getAnnotation(XmlTransient.class) != null;
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return m.getAnnotation(XmlTransient.class) != null;
    }

    public Boolean isIgnorableType(AnnotatedClass ac) {
        return null;
    }

    protected boolean isIndexedType(Class<?> raw) {
        return raw.isArray() || Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw);
    }

    protected boolean isInvisible(Annotated f) {
        boolean invisible = true;
        Annotation[] arr$ = f.getAnnotated().getDeclaredAnnotations();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (isHandled(arr$[i$])) {
                invisible = false;
            }
            i$++;
        }
        if (!invisible) {
            return invisible;
        }
        XmlAccessType accessType = XmlAccessType.PUBLIC_MEMBER;
        XmlAccessorType at = (XmlAccessorType) findAnnotation(XmlAccessorType.class, f, true, true, true);
        if (at != null) {
            accessType = at.value();
        }
        return (accessType == XmlAccessType.FIELD || (accessType == XmlAccessType.PUBLIC_MEMBER && Modifier.isPublic(f.getAnnotated().getModifiers()))) ? false : true;
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }
}