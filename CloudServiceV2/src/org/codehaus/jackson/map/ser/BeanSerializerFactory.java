package org.codehaus.jackson.map.ser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanSerializerFactory extends BasicSerializerFactory {
    public static final BeanSerializerFactory instance;
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final BeanSerializerModifier[] NO_MODIFIERS;
        protected static final Serializers[] NO_SERIALIZERS;
        protected final Serializers[] _additionalKeySerializers;
        protected final Serializers[] _additionalSerializers;
        protected final BeanSerializerModifier[] _modifiers;

        static {
            NO_SERIALIZERS = new Serializers[0];
            NO_MODIFIERS = new BeanSerializerModifier[0];
        }

        public ConfigImpl() {
            this(null, null, null);
        }

        protected ConfigImpl(Serializers[] allAdditionalSerializers, Serializers[] allAdditionalKeySerializers, BeanSerializerModifier[] modifiers) {
            if (allAdditionalSerializers == null) {
                allAdditionalSerializers = NO_SERIALIZERS;
            }
            this._additionalSerializers = allAdditionalSerializers;
            if (allAdditionalKeySerializers == null) {
                allAdditionalKeySerializers = NO_SERIALIZERS;
            }
            this._additionalKeySerializers = allAdditionalKeySerializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
        }

        public boolean hasKeySerializers() {
            return this._additionalKeySerializers.length > 0;
        }

        public boolean hasSerializerModifiers() {
            return this._modifiers.length > 0;
        }

        public boolean hasSerializers() {
            return this._additionalSerializers.length > 0;
        }

        public Iterable<Serializers> keySerializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeySerializers);
        }

        public Iterable<BeanSerializerModifier> serializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }

        public Iterable<Serializers> serializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
        }

        public Config withAdditionalKeySerializers(Serializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null Serializers");
            }
            return new org.codehaus.jackson.map.ser.BeanSerializerFactory.ConfigImpl(this._additionalSerializers, (Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeySerializers, additional), this._modifiers);
        }

        public Config withAdditionalSerializers(Serializers additional) {
            if (additional != null) {
                return new org.codehaus.jackson.map.ser.BeanSerializerFactory.ConfigImpl((Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalSerializers, additional), this._additionalKeySerializers, this._modifiers);
            }
            throw new IllegalArgumentException("Can not pass null Serializers");
        }

        public Config withSerializerModifier(BeanSerializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new org.codehaus.jackson.map.ser.BeanSerializerFactory.ConfigImpl(this._additionalSerializers, this._additionalKeySerializers, (BeanSerializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier));
        }
    }

    static {
        instance = new BeanSerializerFactory(null);
    }

    @Deprecated
    protected BeanSerializerFactory() {
        this(null);
    }

    protected BeanSerializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    protected BeanPropertyWriter _constructWriter(SerializationConfig config, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, String name, Annotated accessor) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            accessor.fixAccess();
        }
        JavaType type = accessor.getType(typeContext);
        Std property = new Std(name, type, pb.getClassAnnotations(), accessor);
        JsonSerializer<Object> annotatedSerializer = findSerializerFromAnnotation(config, accessor, property);
        TypeSerializer contentTypeSer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            contentTypeSer = findPropertyContentTypeSerializer(type, config, accessor, property);
        }
        BeanPropertyWriter pbw = pb.buildWriter(name, type, annotatedSerializer, findPropertyTypeSerializer(type, config, accessor, property), contentTypeSer, accessor, staticTyping);
        pbw.setViews(config.getAnnotationIntrospector().findSerializationViews(accessor));
        return pbw;
    }

    protected List<BeanPropertyWriter> _sortBeanProperties(List<BeanPropertyWriter> props, List<String> creatorProps, String[] propertyOrder, boolean sort) {
        Map<String, BeanPropertyWriter> all;
        String name;
        int size = props.size();
        if (sort) {
            all = new TreeMap();
        } else {
            all = new LinkedHashMap(size * 2);
        }
        Iterator i$ = props.iterator();
        while (i$.hasNext()) {
            BeanPropertyWriter w = (BeanPropertyWriter) i$.next();
            all.put(w.getName(), w);
        }
        Map<String, BeanPropertyWriter> ordered = new LinkedHashMap(size * 2);
        if (propertyOrder != null) {
            String[] arr$ = propertyOrder;
            int len$ = arr$.length;
            int i$2 = 0;
            while (i$2 < len$) {
                name = arr$[i$2];
                w = all.get(name);
                if (w != null) {
                    ordered.put(name, w);
                }
                i$2++;
            }
        }
        i$ = creatorProps.iterator();
        while (i$.hasNext()) {
            name = i$.next();
            w = all.get(name);
            if (w != null) {
                ordered.put(name, w);
            }
        }
        ordered.putAll(all);
        return new ArrayList(ordered.values());
    }

    protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (beanDesc.getBeanClass() == Object.class) {
            throw new IllegalArgumentException("Can not create bean serializer for Object.class");
        }
        Iterator i$;
        BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
        List<BeanPropertyWriter> props = findBeanProperties(config, beanDesc);
        AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
        if (this._factoryConfig.hasSerializerModifiers()) {
            if (props == null) {
                props = new ArrayList();
            }
            i$ = this._factoryConfig.serializerModifiers().iterator();
            while (i$.hasNext()) {
                props = ((BeanSerializerModifier) i$.next()).changeProperties(config, beanDesc, props);
            }
        }
        if (props != null && props.size() != 0) {
            props = sortBeanProperties(config, beanDesc, filterBeanProperties(config, beanDesc, props));
        } else if (anyGetter == null) {
            return beanDesc.hasKnownClassAnnotations() ? builder.createDummy() : null;
        } else {
            props = Collections.emptyList();
        }
        if (this._factoryConfig.hasSerializerModifiers()) {
            i$ = this._factoryConfig.serializerModifiers().iterator();
            while (i$.hasNext()) {
                props = i$.next().orderProperties(config, beanDesc, props);
            }
        }
        builder.setProperties(props);
        builder.setFilterId(findFilterId(config, beanDesc));
        if (anyGetter != null) {
            JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
            builder.setAnyGetter(new AnyGetterWriter(anyGetter, MapSerializer.construct(null, type, config.isEnabled(Feature.USE_STATIC_TYPING), createTypeSerializer(config, type.getContentType(), property), property, null, null)));
        }
        processViews(config, builder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            i$ = this._factoryConfig.serializerModifiers().iterator();
            while (i$.hasNext()) {
                builder = i$.next().updateBuilder(config, beanDesc, builder);
            }
        }
        return builder.build();
    }

    protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanSerializerBuilder(beanDesc);
    }

    protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
        return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
    }

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        return new PropertyBuilder(config, beanDesc);
    }

    public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType type, BeanProperty property) {
        if (!this._factoryConfig.hasKeySerializers()) {
            return null;
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
        JsonSerializer<Object> jsonSerializer = null;
        Iterator i$ = this._factoryConfig.keySerializers().iterator();
        while (i$.hasNext()) {
            jsonSerializer = ((Serializers) i$.next()).findSerializer(config, type, beanDesc, property);
            if (jsonSerializer != null) {
                return jsonSerializer;
            }
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType origType, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(origType);
        JsonSerializer<?> ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ser != null) {
            return ser;
        }
        JavaType type = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), origType);
        boolean staticTyping = type != origType;
        if (origType.isContainerType()) {
            return buildContainerSerializer(config, type, beanDesc, property, staticTyping);
        }
        Iterator i$ = this._factoryConfig.serializers().iterator();
        while (i$.hasNext()) {
            ser = ((Serializers) i$.next()).findSerializer(config, type, beanDesc, property);
            if (ser != null) {
                return ser;
            }
        }
        ser = findSerializerByLookup(type, config, beanDesc, property, staticTyping);
        if (ser != null) {
            return ser;
        }
        ser = findSerializerByPrimaryType(type, config, beanDesc, property, staticTyping);
        if (ser != null) {
            return ser;
        }
        ser = findBeanSerializer(config, type, beanDesc, property);
        if (ser == null) {
            ser = super.findSerializerByAddonType(config, type, beanDesc, property, staticTyping);
        }
        return ser;
    }

    protected Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }

    protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        String[] ignored = config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo());
        if (ignored != null && ignored.length > 0) {
            HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
            Iterator<BeanPropertyWriter> it = props.iterator();
            while (it.hasNext()) {
                if (ignoredSet.contains(((BeanPropertyWriter) it.next()).getName())) {
                    it.remove();
                }
            }
        }
        return props;
    }

    protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_GETTERS)) {
            vchecker = vchecker.withGetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_IS_GETTERS)) {
            vchecker = vchecker.withIsGetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(Visibility.NONE);
        }
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        vchecker = intr.findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        LinkedHashMap<String, AnnotatedMethod> methodsByProp = beanDesc.findGetters(vchecker, null);
        LinkedHashMap<String, AnnotatedField> fieldsByProp = beanDesc.findSerializableFields(vchecker, methodsByProp.keySet());
        removeIgnorableTypes(config, beanDesc, methodsByProp);
        removeIgnorableTypes(config, beanDesc, fieldsByProp);
        if (methodsByProp.isEmpty() && fieldsByProp.isEmpty()) {
            return null;
        }
        boolean staticTyping = usesStaticTyping(config, beanDesc, null, null);
        PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
        int size = methodsByProp.size();
        List<BeanPropertyWriter> arrayList = props;
        TypeBindings typeBind = beanDesc.bindingsForBeanType();
        Iterator i$ = fieldsByProp.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, AnnotatedField> en = (Entry) i$.next();
            ReferenceProperty prop = intr.findReferenceType((AnnotatedMember) en.getValue());
            if (prop == null || !prop.isBackReference()) {
                arrayList = props;
                arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, (String) en.getKey(), (AnnotatedMember) en.getValue()));
            }
        }
        i$ = methodsByProp.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, AnnotatedMethod> en2 = (Entry) i$.next();
            prop = intr.findReferenceType((AnnotatedMember) en2.getValue());
            if (prop == null || !prop.isBackReference()) {
                arrayList = props;
                arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, (String) en2.getKey(), (AnnotatedMember) en2.getValue()));
            }
        }
        return props;
    }

    public JsonSerializer<Object> findBeanSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (!isPotentialBeanType(type.getRawClass())) {
            return null;
        }
        JsonSerializer<Object> serializer = constructBeanSerializer(config, beanDesc, property);
        if (!this._factoryConfig.hasSerializerModifiers()) {
            return serializer;
        }
        Iterator i$ = this._factoryConfig.serializerModifiers().iterator();
        while (i$.hasNext()) {
            serializer = ((BeanSerializerModifier) i$.next()).modifySerializer(config, beanDesc, serializer);
        }
        return serializer;
    }

    protected Object findFilterId(SerializationConfig config, BasicBeanDescription beanDesc) {
        return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, MapperConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        JavaType contentType = containerType.getContentType();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, accessor, containerType);
        return b == null ? createTypeSerializer(config, contentType, property) : b.buildTypeSerializer(config, contentType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai), property);
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType baseType, MapperConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, accessor, baseType);
        return b == null ? createTypeSerializer(config, baseType, property) : b.buildTypeSerializer(config, baseType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai), property);
    }

    public Config getConfig() {
        return this._factoryConfig;
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        return ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type);
    }

    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        List<BeanPropertyWriter> props = builder.getProperties();
        boolean includeByDefault = config.isEnabled(Feature.DEFAULT_VIEW_INCLUSION);
        int propCount = props.size();
        int viewsFound = 0;
        BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
        int i = 0;
        while (i < propCount) {
            BeanPropertyWriter bpw = (BeanPropertyWriter) props.get(i);
            Class<?>[] views = bpw.getViews();
            if (views != null) {
                viewsFound++;
                filtered[i] = constructFilteredBeanWriter(bpw, views);
            } else if (includeByDefault) {
                filtered[i] = bpw;
            }
            i++;
        }
        if (!includeByDefault || viewsFound != 0) {
            builder.setFilteredProperties(filtered);
        }
    }

    protected <T extends AnnotatedMember> void removeIgnorableTypes(SerializationConfig config, BasicBeanDescription beanDesc, Map<String, T> props) {
        if (!props.isEmpty()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            Iterator<Entry<String, T>> it = props.entrySet().iterator();
            HashMap<Class<?>, Boolean> ignores = new HashMap();
            while (it.hasNext()) {
                Class<?> type = ((AnnotatedMember) ((Entry) it.next()).getValue()).getRawType();
                Boolean result = (Boolean) ignores.get(type);
                if (result == null) {
                    result = intr.isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations(type)).getClassInfo());
                    if (result == null) {
                        result = Boolean.FALSE;
                    }
                    ignores.put(type, result);
                }
                if (result.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        boolean sort;
        List<String> creatorProps = beanDesc.findCreatorPropertyNames();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        AnnotatedClass ac = beanDesc.getClassInfo();
        String[] propOrder = intr.findSerializationPropertyOrder(ac);
        Boolean alpha = intr.findSerializationSortAlphabetically(ac);
        if (alpha == null) {
            sort = config.isEnabled(Feature.SORT_PROPERTIES_ALPHABETICALLY);
        } else {
            sort = alpha.booleanValue();
        }
        return (!sort && creatorProps.isEmpty() && propOrder == null) ? props : _sortBeanProperties(props, creatorProps, propOrder, sort);
    }

    public SerializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
        }
        this(config);
        return this;
    }
}